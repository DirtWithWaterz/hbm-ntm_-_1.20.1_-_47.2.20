package com.hbm.nucleartech.hazard;

import java.util.ArrayList;
import java.util.List;

import com.hbm.nucleartech.hazard.modifier.HazardModifier;

import com.hbm.nucleartech.util.ContaminationUtil.HazardType;

public class HazardEntry {

	HazardType type;
	float baseLevel;
	
	/*
	 * Modifiers are evaluated in the order they're being applied to the entry.
	 */
	List<HazardModifier> mods = new ArrayList();
	
	public HazardEntry(HazardType type) {
		this(type, 1F);
	}
	
	public HazardEntry(HazardType type, float level) {
		this.type = type;
		this.baseLevel = level;
	}
	
	public HazardEntry addMod(HazardModifier mod) {
		this.mods.add(mod);
		return this;
	}
	
//	public void applyHazard(ItemStack stack, LivingEntity entity) {
//		type.onUpdate(entity, HazardModifier.evalAllModifiers(stack, entity, baseLevel, mods), stack);
//	}
	
	public HazardType getType() {
		return this.type;
	}
	
	public HazardEntry clone() {
		return clone(1F);
	}
	
	public HazardEntry clone(float mult) {
		HazardEntry clone = new HazardEntry(type, baseLevel * mult);
		clone.mods.addAll(this.mods);
		return clone;
	}

	public float getBaseLevel(){
		return this.baseLevel;
	}

	public void setBaseLevel(float lvl){
		this.baseLevel = lvl;
	}
}
