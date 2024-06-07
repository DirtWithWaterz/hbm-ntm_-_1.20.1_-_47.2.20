package com.hbm.nucleartech.util;

import com.hbm.nucleartech.network.HbmLivingProps;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class ContaminationUtil {

    public static enum HazardType {

        MONOXIDE,
        RADIATION,
        NEUTRON,
        DIGAMMA
    }

    public static enum ContaminationType {
        GAS,				//filterable by gas mask
        GAS_NON_REACTIVE,	//not filterable by gas mask
        GOGGLES,			//preventable by goggles
        FARADAY,			//preventable by metal armor
        HAZMAT,				//preventable by hazmat
        HAZMAT2,			//preventable by heavy hazmat
        DIGAMMA,			//preventable by fau armor or stability
        DIGAMMA2,			//preventable by robes
        CREATIVE,			//preventable by creative mode, for rad calculation armor piece bonuses still apply
        RAD_BYPASS,			//same as creaative but fill not apply radiation resistance calculation
        NONE				//not preventable
    }

    public static boolean contaminate(LivingEntity entity, HazardType hazard, ContaminationType cont, double amount) {

        if(entity instanceof Player) {

            Player player = (Player)entity;

            if(player.isCreative() && cont != ContaminationType.NONE) {

//                if(hazard == HazardType.NEUTRON)
                return false;
            }

            if(player.tickCount < 200)
                return false;
        }

        switch (hazard) {
            case RADIATION:
                if(entity instanceof Player)
                    HbmLivingProps.incrementRadiation((Player) entity, amount);
                break;
            default:
                break;
        }

        return true;
    }
}
