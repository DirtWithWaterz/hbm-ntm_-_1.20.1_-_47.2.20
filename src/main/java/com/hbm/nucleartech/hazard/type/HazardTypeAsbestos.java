package com.hbm.nucleartech.hazard.type;

import com.hbm.nucleartech.hazard.HazardModifier;
import com.hbm.nucleartech.util.I18nUtil;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class HazardTypeAsbestos extends HazardTypeBase {

    @Override
    public void onUpdate(LivingEntity target, float level, ItemStack stack) {

        if(ArmorRegistry.hasProtection(target, EquipmentSlot.HEAD, HazardClass.PARTICLE_FINE))
            ArmorUtil.damageGasMaskFilter(target, (int) level);
        else
            HBMLivingProps.incrementAsbestos(target, (int) Math.min(level, 10));
    }

    @Override
    public void updateEntity(ItemEntity item, float level) {

    }

    @Override
    public void addHazardInformation(Player player, List<String> list, float level, ItemStack stack, List<HazardModifier> modifiers) {

        list.add("§f[" + I18nUtil.resolveKey("trait.asbestos") + "]");
    }
}
