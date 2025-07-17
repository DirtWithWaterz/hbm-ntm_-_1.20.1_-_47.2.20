package com.hbm.nucleartech.handler;

import java.util.Arrays;
import java.util.UUID;

import com.hbm.nucleartech.item.special.ItemArmorMod;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

public class ArmorModHandler {

	public static final ArmorItem.Type helmet_only = ArmorItem.Type.HELMET;
	public static final ArmorItem.Type plate_only = ArmorItem.Type.CHESTPLATE;
	public static final ArmorItem.Type legs_only = ArmorItem.Type.LEGGINGS;
	public static final ArmorItem.Type boots_only = ArmorItem.Type.BOOTS;
	public static final ArmorItem.Type servos = ArmorItem.Type.CHESTPLATE;
	public static final ArmorItem.Type cladding = ArmorItem.Type.CHESTPLATE;
	public static final ArmorItem.Type kevlar = ArmorItem.Type.CHESTPLATE;
	public static final ArmorItem.Type extra = ArmorItem.Type.CHESTPLATE;
	
	public static final UUID[] UUIDs = new UUID[] {
			UUID.fromString("8d6e5c77-133e-4056-9c80-a9e42a1a0b65"),
			UUID.fromString("b1b7ee0e-1d14-4400-8037-f7f2e02f21ca"),
			UUID.fromString("30b50d2a-4858-4e5b-88d4-3e3612224238"),
			UUID.fromString("426ee0d0-7587-4697-aaef-4772ab202e78")
	};
	
	public static final UUID[] fixedUUIDs = new UUID[] {
			UUID.fromString("e572caf4-3e65-4152-bc79-c4d4048cbd29"),
			UUID.fromString("bed30902-8a6a-4769-9f65-2a9b67469fff"),
			UUID.fromString("baebf7b3-1eda-4a14-b233-068e2493e9a2"),
			UUID.fromString("28016c1b-d992-4324-9409-a9f9f0ffb85c")
	};
	
	//The key for the NBTTagCompound that holds the armor mods
	public static final String MOD_COMPOUND_KEY = "ntm_armor_mods";
	//The key for the specific slot inside the armor mod NBT Tag
	public static final String MOD_SLOT_KEY = "mod_slot_";

	/**
	 * Checks if a mod can be applied to an armor piece
	 * Needs to be used to prevent people from inserting invalid items into the armor table
	 * @param armor
	 * @param mod
	 * @return
	 */
	public static boolean isApplicable(ItemStack armor, ItemStack mod) {

		if(armor == null || mod == null)
			return false;

		if(!(armor.getItem() instanceof ArmorItem))
			return false;

		if(!(mod.getItem() instanceof ItemArmorMod))
			return false;

		EquipmentSlot type = ((ArmorItem)armor.getItem()).getEquipmentSlot();

		ItemArmorMod aMod = (ItemArmorMod)mod.getItem();

		return (type == EquipmentSlot.HEAD && aMod.helmet) || (type == EquipmentSlot.CHEST && aMod.chestplate) || (type == EquipmentSlot.LEGS && aMod.leggings) || (type == EquipmentSlot.FEET && aMod.boots);
	}

	/**
	 * Applies a mod to the given armor piece
	 * Make sure to check for applicability first
	 * Will override present mods so make sure to only use unmodded armor pieces
	 * @param armor
	 * @param mod
	 */
	public static void applyMod(ItemStack armor, ItemStack mod) {

		if (armor.isEmpty() || mod.isEmpty()) return;

		// Ensure root tag exists
		CompoundTag root = armor.getOrCreateTag();

		// Ensure the sub‑compound for mods exists
		CompoundTag mods = root.contains(MOD_COMPOUND_KEY, Tag.TAG_COMPOUND)
				? root.getCompound(MOD_COMPOUND_KEY)
				: new CompoundTag();
		root.put(MOD_COMPOUND_KEY, mods);

		// Serialize the mod into its own CompoundTag
		CompoundTag modNbt = new CompoundTag();
		mod.save(modNbt);

		// Slot index comes from your ItemArmorMod.type
		int slot = ((ItemArmorMod) mod.getItem()).type.getSlot().getIndex();
		mods.put(MOD_SLOT_KEY + slot, modNbt);
	}

	/**
	 * Removes the mod from the given slot
	 * @param armor
	 * @param slot
	 */
	public static void removeMod(ItemStack armor, int slot) {

		if (armor.isEmpty()) return;

		CompoundTag root = armor.getTag();
		if (root == null || !root.contains(MOD_COMPOUND_KEY, Tag.TAG_COMPOUND)) return;

		CompoundTag mods = root.getCompound(MOD_COMPOUND_KEY);
		mods.remove(MOD_SLOT_KEY + slot);

		// If no mods left, clear entire mods compound
		if (mods.isEmpty()) {
			root.remove(MOD_COMPOUND_KEY);
			// If root is now empty, drop it too
			if (root.isEmpty()) {
				armor.setTag(null);
			}
		} else {
			// Otherwise write back the trimmed mods
			root.put(MOD_COMPOUND_KEY, mods);
		}
	}

	/** Removes _all_ mods from the armor piece. */
	public static void clearMods(ItemStack armor) {
		if (armor.isEmpty()) return;
		CompoundTag root = armor.getTag();
		if (root == null || !root.contains(MOD_COMPOUND_KEY, Tag.TAG_COMPOUND)) return;

		root.remove(MOD_COMPOUND_KEY);
		if (root.isEmpty()) {
			armor.setTag(null);
		}
	}

	/** Returns true if this armor stack has _any_ mods saved. */
	public static boolean hasMods(ItemStack armor) {
		if (armor.isEmpty()) return false;
		CompoundTag root = armor.getTag();
		return root != null && root.contains(MOD_COMPOUND_KEY, Tag.TAG_COMPOUND);
	}

	/**
	 * Extracts up to 8 mod ItemStacks (one per slot) from the armor.
	 * Empty slots come back as `ItemStack.EMPTY`.
	 */
	public static ItemStack[] pryMods(ItemStack armor) {
		ItemStack[] slots = new ItemStack[8];
		Arrays.fill(slots, ItemStack.EMPTY);

		if (!hasMods(armor)) {
			return slots;
		}

		CompoundTag root = armor.getTag();
		CompoundTag mods = root.getCompound(MOD_COMPOUND_KEY);

		for (int i = 0; i < slots.length; i++) {
			String key = MOD_SLOT_KEY + i;
			if (mods.contains(key, Tag.TAG_COMPOUND)) {
				CompoundTag modTag = mods.getCompound(key);
				slots[i] = ItemStack.of(modTag);
			}
		}
		return slots;
	}

	/** Reads the single mod in the given slot, or `ItemStack.EMPTY` if none. */
	public static ItemStack pryMod(ItemStack armor, int slot) {
		if (!hasMods(armor)) return ItemStack.EMPTY;
		CompoundTag mods = armor.getTag().getCompound(MOD_COMPOUND_KEY);
		String key = MOD_SLOT_KEY + slot;
		if (!mods.contains(key, Tag.TAG_COMPOUND)) {
			return ItemStack.EMPTY;
		}
		return ItemStack.of(mods.getCompound(key));
	}
}