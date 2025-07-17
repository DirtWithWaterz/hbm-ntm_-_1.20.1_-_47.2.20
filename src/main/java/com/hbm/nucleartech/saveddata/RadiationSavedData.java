package com.hbm.nucleartech.saveddata;

import com.hbm.nucleartech.config.GeneralConfig;
import com.hbm.nucleartech.handler.RadiationSystemChunksNT;
import com.hbm.nucleartech.handler.RadiationSystemNT;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class RadiationSavedData {

    private static RadiationSavedData openInstance;

    public ServerLevel worldObj;

    public RadiationSavedData() { }

    public RadiationSavedData(ServerLevel pLevel) {

        this.worldObj = pLevel;
    }

//    public boolean doesEntryExist(int x, int y) {
//
//        return getRadFromCoord(x, y) != null;
//    }

//    public void createEntry(int x, int y, float rad) {
//        contamination.put(new ChunkPos(x, y), new RadiationSaveStructure(x, y, rad));
//        this.setDirty();
//    }

//    public void deleteEntry(RadiationSaveStructure struct) {
//
//        contamination.remove(struct);
//        this.setDirty();
//    }

    public void jettisonData() {
        if(true){ // if GeneralConfig.advancedRadiation

//            RadiationSystemNT.jettisonData(worldObj);
            return;
        }
//        contamination.clear();
//        this.setDirty();
    }

//    public void setRadForChunkCoord(int x, int y, float radiation) {
//        ChunkPos pos = new ChunkPos(x, y);
//        RadiationSaveStructure entry = contamination.get(pos);
//
//        if(entry == null){
//
//            entry = new RadiationSaveStructure(x, y, radiation);
//            contamination.put(pos, entry);
//        }
//
//        entry.radiation = radiation;
//        this.setDirty();
//    }

    public void setRadForCoord(BlockPos pos, float radiation) {
        if(true){ // if GeneralConfig.advancedRadiation
            RadiationSystemChunksNT.setRadForCoord(worldObj, pos, radiation);
//            return;
        }
//        ChunkPos cPos = new ChunkPos(pos);
//        RadiationSaveStructure entry = contamination.get(cPos);
//
//        if(entry == null){
//
//            entry = new RadiationSaveStructure(cPos.x, cPos.z, radiation);
//            contamination.put(cPos, entry);
//        }
//
//        entry.radiation = radiation;
//        this.setDirty();
    }

//    private RadiationSaveStructure getRadFromCoord(int x, int y){
//        ChunkPos pos = new ChunkPos(x, y);
//        return contamination.get(pos);
//    }

//    private float getRadNumFromChunkCoord(int x, int y){
//        RadiationSaveStructure rad = contamination.get(new ChunkPos(x, y));
//        if(rad != null)
//            return rad.radiation;
//        return 0F;
//    }

    public float getRadForCoord(BlockPos pos) {
//        if(true) { // if GeneralConfig.advancedRadiation
            return RadiationSystemChunksNT.getRadForCoord(worldObj, pos);
//        }
//        RadiationSaveStructure rad = contamination.get(new ChunkPos(pos));
//        if(rad != null)
//            return rad.radiation;
//        return 0F;
    }

    public void updateSystem() {
        if(true) // if GeneralConfig.advancedRadiation
            return;
//        Map<ChunkPos, RadiationSaveStructure> tempList = new HashMap<ChunkPos, RadiationSaveStructure>(contamination);
//
//        contamination.clear();
//
//        for(RadiationSaveStructure struct : tempList.values()) {
//
//            if(struct.radiation != 0) {
//
//                //struct.radiation *= 0.999F;
//                struct.radiation *= 0.999F;
//                struct.radiation -= 0.05F;
//
//                if(struct.radiation <= 0) {
//                    struct.radiation = 0;
//                }
//
//                if(struct.radiation > RadiationConfig.fogRad && worldObj != null && worldObj.rand.nextInt(RadiationConfig.fogCh) == 0 && worldObj.getChunkFromChunkCoords(struct.chunkX, struct.chunkY).isLoaded()) {
//
//                    int x = struct.chunkX * 16 + worldObj.rand.nextInt(16);
//                    int z = struct.chunkY * 16 + worldObj.rand.nextInt(16);
//                    int y = worldObj.getHeight(x, z) + worldObj.rand.nextInt(5);
//
//                    PacketDispatcher.wrapper.sendToAllAround(new AuxParticlePacket(x, y, z, 3), new TargetPoint(worldObj.provider.getDimension(), x, y, z, 100));
//                }
//
//                if(struct.radiation > 1) {
//
//                    float[] rads = new float[9];
//
//                    rads[0] = getRadNumFromChunkCoord(struct.chunkX + 1, struct.chunkY + 1);
//                    rads[1] = getRadNumFromChunkCoord(struct.chunkX, struct.chunkY + 1);
//                    rads[2] = getRadNumFromChunkCoord(struct.chunkX - 1, struct.chunkY + 1);
//                    rads[3] = getRadNumFromChunkCoord(struct.chunkX - 1, struct.chunkY);
//                    rads[4] = getRadNumFromChunkCoord(struct.chunkX - 1, struct.chunkY - 1);
//                    rads[5] = getRadNumFromChunkCoord(struct.chunkX, struct.chunkY - 1);
//                    rads[6] = getRadNumFromChunkCoord(struct.chunkX + 1, struct.chunkY - 1);
//                    rads[7] = getRadNumFromChunkCoord(struct.chunkX + 1, struct.chunkY);
//                    rads[8] = getRadNumFromChunkCoord(struct.chunkX, struct.chunkY);
//
//                    float main = 0.6F;
//                    float side = 0.075F;
//                    float corner = 0.025F;
//
//                    setRadForChunkCoord(struct.chunkX + 1, struct.chunkY + 1, rads[0] + struct.radiation * corner);
//                    setRadForChunkCoord(struct.chunkX, struct.chunkY + 1, rads[1] + struct.radiation * side);
//                    setRadForChunkCoord(struct.chunkX - 1, struct.chunkY + 1, rads[2] + struct.radiation * corner);
//                    setRadForChunkCoord(struct.chunkX - 1, struct.chunkY, rads[3] + struct.radiation * side);
//                    setRadForChunkCoord(struct.chunkX - 1, struct.chunkY - 1, rads[4] + struct.radiation * corner);
//                    setRadForChunkCoord(struct.chunkX, struct.chunkY - 1, rads[5] + struct.radiation * side);
//                    setRadForChunkCoord(struct.chunkX + 1, struct.chunkY - 1, rads[6] + struct.radiation * corner);
//                    setRadForChunkCoord(struct.chunkX + 1, struct.chunkY, rads[7] + struct.radiation * side);
//                    setRadForChunkCoord(struct.chunkX, struct.chunkY, rads[8] + struct.radiation * main);
//
//                } else {
//
//                    this.setRadForChunkCoord(struct.chunkX, struct.chunkY, getRadNumFromChunkCoord(struct.chunkX, struct.chunkY) + struct.radiation);
//                }
//            }
//        }
//        this.markDirty();
    }

//    @Override
//    public void readFromNBT(NBTTagCompound nbt) {
//        if(!GeneralConfig.enableRads || GeneralConfig.advancedRadiation) {
//            return;
//        }
//        int count = nbt.getInteger("radCount");
//
//        for(int i = 0; i < count; i++) {
//            RadiationSaveStructure struct = new RadiationSaveStructure();
//            struct.readFromNBT(nbt, i);
//
//            contamination.put(new ChunkPos(struct.chunkX, struct.chunkY), struct);
//        }
//    }
//
//    public CompoundTag writeToNBT(CompoundTag nbt) {
//        nbt.putInt("radCount", contamination.size());
//        int i = 0;
//        Iterator<RadiationSaveStructure> itr = contamination.values().iterator();
//        while(itr.hasNext()){
//            itr.next().writeToNBT(nbt, i);
//            i++;
//        }
//        return nbt;
//    }

//    public void load(CompoundTag tag){
//        if(true) // if GeneralConfig.advancedRadiation or !GeneralConfig.enableRads
//            return;
//
//        int count = tag.getInt("radCount");
//
//        for(int i = 0; i < count; i++) {
//            RadiationSaveStructure struct = new RadiationSaveStructure();
//            struct.readFromNBT(tag, i);
//
//            contamination.put(new ChunkPos(struct.chunkX, struct.chunkY), struct);
//        }
//    }

//    @NotNull
//    @Override
//    public CompoundTag save(CompoundTag nbt) {
//        nbt.putInt("radCount", contamination.size());
//        int i = 0;
//        Iterator<RadiationSaveStructure> itr = contamination.values().iterator();
//        while(itr.hasNext()){
//            itr.next().writeToNBT(nbt, i);
//            i++;
//        }
//        return nbt;
//    }


//    public static RadiationSavedData shitManIdk(CompoundTag tag){
//        RadiationSavedData data = new RadiationSavedData();
//        data.worldObj = openInstance.worldObj;
//        return data;
//    }
//
//    public static RadiationSavedData getData(ServerLevel worldObj) {
//
//        if(openInstance != null && openInstance.worldObj == worldObj)
//            return openInstance;
//        RadiationSavedData data = (RadiationSavedData) worldObj.getDataStorage().computeIfAbsent(RadiationSavedData::shitManIdk, RadiationSavedData::new, "radiation");
//        if(data == null){
//            worldObj.getDataStorage().set("radiation", new RadiationSavedData(worldObj));
//
//            data = worldObj.getDataStorage().computeIfAbsent(RadiationSavedData::shitManIdk, RadiationSavedData::new, "radiation");
//        }
//
//        data.worldObj = worldObj;
//        openInstance = data;
//
//        return openInstance;
//    }

    public static void incrementRad(ServerLevel worldObj, BlockPos pos, float rad, float maxRad) {
        if(true){ // if GeneralConfig.advancedRadiation
//            System.out.println("[Debug] Increment Rad called with size: " + rad);
            RadiationSystemChunksNT.incrementRad(worldObj, pos, rad, maxRad);
//            return;
        }
//        RadiationSavedData data = getData(worldObj);
//
//        LevelChunk chunk = worldObj.getChunkAt(pos);
//
//        float r = data.getRadNumFromChunkCoord(chunk.getPos().x, chunk.getPos().z);
//
//        if(r < maxRad) {
//
//            data.setRadForChunkCoord(chunk.getPos().x, chunk.getPos().z, r + rad);
//        }
    }

    public static void decrementRad(ServerLevel pLevel, BlockPos pos, float rad) {
        if(true) { // if GeneralConfig.advancedRadiation
            RadiationSystemChunksNT.decrementRad(pLevel, pos, rad);
//            return;
        }
//        RadiationSavedData data = getData(pLevel);
//
//        LevelChunk chunk = pLevel.getChunkAt(pos);
//
//        float r = data.getRadNumFromChunkCoord(chunk.getPos().x, chunk.getPos().z);
//
//        r -= rad;
//
//        if(r > 0){
//            data.setRadForChunkCoord(chunk.getPos().x, chunk.getPos().z, r);
//        }
//        else{
//            data.setRadForChunkCoord(chunk.getPos().x, chunk.getPos().z, 0);
//        }
    }



}
