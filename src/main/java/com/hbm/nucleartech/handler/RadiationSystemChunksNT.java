package com.hbm.nucleartech.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hbm.nucleartech.HBM;
import com.hbm.nucleartech.hazard.HazardBlock;
import com.hbm.nucleartech.interfaces.IRadResistantBlock;
import com.hbm.nucleartech.item.custom.GeigerCounterItem;
import com.hbm.nucleartech.util.ContaminationUtil;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import com.hbm.nucleartech.handler.RadiationSystemChunksNT.ChunkStorageCompat.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.joml.Vector4i;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static com.hbm.nucleartech.handler.RadiationSystemNT.getAabb;

/**
 * RadiationSystemNT (Forge 1.20.1 port of HBM’s Nuclear Tech 1.12.2 3D radiation system).
 * <p>
 * This class encapsulates:
 *  - IChunkRadiation: Capability interface for per-chunk radiation storage.
 *  - RadPocket: Represents one connected, “open” region in a 16×16×16 subchunk.
 *  - SubChunkRadiationStorage: Manages flood-fill pocket detection inside one subchunk (24 per chunk).
 *  - ChunkRadiationStorage: The actual capability implementation attached to each LevelChunk.
 *  - RadiationEventHandlers: Forge event subscribers to register, attach, and tick radiation.
 * <p>
 * Key differences from 1.12.2:
 *  - World height is –63..319 (384 blocks = 24 subchunks of 16 blocks).
 *  - Uses Minecraft 1.20.1 Forge capability system (AttachCapabilitiesEvent<LevelChunk>).
 *  - Replaces EnumFacing with Direction.
 *  - Uses world.getBlockState(pos) instead of raw Chunk storage.
 */
@Mod.EventBusSubscriber(modid = "hbm", bus = Mod.EventBusSubscriber.Bus.MOD)
public class RadiationSystemChunksNT {

    private static final Set<RadPocket> activePockets = new HashSet<>();

    /**A tick counter so radiation only updates once every second.*/
    private static int ticks;
    // -----------------------------------
    // CAPABILITY REGISTRATION
    // -----------------------------------

    /** Capability instance token for IChunkRadiation */
    public static final Capability<IChunkRadiation> CHUNK_RAD_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

    @Mod.EventBusSubscriber(modid = "hbm", bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModSetup {
        @SubscribeEvent
        public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
            event.register(IChunkRadiation.class);
        }

        // If any mod-common setup is needed, we could subscribe here:
        @SubscribeEvent
        public static void onCommonSetup(FMLCommonSetupEvent event) {
            // No-op for now
        }
    }

//    public static Set<RadPocket> allActivePockets = new HashSet<>();

    public static Set<RadPocket> getActivePockets() {

        //System.err.println("[Debug] Queried active pockets for world");
        return activePockets;
    }

    public static void addActivePocket(RadPocket pocket) {

        activePockets.add(pocket);
//        BlockPos chunkPos = new BlockPos(
//                pocket.getSubChunkPos().getX() >> 4,
//                ChunkStorageCompat.getIndexFromWorldY(pocket.getSubChunkPos().getY()),
//                pocket.getSubChunkPos().getZ() >> 4);
        //System.err.println("[Debug] Added active pocket " + pocket.index + " (radiation: " + pocket.radiation + ", accumulatedRads: " + pocket.accumulatedRads + ", sealed: " + pocket.isSealed() + ") at " + pocket.getSubChunkPos() + " (Chunk:" + chunkPos + ") for world " + pocket.parent.parentChunk.chunk.getLevel());

    }

    public static void removeActivePocket(RadPocket pocket) {

        activePockets.remove(pocket);
//        BlockPos chunkPos = new BlockPos(
//                pocket.getSubChunkPos().getX() >> 4,
//                ChunkStorageCompat.getIndexFromWorldY(pocket.getSubChunkPos().getY()),
//                pocket.getSubChunkPos().getZ() >> 4);
        //System.err.println("[Debug] Removed active pocket " + pocket.index + " (radiation: " + pocket.radiation + ", accumulatedRads: " + pocket.accumulatedRads + ", sealed: " + pocket.isSealed() + ") at " + pocket.getSubChunkPos() + " (Chunk:" + chunkPos + ") for world " + pocket.parent.parentChunk.chunk.getLevel());

    }

    public static void clearActivePockets() {

        activePockets.clear();
        //System.err.println("[Debug] Cleared active pockets for world");
    }

    /**
     * Gets whether the sub chunk at spefified position is loaded
     * @param level - the world to check in
     * @param pos - ths position to check at
     * @return whether the specified position currently has an active sub chunk
     */
    public static boolean isSubChunkLoaded(Level level, BlockPos pos){
        //If the position is out of bounds, it isn't loaded
        if(pos.getY() > 319 || pos.getY() < -64)
            return false;

        //If the chunk isn't loaded, neither is the sub chunk
        if(!level.getChunkSource().hasChunk(pos.getX() >> 4, pos.getZ() >> 4))
            return false;

        AtomicReference<IChunkRadiation> stAtomic = new AtomicReference<>();
        IChunkRadiation st;

        level.getChunkAt(pos).getCapability(CHUNK_RAD_CAPABILITY, null).ifPresent(stAtomic::set);
        st = stAtomic.get();

        if(st == null){
            return false;
        }
        //Finally, check if the chunk has a sub chunk at the specified y level
        SubChunkRadiationStorage sc = st.getForYLevel(pos.getY());
        return sc != null;
    }

    /**
     * Gets the pocket at the position (pockets explained below)
     * @param world - the world to get the pocket from
     * @param pos - the position the pocket should contain
     * @return - the RadPocket at the specified position
     */
    public static RadPocket getPocket(Level world, BlockPos pos){
        return getSubChunkStorage(world, pos).getPocket(pos);
    }

    /**
     * Gets the sub chunk from the specified pos. Loads it if it doesn't exist
     * @param level - the world to get from
     * @param pos - the position to get the sub chunk at
     * @return the sub chunk at the specified position
     */
    public static SubChunkRadiationStorage getSubChunkStorage(Level level, BlockPos pos) {

        ChunkRadiationStorage st = getChunkStorage(level, pos);
        SubChunkRadiationStorage sc = st.instance.getForYLevel(pos.getY());

        if(sc == null) {

            rebuildChunkPockets(level.getChunkAt(pos), ChunkStorageCompat.getIndexFromWorldY(pos.getY()));
        }
        sc = st.instance.getForYLevel(pos.getY());
        return sc;
    }
    public static SubChunkRadiationStorage getSubChunkStorage(ChunkRadiationStorage st, Integer yLevel, LevelChunk chunk) {

        SubChunkRadiationStorage sc = st.instance.getForYLevel(yLevel);

        if(sc == null) {

            rebuildChunkPockets(chunk, ChunkStorageCompat.getIndexFromWorldY(yLevel));
        }
        sc = st.instance.getForYLevel(yLevel);
        return sc;
    }

