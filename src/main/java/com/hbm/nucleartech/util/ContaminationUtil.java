package com.hbm.nucleartech.util;

import com.hbm.nucleartech.block.custom.RadResistantBlock;
import com.hbm.nucleartech.damagesource.RegisterDamageSources;
import com.hbm.nucleartech.handler.HazmatRegistry;
import com.hbm.nucleartech.handler.RadiationSystemChunksNT;
import com.hbm.nucleartech.hazard.HazardBlock;
import com.hbm.nucleartech.hazard.HazardBlockItem;
import com.hbm.nucleartech.hazard.HazardItem;
import com.hbm.nucleartech.hazard.HazardSystem;
import com.hbm.nucleartech.interfaces.IEntityCapabilityBase.Type;
import com.hbm.nucleartech.capability.HbmCapabilities;
import com.hbm.nucleartech.interfaces.IItemHazard;
import com.hbm.nucleartech.interfaces.IRadResistantBlock;
import com.hbm.nucleartech.lib.Library;
import com.hbm.nucleartech.modules.ItemHazardModule;
import com.hbm.nucleartech.render.amlfrom1710.Vec3;
import com.hbm.nucleartech.saveddata.RadiationSavedData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;

import java.util.ArrayList;
import java.util.List;

import static com.hbm.nucleartech.handler.RadiationSystemChunksNT.getPocket;

@SuppressWarnings("NonAsciiCharacters")
public class ContaminationUtil {

    private static final String NTM_NEUTRON_NBT_KEY = "ntm_neutron";

    public static enum HazardType {

        ASBESTOS,
        BLINDING,
        COAL,
        CONTAMINATING,
        CRYOGENIC,
        DIGAMMA,
        EXPLOSIVE,
        HOT,
        HYDROACTIVE,
        RADIATION,
        TOXIC,
        UNSTABLE,
        NEUTRON
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
        RAD_BYPASS,			//same as creative but will not apply radiation resistance calculation
        NONE				//not preventable
    }

