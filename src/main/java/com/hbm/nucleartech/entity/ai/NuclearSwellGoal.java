package com.hbm.nucleartech.entity.ai;

import com.hbm.nucleartech.entity.custom.NuclearCreeperEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class NuclearSwellGoal extends Goal {
    private final NuclearCreeperEntity nuclearCreeper;
    @Nullable
    private LivingEntity target;

    public NuclearSwellGoal(NuclearCreeperEntity pNuclearCreeper) {
        this.nuclearCreeper = pNuclearCreeper;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        LivingEntity livingEntity = this.nuclearCreeper.getTarget();
        return this.nuclearCreeper.getSwellDir() > 0 || livingEntity != null && this.nuclearCreeper.distanceToSqr(livingEntity) < 9.0D;
    }

    public void start() {
        this.nuclearCreeper.getNavigation().stop();
        this.target = this.nuclearCreeper.getTarget();
    }

    public void stop() {
        this.target = null;
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        if(this.target == null) {
            this.nuclearCreeper.setSwellDir(-1);
        } else if(this.nuclearCreeper.distanceToSqr(this.target) > 49.0D) {
            this.nuclearCreeper.setSwellDir(-1);
        } else if(!this.nuclearCreeper.getSensing().hasLineOfSight(this.target)) {
            this.nuclearCreeper.setSwellDir(-1);
        } else {
            this.nuclearCreeper.setSwellDir(1);
        }
    }
}