    /**
     * Updates the whole radiation system. This loops through every world's radiation data, and updates the value in each pocket.
     * Pockets transfer some of their radiation to pockets they're connected to.
     * It tries to do pretty much the same algorithm as the regular system, but in 3d with pockets.
     */
    public static void updateRadiation() {

        long time = System.currentTimeMillis();

        //System.out.println("[Debug] Started updating radiation");

        List<RadPocket> itrActive = new ArrayList<>(getActivePockets());
        Iterator<RadPocket> itr = itrActive.iterator();
        //System.out.println("[Debug] Active pockets array size: " + itrActive.size());
        while(itr.hasNext()) {

            RadPocket p = itr.next();
            BlockPos pos = p.parent.parentChunk.getWorldPos(p.parent.yLevel);

            // Lower the radiation a bit, and mark the parent chunk as dirty so the radiation gets saved
            p.radiation *= 0.999F;
            p.radiation -= 0.05F;
            p.parent.parentChunk.chunk.setUnsaved(true);
            if(p.radiation <= 0) {

                // If there is no more radiation and the radPocket is unsealed, set it to 0 and remove it
                p.radiation = 0;
                p.accumulatedRads = 0;
                itr.remove();
                p.parent.parentChunk.chunk.setUnsaved(true);
                //System.err.println("[Debug] Pocket is no longer radioactive, removing and continuing to next pocket...");
                continue;
            }
            //System.out.println("[Debug] Pocket is active, proceeding...");
            // Rad fog code goes here

            // Count the number of connections to other pockets we have
            float count = 0;
            for(Direction d : Direction.values())
                count += p.connectionIndices[d.ordinal()].size();

            float amountPer = 0.7F / count;

            if(count == 0 || p.radiation < 1) {

                // Don't update if we have no connections or our own radiation is less than 1
                // Prevents micro radiation bleeding
                amountPer = 0;
            }
            // Don't update if our own radiation is less than 1
            if(p.radiation < 1)
                amountPer = 0;

//            BlockPos chunkPos = new BlockPos(
//                    p.getSubChunkPos().getX() >> 4,
//                    ChunkStorageCompat.getIndexFromWorldY(p.getSubChunkPos().getY()),
//                    p.getSubChunkPos().getZ() >> 4);
//            //System.out.println("[Debug] Pocket " + p.index + " has " + count + " connections to other pockets at chunk " + chunkPos);
//            if (amountPer > 0) {
//                //System.out.println("[Debug] Pocket " + p.index + " will spread " + amountPer + " rads to each adjacent pocket");
//            }

            // This might also cause leaks from sealed pockets to unsealed ones
            // Only update other values if this one has radiation to update with
            if(p.radiation > 0 && amountPer > 0) {

                // For each direction...
                for(Direction e : Direction.values()) {

                    // Get the blockPos for the next sub chunk in that direction
                    BlockPos nPos = pos.relative(e, 16);

                    // If it's not loaded or it's out of bounds, do nothing
                    if(!p.parent.parentChunk.chunk.getLevel().isLoaded(nPos) || nPos.getY() < -64 || nPos.getY() > 319)
                        continue;

                    if(p.connectionIndices[e.ordinal()].size() == 1 && p.connectionIndices[e.ordinal()].get(0) == -1) {

                        // If the chunk in this direction isn't loaded, load it
                        rebuildChunkPockets(p.parent.parentChunk.chunk.getLevel().getChunkAt(nPos), ChunkStorageCompat.getIndexFromWorldY(nPos.getY()));
                    } else {

                        // Otherwise, for every pocket this chunk is connected to in this direction, add radiation to it
                        // Also add those pockets to the active pockets set
                        SubChunkRadiationStorage sc2 = getSubChunkStorage(p.parent.parentChunk.chunk.getLevel(), nPos);
                        for(int idx : p.connectionIndices[e.ordinal()]) {

                            // Don't spread to sealed pockets
                            if(!sc2.pockets[idx].isSealed()) {

                                // Only accumulated rads get updated so the system doesn't interfere with itself while working
                                sc2.pockets[idx].accumulatedRads += p.radiation * amountPer;
                                addActivePocket(sc2.pockets[idx]);
                            }
                        }
                    }
                }
            }
            if(amountPer != 0)
                p.accumulatedRads += p.radiation * 0.3F;

            // Make sure we only use around 20 ms max per tick, to help reduce lag
            // The lage should die down by itself after a few minutes when all radioactive chunks get built
            if(System.currentTimeMillis() - time > 20)
                break;
        }

        // Remove the pockets that reached 0, and set the actual radiation values to the accumulated values
        // We don't remove sealed pockets so that dimensions with background rads can be shielded against
        List<RadPocket> itrActiveCheck = new ArrayList<>(getActivePockets());
        itr = itrActiveCheck.iterator();
        while(itr.hasNext()) {

            RadPocket act = itr.next();
            act.radiation = act.accumulatedRads;
            act.accumulatedRads = 0;
            if(act.radiation <= 0) {

                removeActivePocket(act);
                itr.remove();
            }
        }

        //System.out.println("[Debug] Finished updating radiation");

        //Should ideally never happen because of the 20 ms limit,
        //but who knows, maybe it will, and it's nice to have debug output if it does
//        if(System.currentTimeMillis() - time > 50)
        //System.err.println("Rads took too long: " + (System.currentTimeMillis() - time));
    }

