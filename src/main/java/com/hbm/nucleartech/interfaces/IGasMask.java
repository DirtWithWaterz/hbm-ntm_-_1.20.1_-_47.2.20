package com.hbm.nucleartech.interfaces;

import java.util.ArrayList;

import com.hbm.nucleartech.util.ArmorRegistry.HazardClass;
import net.minecraft.world.item.ItemStack;

public interface IGasMask {
	/**
	 * Returns a list of HazardClasses which can not be protected against by this mask (e.g. chlorine gas for half masks)
	 * @param stack
	 * @return an empty list if there's no blacklist
	 */
	public ArrayList<HazardClass> getBlacklist(ItemStack stack);
	
	/**
	 * Returns the loaded filter, if there is any
	 * @param stack
	 * @return null if no filter is installed
	 */
	public ItemStack getFilter(ItemStack stack);
	
	/**
	 * Checks whether the provided filter can be screwed into the mask, does not take already applied filters into account (those get ejected)
	 * @param stack
	 * @param filter
	 * @return
	 */
	public boolean isFilterApplicable(ItemStack stack, ItemStack filter);
	
	/**
	 * This will write the filter to the stack's NBT, it ignores any previously installed filter and won't eject those
	 * @param stack
	 * @param filter
	 */
	public void installFilter(ItemStack stack, ItemStack filter);
	
	/**
	 * Damages the installed filter, if there is one
	 * @param stack
	 * @param damage
	 */
	public void damageFilter(ItemStack stack, int damage);
}
