package com.hbm.nucleartech.handler;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.nucleartech.HBM;
import com.hbm.nucleartech.item.RegisterItems;
import com.hbm.nucleartech.util.ShadyUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Objects;

public class HazmatRegistry {

    public static void initDefault() {

        //assuming coefficient of 10
        //real coefficient turned out to be 5
        //oops

        double helmet = 0.2D;
        double chest = 0.4D;
        double legs = 0.3D;
        double boots = 0.1D;

        double iron = 0.0225D; // 5%
        double gold = 0.0225D; // 5%
        double netherite = 1.0D; // 90%
//        double steel = 0.045D; // 10%
//        double titanium = 0.045D; // 10%
//        double alloy = 0.07D; // 15%
//        double cobalt = 0.125D; // 25%
//
//        double hazYellow = 0.6D; // 50%
//        double hazRed = 1.0D; // 90%
//        double hazGray = 2D; // 99%
//        double paa = 1.7D; // 97%
//        double liquidator = 2.4D; // 99.6%
//
//        double t45 = 1D; // 90%
//        double ajr = 1.3D; // 95%
//        double bj = 1D; // 90%
//        double env = 1.0D; // 99%
//        double hev = 2.3D; // 99.5%
//        double rpa = 2D; // 99%
//        double trench = 1D; // 90%
//        double fau = 4D; // 99.99%
//        double dns = 5D; // 99.999%
//        double security = 0.825D; // 85%
//        double star = 1D; // 90%
//        double cmb = 1.3D; // 95%
//        double schrab = 3D; // 99.9%
//        double euph = 10D; // <100%

//        HazmatRegistry.registerHazmat(RegisterItems.HAZMAT_HELMET, hazYellow * helmet);
//        HazmatRegistry.registerHazmat(RegisterItems.HAZMAT_CHESTPLATE, hazYellow * chest);
//        HazmatRegistry.registerHazmat(RegisterItems.HAZMAT_LEGGINGS, hazYellow * legs);
//        HazmatRegistry.registerHazmat(RegisterItems.HAZMAT_BOOTS, hazYellow * boots);
//
//        HazmatRegistry.registerHazmat(RegisterItems.HAZMAT_HELMET_RED, hazRed * helmet);
//        HazmatRegistry.registerHazmat(RegisterItems.HAZMAT_CHESTPLATE_RED, hazRed * chest);
//        HazmatRegistry.registerHazmat(RegisterItems.HAZMAT_LEGGINGS_RED, hazRed * legs);
//        HazmatRegistry.registerHazmat(RegisterItems.HAZMAT_BOOTS_RED, hazRed * boots);
//
//        HazmatRegistry.registerHazmat(RegisterItems.HAZMAT_HELMET_GREY, hazGray * helmet);
//        HazmatRegistry.registerHazmat(RegisterItems.HAZMAT_CHESTPLATE_GREY, hazGray * chest);
//        HazmatRegistry.registerHazmat(RegisterItems.HAZMAT_LEGGINGS_GREY, hazGray * legs);
//        HazmatRegistry.registerHazmat(RegisterItems.HAZMAT_BOOTS_GREY, hazGray * boots);
//
//        HazmatRegistry.registerHazmat(RegisterItems.LIQUIDATOR_HELMET, liquidator * helmet);
//        HazmatRegistry.registerHazmat(RegisterItems.LIQUIDATOR_CHESTPLATE, liquidator * chest);
//        HazmatRegistry.registerHazmat(RegisterItems.LIQUIDATOR_LEGGINGS, liquidator * legs);
//        HazmatRegistry.registerHazmat(RegisterItems.LIQUIDATOR_BOOTS, liquidator * boots);
//
//        HazmatRegistry.registerHazmat(RegisterItems.T45_HELMET, t45 * helmet);
//        HazmatRegistry.registerHazmat(RegisterItems.T45_CHESTPLATE, t45 * chest);
//        HazmatRegistry.registerHazmat(RegisterItems.T45_LEGGINGS, t45 * legs);
//        HazmatRegistry.registerHazmat(RegisterItems.T45_BOOTS, t45 * boots);
//
//        HazmatRegistry.registerHazmat(RegisterItems.AJR_HELMET, ajr * helmet);
//        HazmatRegistry.registerHazmat(RegisterItems.AJR_CHESTPLATE, ajr * chest);
//        HazmatRegistry.registerHazmat(RegisterItems.AJR_LEGGINGS, ajr * legs);
//        HazmatRegistry.registerHazmat(RegisterItems.AJR_BOOTS, ajr * boots);
//
//        HazmatRegistry.registerHazmat(RegisterItems.AJRO_HELMET, ajr * helmet);
//        HazmatRegistry.registerHazmat(RegisterItems.AJRO_CHESTPLATE, ajr * chest);
//        HazmatRegistry.registerHazmat(RegisterItems.AJRO_LEGGINGS, ajr * legs);
//        HazmatRegistry.registerHazmat(RegisterItems.AJRO_BOOTS, ajr * boots);
//
//        HazmatRegistry.registerHazmat(RegisterItems.BJ_HELMET, bj * helmet);
//        HazmatRegistry.registerHazmat(RegisterItems.BJ_CHESTPLATE, bj * chest);
//        HazmatRegistry.registerHazmat(RegisterItems.BJ_CHESTPLATE_JETPACK, bj * chest);
//        HazmatRegistry.registerHazmat(RegisterItems.BJ_LEGGINGS, bj * legs);
//        HazmatRegistry.registerHazmat(RegisterItems.BJ_BOOTS, bj * boots);
//
//        HazmatRegistry.registerHazmat(RegisterItems.STEAMSUIT_HELMET, 1.3 * helmet);
//        HazmatRegistry.registerHazmat(RegisterItems.STEAMSUIT_CHESTPLATE, 1.3 * chest);
//        HazmatRegistry.registerHazmat(RegisterItems.STEAMSUIT_LEGGINGS, 1.3 * legs);
//        HazmatRegistry.registerHazmat(RegisterItems.STEAMSUIT_BOOTS, 1.3 * boots);
//
//        HazmatRegistry.registerHazmat(RegisterItems.ENVSUIT_HELMET, env * helmet);
//        HazmatRegistry.registerHazmat(RegisterItems.ENVSUIT_CHESTPLATE, env * chest);
//        HazmatRegistry.registerHazmat(RegisterItems.ENVSUIT_LEGGINGS, env * legs);
//        HazmatRegistry.registerHazmat(RegisterItems.ENVSUIT_BOOTS, env * boots);
//
//        HazmatRegistry.registerHazmat(RegisterItems.HEV_HELMET, hev * helmet);
//        HazmatRegistry.registerHazmat(RegisterItems.HEV_CHESTPLATE, hev * chest);
//        HazmatRegistry.registerHazmat(RegisterItems.HEV_LEGGINGS, hev * legs);
//        HazmatRegistry.registerHazmat(RegisterItems.HEV_BOOTS, hev * boots);
//
//        HazmatRegistry.registerHazmat(RegisterItems.RPA_HELMET, rpa * helmet);
//        HazmatRegistry.registerHazmat(RegisterItems.RPA_CHESTPLATE, rpa * chest);
//        HazmatRegistry.registerHazmat(RegisterItems.RPA_LEGGINGS, rpa * legs);
//        HazmatRegistry.registerHazmat(RegisterItems.RPA_BOOTS, rpa * boots);
//
//        HazmatRegistry.registerHazmat(RegisterItems.TRENCHMASTER_HELMET, trench * helmet);
//        HazmatRegistry.registerHazmat(RegisterItems.TRENCHMASTER_CHESTPLATE, trench * chest);
//        HazmatRegistry.registerHazmat(RegisterItems.TRENCHMASTER_LEGGINGS, trench * legs);
//        HazmatRegistry.registerHazmat(RegisterItems.TRENCHMASTER_BOOTS, trench * boots);
//
//        HazmatRegistry.registerHazmat(RegisterItems.FAU_HELMET, fau * helmet);
//        HazmatRegistry.registerHazmat(RegisterItems.FAU_CHESTPLATE, fau * chest);
//        HazmatRegistry.registerHazmat(RegisterItems.FAU_LEGGINGS, fau * legs);
//        HazmatRegistry.registerHazmat(RegisterItems.FAU_BOOTS, fau * boots);
//
//        HazmatRegistry.registerHazmat(RegisterItems.DNS_HELMET, dns * helmet);
//        HazmatRegistry.registerHazmat(RegisterItems.DNS_CHESTPLATE, dns * chest);
//        HazmatRegistry.registerHazmat(RegisterItems.DNS_LEGGINGS, dns * legs);
//        HazmatRegistry.registerHazmat(RegisterItems.DNS_BOOTS, dns * boots);
//
//        HazmatRegistry.registerHazmat(RegisterItems.PAA_CHESTPLATE, paa * chest);
//        HazmatRegistry.registerHazmat(RegisterItems.PAA_LEGGINGS, paa * legs);
//        HazmatRegistry.registerHazmat(RegisterItems.PAA_BOOTS, paa * boots);
//
//        HazmatRegistry.registerHazmat(RegisterItems.HAZMAT_PAA_HELMET, paa * helmet);
//        HazmatRegistry.registerHazmat(RegisterItems.HAZMAT_PAA_CHESTPLATE, paa * chest);
//        HazmatRegistry.registerHazmat(RegisterItems.HAZMAT_PAA_LEGGINGS, paa * legs);
//        HazmatRegistry.registerHazmat(RegisterItems.HAZMAT_PAA_BOOTS, paa * boots);
//
//        HazmatRegistry.registerHazmat(RegisterItems.SECURITY_HELMET, security * helmet);
//        HazmatRegistry.registerHazmat(RegisterItems.SECURITY_CHESTPLATE, security * chest);
//        HazmatRegistry.registerHazmat(RegisterItems.SECURITY_LEGGINGS, security * legs);
//        HazmatRegistry.registerHazmat(RegisterItems.SECURITY_BOOTS, security * boots);
//
//        HazmatRegistry.registerHazmat(RegisterItems.STARMETAL_HELMET, star * helmet);
//        HazmatRegistry.registerHazmat(RegisterItems.STARMETAL_CHESTPLATE, star * chest);
//        HazmatRegistry.registerHazmat(RegisterItems.STARMETAL_LEGGINGS, star * legs);
//        HazmatRegistry.registerHazmat(RegisterItems.STARMETAL_BOOTS, star * boots);
//
//        HazmatRegistry.registerHazmat(RegisterItems.JACKT, 0.1);
//        HazmatRegistry.registerHazmat(RegisterItems.JACKT2, 0.1);
//
//        HazmatRegistry.registerHazmat(RegisterItems.GAS_MASK, 0.07);
//        HazmatRegistry.registerHazmat(RegisterItems.GAS_MASK_M65, 0.095);
//
//        HazmatRegistry.registerHazmat(RegisterItems.STEEL_HELMET, steel * helmet);
//        HazmatRegistry.registerHazmat(RegisterItems.STEEL_CHESTPLATE, steel * chest);
//        HazmatRegistry.registerHazmat(RegisterItems.STEEL_LEGGINGS, steel * legs);
//        HazmatRegistry.registerHazmat(RegisterItems.STEEL_BOOTS, steel * boots);
//
//        HazmatRegistry.registerHazmat(RegisterItems.TITANIUM_HELMET, titanium * helmet);
//        HazmatRegistry.registerHazmat(RegisterItems.TITANIUM_CHESTPLATE, titanium * chest);
//        HazmatRegistry.registerHazmat(RegisterItems.TITANIUM_LEGGINGS, titanium * legs);
//        HazmatRegistry.registerHazmat(RegisterItems.TITANIUM_BOOTS, titanium * boots);
//
//        HazmatRegistry.registerHazmat(RegisterItems.COBALT_HELMET, cobalt * helmet);
//        HazmatRegistry.registerHazmat(RegisterItems.COBALT_CHESTPLATE, cobalt * chest);
//        HazmatRegistry.registerHazmat(RegisterItems.COBALT_LEGGINGS, cobalt * legs);
//        HazmatRegistry.registerHazmat(RegisterItems.COBALT_BOOTS, cobalt * boots);

        HazmatRegistry.registerHazmat(Items.IRON_HELMET, iron * helmet);
        HazmatRegistry.registerHazmat(Items.IRON_CHESTPLATE, iron * chest);
        HazmatRegistry.registerHazmat(Items.IRON_LEGGINGS, iron * legs);
        HazmatRegistry.registerHazmat(Items.IRON_BOOTS, iron * boots);

        HazmatRegistry.registerHazmat(Items.GOLDEN_HELMET, gold * helmet);
        HazmatRegistry.registerHazmat(Items.GOLDEN_CHESTPLATE, gold * chest);
        HazmatRegistry.registerHazmat(Items.GOLDEN_LEGGINGS, gold * legs);
        HazmatRegistry.registerHazmat(Items.GOLDEN_BOOTS, gold * boots);

//        HazmatRegistry.registerHazmat(RegisterItems.ALLOY_HELMET, alloy * helmet);
//        HazmatRegistry.registerHazmat(RegisterItems.ALLOY_CHESTPLATE, alloy * chest);
//        HazmatRegistry.registerHazmat(RegisterItems.ALLOY_LEGGINGS, alloy * legs);
//        HazmatRegistry.registerHazmat(RegisterItems.ALLOY_BOOTS, alloy * boots);
//
//        HazmatRegistry.registerHazmat(RegisterItems.CMB_HELMET, cmb * helmet);
//        HazmatRegistry.registerHazmat(RegisterItems.CMB_CHESTPLATE, cmb * chest);
//        HazmatRegistry.registerHazmat(RegisterItems.CMB_LEGGINGS, cmb * legs);
//        HazmatRegistry.registerHazmat(RegisterItems.CMB_BOOTS, cmb * boots);
//
//        HazmatRegistry.registerHazmat(RegisterItems.SCHRABIDIUM_HELMET, schrab * helmet);
//        HazmatRegistry.registerHazmat(RegisterItems.SCHRABIDIUM_CHESTPLATE, schrab * chest);
//        HazmatRegistry.registerHazmat(RegisterItems.SCHRABIDIUM_LEGGINGS, schrab * legs);
//        HazmatRegistry.registerHazmat(RegisterItems.SCHRABIDIUM_BOOTS, schrab * boots);
//
//        HazmatRegistry.registerHazmat(RegisterItems.EUPHEMIUM_HELMET, euph * helmet);
//        HazmatRegistry.registerHazmat(RegisterItems.EUPHEMIUM_CHESTPLATE, euph * chest);
//        HazmatRegistry.registerHazmat(RegisterItems.EUPHEMIUM_LEGGINGS, euph * legs);
//        HazmatRegistry.registerHazmat(RegisterItems.EUPHEMIUM_BOOTS, euph * boots);

        HazmatRegistry.registerHazmat(Items.NETHERITE_HELMET, netherite * helmet);
        HazmatRegistry.registerHazmat(Items.NETHERITE_CHESTPLATE, netherite * chest);
        HazmatRegistry.registerHazmat(Items.NETHERITE_LEGGINGS, netherite * legs);
        HazmatRegistry.registerHazmat(Items.NETHERITE_BOOTS, netherite * boots);

//        Compat.registerCompatHazmat();
    }

