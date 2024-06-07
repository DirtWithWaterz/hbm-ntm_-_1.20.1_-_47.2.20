package com.hbm.nucleartech.interfaces;

import com.hbm.nucleartech.modules.ItemHazardModule;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public interface IItemHazard {

    public ItemHazardModule getModule();

    public default IItemHazard addRadiation(double radiation) {

        this.getModule().addRadiation(radiation);
        return this;
    }

    public default IItemHazard addDigamma(double digamma) {

        this.getModule().addDigamma(digamma);
        return this;
    }

    public default IItemHazard addFire(int fire) {

        this.getModule().addFire(fire);
        return this;
    }

    public default IItemHazard addCryogenic(int cryo) {

        this.getModule().addCryogenic(cryo);
        return this;
    }

    public default IItemHazard addToxic(int toxiclvl) {

        this.getModule().addToxic(toxiclvl);
        return this;
    }

    public default IItemHazard addAsbestos(int asbestos) {

        this.getModule().addAsbestos(asbestos);
        return this;
    }

    public default IItemHazard addCoal(int coal) {

        this.getModule().addCoal(coal);
        return this;
    }

    public default IItemHazard addBlinding() {

        this.getModule().addBlinding();
        return this;
    }

    public default IItemHazard addHydroReactivity() {

        this.getModule().addHydroReactivity();
        return this;
    }

    public default IItemHazard addExplosive(float bang) {

        this.getModule().addExplosive(bang);
        return this;
    }

    public default boolean isRadioactive() {

        return this.getModule().isRadioactive();
    }

    public default Item toItem() {

        return (Item)this;
    }

    public default Block toBlock() {

        return (Block)this;
    }
}
