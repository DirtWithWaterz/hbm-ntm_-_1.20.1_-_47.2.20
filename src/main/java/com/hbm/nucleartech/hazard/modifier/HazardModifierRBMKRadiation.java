package com.hbm.nucleartech.hazard.modifier;

//import com.hbm.nucleartech.item.machine.ItemRBMKPellet;
//import com.hbm.nucleartech.item.machine.ItemRBMKRod;


import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class HazardModifierRBMKRadiation extends HazardModifier {
	
	float target;
	boolean linear = false;
	
	public HazardModifierRBMKRadiation(float target, boolean linear) {
		this.target = target;
		this.linear = linear;
	}

	@Override
	public float modify(ItemStack stack, LivingEntity holder, float level) {
		
//		if(stack.getItem() instanceof ItemRBMKRod) {
//			//Due to short-lived fission products, radioactivity rises quicker than depletion when applicable
//			double depletion = linear ? 1D - ItemRBMKRod.getEnrichment(stack) : 1D - Math.pow(ItemRBMKRod.getEnrichment(stack), 2);
//			double xenon = ItemRBMKRod.getPoisonLevel(stack);
//
//			level = (float) (level + (this.target - level) * depletion);
//			level += HazardRegistry.xe135 * xenon;
//
//		} else if(stack.getItem() instanceof ItemRBMKPellet) {
//
//			double depletion = linear ? (ItemRBMKPellet.rectify(stack.getItemDamage()) % 5) / 4F : 1 - Math.pow((4 - ItemRBMKPellet.rectify(stack.getItemDamage()) % 5) / 4F, 2);
//
//			level = level + (target - level) * ((ItemRBMKPellet.rectify(stack.getItemDamage()) % 5) / 4F);
//
//			if(ItemRBMKPellet.hasXenon(stack.getDamageValue()))
//				level += HazardRegistry.xe135 * HazardRegistry.nugget;
//		}
		
		
		return level;
	}

}