    private static HashMap<Item, Double> entries = new HashMap<>();

    public static void registerHazmat(Item item, double resistance) {

        entries.put(item, resistance);
    }

    public static double getResistance(ItemStack stack) {

        if(stack == null)
            return 0;

        double cladding = getCladding(stack);

        Double f = entries.get(stack.getItem());

        if(f != null)
            return f + cladding;

        return cladding;
    }

    public static double getCladding(ItemStack stack) {

//        if(stack.hasTag()) {
//            assert stack.getTag() != null;
//            if (stack.getTag().getFloat("hfr_cladding") > 0)
//                return stack.getTag().getFloat("hfr_cladding");
//        }

//        if(ArmorModHandler.hasMods(stack)) {
//
//            ItemStack[] mods = ArmorModHandler.pryMods(stack);
//            ItemStack cladding = mods[ArmorModHandler.cladding];
//
//            if(cladding != null && cladding.getItem() instanceof ItemModCladding) {
//                return ((ItemModCladding)cladding.getItem()).rad;
//            }
//        }

        return 0;
    }

    public static float getResistance(Player player) {

        float res = 0.0f;

        if(player.getStringUUID().equals(ShadyUtil.Pu_238))
            res += 0.4f;

        for(int i = 0; i < 4; i++)
            res += (float)getResistance(player.getInventory().getArmor(i));

//        if(player.hasEffect(RegisterEffects.RADX))
//            res += 0.2f;

        return res;
    }

