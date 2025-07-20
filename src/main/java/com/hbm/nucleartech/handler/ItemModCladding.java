package com.hbm.nucleartech.handler;

import java.awt.*;
import java.util.List;

import com.hbm.nucleartech.item.special.ItemArmorMod;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import net.minecraft.network.chat.Component;

public class ItemModCladding extends ItemArmorMod {
	
	public double rad;
	
	public ItemModCladding(double rad, Properties properties, ArmorMaterial material) {

		super(properties, material, ArmorModHandler.cladding, true, true, true, true);
		this.rad = rad;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> list, TooltipFlag flagIn){
		list.add(Component.literal(ChatFormatting.YELLOW + "+" + rad + " rad-resistance"));
		list.add(Component.empty());
		super.appendHoverText(stack, worldIn, list, flagIn);
	}

	@Override
	public void addDesc(List<Component> list, ItemStack stack, ItemStack armor) {
		list.add(Component.literal(ChatFormatting.YELLOW + "  " + stack.getDisplayName() + " (+" + rad + " radiation resistence)"));
	}
}