    public static void radiate(ServerLevel level, double x, double y, double z, double range, float rad3d, BlockPos worldPosition) {
        radiate(level, x, y, z, range, rad3d, 0F, 0F, 0F, 0F, worldPosition);
    }
    public static void radiate(ServerLevel level, double x, double y, double z, double range, float rad3d, float dig3d, float fire3d, BlockPos worldPosition) {
        radiate(level, x, y, z, range, rad3d, dig3d, fire3d, 0F, 0F, worldPosition);
    }
    public static void radiate(ServerLevel level, double x, double y, double z, double range, float rad3d, float dig3d, float fire3d, float blast3d, BlockPos worldPosition) {
        radiate(level, x, y, z, range, rad3d, dig3d, fire3d, blast3d, range, worldPosition);
    }
    public static void radiate(ServerLevel pLevel, double x, double y, double z, double range, float rad3d, float dig3d, float fire3d, float blast3d, double blastRange, BlockPos worldPosition) {
        List<Entity> entities = pLevel.getEntities(null, new AABB(x-range, y-range, z-range, x+range, y+range, z+range));

//        System.err.println("radiating");

        List<RadiationSystemChunksNT.RadPocket> pockets = new ArrayList<>();

        for(Direction d : Direction.values()) {

            BlockPos relative = worldPosition.relative(d);
            BlockState state = pLevel.getBlockState(relative);

            RadiationSystemChunksNT.RadPocket decidedPocket = RadiationSystemChunksNT.getPocket(pLevel, relative);

            if(state.isAir() && !pockets.contains(decidedPocket)) {

                RadiationSavedData.incrementRad(pLevel, relative, rad3d, rad3d * 10f);

//                System.out.println("incrementing radiation at " + relative.toString());

                pockets.add(decidedPocket);
            }
        }

        for(Entity e : entities) {

            if(isExplosionExempt(e)) continue;

            Vec3 vec = Vec3.createVectorHelper(e.getX() - x, (e.getY() + e.getEyeHeight()) - y, e.getZ() - z);
            double len = vec.lengthVector();

            if(len > range) continue;
            vec = vec.normalize();
            double dmgLen = Math.max(len, range * 0.05D);

            float res = 0;

            List<Vec2> radResistantBlocks = new ArrayList<>();

            for (int i = 1; i < len; i++) {

                int ix = (int)Math.floor(x + vec.xCoord * i);
                int iy = (int)Math.floor(y + vec.yCoord * i);
                int iz = (int)Math.floor(z + vec.zCoord * i);
                BlockPos stepPos = new BlockPos(ix, iy, iz);
                Block block = pLevel.getBlockState(stepPos).getBlock();

                // Check if it's a radiation-shielding block
                if (block instanceof IRadResistantBlock radBlock) {

//                    System.out.println("[Debug] Found a rad resistant block in-between " + e.getName().getString() + " and " + pLevel.getBlockState(new BlockPos((int)x, (int)y-1, (int)z)).getBlock().getName().getString() + "; Shielding entity from radiation");
                    radResistantBlocks.add(new Vec2(((RadResistantBlock)radBlock).μ, ((RadResistantBlock)radBlock).thickness));
                }
                else
                    res += block.getExplosionResistance();
            }
            boolean isLiving = e instanceof LivingEntity;

            if(res < 1)
                res = 1;

            if(isLiving && rad3d > 0) {

                float eRads = rad3d;
                eRads /= (float)(dmgLen * dmgLen * Math.sqrt(res));

                for(Vec2 vec2 : radResistantBlocks) {

                    double exp = Math.exp(-vec2.x * vec2.y);
                    eRads = (eRads * (float)exp) * 1000f;
//                    System.err.println("[Debug] eRads: " + eRads + ", exponent: " + (float)exp);
                }
//                if(pLevel.getBlockState(worldPosition).getBlock() instanceof HazardBlock && e instanceof Player)
//                    System.err.println("[Hazard Block] contaminating: original=" + rad3d + ", new=" + eRads);
                if(eRads > 0.0001f)
                    contaminate((LivingEntity) e, HazardType.RADIATION, ContaminationType.CREATIVE, eRads);

                BlockPos dPos = e.getOnPos().offset(0,1,0);

                RadiationSystemChunksNT.RadPocket decidedPocket = RadiationSystemChunksNT.getPocket(pLevel, dPos);

                if(!pockets.contains(decidedPocket)) {

                    RadiationSavedData.incrementRad(pLevel, dPos, eRads, eRads * 10f);
                    pockets.add(decidedPocket);
                }
//                RadiationSavedData.decrementRad(pLevel, e.getOnPos().offset(0,1,0), eRads);
//                else
//                    System.err.println("[Debug] Radiation being applied is too close to zero: " + eRads);
            }
            if(isLiving && dig3d > 0) {

                float eDig = dig3d;
                eDig /= (float)(dmgLen * dmgLen * dmgLen);

                contaminate((LivingEntity) e, HazardType.DIGAMMA, ContaminationType.DIGAMMA, eDig);
            }


            if(fire3d > 0.025F) {
                float fireDmg = fire3d;
                fireDmg /= (float)(dmgLen * dmgLen * res * res);
                if(fireDmg > 0.025F){
                    if(fireDmg > 0.1F && e instanceof Player p) {

                        // check if holding a marshmallow and cook it if the player is.
                    }
                    if(radResistantBlocks.isEmpty()) {

                        e.hurt(pLevel.damageSources().inFire(), fireDmg);
                        e.setSecondsOnFire(5);
                    }
                }
            }

            if(len < blastRange && blast3d > 0.025F) {
                float blastDmg = blast3d;
                blastDmg /= (float)(dmgLen * dmgLen * res);
                if(blastDmg > 0.025F){
                    if(rad3d > 0)
                        e.hurt(RegisterDamageSources.NUCLEAR_BLAST, blastDmg);
                    else
                        e.hurt(RegisterDamageSources.BLAST, blastDmg);
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

//        return e instanceof Player && (((Player) e).isCreative() || ((Player) e).isSpectator());
        return false;
    }

    public static void setEntityENV(LivingEntity entity, Level level) {

        double env = 0;

        if(entity instanceof ServerPlayer player) {

            for(ItemStack stack : player.getInventory().items) {

                if(stack.getItem() instanceof IItemHazard hazardItem) {

                    if(hazardItem.getModule().isRadioactive())
                        env = env + hazardItem.getModule().radiation * (double)stack.getCount();
                }
            }
        }
        RadiationSystemChunksNT.RadPocket ePoc = getPocket(level, entity.getOnPos().offset(0, 1, 0));

        env = env + (double)ePoc.radiation;

        HbmCapabilities.getData(entity).setValue(Type.RADENV, (float)env);
        HbmCapabilities.getData(entity).syncLivingVariables(entity);
    }

    public static boolean contaminate(LivingEntity entity, HazardType hazard, ContaminationType cont, float amount) {

//        System.out.println("is " + entity.getName().getString() + " a player?");
        if(entity instanceof Player player) {

//            System.out.println("yes. Are they creative?");
            if((player.isCreative() || player.isSpectator()) && cont != ContaminationType.NONE) {

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

        if((hazard == HazardType.RADIATION || hazard == HazardType.NEUTRON) && isRadImmune(entity)){
            return false;
        }

//        System.out.println("what type of hazard is it?");
        switch (hazard) {
            case RADIATION:

                if(entity instanceof Player player) {

//                System.err.println("[Debug] Radiation before: " + amount + ", resistance: " + (float)((((int)(10000D - ContaminationUtil.calculateRadiationMod(player) * 10000D)) / 100D)) + "%, multiplier: " + (1-(float)((((int)(10000D - ContaminationUtil.calculateRadiationMod(player) * 10000D)) / 100D))/100f));

                amount = amount * (1-(float)((((int)(10000D - ContaminationUtil.calculateRadiationMod(player) * 10000D)) / 100D))/100f);

//                System.err.println("[Debug] Radiation after: " + amount);
                }

//                System.out.println("radiation. adding " + amount + " rads to " + entity.getName().getString());
                HbmCapabilities.getData(entity).addValue(Type.RADIATION, amount);
                if(entity instanceof Player)
                    HbmCapabilities.getData(entity).syncLivingVariables(entity);
                return true;

            case NEUTRON:

                if(entity instanceof Player player) {

//                    System.err.println("[Debug] Neutron before: " + amount + ", resistance: " + (float)((((int)(10000D - ContaminationUtil.calculateRadiationMod(player) * 10000D)) / 100D)) + "%, multiplier: " + (1-(float)((((int)(10000D - ContaminationUtil.calculateRadiationMod(player) * 10000D)) / 100D))/100f));

                    amount = amount * (1-(float)((((int)(10000D - ContaminationUtil.calculateRadiationMod(player) * 10000D)) / 100D))/100f);

//                    System.err.println("[Debug] Neutron after: " + amount);
                }

                HbmCapabilities.getData(entity).addValue(Type.RADIATION, amount);
//                HbmCapabilities.getData(entity).setValue(Type.NEUTRON, amount);
                if(entity instanceof Player)
                    HbmCapabilities.getData(entity).syncLivingVariables(entity);
                return true;

            default:
//                System.out.println("there is no code accosted with the provided hazard type. Skipping call...");
                return false;
        }
    }

    public static boolean isRadImmune(Entity e) {
//        if(e instanceof LivingEntity && ((LivingEntity)e).hasEffect(RegisterPotions.mutation))
//            return true;

        return 	e instanceof Zombie ||
                e instanceof Skeleton /*||
                e instanceof Quackos*/ ||
                e instanceof Ocelot ||
                e instanceof MushroomCow ||
                e instanceof ZombieHorse ||
                e instanceof SkeletonHorse ||
                e instanceof ArmorStand /*||
                e instanceof IRadiationImmune || checkConfigEntityImmunity(e)*/;
    }

    public static void printGeigerData(Player player) {

        Level level = player.level();

        double playerContamination = (double)((HbmCapabilities.getData(player).getValue(Type.RADIATION) * 10)) / 10D;

        double chunkRads = (double)(RadiationSystemChunksNT.getRadForCoord(level, player.getOnPos().offset(0,1,0)) * 10) / 10D;
        double environmentalRads = (double)(HbmCapabilities.getData(player).getValue(Type.RADENV) * 10) / 10D;

        double neutronRads = (double)(HbmCapabilities.getData(player).getValue(Type.NEUTRON) * 10) / 10D;

        double res = ((int)(10000D - ContaminationUtil.calculateRadiationMod(player) * 10000D)) / 100D;
        double resKoeff = ((int)(HazmatRegistry.getResistance(player) * 100D)) / 100D;

        String chunkPrefix = getPrefixFromRad(chunkRads);
        String envPrefix = getPrefixFromRad(environmentalRads);
        String radPrefix = "";
        String resPrefix = "" + ChatFormatting.WHITE;

        if(playerContamination < 200)
            radPrefix += ChatFormatting.GREEN;
        else if(playerContamination < 400)
            radPrefix += ChatFormatting.YELLOW;
        else if(playerContamination < 600)
            radPrefix += ChatFormatting.GOLD;
        else if(playerContamination < 800)
            radPrefix += ChatFormatting.RED;
        else if(playerContamination < 1000)
            radPrefix += ChatFormatting.DARK_RED;
        else
            radPrefix += ChatFormatting.DARK_GRAY;

        if(resKoeff > 0)
            resPrefix += ChatFormatting.GREEN;

        player.sendSystemMessage(Component.literal("===== ☢ ").append(Component.translatable("geiger.title")).append(Component.literal(" ☢ =====")).withStyle(ChatFormatting.GOLD));
        player.sendSystemMessage(Component.translatable("geiger.chunk_rad").append(Component.literal(" " + chunkPrefix + String.format("%.1f", chunkRads) + " RAD/s")).withStyle(ChatFormatting.YELLOW));
        player.sendSystemMessage(Component.translatable("geiger.env_rad").append(Component.literal(" " + envPrefix + String.format("%.1f", environmentalRads + neutronRads) + " RAD/s")).withStyle(ChatFormatting.YELLOW));
        player.sendSystemMessage(Component.translatable("geiger.player_rad").append(Component.literal(" " + radPrefix + String.format("%.1f", playerContamination) + " RAD")).withStyle(ChatFormatting.YELLOW));
        player.sendSystemMessage(Component.translatable("geiger.player_res").append(Component.literal(" " + resPrefix + res + "% (" + resKoeff + ")")).withStyle(ChatFormatting.YELLOW));
    }

    public static float calculateRadiationMod(LivingEntity entity) {

        if(entity instanceof Player player) {

            float koeff = 10.0f;
            return (float)Math.pow(koeff, -HazmatRegistry.getResistance(player));
        }

        return 1;
    }

    public static double getNoNeutronPlayerRads(LivingEntity entity) {
        return (double)(HbmCapabilities.getData(entity).getValue(Type.RADENV)) * (double)(ContaminationUtil.calculateRadiationMod(entity));
    }

    public static float getPlayerNeutronRads(Player player){
        float radBuffer = 0F;
        for(ItemStack slotI : player.getInventory().items){
            radBuffer = radBuffer + getNeutronRads(slotI);
        }
        for(ItemStack slotA : player.getInventory().armor){
            radBuffer = radBuffer + getNeutronRads(slotA);
        }
        HbmCapabilities.getData(player).setValue(Type.NEUTRON, radBuffer);
        HbmCapabilities.getData(player).syncLivingVariables(player);
        return radBuffer;
    }

    public static boolean isRadItem(ItemStack stack){
        if(stack == null)
            return false;

        if(HazardSystem.getRawRadsFromStack(stack) > 0){
            return true;
        }

        return false;
    }

    public static float getNeutronRads(ItemStack stack){
        if(stack != null && !stack.isEmpty() && !isRadItem(stack)){
            if(stack.hasTag()){
                CompoundTag nbt = stack.getTag();
                if(nbt.contains(NTM_NEUTRON_NBT_KEY)){
                    return nbt.getFloat(NTM_NEUTRON_NBT_KEY) * stack.getCount();
                }
            }
        }
        return 0F;
    }

    public static void addNeutronRadInfo(ItemStack stack, Player player, List<Component> list, TooltipFlag flagIn){
        float activationRads = getNeutronRads(stack);
        if(activationRads > 0) {
            list.add(Component.literal("§a[" + I18nUtil.resolveKey("trait.radioactive") + "]"));
            float stackRad = activationRads / stack.getCount();
            list.add(Component.literal(" §e" + Library.roundFloat((float)ItemHazardModule.getNewValue(stackRad), 3) + ItemHazardModule.getSuffix(stackRad) + " RAD/s"));

            if(stack.getCount() > 1) {
                list.add(Component.literal(" §eStack: " + Library.roundFloat((float)ItemHazardModule.getNewValue(activationRads), 3) + ItemHazardModule.getSuffix(activationRads) + " RAD/s"));
            }
        }
    }

    public static void neutronActivateInventory(Player player, float rad, float decay){
        for(ItemStack slotI : player.getInventory().items){

//            System.out.println("[Debug] Attempting to activate " + slotI.getDescriptionId());

            if(slotI != player.getInventory().getSelected() &&
                    !(slotI.getItem() instanceof HazardItem) &&
                    !(slotI.getItem() instanceof HazardBlockItem))
                neutronActivateItem(slotI, rad, decay);
        }
        for(ItemStack slotA : player.getInventory().armor){

//            System.out.println("[Debug] Attempting to activate " + slotA.getDescriptionId());

            neutronActivateItem(slotA, rad, decay);
        }
    }

    public static void neutronActivateItem(ItemStack stack, float rad, float decay){
        if(stack != null && !stack.isEmpty() /*&& stack.getCount() == 1*/ && !isRadItem(stack)){

//            System.out.println("[Debug] Adding NBT...");

            CompoundTag nbt;
            if(stack.hasTag()){
                nbt = stack.getTag();
            } else{
                nbt = new CompoundTag();
            }
            float prevActivation = 0;
            if(nbt.contains(NTM_NEUTRON_NBT_KEY)){
                prevActivation = nbt.getFloat(NTM_NEUTRON_NBT_KEY);
            }

            if(prevActivation + rad == 0)
                return;

            float newActivation = prevActivation * decay + (rad / stack.getCount());
            if(prevActivation * decay + rad < 0.0001F || (rad <= 0 && newActivation < 0.001F )){
                nbt.remove(NTM_NEUTRON_NBT_KEY);
            } else {
                nbt.putFloat(NTM_NEUTRON_NBT_KEY, newActivation);
            }
            if(nbt.isEmpty()){
                stack.setTag(null);
            } else {
                stack.setTag(nbt);
            }

//            System.out.println("[Debug] NBT data is: " + nbt.getFloat(NTM_NEUTRON_NBT_KEY));
        }
    }

    public static String getPrefixFromRad(double rads) {

        String chunkPrefix = "";

        if(rads == 0)
            chunkPrefix += ChatFormatting.GREEN;
        else if(rads < 1)
            chunkPrefix += ChatFormatting.YELLOW;
        else if(rads < 10)
            chunkPrefix += ChatFormatting.GOLD;
        else if(rads < 100)
            chunkPrefix += ChatFormatting.RED;
        else if(rads < 1000)
            chunkPrefix += ChatFormatting.DARK_RED;
        else
            chunkPrefix += ChatFormatting.DARK_GRAY;

        return chunkPrefix;
    }
}