    public static void updateEntities(ServerLevel level) {

        if(level != null && !level.isClientSide /* && GeneralConfig.enableRads */ ) {

            Set<ChunkPos> processedChunks = new HashSet<>();

            for (Player playerI : level.players()) {
                ChunkPos playerChunkPos = new ChunkPos(playerI.blockPosition());
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        ChunkPos chunkPos = new ChunkPos(playerChunkPos.x + dx, playerChunkPos.z + dz);
                        if (processedChunks.add(chunkPos) && level.hasChunk(chunkPos.x, chunkPos.z)) {
//                            LevelChunk chunk = level.getChunk(chunkPos.x, chunkPos.z);
                            AABB chunkAABB = getAabb(level, chunkPos);
                            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, chunkAABB);

                            for (LivingEntity entity : entities) {

                                RadPocket ePoc = getPocket(level, entity.getOnPos().offset(0, 1, 0));
//                                System.out.println("[Debug] Got pocket " + ePoc + " at " + entity.getOnPos().offset(0, 1, 0) + " and adding " + ePoc.radiation + " rads to " + entity.getName().getString());
                                ContaminationUtil.contaminate(entity, ContaminationUtil.HazardType.RADIATION, ContaminationUtil.ContaminationType.CREATIVE, ePoc.radiation);
                            }
                        }
                    }
                }
            }
        }
    }

    private static RadPocket[] pocketsByBlock = null;
    private static BlockPos[] blocksByPocket = null;

    /**
     * Divides a 16x16x16 sub chunk into pockets that are separated by radiation-resistant blocks.
     * These pockets are also linked to other pockets in neighboring chunks
     * @param chunk - the chunk to rebuild
     * @param yIndex - the Y index of the sub chunk to rebuild
     */
    private static void rebuildChunkPockets(LevelChunk chunk, int yIndex) {

        BlockPos subChunkPos = new BlockPos(
                chunk.getPos().x << 4,
                ChunkStorageCompat.getWorldYFromIndex(yIndex),
                chunk.getPos().z << 4);

        //System.out.println("[Debug] Starting rebuild of chunk at " + new BlockPos(chunk.getPos().x, yIndex, chunk.getPos().z));

        //Initialize all the necessary variables. A list of pockets for the sub chunk, the block storage for this sub chunk,
        //an array of rad pockets for fast pocket lookup by blockpos, chunk radiation storage for this position
        //And finally a new sub chunk that will be added to the chunk radiation storage when it's filled with data
        List<RadPocket> pockets = new ArrayList<>();
        ExtendedBlockStorage blocks = ChunkStorageCompat.getBlockStorageArray(chunk)[yIndex];
        if(pocketsByBlock == null) {

            pocketsByBlock = new RadPocket[16*16*16];
        } else {

            Arrays.fill(pocketsByBlock, null);
        }
        if(blocksByPocket == null) {

            blocksByPocket = new BlockPos[0];
        } else {

            Arrays.fill(blocksByPocket, null);
        }
        ChunkRadiationStorage st = getChunkStorage(chunk.getLevel(), subChunkPos);
        SubChunkRadiationStorage subChunk = new SubChunkRadiationStorage(st, subChunkPos.getY(), null, null);

        if(blocks != null) {

            // Loop over every block in the sub chunk
            for(int x = 0; x < 16; x++) {

                for(int y = 0; y < 16; y++) {

                    for(int z = 0; z < 16; z++) {

//                        //System.out.println("[Debug] Checking position: " + subChunkPos.offset(x, y, z) + " in sub chunk: " + new BlockPos(chunk.getPos().x, yIndex, chunk.getPos().z) + " for if block is already apart of a pocket");
                        if(pocketsByBlock[x*16*16+y*16+z] != null) {

//                            //System.err.println("[Debug] Block is already apart of a pocket, skipping...");
                            continue;
                        }
//                        else {
//
//                            //System.out.println("[Debug] Block is not apart of a pocket");
//                        }

                        Block block = blocks.get(x, y, z).getBlock();
                        BlockPos globalPos = subChunkPos.offset(x, y, z);

                        BlockPos[] old = blocksByPocket;

                        blocksByPocket = new BlockPos[old.length+1];
                        for(int i = 0; i < old.length; i++) {

                            blocksByPocket[i] = old[i];
                        }
                        blocksByPocket[old.length] = globalPos;

//                        BlockPos globalPos = subChunkPos.offset(x, y, z);
//                        Block block = chunk.getLevel().getBlockState(globalPos).getBlock();

                        // If it's not a radiation-resistant block, and there isn't currently a pocket here,
                        // do a flood fill pocket build
                        if(!(block instanceof IRadResistantBlock && ((IRadResistantBlock) block).isRadResistant((ServerLevel) chunk.getLevel(), globalPos))) {

                            //System.out.println("[Debug] Block " + block + " at " + globalPos + " was not rad resistant; add pocket");

                            pockets.add(buildPocket(subChunk, chunk.getLevel(), new BlockPos(x, y, z), subChunkPos, blocks, pocketsByBlock, pockets.size()));
                        }
//                        else {
//
//                            //System.out.println("[Debug] Block " + block + " at " + globalPos + " was rad resistant; do not add pocket");
//                        }
                    }
                }
            }
        } else {

            RadPocket pocket = new RadPocket(subChunk, 0);

            for(int x = 0; x < 16; x++) {

                for(int y = 0; y < 16; y++) {

                    doEmptyChunk(chunk, subChunkPos, new BlockPos(x, 0, y), pocket, Direction.DOWN);
                    doEmptyChunk(chunk, subChunkPos, new BlockPos(x, 15, y), pocket, Direction.UP);
                    doEmptyChunk(chunk, subChunkPos, new BlockPos(x, y, 0), pocket, Direction.NORTH);
                    doEmptyChunk(chunk, subChunkPos, new BlockPos(x, y, 15), pocket, Direction.SOUTH);
                    doEmptyChunk(chunk, subChunkPos, new BlockPos(0, y, x), pocket, Direction.WEST);
                    doEmptyChunk(chunk, subChunkPos, new BlockPos(15, y, x), pocket, Direction.EAST);
                }
            }
            pockets.add(pocket);
        }
        // If there's only one pocket, we don't need to waste memory by storing a whole 16x16x16 array, so just store null
        subChunk.pocketsByBlock = pockets.size() == 1 ? null : pocketsByBlock;

        subChunk.blocksByPocket = pockets.size() == 1 ? null : blocksByPocket;

//        if (pockets.size() == 1) {
//            //System.err.println("[Debug] There was only a single pocket for subchunk at " + new BlockPos(chunk.getPos().x, yIndex, chunk.getPos().z));
//        } else {
//            //System.out.println("[Debug] There was " + pockets.size() + " pockets for subchunk at " + new BlockPos(chunk.getPos().x, yIndex, chunk.getPos().z));
//        }

        if(subChunk.pocketsByBlock != null)
            pocketsByBlock = null;
        if(subChunk.blocksByPocket != null)
            blocksByPocket = null;
        //noinspection ToArrayCallWithZeroLengthArrayArgument
        subChunk.pockets = pockets.toArray(new RadPocket[pockets.size()]);

        // Finally, put the newly built sub chunk into the chunk
        st.instance.setForYLevel(ChunkStorageCompat.getWorldYFromIndex(yIndex), subChunk);

        //System.out.println("[Debug] Finished rebuild of chunk at " + new BlockPos(chunk.getPos().x, yIndex, chunk.getPos().z));

    }

    private static void doEmptyChunk(LevelChunk chunk, BlockPos subChunkPos, BlockPos pos, RadPocket pocket, Direction facing) {

        BlockPos newPos = pos.relative(facing);
        BlockPos outPos = newPos.offset(subChunkPos);
        Block block = chunk.getLevel().getBlockState(outPos).getBlock();
        // If the block isn't radiation-resistant...
        if(!(block instanceof IRadResistantBlock && ((IRadResistantBlock) block).isRadResistant((ServerLevel) chunk.getLevel(), outPos))) {

            if(!isSubChunkLoaded(chunk.getLevel(), outPos)) {

                // If it's not loaded, mark it with a single -1 value. This will tell the update method that the
                // Chunk still needs to be loaded to propagate radiation into it
                if(!pocket.connectionIndices[facing.ordinal()].contains(-1)) {

                    pocket.connectionIndices[facing.ordinal()].add(-1);
                }
            } else {

                // If it is loaded, see if the pocket at that position is already connected to us. If not, add it as a connection.
                // Setting outPocket's connection will be handled in setForYLevel

                RadPocket outPocket = getPocket(chunk.getLevel(), outPos);
                if(!pocket.connectionIndices[facing.ordinal()].contains(outPocket.index))
                    pocket.connectionIndices[facing.ordinal()].add(outPocket.index);
            }
        }
    }

    private static final Queue<BlockPos> stack = new ArrayDeque<>(1024);

    /**
     * Builds a pocket using a flood fill.
     * @param subChunk - sub chunk to build a pocket in
     * @param level - world we're building in
     * @param start - the block pos to flood fill from
     * @param subChunkWorldPos - the world position of the sub chunk
     * @param chunk - the block storage to pull blocks from
     * @param pocketsByBlock - the array to populate with the flood fill
     * @param index - the current pocket number
     * @return a new rad pocket made from the flood fill data
     */
    private static RadPocket buildPocket(SubChunkRadiationStorage subChunk, Level level, BlockPos start,
                                         BlockPos subChunkWorldPos, ExtendedBlockStorage chunk,
                                         RadPocket[] pocketsByBlock, int index) {

        // Create the new pocket we're going to use
        RadPocket pocket = new RadPocket(subChunk, index);

//        BlockPos chunkPos = new BlockPos(
//                pocket.getSubChunkPos().getX() >> 4,
//                (pocket.getSubChunkPos().getY() >> 4) - 64,
//                pocket.getSubChunkPos().getZ() >> 4
//        );
        //System.out.println("[Debug] Starting build of pocket of index " + index + " for chunk at " + chunkPos + ", at local position " + start);

        // Make sure stack is empty
        stack.clear();
        stack.add(start);
        // Flood fill
        while(!stack.isEmpty()) {

            BlockPos pos = stack.poll();
            Block block = chunk.get(pos.getX(), pos.getY(), pos.getZ()).getBlock();

            // If the block is radiation-resistant, or we've already flood-filled here, continue
            if(pocketsByBlock[pos.getX()*16*16+pos.getY()*16+pos.getZ()] != null)
                continue;

            // Set the current position in the array to be this pocket
            pocketsByBlock[pos.getX()*16*16+pos.getY()*16+pos.getZ()] = pocket;

            // For each direction...
            for(Direction facing : Direction.values()) {

                BlockPos newPos = pos.relative(facing);
                if(Math.max(Math.max(newPos.getX(), newPos.getY()), newPos.getZ()) > 15 ||
                        Math.min(Math.min(newPos.getX(), newPos.getY()), newPos.getZ()) < 0) {

                    // If we're outside the sub chunk bounds, try to connect to neighboring chunk pockets
                    BlockPos outPos = newPos.offset(subChunkWorldPos);

                    // If this position is out of bounds, do nothing
                    if(outPos.getY() < -64 || outPos.getY() > 319)
                        continue;

                    // Will also attempt to load the chunk, which will cause neighbor data to be updated correctly if it's unloaded
                    block = level.getBlockState(outPos).getBlock();
                    // If the block isn't radiation-resistant...
                    if(!(block instanceof IRadResistantBlock && ((IRadResistantBlock) block).isRadResistant((ServerLevel) level, outPos))) {

                        if(!isSubChunkLoaded(level, outPos)) {

                            // If it's not loaded, mark it with a single -1 value. This will tell the update method that the
                            // Chunk still needs to be loaded to propagate radiation into it
                            if(!pocket.connectionIndices[facing.ordinal()].contains(-1)) {

                                pocket.connectionIndices[facing.ordinal()].add(-1);
                            }
                        } else {

                            // If it is loaded, see if the pocket at that position is already connected to us. If not, add it as a connection
                            // Setting outPocket's connection will be handled in setForYLevel
                            RadPocket outPocket = getPocket(level, outPos);
                            if(!pocket.connectionIndices[facing.ordinal()].contains(outPocket.index))
                                pocket.connectionIndices[facing.ordinal()].add(outPocket.index);
                        }
                    }
                    continue;
                }
                // Add the new position onto the stack, to be flood-fill checked later
                if(!(block instanceof IRadResistantBlock && ((IRadResistantBlock) block).isRadResistant((ServerLevel) level, pos.offset(subChunkWorldPos)))) {

//                    System.err.println("[Debug] block is not rad resistant: " + pos.offset(subChunkWorldPos));
                    stack.add(newPos);
                }
//                else {
//
//                    System.err.println("[Debug] block is rad resistant: " + pos.offset(subChunkWorldPos) + " (Resistance: " + ((IRadResistantBlock) block).getResistance() + ")");
//                }
            }
        }

//        chunkPos = new BlockPos(
//                pocket.getSubChunkPos().getX() >> 4,
//                ChunkStorageCompat.getIndexFromWorldY(pocket.getSubChunkPos().getY()),
//                pocket.getSubChunkPos().getZ() >> 4);
        //System.out.println("[Debug] Finished build of pocket of index " + index + " for chunk at " + chunkPos + ", at local position " + start);

        return pocket;
    }

    /**
     * Helper that mimics the behavior of 1.12.2's Chunk.getBlockStorageArray(),
     * returning a 16-element array of LevelChunkSection, each representing a 16x16x16 Y-slice of the chunk.
     */
    public static class ChunkStorageCompat {

        /**
         * Returns an array of 24 vertical chunk sections covering Y = -64 to 319,
         * mimicking ExtendedBlockStorage[] in 1.12.2.
         * Each index corresponds to a 16-block vertical range (0 = Y -64 to -49, ..., 23 = Y 304 to 319).
         *
         * @param chunk The LevelChunk to extract sections from
         * @return LevelChunkSection[24] matching the 1.12.2 vertical layout
         */
        public static ExtendedBlockStorage[] getBlockStorageArray(LevelChunk chunk) {
            LevelChunkSection[] modernSections = chunk.getSections();
            ExtendedBlockStorage[] legacyCompat = new ExtendedBlockStorage[24];

            for (int i = 0; i < modernSections.length; i++) {
                LevelChunkSection section = modernSections[i];
                if (section == null) continue;
                legacyCompat[i] = new ExtendedBlockStorage(section); // In 1.20.1, index 0 = Y -64 to -49
            }
            return legacyCompat;
        }

        public static class ExtendedBlockStorage {

            private final LevelChunkSection levelChunkSection;

            public ExtendedBlockStorage(LevelChunkSection pLevelChunkSection) {

                levelChunkSection = pLevelChunkSection;
            }

            public BlockState get(int x, int y, int z) {

                return getBlockState(levelChunkSection, x, y, z);
            }

            public void set(int x, int y, int z, BlockState state) {

                setBlockState(levelChunkSection, x, y, z, state);
            }
        }

        /**
         * Gets the block state from local chunk section coords like in 1.12.2.
         *
         * @param section The LevelChunkSection (can be null)
         * @param x Local X (0–15)
         * @param y Local Y (0–15)
         * @param z Local Z (0–15)
         * @return BlockState or null if section is null
         */
        public static BlockState getBlockState(LevelChunkSection section, int x, int y, int z) {
            if (section == null) return null;
            return section.getBlockState(x, y, z);
        }

        /**
         * Sets the block state in the given section at the local (0–15) coordinates.
         *
         * @param section The LevelChunkSection (must not be null)
         * @param x Local X (0–15)
         * @param y Local Y (0–15)
         * @param z Local Z (0–15)
         * @param state The BlockState to set
         */
        public static void setBlockState(LevelChunkSection section, int x, int y, int z, BlockState state) {
            if (section != null) {
                section.setBlockState(x, y, z, state);

            }
        }

        /**
         * Converts a world Y value (-64 to 319) to its corresponding section index (0 to 23).
         *
         * @param y World Y coordinate
         * @return Section index (0–23), or 0/23 if out of bounds
         */
        public static int getIndexFromWorldY(int y) {
            if (y < -64) return 0;
            else if (y > 319) return 23;
//            //System.err.println("[Debug] getting index for y: " + y + " = " + ((y + 64) >> 4));
            return (y + 64) >> 4;
        }

        /**
         * Converts a section index (0 to 23) to its corresponding world Y value (-64 to 319).
         *
         * @param idx Section index (0–23)
         * @return World Y coordinate, or -64/319 if out of bounds
         */
        public static int getWorldYFromIndex(int idx) {
            if (idx < 0) return -64;
            else if (idx > 23) return 319;

            return (idx << 4) - 64;
        }
    }

    /**
     * Gets the chunk at the specified pos. Loads it if it doesn't exist
     * @param level - the world to get the chunk storage from
     * @param pos - the position of the chunk
     * @return the chunk radiation storage at the specified position
     */
    public static ChunkRadiationStorage getChunkStorage(Level level, BlockPos pos) {

        AtomicReference<IChunkRadiation> stAtomic = new AtomicReference<>();
        ChunkRadiationStorage st;

        level.getChunkAt(pos).getCapability(CHUNK_RAD_CAPABILITY, null).ifPresent(stAtomic::set);
        st = stAtomic.get().getParent();

        return st;
    }
    public static ChunkRadiationStorage getChunkStorage(LevelChunk chunk) {

        AtomicReference<IChunkRadiation> stAtomic = new AtomicReference<>();
        ChunkRadiationStorage st;

        chunk.getCapability(CHUNK_RAD_CAPABILITY, null).ifPresent(stAtomic::set);
        st = stAtomic.get().getParent();

        return st;
    }

    public static void incrementRad(ServerLevel level, BlockPos pos, float amount, float max) {

        if(pos.getY() < -64 || pos.getY() > 319 || !level.isLoaded(pos))
            return;

        RadPocket p = getPocket(level, pos);
        if(p.radiation < max) {

            p.radiation += amount;
        }
        // Mark this pocket as active so it gets updated
        if(amount > 0) {
//            System.out.println(p.radiation);
            addActivePocket(p);
        }
    }

    public static void decrementRad(ServerLevel level, BlockPos pos, float amount) {

        if(pos.getY() < -64 || pos.getY() > 319 || !level.isLoaded(pos))
            return;

        RadPocket p = getPocket(level, pos);
        p.radiation -= Math.max(amount, 0);
        if(p.radiation < 0) {

            p.radiation = 0;
        }
    }

    public static void setRadForCoord(ServerLevel level, BlockPos pos, float amount) {

        RadPocket p = getPocket(level, pos);
        p.radiation = Math.max(amount, 0);
        // If the amount is greater than 0, make sure to mark it as dirty so it gets updated
        if(amount > 0){

            addActivePocket(p);
        }
    }

    public static float getRadForCoord(Level level, BlockPos pos) {

        // If it's not loaded, assume there's no radiation. Makes sure to not keep a lot of chunks loaded
        if(!isSubChunkLoaded(level, pos))
            return 0;

        // If no pockets, assume no radiation
        if (getPocket(level, pos) == null)
            return 0;

        return getPocket(level, pos).radiation;
    }

    // -----------------------------------
    // IChunkRadiation INTERFACE
    // -----------------------------------

    /**
     * Capability interface for storing 3D radiation per chunk.
     */
    public interface IChunkRadiation {

        ChunkRadiationStorage getParent();

        void setParent(ChunkRadiationStorage pParent);

//        /**
//         * Rebuilds (flood-fills) all radPockets in each of the 24 subchunks of this chunk.
//         * Should be called on chunk load or when blocks change.
//         */
//        void rebuildAllPockets(ServerLevel world, LevelChunk chunk);

        SubChunkRadiationStorage getForYLevel(int y);

        void setForYLevel(int y, SubChunkRadiationStorage sc);

        void setInitialized();

        boolean wasInitialized();

        void unload();

        /**
         * Called each server tick to diffuse and decay radiation in all active pockets.
         */
//        void tickSpreadAndDecay(ServerLevel world, LevelChunk chunk);

        CompoundTag serializeNBT();

        void deserializeNBT(CompoundTag nbt);
    }

    // -----------------------------------
    // RadPocket CLASS
    // -----------------------------------

    /**
     * Represents one contiguous “open” region (air or non-rad-resistant) in a 16×16×16 subchunk.
     */
    public static class RadPocket {
        public final SubChunkRadiationStorage parent;
        public final int index;           // 0..(pocketsInThisSubchunk−1)
        public float radiation = 0f;      // Current stored radiation value
        public float accumulatedRads = 0f;// Temporary accumulator for simultaneous spreading

        /** For each of the six directions, holds indices of connected pockets, or −1 if neighbor subchunk not loaded */
        @SuppressWarnings("unchecked")
        public final List<Integer>[] connectionIndices = new ArrayList[Direction.values().length];

        public RadPocket(SubChunkRadiationStorage parent, int index) {
            this.parent = parent;
            this.index = index;
            for (int i = 0; i < Direction.values().length; i++) {
                connectionIndices[i] = new ArrayList<>(1);
            }
        }

        protected void remove() {

            for(Direction d : Direction.values()) {

                connectionIndices[d.ordinal()].clear();
            }
            removeActivePocket(this);
        }

        public BlockPos getSubChunkPos() {

            return parent.parentChunk.getWorldPos(parent.yLevel);
        }

        /** Checks whether this pocket is fully sealed (no adjacent pockets, no unloaded connections). */
        @SuppressWarnings("BooleanMethodIsAlwaysInverted")
        public boolean isSealed() {
            int count = 0;
            for (Direction d : Direction.values()) {
                count += connectionIndices[d.ordinal()].size();
            }
            return (count == 0);
        }
    }

    // -----------------------------------
    // SubChunkRadiationStorage CLASS
    // -----------------------------------

    /**
     * Stores all radPocket data for exactly one 16×16×16 vertical slice (“subchunk”) of a chunk.
     * There are 24 such slices per chunk, because world Y = –63..319 (384/16 = 24).
     */
    public static class SubChunkRadiationStorage {
        public final ChunkRadiationStorage parentChunk; // Back-pointer to the chunk capability
        public final int yLevel;                      // -64 to 319
        public RadPocket[] pockets;                    // All pockets found in this subchunk
        public RadPocket[] pocketsByBlock;
        public BlockPos[] blocksByPocket;

        BlockPos subChunkPos;

        public SubChunkRadiationStorage(ChunkRadiationStorage parent, int yLevel, RadPocket[] pockets, RadPocket[] pocketsByBlock) {
            this.parentChunk = parent;
            this.yLevel = yLevel;
            this.pocketsByBlock = pocketsByBlock;
            this.pockets = pockets;

            this.subChunkPos = new BlockPos(
                    parentChunk.chunk.getPos().x << 4,
                    yLevel,
                    parentChunk.chunk.getPos().z << 4);
        }

        public BlockPos getBlock(Integer pIndex) {

            if(blocksByPocket == null) {

                return subChunkPos;
            } else if(blocksByPocket.length > pIndex) {

                BlockPos pos = blocksByPocket[pIndex];
                if(pos == null) {

                    if(blocksByPocket[0] != null) {

                        return blocksByPocket[0];
                    } else {

                        pos = subChunkPos;
                        blocksByPocket[0] = pos;
                        return pos;
                    }
                } else {

                    return pos;
                }
            }

            return subChunkPos;
        }

        /**
         * Gets the pocket at the position
         * @param pos - the position to get the pocket at
         * @return the pocket at the specified position, or the first pocket if it doesn't exist
         */
        public RadPocket getPocket(BlockPos pos) {

            if(pocketsByBlock == null) {
                //If pocketsByBlock is null, there's only one pocket anyway
                return pockets[0];
            } else {

                int x = pos.getX()&15;
                int y = pos.getY()&15;
                int z = pos.getZ()&15;
                RadPocket p = pocketsByBlock[x*16*16+y*16+z];
                if(p == null) {

                    if(pockets != null && pockets.length > 0 && pockets[0] != null) {
                        // If for whatever reason there isn't a pocket there, return the first pocket as a fallback if present
                        return pockets[0];
                    } else {
                        // if the first pocket isn't present either, create one
                        p = new RadPocket(this, 0);
                        p.radiation = 0;
                        if(pockets == null || pockets.length == 0) {

                            pockets = new RadPocket[1];
                        }
                        pockets[0] = p;
                        return p;
                    }
                } else {

                    return p;
                }
            }
        }

        /**
         * Attempts to distribute radiation from another sub chunk into this one's pockets.
         * @param other - the sub chunk to set from
         */
        public void setRad(SubChunkRadiationStorage other) {

            // Accumulate a total and divide that evenly between our pockets
            float total = 0;
            for(RadPocket p : other.pockets) {
                if(!p.isSealed()) {
                    // Sealed pockets should not contribute to total rad count
                    total += p.radiation;
                }
            }

            float radPer = total / pockets.length;
            for(RadPocket p : pockets) {

                p.radiation = radPer;
                if(radPer > 0) {
                    // If the pocket now has radiation or is sealed, mark it as active
                    addActivePocket(p);
                }
            }
        }

        /**
         * Remove from the world
         * @param level - the world to remove from
         * @param pos - the pos to remove from
         */
        public void remove(Level level, BlockPos pos) {

            for(RadPocket p : pockets) {
                // Call remove for each pocket
                p.remove();
            }
            for(Direction d : Direction.values()) {
                // Tries to load the chunk so it updates right
                level.getBlockState(pos.relative(d, 16));
                if(isSubChunkLoaded(level, pos.relative(d, 16))) {

                    SubChunkRadiationStorage sc = getSubChunkStorage(level, pos.relative(d, 16));

                    // Clears any connections the neighboring chunk has to this sub chunk
                    for(RadPocket p : sc.pockets) {

                        p.connectionIndices[d.getOpposite().ordinal()].clear();
                    }
                }
            }
        }

        /**
         * Adds to the world
         * @param world - the world to add to
         * @param pos - the position to add to
         */
        public void add(Level world, BlockPos pos){
            for(Direction e : Direction.values()){
                // Tries to load the chunk so it updates right.
                world.getBlockState(pos.relative(e, 16));
                if(isSubChunkLoaded(world, pos.relative(e, 16))){
                    SubChunkRadiationStorage sc = getSubChunkStorage(world, pos.relative(e, 16));
                    // Clear all the neighbor's references to this sub chunk
                    for(RadPocket p : sc.pockets){
                        p.connectionIndices[e.getOpposite().ordinal()].clear();
                    }
                    // Sync connections to the neighbor to make it two ways
                    for(RadPocket p : pockets){
                        List<Integer> indc = p.connectionIndices[e.ordinal()];
                        for(int idx : indc){
                            sc.pockets[idx].connectionIndices[e.getOpposite().ordinal()].add(p.index);
                        }
                    }
                }
            }
        }
    }

    // -----------------------------------
    // ChunkRadiationStorage (Capability) CLASS
    // -----------------------------------

    /**
     * Capability implementation attached to each LevelChunk. Holds 24 SubChunkRadiationStorage instances (one per vertical slice).
     */
    public static class ChunkRadiationStorage implements ICapabilitySerializable<CompoundTag> {
        public final LevelChunk chunk;
        private final IChunkRadiation instance = new Impl();
        public final SubChunkRadiationStorage[] subData = new SubChunkRadiationStorage[24];

        public ChunkRadiationStorage(LevelChunk chunk) {
            this.chunk = chunk;
            instance.setParent(this);
        }

        /**
         * Gets the world position of this chunk, using the specified y coordinate
         * @param y - the y coordinate for the returned position
         * @return a blockPos with x and z from the chunk and y from the parameter
         */
        public BlockPos getWorldPos(int y) {

            return new BlockPos(chunk.getPos().x << 4, y, chunk.getPos().z << 4);
        }

        /** The actual implementation of IChunkRadiation */
        private class Impl implements IChunkRadiation {
            //            private final Set<RadPocket> activePockets = new HashSet<>();
            private ChunkRadiationStorage parent;

            private boolean wasInitialized = false;

            @Override
            public ChunkRadiationStorage getParent() {

                return parent;
            }

            @Override
            public void setParent(ChunkRadiationStorage pParent) {

                parent = pParent;
            }

            /**
             * Gets the sub chunk for the specified y coordinate
             * @param y - the y coordinate of the sub chunk
             * @return the sub chunk at the y coordinate
             */
            @Override
            public SubChunkRadiationStorage getForYLevel(int y) {
                // Add 64 and right shift it by 4 to get the index, because each subchunk is 16 high and spans from -64 to 319
                int index = ChunkStorageCompat.getIndexFromWorldY(y);
                // If the index is out of range, return null. Otherwise, return the chunk at the index
                if(index < 0 || index > subData.length) {

                    return null;
                }
                return subData[index];
            }

            /**
             * Sets the sub chunk at the y level to the new sub chunk
             * @param y - the y level to set
             * @param sc - the new sub chunk
             */
            @Override
            public void setForYLevel(int y, SubChunkRadiationStorage sc) {

                int index = ChunkStorageCompat.getIndexFromWorldY(y);

                if(subData[index] != null) {

                    // If there's already a sub chunk here, make sure to remove it from the world safely
                    subData[index].remove(chunk.getLevel(), getWorldPos(y));

                    // If we're not nullifying it, set the new chunk's radiation to preserve rads
                    if(sc != null)
                        sc.setRad(subData[index]);
                }
                // If we're not nullifying it, add it to the world to update neighboring chunks and stuff like that
                if(sc != null)
                    sc.add(chunk.getLevel(), getWorldPos(y));
                subData[index] = sc;
            }

            @Override
            public void setInitialized() {

                wasInitialized = true;
            }

            @Override
            public boolean wasInitialized() {
                return wasInitialized;
            }

            /**
             * Removes all active pockets on unload
             */
            @Override
            public void unload() {

                for(int y = 0; y < subData.length; y++) {

                    if(subData[y] == null)
                        continue;
                    for(RadPocket p : subData[y].pockets) {

                        removeActivePocket(p);
                    }
                    subData[y] = null;
                }
            }

            /**
             * Gets the index of the rad pocket in the array of pockets
             * There's probably a helper method for this in the Arrays class or something, but this works fine too
             * @param p - the pocket to find the index of
             * @param pockets - the array to search in
             * @return the index of the pocket in the array, -1 if not present
             */
            public short arrayIndex(RadPocket p, RadPocket[] pockets){
                for(short i = 0; i < pockets.length; i ++){
                    if(p == pockets[i])
                        return i;
                }
                return -1;
            }

            @Override
            public CompoundTag serializeNBT() {
                CompoundTag root = new CompoundTag();
                for (int i = 0; i < 24; i++) {
                    SubChunkRadiationStorage sc = subData[i];
                    if (sc == null) continue;

                    CompoundTag subTag = new CompoundTag();

                    // Save each RadPocket
                    ListTag pocketListNBT = new ListTag();
                    for (RadPocket p : sc.pockets) {
                        CompoundTag pTag = new CompoundTag();
                        pTag.putInt("index", p.index);
                        pTag.putFloat("radiation", p.radiation);
                        // Save connectionIndices for each direction
                        for (Direction d : Direction.values()) {
                            IntArrayTag connArr = new IntArrayTag(
                                    p.connectionIndices[d.ordinal()].stream().mapToInt(Integer::intValue).toArray()
                            );
                            pTag.put("conn_" + d.getSerializedName(), connArr);
                        }

                        pocketListNBT.add(pTag);
                    }
                    subTag.put("pockets", pocketListNBT);
                    root.put("sub_" + i, subTag);

                    // Save pocketsByBlock
                    if(sc.pocketsByBlock == null)
                        subTag.putByte("p_nul", (byte)0);
                    else {

                        subTag.putByte("p_nul", (byte)1);
                        for(int j = 0; j < sc.pocketsByBlock.length; j++) {

                            RadPocket p = sc.pocketsByBlock[j];
                            subTag.putShort("pbb_" + j, arrayIndex(p, sc.pockets));
                        }
                    }
                    // Save blocksByPocket
                    if(sc.blocksByPocket == null)
                        subTag.putByte("b_nul", (byte)0);
                    else {

                        subTag.putByte("b_nul", (byte)1);
                        for(int j = 0; j < sc.blocksByPocket.length; j++) {

                            BlockPos b = sc.getBlock(j);
                            subTag.putLong("bbp_" + j, b.asLong());
                        }
                    }
                }
                root.putBoolean("was_initialized", wasInitialized);
                return root;
            }

            @Override
            public void deserializeNBT(CompoundTag nbt) {
                for (int i = 0; i < 24; i++) {
                    String key = "sub_" + i;
                    if (nbt.contains(key, Tag.TAG_COMPOUND)) {

                        CompoundTag subTag = nbt.getCompound(key);

                        SubChunkRadiationStorage sc = new SubChunkRadiationStorage(ChunkRadiationStorage.this, ChunkStorageCompat.getWorldYFromIndex(i), null, null);

                        // Load RadPockets
                        ListTag pocketListNBT = subTag.getList("pockets", Tag.TAG_COMPOUND);
                        sc.pockets = new RadPocket[pocketListNBT.size()];
                        for (int pi = 0; pi < pocketListNBT.size(); pi++) {
                            CompoundTag pTag = pocketListNBT.getCompound(pi);
                            int idx = pTag.getInt("index");
                            RadPocket p = new RadPocket(sc, idx);
                            p.radiation = pTag.getFloat("radiation");
                            // Load connectionIndices
                            for (Direction d : Direction.values()) {
                                int[] connArr = pTag.getIntArray("conn_" + d.getSerializedName());
                                for (int val : connArr) {
                                    p.connectionIndices[d.ordinal()].add(val);
                                }
                            }

                            sc.pockets[idx] = p;
                        }

                        // Load pocketsByBlock
                        boolean perBlockDataExists = subTag.getByte("p_nul") == 1;
                        if(perBlockDataExists) {

                            // If the per block data exists, read indices sequentially and set each array slot to the rad pocket at that index
                            sc.pocketsByBlock = new RadPocket[16*16*16];
                            for(int j = 0; j < 16*16*16 ; j++) {

                                int idx = subTag.getShort("pbb_" + j);
                                if(idx >= 0)
                                    sc.pocketsByBlock[j] = sc.pockets[idx];
                            }
                        }
                        // Load blocksByPocket
                        perBlockDataExists = subTag.getByte("b_nul") == 1;
                        if(perBlockDataExists) {

                            // If the per block data exists, read indices sequentially and set each array slot to the rad pocket at that index
                            sc.blocksByPocket = new BlockPos[sc.pockets.length];
                            for(int j = 0; j < sc.pockets.length ; j++) {

                                BlockPos pos = BlockPos.of(subTag.getLong("bbp_" + j));
                                sc.blocksByPocket[j] = pos;
                            }
                        }
                        subData[i] = sc;
                    } else {

                        subData[i] = null;
                    }
                }
                wasInitialized = nbt.contains("was_initialized") && nbt.getBoolean("was_initialized");
            }
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            if (cap == CHUNK_RAD_CAPABILITY) {
                return LazyOptional.of(() -> instance).cast();
            }
            return LazyOptional.empty();
        }

        @Override
        public CompoundTag serializeNBT() {
            return instance.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            instance.deserializeNBT(nbt);
        }
    }

    // -----------------------------------
    // RadiationEventHandlers CLASS
    // -----------------------------------

