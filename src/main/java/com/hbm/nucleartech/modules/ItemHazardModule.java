package com.hbm.nucleartech.modules;

import com.hbm.nucleartech.item.RegisterItems;
import com.hbm.nucleartech.lib.Library;
import com.hbm.nucleartech.util.ArmorRegistry;
import com.hbm.nucleartech.util.ArmorRegistry.HazardClass;
import com.hbm.nucleartech.util.ArmorUtil;
import com.hbm.nucleartech.util.ContaminationUtil;
import com.hbm.nucleartech.util.ContaminationUtil.HazardType;
import com.hbm.nucleartech.util.ContaminationUtil.ContaminationType;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

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

        if(entity instanceof Player player /* && !GeneralConfig.enable528 */) {

//            if(player.isCreative() || player.isSpectator())
//                return;

            reacher = Library.checkForHeld(player, RegisterItems.REACHER.get());
        }


        if(this.radiation * tempMod > 0) {

            double rad = this.radiation * tempMod * mod / 20f;

//            System.out.println("[Debug] reacher: " + reacher);
//            System.out.println("[Debug] og rad: " + rad);

            if(reacher)
                rad = (double) Math.min(Math.sqrt(rad), rad);

//            System.out.println("[Debug] new rad: " + rad);

//            System.err.println("calling ContaminationUtil.contaminate() for " + entity.getName().getString() + " with rad value: " + rad);
            ContaminationUtil.contaminate(entity, HazardType.RADIATION, ContaminationType.CREATIVE, (float) rad);
        }
        if(this.toxic * tempMod > 0) {

            boolean hasToxFilter = false;
            boolean hasHazmat = false;
            if(entity instanceof Player player) {

                if(ArmorRegistry.hasProtection(player, EquipmentSlot.HEAD, HazardClass.NERVE_AGENT)) {

                    ArmorUtil.damageGasMaskFilter(player, 1);
                    hasToxFilter = true;
                }
                hasHazmat = ArmorUtil.checkForHazmat(entity);
            }

            if(!hasToxFilter && !hasHazmat) {

                entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 110, this.toxic-1, true, false));

                if(this.toxic > 2)
                    entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 110, Math.min(4, this.toxic-1), true, false));
                if(this.toxic > 4)
                    entity.addEffect(new MobEffectInstance(MobEffects.HUNGER, 110, this.toxic, true, false));
                if(this.toxic > 6)
                    if(entity.level().random.nextInt((int)(2000/toxic)) == 0)
                        entity.addEffect(new MobEffectInstance(MobEffects.POISON, 110, this.toxic-4, true, false));
            }
            if(!(hasHazmat && hasToxFilter)) {

                if(this.toxic > 8)
                    entity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 110, this.toxic-6, true, false));
                if(this.toxic > 16)
                    entity.addEffect(new MobEffectInstance(MobEffects.HARM, 110, this.toxic-16, true, false));
            }
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

        // Rad
        if(this.radiation * tempMod > 0) {

            list.add(Component.literal("§a[Radioactive]"));
            double itemRad = radiation * tempMod;
            list.add(Component.literal("§e" + (Library.roundDouble(getNewValue(itemRad), 3) + getSuffix(itemRad) + " RAD/s")));

            if(stack.getCount() > 1) {

                double stackRad = radiation * tempMod * stack.getCount();
                list.add(Component.literal("§eStack: " + Library.roundDouble(getNewValue(stackRad), 3) + getSuffix(stackRad) + " RAD/s"));
            }
        }
        // Pyro

        // Toxic
        if(this.toxic * tempMod > 0) {

            if(this.toxic > 16)
                list.add(Component.literal("[").append(Component.translatable("adjective.extreme")).append(" ").append(Component.translatable("trait.toxic")).append("]").withStyle(ChatFormatting.GREEN));
            else if(this.toxic > 8)
                list.add(Component.literal("[").append(Component.translatable("adjective.very_high")).append(" ").append(Component.translatable("trait.toxic")).append("]").withStyle(ChatFormatting.GREEN));
            else if(this.toxic > 4)
                list.add(Component.literal("[").append(Component.translatable("adjective.high")).append(" ").append(Component.translatable("trait.toxic")).append("]").withStyle(ChatFormatting.GREEN));
            else if(this.toxic > 2)
                list.add(Component.literal("[").append(Component.translatable("adjective.medium")).append(" ").append(Component.translatable("trait.toxic")).append("]").withStyle(ChatFormatting.GREEN));
            else
                list.add(Component.literal("[").append(Component.translatable("adjective.low")).append(" ").append(Component.translatable("trait.toxic")).append("]").withStyle(ChatFormatting.GREEN));
        }
        // Blinding

        // Hydro
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

            if(this.isRadioactive()) {

//                System.err.println("[Debug] radiating...");
                ContaminationUtil.radiate((ServerLevel) item.level(), item.getOnPos().getX(), item.getOnPos().getY()+1, item.getOnPos().getZ(), 32, (float)(this.radiation*0.00004D-(0.00004D*20)), item.getOnPos().offset(0, 1, 0));
            }
        }

        return false;
    }
}
