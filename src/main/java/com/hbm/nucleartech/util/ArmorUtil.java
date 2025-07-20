package com.hbm.nucleartech.util;

import com.hbm.nucleartech.handler.ArmorModHandler;
import com.hbm.nucleartech.handler.HazmatRegistry;
import com.hbm.nucleartech.interfaces.IGasMask;
import com.hbm.nucleartech.item.RegisterItems;

import com.hbm.nucleartech.lib.Library;
import com.hbm.nucleartech.util.ArmorRegistry.HazardClass;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class ArmorUtil {

	public static void register() {
		ArmorRegistry.registerHazard(RegisterItems.GAS_MASK_FILTER.get(), HazardClass.PARTICLE_COARSE, HazardClass.PARTICLE_FINE, HazardClass.GAS_CHLORINE, HazardClass.BACTERIA);
		ArmorRegistry.registerHazard(RegisterItems.GAS_MASK_FILTER_MONO.get(), HazardClass.PARTICLE_COARSE, HazardClass.GAS_MONOXIDE);
		ArmorRegistry.registerHazard(RegisterItems.GAS_MASK_FILTER_COMBO.get(), HazardClass.PARTICLE_COARSE, HazardClass.PARTICLE_FINE, HazardClass.GAS_CHLORINE, HazardClass.BACTERIA, HazardClass.GAS_MONOXIDE, HazardClass.NERVE_AGENT);
		ArmorRegistry.registerHazard(RegisterItems.GAS_MASK_FILTER_RADON.get(), HazardClass.RAD_GAS, HazardClass.PARTICLE_COARSE, HazardClass.PARTICLE_FINE, HazardClass.GAS_CHLORINE, HazardClass.BACTERIA, HazardClass.GAS_MONOXIDE, HazardClass.NERVE_AGENT);
//		ArmorRegistry.registerHazard(RegisterItems.gas_mask_filter_rag, HazardClass.PARTICLE_COARSE);
//		ArmorRegistry.registerHazard(RegisterItems.gas_mask_filter_piss, HazardClass.PARTICLE_COARSE, HazardClass.GAS_CHLORINE);
//
//		ArmorRegistry.registerHazard(RegisterItems.gas_mask, HazardClass.SAND, HazardClass.LIGHT);
		ArmorRegistry.registerHazard(RegisterItems.M65_MASK.get(), HazardClass.SAND);
//		ArmorRegistry.registerHazard(RegisterItems.mask_damp, HazardClass.PARTICLE_COARSE);
//		ArmorRegistry.registerHazard(RegisterItems.mask_piss, HazardClass.PARTICLE_COARSE, HazardClass.GAS_CHLORINE);
//
//		ArmorRegistry.registerHazard(RegisterItems.goggles, HazardClass.LIGHT, HazardClass.SAND);
//		ArmorRegistry.registerHazard(RegisterItems.ashglasses, HazardClass.LIGHT, HazardClass.SAND);
//
//		ArmorRegistry.registerHazard(RegisterItems.attachment_mask, HazardClass.SAND);
//		ArmorRegistry.registerHazard(RegisterItems.spider_milk, HazardClass.LIGHT);
//
//		ArmorRegistry.registerHazard(RegisterItems.asbestos_helmet, HazardClass.SAND, HazardClass.LIGHT);
		ArmorRegistry.registerHazard(RegisterItems.HAZMAT_HELMET.get(), HazardClass.SAND);
		ArmorRegistry.registerHazard(RegisterItems.HAZMAT_HELMET_RED.get(), HazardClass.SAND);
		ArmorRegistry.registerHazard(RegisterItems.HAZMAT_HELMET_GREY.get(), HazardClass.SAND);
//		ArmorRegistry.registerHazard(RegisterItems.liquidator_helmet, HazardClass.LIGHT, HazardClass.SAND);
//		ArmorRegistry.registerHazard(RegisterItems.hazmat_paa_helmet, HazardClass.PARTICLE_COARSE, HazardClass.PARTICLE_FINE, HazardClass.LIGHT, HazardClass.SAND);
//		ArmorRegistry.registerHazard(RegisterItems.paa_helmet, HazardClass.PARTICLE_COARSE, HazardClass.PARTICLE_FINE, HazardClass.GAS_CHLORINE, HazardClass.BACTERIA, HazardClass.GAS_MONOXIDE, HazardClass.LIGHT, HazardClass.SAND);
//		ArmorRegistry.registerHazard(RegisterItems.t45_helmet, HazardClass.PARTICLE_COARSE, HazardClass.PARTICLE_FINE, HazardClass.GAS_CHLORINE, HazardClass.BACTERIA, HazardClass.GAS_MONOXIDE, HazardClass.LIGHT, HazardClass.SAND);
//		ArmorRegistry.registerHazard(RegisterItems.ajr_helmet, HazardClass.PARTICLE_COARSE, HazardClass.PARTICLE_FINE, HazardClass.GAS_CHLORINE, HazardClass.BACTERIA, HazardClass.GAS_MONOXIDE, HazardClass.LIGHT, HazardClass.SAND);
//		ArmorRegistry.registerHazard(RegisterItems.ajro_helmet, HazardClass.PARTICLE_COARSE, HazardClass.PARTICLE_FINE, HazardClass.GAS_CHLORINE, HazardClass.BACTERIA, HazardClass.GAS_MONOXIDE, HazardClass.LIGHT, HazardClass.SAND);
//		ArmorRegistry.registerHazard(RegisterItems.rpa_helmet, HazardClass.PARTICLE_COARSE, HazardClass.PARTICLE_FINE, HazardClass.GAS_CHLORINE, HazardClass.BACTERIA, HazardClass.GAS_MONOXIDE, HazardClass.LIGHT, HazardClass.SAND);
//		ArmorRegistry.registerHazard(RegisterItems.hev_helmet, HazardClass.PARTICLE_COARSE, HazardClass.PARTICLE_FINE, HazardClass.GAS_CHLORINE, HazardClass.BACTERIA, HazardClass.GAS_MONOXIDE, HazardClass.LIGHT, HazardClass.SAND);
//		ArmorRegistry.registerHazard(RegisterItems.fau_helmet, HazardClass.RAD_GAS, HazardClass.PARTICLE_COARSE, HazardClass.PARTICLE_FINE, HazardClass.GAS_CHLORINE, HazardClass.BACTERIA, HazardClass.GAS_MONOXIDE, HazardClass.LIGHT, HazardClass.SAND);
//		ArmorRegistry.registerHazard(RegisterItems.dns_helmet, HazardClass.RAD_GAS, HazardClass.PARTICLE_COARSE, HazardClass.PARTICLE_FINE, HazardClass.GAS_CHLORINE, HazardClass.BACTERIA, HazardClass.GAS_MONOXIDE, HazardClass.LIGHT, HazardClass.SAND, HazardClass.GAS_CORROSIVE);
//		ArmorRegistry.registerHazard(RegisterItems.schrabidium_helmet, HazardClass.PARTICLE_COARSE, HazardClass.PARTICLE_FINE, HazardClass.GAS_CHLORINE, HazardClass.BACTERIA, HazardClass.GAS_MONOXIDE, HazardClass.LIGHT, HazardClass.SAND);
//		ArmorRegistry.registerHazard(RegisterItems.euphemium_helmet, HazardClass.RAD_GAS, HazardClass.PARTICLE_COARSE, HazardClass.PARTICLE_FINE, HazardClass.GAS_CHLORINE, HazardClass.BACTERIA, HazardClass.GAS_MONOXIDE, HazardClass.LIGHT, HazardClass.SAND);
		
		//Ob ihr wirklich richtig steht, seht ihr wenn das Licht angeht!
//		registerIfExists("gregtech", "gt.armor.hazmat.universal.head", HazardClass.PARTICLE_COARSE, HazardClass.PARTICLE_FINE, HazardClass.GAS_CHLORINE, HazardClass.BACTERIA, HazardClass.GAS_MONOXIDE, HazardClass.LIGHT, HazardClass.SAND);
//		registerIfExists("gregtech", "gt.armor.hazmat.biochemgas.head", HazardClass.PARTICLE_COARSE, HazardClass.PARTICLE_FINE, HazardClass.GAS_CHLORINE, HazardClass.BACTERIA, HazardClass.GAS_MONOXIDE, HazardClass.LIGHT, HazardClass.SAND);
//		registerIfExists("gregtech", "gt.armor.hazmat.radiation.head", HazardClass.PARTICLE_COARSE, HazardClass.PARTICLE_FINE, HazardClass.GAS_CHLORINE, HazardClass.BACTERIA, HazardClass.GAS_MONOXIDE, HazardClass.LIGHT, HazardClass.SAND);
	}
	
//	private static void registerIfExists(String domain, String name, HazardClass... classes) {
//		Item item = Compat.tryLoadItem(domain, name);
//		if(item != null)
//			ArmorRegistry.registerHazard(item, classes);
//	}
	
	public static boolean checkForFaraday(Player player) {
		
		NonNullList<ItemStack> armor = player.getInventory().armor;
		
		if(armor.get(0).isEmpty() || armor.get(1).isEmpty() || armor.get(2).isEmpty() || armor.get(3).isEmpty()) return false;
		
		if(ArmorUtil.isFaradayArmor(armor.get(0)) &&
				ArmorUtil.isFaradayArmor(armor.get(1)) &&
				ArmorUtil.isFaradayArmor(armor.get(2)) &&
				ArmorUtil.isFaradayArmor(armor.get(3)))
			return true;
		
		return false;
	}

	public static boolean isFaradayArmor(ItemStack item) {
		
		String name = item.getDescriptionId();
		
		if(HazmatRegistry.getCladding(item) > 0)
			return true;
		
		for(String metal : metals) {
			
			if(name.toLowerCase().contains(metal))
				return true;
		}
		
		return false;
	}
	
	public static boolean checkArmorNull(LivingEntity player, EquipmentSlot slot) {
		return player.getItemBySlot(slot).isEmpty();
	}

	public static final String[] metals = new String[] {
			"chainmail",
			"iron",
			"silver",
			"gold",
			"platinum",
			"tin",
			"lead",
			"liquidator",
			"schrabidium",
			"euphemium",
			"steel",
			"cmb",
			"titanium",
			"alloy",
			"copper",
			"bronze",
			"electrum",
			"t45",
			"bj",
			"starmetal",
			"hazmat", //also count because rubber is insulating
			"rubber",
			"hev",
			"ajr",
			"rpa",
			"spacesuit"
	};

	private static final EquipmentSlot[] ARMOR_SLOTS = new EquipmentSlot[] {
			EquipmentSlot.FEET,
			EquipmentSlot.LEGS,
			EquipmentSlot.CHEST,
			EquipmentSlot.HEAD
	};

	public static void damageSuit(Player player, int slot, int amount) {
	
		player.getInventory().armor.get(slot).hurtAndBreak(amount, player, (p_32290_) -> {
			p_32290_.broadcastBreakEvent(ARMOR_SLOTS[slot]);
		});

//		if(player.getInventory().armor.get(slot) == ItemStack.EMPTY)
//			return;
//
//		int j = player.getInventory().armor.get(slot).getDamageValue();
//		player.getInventory().armor.get(slot).setDamageValue(j += amount);
//
//		if(player.getInventory().armor.get(slot).getDamageValue() >= player.getInventory().armor.get(slot).getMaxDamage()) {
//			player.getInventory().armor.set(slot, ItemStack.EMPTY);
//		}
	}
	
//	@SuppressWarnings("deprecation")
//	public static void resetFlightTime(Player player) {
//		if(player instanceof ServerPlayer mp) {
//            ReflectionHelper.setPrivateValue(NetHandlerPlayServer.class, mp.connection, 0, "floatingTickCount", "field_147365_f");
//		}
//	}

//	public static boolean checkForFiend2(Player player) {
//
//		return ArmorUtil.checkArmorPiece(player, RegisterItems.jackt2, 2) && Library.checkForHeld(player, RegisterItems.shimmer_axe);
//	}
//
//	public static boolean checkForFiend(Player player) {
//
//		return ArmorUtil.checkArmorPiece(player, RegisterItems.jackt, 2) && Library.checkForHeld(player, RegisterItems.shimmer_sledge);
//	}
//
//	public static boolean checkPAA(LivingEntity player){
//		return checkArmor(player, RegisterItems.paa_helmet, RegisterItems.paa_plate, RegisterItems.paa_legs, RegisterItems.paa_boots);
//	}
//
//	// Drillgon200: Is there a reason for this method? I don't know and I don't
//	// care to find out.
//	// Alcater: Looks like some kind of hazmat tier 2 check
//	public static boolean checkForHaz2(LivingEntity player) {
//
//		if(checkArmor(player, RegisterItems.hazmat_paa_helmet, RegisterItems.hazmat_paa_plate, RegisterItems.hazmat_paa_legs, RegisterItems.hazmat_paa_boots) ||
//			checkArmor(player, RegisterItems.paa_helmet, RegisterItems.paa_plate, RegisterItems.paa_legs, RegisterItems.paa_boots) ||
//			checkArmor(player, RegisterItems.liquidator_helmet, RegisterItems.liquidator_plate, RegisterItems.liquidator_legs, RegisterItems.liquidator_boots) ||
//			checkArmor(player, RegisterItems.euphemium_helmet, RegisterItems.euphemium_plate, RegisterItems.euphemium_legs, RegisterItems.euphemium_boots) ||
//			checkArmor(player, RegisterItems.hev_helmet, RegisterItems.hev_plate, RegisterItems.hev_legs, RegisterItems.hev_boots) ||
//			checkArmor(player, RegisterItems.ajr_helmet, RegisterItems.ajr_plate, RegisterItems.ajr_legs, RegisterItems.ajr_boots) ||
//			checkArmor(player, RegisterItems.ajro_helmet, RegisterItems.ajro_plate, RegisterItems.ajro_legs, RegisterItems.ajro_boots) ||
//			checkArmor(player, RegisterItems.rpa_helmet, RegisterItems.rpa_plate, RegisterItems.rpa_legs, RegisterItems.rpa_boots) ||
//			checkArmor(player, RegisterItems.fau_helmet, RegisterItems.fau_plate, RegisterItems.fau_legs, RegisterItems.fau_boots) ||
//			checkArmor(player, RegisterItems.dns_helmet, RegisterItems.dns_plate, RegisterItems.dns_legs, RegisterItems.dns_boots)) {
//			return true;
//		}
//
//		return false;
//	}
//
//	public static boolean checkForHazmatOnly(LivingEntity player) {
//		if(checkArmor(player, RegisterItems.hazmat_helmet, RegisterItems.hazmat_plate, RegisterItems.hazmat_legs, RegisterItems.hazmat_boots) ||
//			checkArmor(player, RegisterItems.hazmat_helmet_red, RegisterItems.hazmat_plate_red, RegisterItems.hazmat_legs_red, RegisterItems.hazmat_boots_red) ||
//			checkArmor(player, RegisterItems.hazmat_helmet_grey, RegisterItems.hazmat_plate_grey, RegisterItems.hazmat_legs_grey, RegisterItems.hazmat_boots_grey) ||
//			checkArmor(player, RegisterItems.hazmat_paa_helmet, RegisterItems.hazmat_paa_plate, RegisterItems.hazmat_paa_legs, RegisterItems.hazmat_paa_boots) ||
//			checkArmor(player, RegisterItems.paa_helmet, RegisterItems.paa_plate, RegisterItems.paa_legs, RegisterItems.paa_boots) ||
//			checkArmor(player, RegisterItems.liquidator_helmet, RegisterItems.liquidator_plate, RegisterItems.liquidator_legs, RegisterItems.liquidator_boots)){
//			return true;
//		}
//		return false;
//	}
//
//	public static boolean checkForHazmat(LivingEntity player) {
//		if(ArmorUtil.checkArmor(player, RegisterItems.hazmat_helmet, RegisterItems.hazmat_plate, RegisterItems.hazmat_legs, RegisterItems.hazmat_boots) ||
//			ArmorUtil.checkArmor(player, RegisterItems.hazmat_helmet_red, RegisterItems.hazmat_plate_red, RegisterItems.hazmat_legs_red, RegisterItems.hazmat_boots_red) ||
//			ArmorUtil.checkArmor(player, RegisterItems.hazmat_helmet_grey, RegisterItems.hazmat_plate_grey, RegisterItems.hazmat_legs_grey, RegisterItems.hazmat_boots_grey) ||
//			ArmorUtil.checkArmor(player, RegisterItems.t45_helmet, RegisterItems.t45_plate, RegisterItems.t45_legs, RegisterItems.t45_boots) ||
//			ArmorUtil.checkArmor(player, RegisterItems.schrabidium_helmet, RegisterItems.schrabidium_plate, RegisterItems.schrabidium_legs, RegisterItems.schrabidium_boots) ||
//			checkForHaz2(player)) {
//
//			return true;
//		}
//
////		if(player.hasEffect(RegisterPotions.mutation))
////			return true;
//
//		return false;
//	}
//
//	public static boolean checkForAsbestos(LivingEntity player) {
//
//		if(ArmorUtil.checkArmor(player, RegisterItems.asbestos_helmet, RegisterItems.asbestos_plate, RegisterItems.asbestos_legs, RegisterItems.asbestos_boots)) {
//			return true;
//		}
//
//		return false;
//	}

	public static boolean checkArmor(LivingEntity player, Item helm, Item chest, Item leg, Item shoe) {
		if(player.getItemBySlot(EquipmentSlot.FEET).getItem() == shoe && 
			player.getItemBySlot(EquipmentSlot.LEGS).getItem() == leg && 
			player.getItemBySlot(EquipmentSlot.CHEST).getItem() == chest && 
			player.getItemBySlot(EquipmentSlot.HEAD).getItem() == helm) {
			return true;
		}
	
		return false;
	}

	public static boolean checkArmorPiece(Player player, Item armor, int slot) {
		if(player.getInventory().armor.get(slot).getItem() == armor) {
			return true;
		}
	
		return false;
	}
	
	/*
	 * Default implementations for IGasMask items
	 */
	public static final String FILTER_KEY = "hfrFilter";
	
	public static void damageGasMaskFilter(LivingEntity entity, int damage) {
		
		ItemStack mask = entity.getItemBySlot(EquipmentSlot.HEAD);
		
		if(mask.isEmpty())
			return;
		
		if(!(mask.getItem() instanceof IGasMask)) {
			
			if(ArmorModHandler.hasMods(mask)) {
				
				ItemStack[] mods = ArmorModHandler.pryMods(mask);
				
				if(mods[ArmorModHandler.helmet_only.ordinal()] != null && mods[ArmorModHandler.helmet_only.ordinal()].getItem() instanceof IGasMask)
					mask = mods[ArmorModHandler.helmet_only.ordinal()];
			}
		}
		
		if(mask != null)
			damageGasMaskFilter(mask, damage);
	}
	
	public static void damageGasMaskFilter(ItemStack mask, int damage) {
		ItemStack filter = getGasMaskFilter(mask);
		
		if(filter == null) {
			if(ArmorModHandler.hasMods(mask)) {
				ItemStack[] mods = ArmorModHandler.pryMods(mask);
				
				if(mods[ArmorModHandler.helmet_only.ordinal()] != null && mods[ArmorModHandler.helmet_only.ordinal()].getItem() instanceof IGasMask)
					filter = getGasMaskFilter(mods[ArmorModHandler.helmet_only.ordinal()]);
			}
		}
		
		if(filter == null || filter.getMaxDamage() == 0)
			return;
		
		filter.setDamageValue(filter.getDamageValue() + damage);
		
		if(filter.getDamageValue() > filter.getMaxDamage()){
			removeFilter(mask);
		}
		else{
			installGasMaskFilter(mask, filter);
		}
	}

	public static void installGasMaskFilter(ItemStack mask, ItemStack filter) {
		if (mask.isEmpty() || filter.isEmpty()) return;

		// Ensure the mask has a tag
		CompoundTag maskTag = mask.getOrCreateTag();

		// Serialize the filter into its own tag
		CompoundTag filterTag = new CompoundTag();
		filter.save(filterTag);

		// Attach under FILTER_KEY
		maskTag.put(FILTER_KEY, filterTag);
	}

	public static void removeFilter(ItemStack mask) {
		if (mask.isEmpty()) return;
		CompoundTag maskTag = mask.getTag();
		if (maskTag == null || !maskTag.contains(FILTER_KEY)) return;

		// Remove the filter entry
		maskTag.remove(FILTER_KEY);

		// If nothing left in the tag, clear it entirely
		if (maskTag.isEmpty()) {
			mask.setTag(null); // removes the entire tag compound
		}
	}

	public static ItemStack getGasMaskFilter(ItemStack mask) {
		if (mask.isEmpty()) return ItemStack.EMPTY;
		CompoundTag maskTag = mask.getTag();
		if (maskTag == null || !maskTag.contains(FILTER_KEY, Tag.TAG_COMPOUND)) {

//			System.out.println("[Debug] filter is empty");
			return ItemStack.EMPTY;
		}

		CompoundTag filterTag = maskTag.getCompound(FILTER_KEY);
		ItemStack filter = ItemStack.of(filterTag);
//		System.err.println("[Debug] filter: " + filter.getItem().getDescriptionId());
		return filter.isEmpty() ? ItemStack.EMPTY : filter;
	}
	
//	public static boolean checkForDigamma(Player player) {
//
//		if(checkArmor(player, RegisterItems.fau_helmet, RegisterItems.fau_plate, RegisterItems.fau_legs, RegisterItems.fau_boots))
//			return true;
//
//		if(checkArmor(player, RegisterItems.dns_helmet, RegisterItems.dns_plate, RegisterItems.dns_legs, RegisterItems.dns_boots))
//			return true;
//
////		if(player.hasEffect(RegisterPotions.stability))
////			return true;
//
//		return false;
//	}
	
//	public static boolean checkForMonoMask(Player player) {
//
//		if(checkArmorPiece(player, RegisterItems.gas_mask, 3)) {
//			return true;
//		}
//		if(checkArmorPiece(player, RegisterItems.gas_mask_m65, 3)) {
//			return true;
//		}
//
//		if(checkArmorPiece(player, RegisterItems.gas_mask_mono, 3))
//			return true;
//
//		if(checkArmorPiece(player, RegisterItems.liquidator_helmet, 3))
//			return true;
//
////		if(player.hasEffect(RegisterPotions.mutation))
////			return true;
//
//		ItemStack helmet = player.getInventory().armor.get(3);
//		if(ArmorModHandler.hasMods(helmet)) {
//
//			ItemStack[] mods = ArmorModHandler.pryMods(helmet);
//
//			if(mods[ArmorModHandler.helmet_only.ordinal()] != null && mods[ArmorModHandler.helmet_only.ordinal()].getItem() == RegisterItems.attachment_mask_mono)
//				return true;
//		}
//
//		return false;
//	}
	
//	public static boolean checkForGoggles(Player player) {
//
//		if(checkArmorPiece(player, RegisterItems.goggles, 3))
//		{
//			return true;
//		}
//		if(checkArmorPiece(player, RegisterItems.ashglasses, 3))
//		{
//			return true;
//		}
//		if(checkArmorPiece(player, RegisterItems.t45_helmet, 3))
//		{
//			return true;
//		}
//		if(checkArmorPiece(player, RegisterItems.ajr_helmet, 3))
//		{
//			return true;
//		}
//		if(checkArmorPiece(player, RegisterItems.rpa_helmet, 3))
//		{
//			return true;
//		}
//		if(checkArmorPiece(player, RegisterItems.bj_helmet, 3))
//		{
//			return true;
//		}
//		if(checkArmorPiece(player, RegisterItems.hev_helmet, 3))
//		{
//			return true;
//		}
//		if(checkArmorPiece(player, RegisterItems.hazmat_paa_helmet, 3))
//		{
//			return true;
//		}
//		if(checkArmorPiece(player, RegisterItems.paa_helmet, 3))
//		{
//			return true;
//		}
//		return false;
//	}

	/**
	 * Grabs the installed filter or the filter of the attachment, used for attachment rendering
	 * @param mask
	 * @return
	 */
	public static ItemStack getGasMaskFilterRecursively(ItemStack mask) {
		
		ItemStack filter = getGasMaskFilter(mask);
		
		if((filter == null || filter.isEmpty()) && ArmorModHandler.hasMods(mask)) {
			
			ItemStack[] mods = ArmorModHandler.pryMods(mask);
			
			if(mods[ArmorModHandler.helmet_only.ordinal()] != null && mods[ArmorModHandler.helmet_only.ordinal()].getItem() instanceof IGasMask)
				filter = ((IGasMask)mods[ArmorModHandler.helmet_only.ordinal()].getItem()).getFilter(mods[ArmorModHandler.helmet_only.ordinal()]);
		}

//		if(filter != null)
//        	System.err.println("[Debug] recursive return filter: " + filter.getItem().getDescriptionId());
//		else
//			System.err.println("[Debug] recursive return filter: null");

		return filter;
	}

	public static void addGasMaskTooltip(ItemStack mask, Level world, List<Component> list, TooltipFlag flagIn){
		
		if(mask == null || !(mask.getItem() instanceof IGasMask))
			return;
		
		ItemStack filter = ((IGasMask)mask.getItem()).getFilter(mask);
		
		if(filter.isEmpty()) {
			list.add(Component.literal("§c" + I18nUtil.resolveKey("desc.nofilter")));
			return;
		}
		
		list.add(Component.literal("§6" + I18nUtil.resolveKey("desc.infilter")));
		
		int meta = filter.getDamageValue();
		int max = filter.getMaxDamage();
		
		String append = "";
		String dur = "";

		if(max > 0) {
			append = " (" + Library.getPercentage((max - meta) / (double)max) + "%)";
			dur = (max-meta)+"/"+max;
		}
		
		List<Component> lore = new ArrayList();
		list.add(Component.literal("  " + filter.getDisplayName().getString().replace("[", "").replace("]", "") + append));
		list.add(Component.literal("  " + dur));
		filter.getItem().appendHoverText(filter, world, lore, flagIn);
		ForgeEventFactory.onItemTooltip(filter, null, lore, flagIn);
		lore.forEach(x -> list.add(Component.literal("§e  ").append(x)));
	}
	
	// public static boolean isWearingEmptyMask(Player player) {
		
	// 	ItemStack mask = player.getEquipmentInSlot(4);
		
	// 	if(mask == null)
	// 		return false;
		
	// 	if(mask.getItem() instanceof IGasMask) {
	// 		return getGasMaskFilter(mask) == null;
	// 	}
		
	// 	ItemStack mod = ArmorModHandler.pryMods(mask)[ArmorModHandler.helmet_only];
		
	// 	if(mod != null && mod.getItem() instanceof IGasMask) {
	// 		return getGasMaskFilter(mod) == null;
	// 	}
		
	// 	return false;
	// }
}