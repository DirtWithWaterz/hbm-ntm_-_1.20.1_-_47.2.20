package com.hbm.nucleartech.lib;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Library {

    static Random rand = new Random();

    //this is a list of UUIDs used for various things, primarily for accessories.
    //for a comprehensive list, check RenderAccessoryUtility.java
    public static String HbMinecraft = "192af5d7-ed0f-48d8-bd89-9d41af8524f8";
    public static String TacoRedneck = "5aee1e3d-3767-4987-a222-e7ce1fbdf88e";
    // Earl0fPudding
    public static String LPkukin = "937c9804-e11f-4ad2-a5b1-42e62ac73077";
    public static String Dafnik = "3af1c262-61c0-4b12-a4cb-424cc3a9c8c0";
    // anna20
    public static String a20 = "4729b498-a81c-42fd-8acd-20d6d9f759e0";
    public static String rodolphito = "c3f5e449-6d8c-4fe3-acc9-47ef50e7e7ae";
    public static String LordVertice = "a41df45e-13d8-4677-9398-090d3882b74f";
    // twillycorn
    public static String CodeRed_ = "912ec334-e920-4dd7-8338-4d9b2d42e0a1";
    public static String dxmaster769 = "62c168b2-d11d-4dbf-9168-c6cea3dcb20e";
    public static String Dr_Nostalgia = "e82684a7-30f1-44d2-ab37-41b342be1bbd";
    public static String Samino2 = "87c3960a-4332-46a0-a929-ef2a488d1cda";
    public static String Hoboy03new = "d7f29d9c-5103-4f6f-88e1-2632ff95973f";
    public static String Dragon59MC = "dc23a304-0f84-4e2d-b47d-84c8d3bfbcdb";
    public static String SteelCourage = "ac49720b-4a9a-4459-a26f-bee92160287a";
    public static String Ducxkskiziko = "122fe98f-be19-49ca-a96b-d4dee4f0b22e";

    public static String SweatySwiggs = "5544aa30-b305-4362-b2c1-67349bb499d5";
    public static String Drillgon = "41ebd03f-7a12-42f3-b037-0caa4d6f235b";
    public static String Alcater = "0b399a4a-8545-45a1-be3d-ece70d7d48e9";
    public static String ege444 = "42ee978c-442a-4cd8-95b6-29e469b6df10";
    public static String Doctor17 = "e4ab1199-1c22-4f82-a516-c3238bc2d0d1";
    public static String Doctor17PH = "4d0477d7-58da-41a9-a945-e93df8601c5a";
    public static String ShimmeringBlaze = "061bc566-ec74-4307-9614-ac3a70d2ef38";
    public static String FifeMiner = "37e5eb63-b9a2-4735-9007-1c77d703daa3";
    public static String lag_add = "259785a0-20e9-4c63-9286-ac2f93ff528f";
    public static String Pu_238 = "c95fdfd3-bea7-4255-a44b-d21bc3df95e3";

    public static final Direction POS_X = Direction.EAST;
    public static final Direction NEG_X = Direction.WEST;
    public static final Direction POS_Y = Direction.UP;
    public static final Direction NEG_Y = Direction.DOWN;
    public static final Direction POS_Z = Direction.SOUTH;
    public static final Direction NEG_Z = Direction.NORTH;

    public static final int[] powersOfTen = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000};

    public static DecimalFormat numberFormat = new DecimalFormat("0.00");

    public static boolean checkForHeld(Player player, Item item) {

        return player.getMainHandItem().getItem() == item || player.getOffhandItem().getItem() == item;
    }

    public static float roundFloat(float number, int decimal) {

        return (float) (Math.round(number * powersOfTen[decimal]) / (float)powersOfTen[decimal]);
    }

    public static double roundDouble(double number, int decimal) {

        return (double) (Math.round(number * powersOfTen[decimal]) / (double)powersOfTen[decimal]);
    }

    public static List<ItemStack> copyItemStackList(List<ItemStack> inputs) {
        List<ItemStack> list = new ArrayList<>();
        inputs.forEach(stack -> {list.add(stack.copy());});
        return list;
    }

    /**
     * Returns true if the second compound contains all the tags and values of the first one, but it can have more. This helps with intermod compatibility
     */
    public static boolean tagContainsOther(CompoundTag tester, CompoundTag container){
        if(tester == null && container == null){
            return true;
        } else if (tester == null) {
            return true;
        } else if(container == null) {
            return false;
        } else {
            for(String s : tester.getAllKeys()){
                if(!container.contains(s)){
                    return false;
                } else {
                    Tag nbt1 = tester.get(s);
                    Tag nbt2 = container.get(s);
                    if(nbt1 instanceof CompoundTag && nbt2 instanceof CompoundTag){
                        if(!tagContainsOther((CompoundTag) nbt1, (CompoundTag) nbt2))
                            return false;
                    } else {
                        if(!nbt1.equals(nbt2))
                            return false;
                    }
                }
            }
        }
        return true;
    }
}