//    private static final Set<LevelChunk> loadedChunks = Collections.synchronizedSet(new HashSet<>());

    @Mod.EventBusSubscriber(modid = "hbm", bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class RadiationEventHandlers {

//        private static final Set<LevelChunk> chunksWithRebuildRequests = Collections.synchronizedSet(new HashSet<>());

        private static final ResourceLocation RADIATION_CAP_KEY = ResourceLocation.fromNamespaceAndPath("hbm", "chunk_radiation");

        @SubscribeEvent
        public static void attachChunkRadiation(AttachCapabilitiesEvent<LevelChunk> event) {
            LevelChunk chunk = event.getObject();
            ChunkRadiationStorage storage = new ChunkRadiationStorage(chunk);
            event.addCapability(RADIATION_CAP_KEY, storage);
        }

        @SubscribeEvent
        public static void onChunkUnload(ChunkEvent.Unload e) {

            if(e.getLevel().isClientSide())
                return;

            // When the chunk is unloaded, also unload it from our radiation data if it exists
            ((LevelChunk) e.getChunk()).getCapability(CHUNK_RAD_CAPABILITY).ifPresent(IChunkRadiation::unload);
        }

//        private static final LongSet processedChunks = new it.unimi.dsi.fastutil.longs.LongOpenHashSet();
//
//        private static long chunkKey(LevelChunk chunk) {
//            // chunk.getPos().x and z are ints; pack into a long
//            ChunkPos pos = chunk.getPos();
//            return (((long) pos.x) << 32) | (pos.z & 0xFFFFFFFFL);
//        }

        @SubscribeEvent
        public static void onChunkLoad(ChunkEvent.Load e) {

            if(e.getLevel().isClientSide())
                return;

            LevelChunk chunk = (LevelChunk)e.getChunk();

//            long key = chunkKey(chunk);

//            if(processedChunks.contains(key))
//                return;

            if(getChunkStorage(chunk).instance.wasInitialized()) {

//                System.out.println("[Debug] chunk already initialized: " + chunk.getPos().toString());
                return;
            }
//            else
//                System.err.println("[Debug] chunk not initialized: " + chunk.getPos().toString());

            BlockPos.MutableBlockPos mPos = new BlockPos.MutableBlockPos();

            int baseX = chunk.getPos().getMinBlockX();
            int baseZ = chunk.getPos().getMinBlockZ();

            LevelChunkSection[] sections = chunk.getSections();
            for(int i = 0; i < sections.length; i++) {

                LevelChunkSection section = sections[i];

                if(section == null || section.hasOnlyAir()) continue;

                int sectionBaseY = (i << 4) -64;

                for(int lx = 0; lx < 16; lx++) {

                    int wx = baseX + lx;
                    for(int ly = 0; ly < 16; ly++) {

                        int wy = sectionBaseY + ly;
                        for(int lz = 0; lz < 16; lz++) {

                            int wz = baseZ + lz;

                            if(section.getBlockState(lx, ly, lz).getBlock() instanceof HazardBlock hb) {

                                mPos.set(wx, wy, wz);
                                hb.onGenerated((ServerLevel)e.getLevel(), mPos);
                            }
                        }
                    }
                }
            }
//            processedChunks.add(key);
            getChunkStorage(chunk).instance.setInitialized();
        }

        static boolean iteratingDirty;
        static Set<BlockPos> dirtyChunks = new HashSet<>();
        static Set<BlockPos> dirtyChunks2 = new HashSet<>();

        public static void markChunkForRebuild(Level level, BlockPos pos) {

            // I'm using this blockPos as a sub chunk pos
            BlockPos chunkPos = new BlockPos(pos.getX() >> 4, ChunkStorageCompat.getIndexFromWorldY(pos.getY()), pos.getZ() >> 4);
            //System.out.println("[Debug] Converting " + pos + " to chunk position: " + chunkPos);


            //System.out.println("[Debug] Marking chunk dirty at " + chunkPos);

            if(iteratingDirty)
                dirtyChunks2.add(chunkPos);
            else
                dirtyChunks.add(chunkPos);
        }

        private static void rebuildDirty(Level level) {

            boolean hadDirty = false;

            // Set iteration flag to avoid concurrent modification
            iteratingDirty = true;

            // For each dirty sub chunk, rebuild it
            for(BlockPos dirtyChunkPos : dirtyChunks) {

                //System.out.println("[Debug] Rebuilding chunk pockets for dirty chunk at " + dirtyChunkPos);

                rebuildChunkPockets(level.getChunk(dirtyChunkPos.getX(), dirtyChunkPos.getZ()), dirtyChunkPos.getY());
                hadDirty = true;
            }

            iteratingDirty = false;
            // Clear the dirty chunks lists, and add any chunks that might have been marked while iterating to be dealt with next tick.
            dirtyChunks.clear();
            dirtyChunks.addAll(dirtyChunks2);
            dirtyChunks2.clear();
        }

        public static final int FRAME_COUNT = 13;
        public static final ResourceLocation[] FRAMES = new ResourceLocation[FRAME_COUNT];

        static {
            for (int i = 0; i < FRAME_COUNT; i++) {
                FRAMES[i] = ResourceLocation.fromNamespaceAndPath(HBM.MOD_ID, "textures/gui/jmpscr/frame_" + String.format("%02d", i) + ".png");
            }
        }

        @SubscribeEvent
        public static void onUpdate(TickEvent.ServerTickEvent e) {

//            //System.out.println("[Debug] onUpdate called for RadSys tick " + ticks);

//            System.out.println(activePockets.size());

            if(e.phase == TickEvent.Phase.END) {

                ticks ++;
                if(ticks % 20 == 17) {

                    long mil = System.nanoTime();

                    // Every second, do a full system update, which will spread around radiation and all that
                    updateRadiation();

                    for(ServerLevel level : e.getServer().getAllLevels()) {

                        updateEntities(level);
                        if(level.random.nextInt(1000000) == 0)
                            GeigerCounterItem.jmp = true;
                    }

                    //System.out.println("rad tick event took: " + (System.nanoTime()-mil));
                }
            }

            // Make sure any chunks marked as dirty by radiation-resistant blocks are rebuilt instantly
            for(ServerLevel level : e.getServer().getAllLevels())
                rebuildDirty(level);
        }

        static Map<BlockPos, Integer> activePositions = new HashMap<>();

        @SubscribeEvent
        public static void onWorldLoad(LevelEvent.Load event) {
            LevelAccessor level = event.getLevel();
            if (!level.isClientSide()) {
                // Server-side world has just loaded

                activePositions = loadMapFromJson((ServerLevel)level, "data/hbm/rad_vectors.json");

                Iterator<Map.Entry<BlockPos, Integer>> itr = activePositions.entrySet().iterator();

                while(itr.hasNext()) {

                    Map.Entry<BlockPos, Integer> nxt = itr.next();

//                    System.err.println("[Debug] Populating rad pocket starting at: " + nxt.getKey() + ", with " + nxt.getValue() + " rads");

                    LevelChunk chunk = ((ServerLevel) level).getChunkAt(nxt.getKey());
                    rebuildChunkPockets(chunk, ChunkStorageCompat.getIndexFromWorldY(nxt.getKey().getY()));

                    setRadForCoord((ServerLevel)level, nxt.getKey(), nxt.getValue());
                }
            }
        }

        @SubscribeEvent
        public static void onWorldUnload(PlayerEvent.PlayerLoggedOutEvent event) throws IOException {
            LevelAccessor level = event.getEntity().level();
            if (!level.isClientSide()) {
                // Runs during server world unload (including shutdown)

//                System.err.println(activePockets.size());

                activePositions.clear();

                for(RadPocket p : activePockets) {

                    activePositions.put(
                            p.parent.getBlock(p.index),
                            Math.round(p.radiation)
                    );
                }

                saveMapToJson((ServerLevel)level, activePositions, "data/hbm/rad_vectors.json");
            }
        }

        public static void saveMapToJson(ServerLevel level, Map<BlockPos, Integer> map, String fileName) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            Map<String, Integer> jsonMap = new HashMap<>();
            for (Map.Entry<BlockPos, Integer> entry : map.entrySet()) {
                jsonMap.put(entry.getKey().getX()+":"+entry.getKey().getY()+":"+entry.getKey().getZ(), entry.getValue());
            }

            Path path = level.getServer().getWorldPath(LevelResource.ROOT).resolve(fileName);
            try {

                Files.createDirectories(path.getParent());

                if(activePositions.isEmpty())
                    Files.deleteIfExists(path);

                try (Writer writer = Files.newBufferedWriter(path)) {
                    gson.toJson(jsonMap, writer);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static Map<BlockPos, Integer> loadMapFromJson(ServerLevel level, String fileName) {
            Gson gson = new Gson();
            Type mapType = new TypeToken<Map<String, Integer>>() {}.getType();

            Path path = level.getServer().getWorldPath(LevelResource.ROOT).resolve(fileName);
            if (!Files.exists(path)) return new HashMap<>();

            try (Reader reader = Files.newBufferedReader(path)) {
                Map<String, Integer> jsonMap = gson.fromJson(reader, mapType);
                Map<BlockPos, Integer> result = new HashMap<>();
                for (Map.Entry<String, Integer> entry : jsonMap.entrySet()) {

                    String[] strA = entry.getKey().split(":");

                    BlockPos vecR = new BlockPos(
                            Integer.parseInt(strA[0]),
                            Integer.parseInt(strA[1]),
                            Integer.parseInt(strA[2]));

                    result.put(vecR, entry.getValue());
                }
                return result;
            } catch (IOException e) {
                e.printStackTrace();
                return new HashMap<>();
            }
        }
    }
}
