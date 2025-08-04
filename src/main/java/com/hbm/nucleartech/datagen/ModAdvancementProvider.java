package com.hbm.nucleartech.datagen;

import com.hbm.nucleartech.item.RegisterItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModAdvancementProvider extends ForgeAdvancementProvider {

    private static class ModAdvancementGenerator implements AdvancementGenerator {

        @Override
        public void generate(HolderLookup.Provider registries, Consumer<Advancement> saver, ExistingFileHelper existingFileHelper) {

            Advancement root = Advancement.Builder.advancement()
                    .display(
                            Items.BOOK.getDefaultInstance(),
                            Component.nullToEmpty("NTM Rewrote"),
                            Component.nullToEmpty("And so the nuclear adventure starts... again."),
                            new ResourceLocation("hbm:textures/block/concrete_bricks.png"),
                            FrameType.TASK,
                            true, false, false
                    )
                    .addCriterion("impossible", new ImpossibleTrigger.TriggerInstance())
                    .save(saver, "hbm:root");

            Advancement rad_poison = Advancement.Builder.advancement()
                    .parent(root)
                    .display(
                            RegisterItems.URANIUM_BILLET.get().getDefaultInstance(),
                            Component.nullToEmpty("Yay, Radiation!"),
                            Component.nullToEmpty("Suffer the effects of radiation poisoning."),
                            null,
                            FrameType.TASK,
                            true, true, false
                    )
                    .addCriterion("impossible", new ImpossibleTrigger.TriggerInstance())
                    .rewards(new AdvancementRewards.Builder().addExperience(17))
                    .save(saver, "hbm:rad_poison");
        }
    }

    public ModAdvancementProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries, ExistingFileHelper existingFileHelper) {

        super(pOutput, pRegistries, existingFileHelper, List.of(new ModAdvancementGenerator()));
    }

}
