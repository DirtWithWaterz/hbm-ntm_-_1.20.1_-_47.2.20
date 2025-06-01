package com.hbm.nucleartech.handler;


import com.hbm.nucleartech.AdvancementManager;
import com.hbm.nucleartech.HBM;
import com.hbm.nucleartech.capability.HbmCapabilities;
import com.hbm.nucleartech.damagesource.RegisterDamageSources;
import com.hbm.nucleartech.interfaces.IEntityCapabilityBase.Type;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.*;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.*;


@Mod.EventBusSubscriber(modid = HBM.MOD_ID)
public class RadiationSystemNT {

    static MinecraftServer server;

    private static int ticks;

    public static final int Y_MIN = -64;
    public static final int Y_MAX = 320;
    public static final int Y_RANGE = Y_MAX - Y_MIN; // 384

    public static void init(MinecraftServer pServer) {

        server = pServer;

    }

    private static void updateEntityContamination(Level world, boolean updateData) {

//        System.err.println("updating contamination");
        if(world != null && !world.isClientSide /* && GeneralConfig.enableRads */ ) {

            Set<ChunkPos> processedChunks = new HashSet<>();

            for (Player playerI : world.players()) {
                ChunkPos playerChunkPos = new ChunkPos(playerI.blockPosition());
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        ChunkPos chunkPos = new ChunkPos(playerChunkPos.x + dx, playerChunkPos.z + dz);
                        if (processedChunks.add(chunkPos) && world.hasChunk(chunkPos.x, chunkPos.z)) {
                            LevelChunk chunk = world.getChunk(chunkPos.x, chunkPos.z);
                            AABB chunkAABB = getAabb(world, chunkPos);
                            List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class, chunkAABB);

                            for (LivingEntity entity : entities) {
                                // Apply contamination logic to each living entity (but of course, FIGURE OUT GIVING MOBS FUCKING NBT TAGS AAHHHHHAIWHUDHAWIUDHAWIDUYHAWD)
                                double eRad = (double) HbmCapabilities.getData(entity).getValue(Type.RADIATION);

                                if (entity instanceof ServerPlayer player) {

                                    if (player.isCreative() || player.isSpectator())
                                        continue;


                                    if (eRad > 2500000)
                                        HbmCapabilities.getData(player).setValue(Type.RADIATION, 2500000); // grant achievement "HOW"

                                    if (eRad >= 1000) {

//                                        player.hurt(new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("hbm:radiation")))), 1000f);
                                        try {

                                            player.hurt(RegisterDamageSources.RADIATION_DAMAGE, 1000f);
                                            // grant achievement "wait, what?"
                                        } catch (Exception ignored) { System.err.println("client had a packet error!"); }
                                        // Grant achievement, "Ouch, Radiation!"


                                    } else if (eRad >= 800) {

                                        if (world.random.nextInt(300) == 0)
                                            player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 5 * 30, 0, true, false));
                                        if (world.random.nextInt(300) == 0)
                                            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10 * 20, 2, true, false));
                                        if (world.random.nextInt(300) == 0)
                                            player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 10 * 20, 2, true, false));
                                        if (world.random.nextInt(300) == 0)
                                            player.addEffect(new MobEffectInstance(MobEffects.POISON, 3 * 20, 2, true, false));
                                        if (world.random.nextInt(300) == 0)
                                            player.addEffect(new MobEffectInstance(MobEffects.WITHER, 3 * 20, 1, true, false));
                                        if (world.random.nextInt(300) == 0)
                                            player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 5 * 20, 3, true, false));
                                        if (world.random.nextInt(300) == 0)
                                            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 5 * 20, 3, true, false));
                                    } else if (eRad >= 600) {

                                        if (world.random.nextInt(300) == 0)
                                            player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 5 * 30, 0, true, false));
                                        if (world.random.nextInt(300) == 0)
                                            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10 * 20, 2, true, false));
                                        if (world.random.nextInt(300) == 0)
                                            player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 10 * 20, 2, true, false));
                                        if (world.random.nextInt(500) == 0)
                                            player.addEffect(new MobEffectInstance(MobEffects.POISON, 3 * 20, 1, true, false));
                                        if (world.random.nextInt(300) == 0)
                                            player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 5 * 20, 3, true, false));
                                        if (world.random.nextInt(400) == 0)
                                            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 6 * 20, 2, true, false));
                                    } else if (eRad >= 400) {

                                        if (world.random.nextInt(300) == 0)
                                            player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 5 * 30, 0, true, false));
                                        if (world.random.nextInt(500) == 0)
                                            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5 * 20, 0, true, false));
                                        if (world.random.nextInt(300) == 0)
                                            player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 5 * 20, 1, true, false));
                                        if (world.random.nextInt(500) == 0)
                                            player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 3 * 20, 2, true, false));
                                        if (world.random.nextInt(600) == 0)
                                            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 4 * 20, 1, true, false));
                                    } else if (eRad >= 200) {

                                        if (world.random.nextInt(300) == 0)
                                            player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 5 * 20, 0, true, false));
                                        if (world.random.nextInt(500) == 0)
                                            player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 5 * 20, 0, true, false));
                                        if (world.random.nextInt(700) == 0)
                                            player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 3 * 20, 2, true, false));
                                        if (world.random.nextInt(800) == 0)
                                            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 4 * 20, 0, true, false));
                                    } else if (eRad >= 100) {

                                        if (world.random.nextInt(800) == 0)
                                            player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 2 * 20, 0, true, false));
                                        if (world.random.nextInt(1000) == 0)
                                            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 1 * 20, 0, true, false));

                                        // Grant achievement, "Yay, Radiation!"
                                        AdvancementManager.grant(player, "rad_poison");
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
    }

