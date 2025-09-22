package com.hbm.nucleartech.handler;

import com.hbm.nucleartech.block.RegisterBlocks;
import com.hbm.nucleartech.handler.RadiationSystemChunksNT.ChunkStorageCompat;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RadiationWorldHandler {

    private static final int MAX_CHUNKS_PER_TICK = 2; // Process up to 2 chunks per tick
    private static final Random random = ThreadLocalRandom.current();

    public static void handleWorldDestruction(Level level) {
        if (!(level instanceof ServerLevel world)) return;

        Collection<RadiationSystemChunksNT.RadPocket> active = RadiationSystemChunksNT.getActivePockets();
        if (active.isEmpty()) return;

        int threshold = 5;
        List<RadiationSystemChunksNT.RadPocket> pockets = getPocketsAboveThreshold(active, world, threshold);
        if (pockets.isEmpty()) return;

        List<RadiationSystemChunksNT.RadPocket> theChosenONESSSSS = choosePocketsFromList(pockets, world, MAX_CHUNKS_PER_TICK);
        String str = "";
        for(RadiationSystemChunksNT.RadPocket p : theChosenONESSSSS)
            str = str.concat(p.parent.parentChunk.chunk.getPos().toString() + "\n");
        System.err.println("================\n" + str + "================");
        if(theChosenONESSSSS.isEmpty()) return;

        // Process multiple pockets in parallel
        int processed = 0;
        for (RadiationSystemChunksNT.RadPocket pocket : theChosenONESSSSS) {
            if (processed >= MAX_CHUNKS_PER_TICK) break;
            
            RadiationSystemChunksNT.SubChunkRadiationStorage sc = pocket.parent;
            if (sc == null || sc.parentChunk == null || sc.parentChunk.chunk == null) continue;

            // Queue the chunk section for async processing
            LevelChunk chunk = sc.parentChunk.chunk;
            int sectionY = ChunkStorageCompat.getIndexFromWorldY(sc.yLevel);
            
//            if (sectionY >= 0 && sectionY < chunk.getSections().length) {
                AsyncChunkProcessor.queueChunkForProcessing(world, chunk, sectionY, pocket);
                processed++;
//            }
        }
    }

    private static List<RadiationSystemChunksNT.RadPocket> choosePocketsFromList(List<RadiationSystemChunksNT.RadPocket> pockets, ServerLevel world, int i) {

        List<RadiationSystemChunksNT.RadPocket> list = new ArrayList<>();

        for(int j = 0; j < i; j++)
            list.add(pockets.get(world.random.nextInt(pockets.size())));

        return list;
    }

    // Get all pockets above the threshold
    private static List<RadiationSystemChunksNT.RadPocket> getPocketsAboveThreshold(
            Collection<RadiationSystemChunksNT.RadPocket> pockets, ServerLevel world, int threshold) {
        
        if (pockets == null || pockets.isEmpty()) return List.of();

        // Snapshot to avoid concurrent modification
        List<RadiationSystemChunksNT.RadPocket> snapshot = new ArrayList<>(pockets);
        List<RadiationSystemChunksNT.RadPocket> result = new ArrayList<>(Math.min(snapshot.size(), 16));

        for (RadiationSystemChunksNT.RadPocket pocket : snapshot) {
            if (pocket == null) continue;
            if (Float.isNaN(pocket.radiation)) continue; // Skip invalid radiation values
            
            // Optional: filter by world if needed
            // if (pocket.parent == null || pocket.parent.parentChunk == null || 
            //     pocket.parent.parentChunk.chunk.getLevel() != world) continue;
                
            if (pocket.radiation >= threshold) {
                result.add(pocket);
            }
        }

        // Shuffle to distribute processing across chunks
        if (result.size() > 1) {
            for (int i = result.size() - 1; i > 0; i--) {
                int index = random.nextInt(i + 1);
                RadiationSystemChunksNT.RadPocket temp = result.get(index);
                result.set(index, result.get(i));
                result.set(i, temp);
            }
        }

        return result;
    }
    
    // Called during server shutdown to clean up resources
    public static void shutdown() {
        AsyncChunkProcessor.shutdown();
    }
}
