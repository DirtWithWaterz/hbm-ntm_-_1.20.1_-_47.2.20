package com.hbm.nucleartech.item.special;

import com.google.common.collect.Multimap;
import com.hbm.nucleartech.util.I18nUtil;
import com.hbm.nucleartech.handler.ArmorModHandler;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.List;

public class ItemArmorMod extends CustomLoreArmorItem {

    public final Type type;
    public final boolean helmet;
    public final boolean chestplate;
    public final boolean leggings;
    public final boolean boots;

    public ItemArmorMod(Properties properties, ArmorMaterial material, Type type,
                        boolean helmet, boolean chestplate, boolean leggings, boolean boots) {
        super(material, type, properties);
        this.type = type;
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
    }

    @Override
    public void appendHoverText(ItemStack stack, net.minecraft.world.level.Level world, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        tooltip.add(Component.literal(I18nUtil.resolveKey("desc.applicable")).withStyle(ChatFormatting.DARK_PURPLE));

        if (helmet && chestplate && leggings && boots) {
            tooltip.add(Component.literal("  " + I18nUtil.resolveKey("desc.applicableall")));
        } else {
            if (helmet)
                tooltip.add(Component.literal("  " + I18nUtil.resolveKey("desc.applicableh")));
            if (chestplate)
                tooltip.add(Component.literal("  " + I18nUtil.resolveKey("desc.applicablec")));
            if (leggings)
                tooltip.add(Component.literal("  " + I18nUtil.resolveKey("desc.applicablel")));
            if (boots)
                tooltip.add(Component.literal("  " + I18nUtil.resolveKey("desc.applicableb")));
        }
        tooltip.add(Component.literal(I18nUtil.resolveKey("desc.applicableslot")).withStyle(ChatFormatting.DARK_PURPLE));

        if(this.type == ArmorModHandler.helmet_only)
            tooltip.add(Component.literal("  " + I18nUtil.resolveKey("desc.applicableh1")));

        else if(this.type == ArmorModHandler.plate_only)
            tooltip.add(Component.literal("  " + I18nUtil.resolveKey("desc.applicablec1")));

        else if(this.type == ArmorModHandler.legs_only)
            tooltip.add(Component.literal("  " + I18nUtil.resolveKey("desc.applicablel1")));

        else if(this.type == ArmorModHandler.boots_only)
            tooltip.add(Component.literal("  " + I18nUtil.resolveKey("desc.applicableb1")));

        else if(this.type == ArmorModHandler.servos)
            tooltip.add(Component.literal("  " + I18nUtil.resolveKey("desc.applicableservo")));

        else if(this.type == ArmorModHandler.cladding)
            tooltip.add(Component.literal("  " + I18nUtil.resolveKey("desc.applicablecladding")));

        else if(this.type == ArmorModHandler.kevlar)
            tooltip.add(Component.literal("  " + I18nUtil.resolveKey("desc.applicableinsert")));

        else if(this.type == ArmorModHandler.extra)
            tooltip.add(Component.literal("  " + I18nUtil.resolveKey("desc.applicableextra")));


    }

    public boolean isCompleteSet(List<ArmorItem> set) {

        return false;
    }

    @Override
    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
        super.onInventoryTick(stack, level, player, slotIndex, selectedIndex);
    }

    @OnlyIn(Dist.CLIENT)
    public void addDesc(List<Component> list, ItemStack stack, ItemStack armor) {

        list.add(stack.getDisplayName());
    }

    public void modUpdate(LivingEntity entity, ItemStack armor) {

    }

    public void modDamage(LivingHurtEvent event, ItemStack armor) {
        // migrate modDamage logic if needed
    }

    public Multimap<String, AttributeModifier> getModifiers(EquipmentSlot slot, ItemStack armor) { return null; }

    @OnlyIn(Dist.CLIENT)
    public void modRender(RenderPlayerEvent.Pre event, ItemStack armor) { }

//    @Override
//    public void initializeClient(java.util.function.Consumer<net.minecraft.client.renderer.ItemBlockRenderTypes> consumer) {
//        // client-specific rendering if needed
//    }

//    @Override
//    public Rarity getRarity(ItemStack stack) {
//        return material.getRarity();
//    }

//    @Override
//    public int getDefenseForSlot(EquipmentSlot slot, ItemStack stack) {
//        return material.getDefenseForSlot(slot);
//    }

//    @Override
//    public int getEnchantmentValue() {
//        return material.getEnchantmentValue();
//    }

//    @Override
//    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
//        return "hbm:textures/models/armor/armor_mod.png";
//    }

    // Additional helper methods migrated as needed
}