//    public static void updateRadiation() {
//        long time = System.currentTimeMillis();
//
//        for(WorldRadiationData w : worldMap.values()){
//
//            List<RadPocket> itrActive = new ArrayList<>(w.getActivePockets());
//            Iterator<RadPocket> itr = itrActive.iterator();
//            while(itr.hasNext()){
//                RadPocket p = itr.next();
//                BlockPos pos = p.parent.parent.getWorldPos(p.parent.yLevel);
//                ChunkPos chunkPos = p.parent.parent.chunk.getPos();
//                ServerLevel sLevel = (ServerLevel) w.pLevel;
//                LevelChunk chunk = sLevel.getChunkSource().getChunkNow(chunkPos.x, chunkPos.z);
//                if(chunk == null || sLevel.getPlayers(player -> {
//                    BlockPos playerPos = player.blockPosition();
//                    int dx = (playerPos.getX() >> 4) - chunkPos.x;
//                    int dz = (playerPos.getZ() >> 4) - chunkPos.z;
//                    return dx * dx + dz * dz <= 64;
//                }).isEmpty()) {
//                    sLevel.setChunkForced(chunkPos.x, chunkPos.z, false);
//                }
//                p.radiation *= 0.999F;
//                p.radiation -= 0.05F;
//                p.parent.parent.chunk.setUnsaved(true);
//                if(p.radiation <= 0){
//                    p.radiation = 0;
//                    p.accumulatedRads = 0;
//                    itr.remove();
//                    p.parent.parent.chunk.setUnsaved(true);
//                    continue;
//                }
//
//                // radioactive fog spawning goes here vvv
//                if(w.pLevel.random.nextInt(10) == 0){
//                    for (int i = 0; i < 10; i++) {
//                        BlockPos randPos = new BlockPos(
//                                w.pLevel.random.nextInt(16),
//                                w.pLevel.random.nextInt(16),
//                                w.pLevel.random.nextInt(16)
//                        );
//
//                        if (p.parent.pocketsByBlock == null || p.parent.pocketsByBlock[
//                                randPos.getX() * 16 * 16 + randPos.getY() * 16 + randPos.getZ()] == p) {
//
//                            randPos = randPos.offset(p.parent.parent.getWorldPos(p.parent.yLevel));
//                            BlockState state = w.pLevel.getBlockState(randPos);
//
//                            Vec3 rPos = new Vec3(
//                                    randPos.getX() + 0.5,
//                                    randPos.getY() + 0.5,
//                                    randPos.getZ() + 0.5
//                            );
//
//                            BlockHitResult trace = w.pLevel.clip(new ClipContext(
//                                    rPos,
//                                    rPos.add(0, -6, 0),
//                                    ClipContext.Block.COLLIDER,
//                                    ClipContext.Fluid.NONE,
//                                    null
//                            ));
//
//                            if (state.isAir() && trace.getType() == HitResult.Type.BLOCK) {
//                                w.pLevel.addParticle(ParticleTypes.MYCELIUM, randPos.getX(), randPos.getY(), randPos.getZ(), 0.0, 0.0, 0.0);
//                                break;
//                            }
//                        }
//                    }
//                }
//                // ^^^                                ^^^
//
//                float count = 0;
//                for(Direction e : Direction.values()){
//                    count += p.connectionIndicies[e.ordinal()].size();
//                }
//                float amountPer = 0.7F / count;
//                if(count == 0 || p.radiation < 1){
//                    amountPer = 0;
//                }
//
//                if(p.radiation > 0 && amountPer > 0){
//                    for(Direction e : Direction.values()){
//                        BlockPos nPos = pos.relative(e, 16);
//
//                        if(!p.parent.parent.chunk.getLevel().hasChunkAt(nPos) || nPos.getY() < -64 || nPos.getY() > 320)
//                            continue;
//
//                        if(p.connectionIndicies[e.ordinal()].size() == 1 && p.connectionIndicies[e.ordinal()].get(0) == -1) {
//                            rebuildChunkPockets(p.parent.parent.chunk.getLevel().getChunkAt(nPos), (nPos.getY()) >> 4);
//                        }
//                        else {
//                            SubChunkRadiationStorage sc2 = getSubChunkStorage(p.parent.parent.chunk.getLevel(), nPos);
//                            for(int idx : p.connectionIndicies[e.ordinal()]){
//                                if(!sc2.pockets[idx].isSealed()){
//                                    sc2.pockets[idx].accumulatedRads += p.radiation * amountPer;
//                                    w.addActivePocket(sc2.pockets[idx]);
//                                }
//                            }
//                        }
//                    }
//                }
//                if(amountPer != 0) {
//                    p.accumulatedRads += p.radiation * 0.3F;
//                }
//                if(System.currentTimeMillis() - time > 20) {
//                    break;
//                }
//            }
//
//            List<RadPocket> itrActiveCheck = new ArrayList<>(w.getActivePockets());
//            itr = itrActiveCheck.iterator();
//            while(itr.hasNext()){
//                RadPocket act = itr.next();
//                act.radiation = act.accumulatedRads;
//                act.accumulatedRads = 0;
//                if(act.radiation <= 0){
//                    w.removeActivePocket(act);
//                    itr.remove();
//                }
//            }
//        }
//
//        if(System.currentTimeMillis() - time > 50){
//            System.out.println("Rads took too long: " + (System.currentTimeMillis() - time));
//        }
//    }
//

    @SubscribeEvent
    public static void onWorldUpdate(TickEvent.LevelTickEvent event) {
//        if(GeneralConfig.enableDebugMode) {
//            MainRegistry.logger.info("[Debug] onWorldUpdate called for RadSys tick " + ticks);
//        }

        boolean allowUpdate = (event.phase == TickEvent.Phase.START);

//        if (allowUpdate) {
//            // Make the world stinky
//            RadiationWorldHandler.handleWorldDestruction(e.world);
//        }

        // Make entities stinky
        if(!event.level.isClientSide())
            updateEntityContamination(event.level, allowUpdate);
    }

    private static @NotNull AABB getAabb(Level world, ChunkPos chunkPos) {
        int chunkStartX = chunkPos.getMinBlockX();
        int chunkStartZ = chunkPos.getMinBlockZ();
        int chunkEndX = chunkStartX + 15;
        int chunkEndZ = chunkStartZ + 15;
        int worldMinY = world.getMinBuildHeight();
        int worldMaxY = world.getMaxBuildHeight();

        return new AABB(chunkStartX, worldMinY, chunkStartZ, chunkEndX + 1, worldMaxY, chunkEndZ + 1);
    }

}
