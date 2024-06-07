package com.hbm.nucleartech.network;

import net.minecraft.world.entity.player.Player;

public class HbmLivingProps {

    /// RADIATION ///
    public static double getRadiation(Player entity) {

        return entity.getCapability(HbmModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(
                new HbmModVariables.PlayerVariables()).radiation;
    }

    public static void setRadiation(Player entity, double rad) {

            entity.getCapability(HbmModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.radiation = rad;
                capability.syncPlayerVariables(entity);
            });
    }

    public static void incrementRadiation(Player entity, double rad) {

        double radiation = getRadiation(entity) + rad;

        if(radiation > 25000000)
            radiation = 25000000;
        if(radiation < 0)
            radiation = 0;

        setRadiation(entity, radiation);

        System.out.println("Total Player Rads: " + radiation);
    }
}
