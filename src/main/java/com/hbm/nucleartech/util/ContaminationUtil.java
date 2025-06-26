package com.hbm.nucleartech.util;

import com.hbm.nucleartech.damagesource.RegisterDamageSources;
import com.hbm.nucleartech.interfaces.IEntityCapabilityBase.Type;
import com.hbm.nucleartech.capability.HbmCapabilities;
import com.hbm.nucleartech.interfaces.IRadResistantBlock;
import com.hbm.nucleartech.render.amlfrom1710.Vec3;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;

import java.util.List;

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

    public static void radiate(Level level, double x, double y, double z, double range, float rad3d) {
        radiate(level, x, y, z, range, rad3d, 0F, 0F, 0F, 0F);
    }
    public static void radiate(Level level, double x, double y, double z, double range, float rad3d, float dig3d, float fire3d) {
        radiate(level, x, y, z, range, rad3d, dig3d, fire3d, 0F, 0F);
    }
    public static void radiate(Level level, double x, double y, double z, double range, float rad3d, float dig3d, float fire3d, float blast3d) {
        radiate(level, x, y, z, range, rad3d, dig3d, fire3d, blast3d, range);
    }
    public static void radiate(Level pLevel, double x, double y, double z, double range, float rad3d, float dig3d, float fire3d, float blast3d, double blastRange) {
        List<Entity> entities = pLevel.getEntities(null, new AABB(x-range, y-range, z-range, x+range, y+range, z+range));

        for(Entity e : entities) {

            if(isExplosionExempt(e)) continue;

            Vec3 vec = Vec3.createVectorHelper(e.getX() - x, (e.getY() + e.getEyeHeight()) - y, e.getZ() - z);
            double len = vec.lengthVector();

            if(len > range) continue;
            vec = vec.normalize();
            double dmgLen = Math.max(len, range * 0.05D);

            float res = 0;

            boolean blockedByRadShield = false;

            for (int i = 1; i < len; i++) {

                int ix = (int)Math.floor(x + vec.xCoord * i);
                int iy = (int)Math.floor(y + vec.yCoord * i);
                int iz = (int)Math.floor(z + vec.zCoord * i);
                BlockPos stepPos = new BlockPos(ix, iy, iz);
                Block block = pLevel.getBlockState(stepPos).getBlock();

                // Check if it's a radiation-shielding block
                if (block instanceof IRadResistantBlock radBlock && radBlock.isRadResistant(pLevel, stepPos)) {

//                    System.out.println("[Debug] Found a rad resistant block in-between " + e.getName().getString() + " and " + pLevel.getBlockState(new BlockPos((int)x, (int)y, (int)z)).getBlock().getName().getString() + "; Shielding entity from radiation");
                    blockedByRadShield = true;
                    break; // Radiation blocked, no need to continue
                }

                res += block.getExplosionResistance();
            }
            boolean isLiving = e instanceof LivingEntity;

            if(res < 1)
                res = 1;
            if(!blockedByRadShield) {

                if(isLiving && rad3d > 0) {

                    float eRads = rad3d;
                    eRads /= (float)(dmgLen * dmgLen * Math.sqrt(res));

                    contaminate((LivingEntity) e, HazardType.RADIATION, ContaminationType.CREATIVE, eRads);
                }
                if(isLiving && dig3d > 0) {

                    float eDig = dig3d;
                    eDig /= (float)(dmgLen * dmgLen * dmgLen);

                    contaminate((LivingEntity) e, HazardType.DIGAMMA, ContaminationType.DIGAMMA, eDig);
                }
            }

            if(fire3d > 0.025F) {
                float fireDmg = fire3d;
                fireDmg /= (float)(dmgLen * dmgLen * res * res);
                if(fireDmg > 0.025F){
                    if(fireDmg > 0.1F && e instanceof Player p) {

                        // check if holding a marshmallow and cook it if the player is.
                    }
                    e.hurt(pLevel.damageSources().inFire(), fireDmg);
                    e.setSecondsOnFire(5);
                }
            }

            if(len < blastRange && blast3d > 0.025F) {
                float blastDmg = blast3d;
                blastDmg /= (float)(dmgLen * dmgLen * res);
                if(blastDmg > 0.025F){
                    if(rad3d > 0)
                        e.hurt(RegisterDamageSources.NUCLEAR_BLAST_DAMAGE, blastDmg);
                    else
                        e.hurt(RegisterDamageSources.BLAST_DAMAGE, blastDmg);
                }
                net.minecraft.world.phys.Vec3 cVel = e.getDeltaMovement();
                e.setDeltaMovement(
                        cVel.x + vec.xCoord * 0.005D * blastDmg,
                        cVel.y + vec.yCoord * 0.005D * blastDmg,
                        cVel.z + vec.zCoord * 0.005D * blastDmg
                );
            }
        }
    }

    private static boolean isExplosionExempt(Entity e) {

        if(e instanceof Ocelot) {
//              e instanceof EntityNukeTorex ||
//				e instanceof EntityNukeExplosionMK5 ||
//				e instanceof EntityMIRV ||
//				e instanceof EntityMiniNuke ||
//				e instanceof EntityMiniMIRV ||
//				e instanceof EntityGrenadeASchrab ||
//				e instanceof EntityGrenadeNuclear ||
//				e instanceof EntityExplosiveBeam ||
//				e instanceof EntityBulletBase ||
//				(e instanceof EntityPlayer &&
//				ArmorUtil.checkArmor((EntityPlayer) e, ModItems.euphemium_helmet, ModItems.euphemium_plate, ModItems.euphemium_legs, ModItems.euphemium_boots))) {
            return true;
        }

        return e instanceof Player && (((Player) e).isCreative() || ((Player) e).isSpectator());
    }



    public static boolean contaminate(LivingEntity entity, HazardType hazard, ContaminationType cont, float amount) {

//        System.out.println("checking for hazard type == radation?");
        if(hazard == HazardType.RADIATION) {
//            System.out.println("true. adding " + amount + " to " + entity.getName().getString() + "'s Rad Env.");
            HbmCapabilities.getData(entity).addValue(Type.RADENV, amount);
            if(entity instanceof Player)
                HbmCapabilities.getData(entity).syncPlayerVariables(entity);
        }

//        System.out.println("is " + entity.getName().getString() + " a player?");
        if(entity instanceof Player player) {

//            System.out.println("yes. Are they creative?");
            if(player.isCreative() && cont != ContaminationType.NONE) {

//                if(hazard == HazardType.NEUTRON)
//                System.err.println("player is creative! Skipping contamination call...");
                return false;
            }

//            System.out.println("no. Is their tick count too high?");
//            if(player.tickCount > 200){
//
//                System.err.println("player tick count is too high! (" + player.tickCount + ") skipping contamination call...");
//                return false;
//            }
//            System.out.println("no. Continuing to hazard application...");
        }

//        System.out.println("what type of hazard is it?");
        switch (hazard) {
            case RADIATION:

//                System.out.println("radiation. adding " + amount + " rads to " + entity.getName().getString());
                HbmCapabilities.getData(entity).addValue(Type.RADIATION, amount);
                if(entity instanceof Player)
                    HbmCapabilities.getData(entity).syncPlayerVariables(entity);
                return true;

            default:
                System.out.println("there is no code accosted with the provided hazard type. Skipping call...");
                return false;
        }
    }
}
