package com.hbm.nucleartech.item.custom;

import com.hbm.nucleartech.handler.ArmorModHandler;
import com.hbm.nucleartech.item.RegisterItems;
import com.hbm.nucleartech.item.special.ItemArmorMod;
import com.hbm.nucleartech.render.armor.HazmatRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Consumer;

public class HazmatItem extends ItemArmorMod implements GeoItem {

	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

	public HazmatItem(Properties s, ArmorMaterial material, ArmorItem.Type type) {
		super(
				s,
				material,
				type,
                type == ArmorModHandler.helmet_only,
				type == ArmorModHandler.plate_only,
				type == ArmorModHandler.legs_only,
				type == ArmorModHandler.boots_only
		);
	}

	@Override
	public @Nullable String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		return "hbm:textures/item/armor/hazmat.png";
	}

	@Override
	public boolean isCompleteSet(List<ArmorItem> set) {

		for(ArmorItem item : set) {

			switch (item.getType()) {

				case HELMET -> {

					if(!item.getDefaultInstance().is(RegisterItems.HAZMAT_HELMET.get()))
						return false;
				}
				case CHESTPLATE -> {

					if(!item.getDefaultInstance().is(RegisterItems.HAZMAT_CHESTPLATE.get()))
						return false;
				}
				case LEGGINGS -> {

					if(!item.getDefaultInstance().is(RegisterItems.HAZMAT_LEGGINGS.get()))
						return false;
				}
				case BOOTS -> {

					if(!item.getDefaultInstance().is(RegisterItems.HAZMAT_BOOTS.get()))
						return false;
				}
			}
		}

		return true;
	}

	@Override
	public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag) {}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {

		consumer.accept(new IClientItemExtensions() {

			private GeoArmorRenderer<?> renderer;

			@Override
			public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {

				if(this.renderer == null)
					this.renderer = new HazmatRenderer();

				this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);

				return this.renderer;
			}
		});
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

		controllers.add(new AnimationController<>(this, 0, state -> {

			// This is the entity that is currently wearing/holding the item
//			Entity entity = state.getData(DataTickets.ENTITY);
//
//			if(!(entity instanceof LivingEntity) || ArmorUtil.getGasMaskFilterRecursively(((LivingEntity)entity).getItemBySlot(EquipmentSlot.HEAD)).isEmpty())
//				state.setAnimation(RawAnimation.begin().thenLoop(no_filter));
//			else
//				state.setAnimation(RawAnimation.begin().thenLoop(filter));

			// Play the animation if the full set is being worn, otherwise stop
			return PlayState.CONTINUE;
		}));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}
}
