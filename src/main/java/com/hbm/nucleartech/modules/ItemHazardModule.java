package com.hbm.nucleartech.modules;

import com.hbm.nucleartech.item.RegisterItems;
import com.hbm.nucleartech.lib.Library;
import com.hbm.nucleartech.util.ContaminationUtil;
import com.hbm.nucleartech.util.ContaminationUtil.HazardType;
import com.hbm.nucleartech.util.ContaminationUtil.ContaminationType;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class ItemHazardModule {

    public double radiation;
    public double digamma;
    public int fire;
    public int cryogenic;
    public int toxic;
    public boolean blinding;
    public int asbestos;
    public int coal;
    public boolean hydro;
    public float explosive;

    public float tempMod = 1f;

    public void setMod(float tempMod) {

        this.tempMod = tempMod;
    }

    public boolean isRadioactive() {

        return this.radiation > 0;
    }

    public void addRadiation(double radiation) {

        this.radiation = radiation;
    }

    public void addDigamma(double digamma) {

        this.digamma = digamma;
    }

    public void addFire(int fire) {

        this.fire = fire;
    }

    public void addCryogenic(int cryogenic) {

        this.cryogenic = cryogenic;
    }

    public void addToxic(int toxiclvl) {

        this.toxic = toxiclvl;
    }

    public void addCoal(int coal) {

        this.coal = coal;
    }

    public void addAsbestos(int asbestos) {

        this.asbestos = asbestos;
    }

    public void addBlinding() {

        this.blinding = true;
    }

    public void addHydroReactivity() {

        this.hydro = true;
    }

    public void addExplosive(float bang) {

        this.explosive = bang;
    }

    public void applyEffects(LivingEntity entity, float mod, int slot, boolean currentItem, InteractionHand hand) {

        boolean reacher = false;

        if(entity instanceof Player /* && !GeneralConfig.enable528 */)
            reacher = Library.checkForHeld((Player) entity, RegisterItems.REACHER.get());

        if(this.radiation * tempMod > 0) {
            double rad = this.radiation * tempMod * mod / 20f;

            if(reacher)
                rad = (double) Math.min(Math.sqrt(rad), rad);

            ContaminationUtil.contaminate(entity, HazardType.RADIATION, ContaminationType.CREATIVE, rad);
        }
    }

    public static double getNewValue(double radiation) {
        
        if(radiation < 1000000){
            return radiation;
        } else if (radiation < 1000000000) {
            return radiation * 0.000001;
        }
        else {
            return radiation * 0.000000001;
        }
    }

    public static String getSuffix(double radiation) {

        if(radiation < 1000000){
            return "";
        } else if(radiation < 1000000000){
            return "M";
        } else{
            return "B";
        }
    }

    public void addInformation(ItemStack stack, List<Component> list, TooltipFlag flagIn) {

        if(this.radiation * tempMod > 0) {

            list.add(Component.literal("§a[Radioactive]"));
            double itemRad = radiation * tempMod;
            list.add(Component.literal("§e" + (Library.roundDouble(getNewValue(itemRad), 3) + getSuffix(itemRad) + " RAD/s")));

            if(stack.getCount() > 1) {

                double stackRad = radiation * tempMod * stack.getCount();
                list.add(Component.literal("§eStack: " + Library.roundDouble(getNewValue(stackRad), 3) + getSuffix(stackRad) + " RAD/s"));
            }
        }
    }

    public boolean onEntityItemUpdate(ItemEntity item) {

        if(!item.level().isClientSide) {

            if(this.hydro && (item.isInWaterOrRain())) {

                item.remove(RemovalReason.KILLED);
                item.level().explode(item, item.position().x, item.position().y, item.position().z, 2f, Level.ExplosionInteraction.TNT);
                return true;
            }

            if(this.explosive > 0 && item.isOnFire()) {

                item.remove(RemovalReason.KILLED);
                item.level().explode(item, item.position().x, item.position().y, item.position().z, this.explosive, Level.ExplosionInteraction.TNT);
                return true;
            }
        }

        return false;
    }
}
