package com.hbm.nucleartech.handler;

import com.hbm.nucleartech.item.special.ItemArmorMod;
import com.hbm.nucleartech.util.ArmorRegistry;
import com.hbm.nucleartech.util.ArmorRegistry.HazardClass;
import com.hbm.nucleartech.util.ContaminationUtil;
import com.hbm.nucleartech.util.I18nUtil;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import java.util.List;

@Mod.EventBusSubscriber
public class ItemHandler {

    @SubscribeEvent
    public static void drawTooltip(ItemTooltipEvent event) {


        long window = Minecraft.getInstance().getWindow().getWindow();
        boolean leftShiftDown = InputConstants.isKeyDown(window, GLFW.GLFW_KEY_LEFT_SHIFT);

        ItemStack stack = event.getItemStack();
        List<Component> list = event.getToolTip();

        /// HAZMAT INFO ///
        List<HazardClass> hazInfo = ArmorRegistry.hazardClasses.get(stack.getItem());

        if(hazInfo != null) {

            if(leftShiftDown) {
                list.add(Component.literal(ChatFormatting.GOLD + I18nUtil.resolveKey("hazard.prot")));
                for(HazardClass clazz : hazInfo) {
                    list.add(Component.literal(ChatFormatting.YELLOW + "  " + I18nUtil.resolveKey(clazz.lang)));
                }
            } else {

                list.add(Component.literal(I18nUtil.resolveKey("desc.tooltip.hold", "LSHIFT")));
            }
        }

        /// CLADDING ///
        double rad = HazmatRegistry.getResistance(stack);
//        rad = ((int) (rad * 100)) / 100D;
        if(rad > 0)
            list.add(Component.literal(ChatFormatting.YELLOW + I18nUtil.resolveKey("trait.rad_resistance", rad)));


        /// ARMOR MODS ///
        if(stack.getItem() instanceof ArmorItem && ArmorModHandler.hasMods(stack)) {

            if(!leftShiftDown) {

                list.add(Component.literal(ChatFormatting.DARK_GRAY + "" + ChatFormatting.ITALIC + "Hold <" +
                        ChatFormatting.YELLOW + "" + ChatFormatting.ITALIC + "LSHIFT" +
                        ChatFormatting.DARK_GRAY + "" + ChatFormatting.ITALIC + "> to display installed armor mods"));

            } else {

                list.add(Component.literal(ChatFormatting.YELLOW + "Mods:"));

                ItemStack[] mods = ArmorModHandler.pryMods(stack);

                for(int i = 0; i < 8; i++) {

                    if(mods[i] != null && mods[i].getItem() instanceof ItemArmorMod) {

                        ((ItemArmorMod)mods[i].getItem()).addDesc(list, mods[i], stack);
                    }
                }
            }
        }

//        System.err.println("[Debug] Adding neutron rad info...");

        /// NEUTRON RADS ///
        ContaminationUtil.addNeutronRadInfo(stack, event.getEntity(), list, event.getFlags());

        /// HAZARDS ///
//        HazardSystem.addHazardInfo(stack, event.getEntityPlayer(), list, event.getFlags());

        /// BREEDING ///
//        BreederRecipes.addBreedingTips(stack, event.getEntityPlayer(), list, event.getFlags());

        //MKU
//        if(stack.hasTagCompound()){
//            if(stack.getTagCompound().getBoolean("ntmContagion"))
//                list.add("§4§l[" + I18nUtil.resolveKey("trait.mkuinfected") + "§4§l]");
//        }

        //Foundry
//        if(event.getFlags().isAdvanced()) {
//            Mats.drawFoundryTips(stack, list, Keyboard.isKeyDown(Keyboard.KEY_LSHIFT));
//        }

        /// CUSTOM NUKE ///
//        RecipesCommon.ComparableStack comp = new RecipesCommon.NbtComparableStack(stack).makeSingular();
//        CustomNukeEntry entry = TileEntityNukeCustom.entries.get(comp);
//
//        if(entry != null) {
//
//            if(!list.isEmpty())
//                list.add("");
//
//            if(entry.entry == EnumEntryType.ADD)
//                list.add(ChatFormatting.GOLD + "Adds " + entry.value + " to the custom nuke stage " + entry.type);
//
//            if(entry.entry == EnumEntryType.MULT)
//                list.add(ChatFormatting.GOLD + "Adds multiplier " + entry.value + " to the custom nuke stage " + entry.type);
//        }
//
//        if(event.getFlags().isAdvanced()) {
//            List<String> names = ItemStackUtil.getOreDictNames(stack);
//
//            if(names.size() > 0) {
//                list.add("§bOre Dict:");
//                for(String s : names) {
//                    list.add("§3 - " + s);
//                }
//            }
//        }
    }

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {

        if(event.side.isClient())
            return;

        if(event.phase == TickEvent.Phase.END) {

            Level level = event.level;

            for(Player p : level.players()) {

                LevelChunk chunk = level.getChunkAt(p.getOnPos());

                AABB aabb = RadiationSystemNT.getAabb(level, chunk.getPos());

                List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, aabb);

                if(items.isEmpty())
                    continue;

                for(ItemEntity i : items) {

                    float neutronRads = ContaminationUtil.getNeutronRads(i.getItem());

                    if(neutronRads == 0)
                        continue;

                    ContaminationUtil.radiate((ServerLevel) level, i.getOnPos().getX()+0.5, i.getOnPos().getY()+1, i.getOnPos().getZ()+0.5, 5, neutronRads);
                }
            }
        }
    }
}
