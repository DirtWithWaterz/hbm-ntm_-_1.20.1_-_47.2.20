package com.hbm.nucleartech.item.custom;

import com.hbm.nucleartech.item.custom.base.GasmaskItem;
import com.hbm.nucleartech.render.armor.M65Renderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Consumer;

public class M65Item extends GasmaskItem {

	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

	public M65Item(Properties s, ArmorMaterial material) {
		super(
				s,
				material,
				"hbm:textures/item/armor/m65_mask.png",
				"hbm:textures/armor/modelm65mono.png",
				"animation.m65_mask.no_filter",
				"animation.m65_mask.filter"
		);
	}

	//	@Override
//	@OnlyIn(Dist.CLIENT)
//	public void modRender(RenderPlayerEvent.Pre event, ItemStack armor){
//
//		if(this.modelM65 == null) {
//			this.modelM65 = new ModelM65();
//		}
//		RenderPlayer renderer = event.getRenderer();
//		ModelBiped model = renderer.getMainModel();
//		EntityPlayer player = event.getEntityPlayer();
//
//		copyRot(modelM65, model);
//
//		float interp = event.getPartialRenderTick();
//		float yawWrapped = MathHelper.wrapDegrees(player.prevRotationYawHead + (player.rotationYawHead - player.prevRotationYawHead) * interp + 180);
//		float pitch = player.rotationPitch;
//
//		if(this == RegisterItems.attachment_mask)
//			Minecraft.getMinecraft().renderEngine.bindTexture(tex);
//		if(this == RegisterItems.attachment_mask_mono)
//			Minecraft.getMinecraft().renderEngine.bindTexture(tex_mono);
//
//		EntityPlayer me = MainRegistry.proxy.me();
//		boolean isMe = player == me;
//		if(!isMe){
//			GL11.glPushMatrix();
//			offset(player, me, interp);
//		}
//		if(model.isSneak) GL11.glTranslatef(0, -0.1875F, 0);
//		modelM65.render(player, 0F, 0F, 0, yawWrapped, pitch, 0.0625F);
//		if(!isMe){
//			GL11.glPopMatrix();
//		}
//	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {

		consumer.accept(new IClientItemExtensions() {

			private GeoArmorRenderer<?> renderer;

			@Override
			public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {

				if(this.renderer == null)
					this.renderer = new M65Renderer();

				this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);

				return this.renderer;
			}
		});
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}
}
