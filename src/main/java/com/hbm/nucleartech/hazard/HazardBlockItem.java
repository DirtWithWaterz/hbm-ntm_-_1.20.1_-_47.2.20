package com.hbm.nucleartech.hazard;

import com.hbm.nucleartech.interfaces.IItemHazard;
import com.hbm.nucleartech.modules.ItemHazardModule;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HazardBlockItem extends BlockItem implements IItemHazard {

    ItemHazardModule module;

    public HazardBlockItem(double radiation, Block block, Properties pProperties) {
        super(block, pProperties);
        this.module = new ItemHazardModule();
        this.module.addRadiation(radiation);
    }

    public HazardBlockItem(double radiation, double digamma, Block block, Properties pProperties) {
        super(block, pProperties);
        this.module = new ItemHazardModule();
        this.module.addRadiation(radiation);
        this.module.addDigamma(digamma);
    }

    public HazardBlockItem(double radiation, double digamma, int fire, Block block, Properties pProperties) {
        super(block, pProperties);
        this.module = new ItemHazardModule();
        this.module.addRadiation(radiation);
        this.module.addDigamma(digamma);
        this.module.addFire(fire);
    }

    public HazardBlockItem(double radiation, double digamma, int fire, int cryogenic, Block block, Properties pProperties) {
        super(block, pProperties);
        this.module = new ItemHazardModule();
        this.module.addRadiation(radiation);
        this.module.addDigamma(digamma);
        this.module.addFire(fire);
        this.module.addCryogenic(cryogenic);
    }

    public HazardBlockItem(double radiation, double digamma, int fire, int cryogenic, int toxiclvl, Block block, Properties pProperties) {
        super(block, pProperties);
        this.module = new ItemHazardModule();
        this.module.addRadiation(radiation);
        this.module.addDigamma(digamma);
        this.module.addFire(fire);
        this.module.addCryogenic(cryogenic);
        this.module.addToxic(toxiclvl);
    }

    public HazardBlockItem(double radiation, double digamma, int fire, int cryogenic, int toxiclvl, int asbestos, Block block, Properties pProperties) {
        super(block, pProperties);
        this.module = new ItemHazardModule();
        this.module.addRadiation(radiation);
        this.module.addDigamma(digamma);
        this.module.addFire(fire);
        this.module.addCryogenic(cryogenic);
        this.module.addToxic(toxiclvl);
        this.module.addAsbestos(asbestos);
    }

    public HazardBlockItem(double radiation, double digamma, int fire, int cryogenic, int toxiclvl, int asbestos, int coal, Block block, Properties pProperties) {
        super(block, pProperties);
        this.module = new ItemHazardModule();
        this.module.addRadiation(radiation);
        this.module.addDigamma(digamma);
        this.module.addFire(fire);
        this.module.addCryogenic(cryogenic);
        this.module.addToxic(toxiclvl);
        this.module.addAsbestos(asbestos);
        this.module.addCoal(coal);
    }

    public HazardBlockItem(double radiation, double digamma, int fire, int cryogenic, int toxiclvl, int asbestos, int coal, boolean blinding, Block block, Properties pProperties) {
        super(block, pProperties);
        this.module = new ItemHazardModule();
        this.module.addRadiation(radiation);
        this.module.addDigamma(digamma);
        this.module.addFire(fire);
        this.module.addCryogenic(cryogenic);
        this.module.addToxic(toxiclvl);
        this.module.addAsbestos(asbestos);
        this.module.addCoal(coal);
        if(blinding)
            this.module.addBlinding();
    }

    public HazardBlockItem(double radiation, double digamma, int fire, int cryogenic, int toxiclvl, int asbestos, int coal, boolean blinding, boolean hydroReactive, Block block, Properties pProperties) {
        super(block, pProperties);
        this.module = new ItemHazardModule();
        this.module.addRadiation(radiation);
        this.module.addDigamma(digamma);
        this.module.addFire(fire);
        this.module.addCryogenic(cryogenic);
        this.module.addToxic(toxiclvl);
        this.module.addAsbestos(asbestos);
        this.module.addCoal(coal);
        if(blinding)
            this.module.addBlinding();
        if(hydroReactive)
            this.module.addHydroReactivity();
    }

    public HazardBlockItem(double radiation, double digamma, int fire, int cryogenic, int toxiclvl, int asbestos, int coal, boolean blinding, boolean hydroReactive, float explosive, Block block, Properties pProperties) {
        super(block, pProperties);
        this.module = new ItemHazardModule();
        this.module.addRadiation(radiation);
        this.module.addDigamma(digamma);
        this.module.addFire(fire);
        this.module.addCryogenic(cryogenic);
        this.module.addToxic(toxiclvl);
        this.module.addAsbestos(asbestos);
        this.module.addCoal(coal);
        if(blinding)
            this.module.addBlinding();
        if(hydroReactive)
            this.module.addHydroReactivity();
        this.module.addExplosive(explosive);
    }

    @Override
    public ItemHazardModule getModule() {

        return this.module;
    }


    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);

        if(!pLevel.isClientSide && pEntity instanceof LivingEntity)
            this.module.applyEffects(
                    (LivingEntity) pEntity,
                    pStack.getCount(),
                    pSlotId,
                    pIsSelected,
                    ((LivingEntity)pEntity).getMainHandItem() == pStack ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND
            );
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {

        this.module.addInformation(pStack, pTooltipComponents, pIsAdvanced);
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {

        boolean m = this.module.onEntityItemUpdate(entity);
        boolean i = super.onEntityItemUpdate(stack, entity);

        return m || i;
    }
}