    public static final Gson gson = new Gson();
    public static void registerHazmats() {

        File folder = HBM.configHbmDir;

        File config = new File(folder.getAbsolutePath() + File.separatorChar + "hbmRadResist.json");
        File template = new File(folder.getAbsolutePath() + File.separatorChar + "_hbmRadResist.json");

        initDefault();

        if(!config.exists())
            writeDefault(template);
        else {

            HashMap<Item, Double> conf = readConfig(config);

            if(conf != null) {

                entries.clear();
                entries.putAll(conf);
            }
        }
    }

    private static void writeDefault(File file) {

        try {

            JsonWriter writer = new JsonWriter(new FileWriter(file));
            writer.setIndent("  "); // pretty formatting
            writer.beginObject(); // initial '{'
            writer.name("comment").value("Template file, remove the underscore ('_') from the name to enable the config.");
            writer.name("entries").beginArray(); //all recipes are stored in an array called "entries"

            for(Entry<Item, Double> entry : entries.entrySet()) {

                writer.beginObject(); // begin the object for a single recipe
                writer.name("item").value(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(entry.getKey())).toString());
                writer.name("resistance").value(entry.getValue());
                writer.endObject(); // end recipe object
            }

            writer.endArray(); // end recipe array
            writer.endObject(); // final '}'
            writer.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    private static HashMap<Item, Double> readConfig(File config) {

        try {

            JsonObject json = gson.fromJson(new FileReader(config), JsonObject.class);
            JsonArray array = json.get("entries").getAsJsonArray();
            HashMap<Item, Double> conf = new HashMap<>();

            for(JsonElement element : array) {

                JsonObject object = (JsonObject)element;

                try {

                    String name = object.get("item").getAsString();
                    Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(name));
                    double resistance = object.get("resistance").getAsDouble();

                    if(item != null)
                        conf.put(item, resistance);
                    else
                        HBM.LOGGER.error("Tried loading unknown item " + name + " for hazmat entry.");
                } catch(Exception e) {

                    HBM.LOGGER.error("Encountered " + e + " trying to read hazmat entry " + element.toString());
                }
            }
            return conf;

        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }

        return null;
    }
}
