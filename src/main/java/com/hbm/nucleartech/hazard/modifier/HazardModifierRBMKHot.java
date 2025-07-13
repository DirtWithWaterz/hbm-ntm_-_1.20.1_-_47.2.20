package com.hbm.nucleartech.hazard.modifier;

//import com.hbm.nucleartech.item.machine.ItemRBMKRod;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;


public class HazardModifierRBMKHot extends HazardModifier {

	@Override
	public float modify(ItemStack stack, LivingEntity holder, float level) {
		
		level = 0;
		
//		if(stack.getItem() instanceof ItemRBMKRod) {
//			double heat = ItemRBMKRod.getHullHeat(stack);
//			int fire = (int)Math.min(Math.ceil((heat - 100) / 10D), 60);
//			level = fire;
//		}
		
		return level;
	}

}
