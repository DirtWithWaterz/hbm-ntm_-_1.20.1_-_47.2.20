package com.hbm.nucleartech.handler;

import net.minecraft.nbt.CompoundTag;

public class RadiationSaveStructure {
	public int chunkX;
	public int chunkY;
	public float radiation;

	public RadiationSaveStructure() { }

	public RadiationSaveStructure(int x, int y, float rad) {
		chunkX = x;
		chunkY = y;
		radiation = rad;
	}

	public void readFromNBT(CompoundTag nbt, int index) {
		chunkX = nbt.getInt("rad_" + index + "_x");
		chunkY = nbt.getInt("rad_" + index + "_y");
		radiation = nbt.getFloat("rad_" + index + "_level");
	}

	public void writeToNBT(CompoundTag nbt, int index) {
		nbt.putInt("rad_" + index + "_x", chunkX);
		nbt.putInt("rad_" + index + "_y", chunkY);
		nbt.putFloat("rad_" + index + "_level", radiation);
	}
}
