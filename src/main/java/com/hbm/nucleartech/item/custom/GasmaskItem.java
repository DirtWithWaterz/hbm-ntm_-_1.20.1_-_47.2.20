package com.hbm.nucleartech.item.custom;

import java.util.*;
import java.util.function.Consumer;

import com.hbm.nucleartech.interfaces.IGasMask;
import com.hbm.nucleartech.item.special.ItemArmorMod;
import com.hbm.nucleartech.render.armor.GasmaskRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.hbm.nucleartech.handler.ArmorModHandler;
import com.hbm.nucleartech.util.ArmorUtil;
import com.hbm.nucleartech.util.I18nUtil;
import com.hbm.nucleartech.util.ArmorRegistry.HazardClass;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

public class GasmaskItem extends ItemArmorMod implements IGasMask, GeoItem {

	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

	private String tex = "hbm:textures/item/armor/m65_mask.png";
	private String tex_mono = "hbm:textures/armor/modelm65mono.png";

	public GasmaskItem(Properties s, ArmorMaterial material) {
		super(s, material, ArmorModHandler.helmet_only, true, false, false, false);
	}

	@Override
	public @Nullable String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		return tex;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> list, TooltipFlag flagIn){
//		if(this == RegisterItems.attachment_mask)
//			list.add(Component.literal(ChatFormatting.GREEN + "Gas protection"));

		list.add(Component.empty());
		super.appendHoverText(stack, worldIn, list, flagIn);

		ArmorUtil.addGasMaskTooltip(stack, worldIn, list, flagIn);

		List<HazardClass> haz = getBlacklist(stack);

		if(!haz.isEmpty()) {
			list.add(Component.literal("§c"+I18nUtil.resolveKey("hazard.never_protects")));

			for(HazardClass clazz : haz) {
				list.add(Component.literal("§4 -" + I18nUtil.resolveKey(clazz.lang)));
			}
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addDesc(List<Component> list, ItemStack stack, ItemStack armor){
		list.add(Component.literal("§a  " + stack.getDisplayName() + " (gas protection)"));
		ArmorUtil.addGasMaskTooltip(stack, null, list, TooltipFlag.NORMAL);
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
	public ArrayList<HazardClass> getBlacklist(ItemStack stack) {

//		if(this == RegisterItems.attachment_mask_mono) {
//			return new ArrayList<HazardClass>(Arrays.asList(new HazardClass[] {HazardClass.GAS_CHLORINE, HazardClass.GAS_CORROSIVE, HazardClass.NERVE_AGENT, HazardClass.BACTERIA}));
//		} else {
			return new ArrayList<HazardClass>(Arrays.asList(new HazardClass[] {HazardClass.GAS_CORROSIVE, HazardClass.NERVE_AGENT}));
//		}
	}

	@Override
	public ItemStack getFilter(ItemStack stack) {
		return ArmorUtil.getGasMaskFilter(stack);
	}

	@Override
	public void installFilter(ItemStack stack, ItemStack filter) {
		ArmorUtil.installGasMaskFilter(stack, filter);
	}

	@Override
	public void damageFilter(ItemStack stack, int damage) {
		ArmorUtil.damageGasMaskFilter(stack, damage);
	}

	@Override
	public boolean isFilterApplicable(ItemStack stack, ItemStack filter) {
		return true;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {

		if(pPlayer.isCrouching()) {

			ItemStack stack = pPlayer.getItemInHand(pUsedHand);
			ItemStack filter = this.getFilter(stack);

			if(filter != null) {

				ArmorUtil.removeFilter(stack);

				if(!pPlayer.getInventory().add(filter)) {

					pPlayer.drop(filter, true, false);
				}
			}
		}

		return super.use(pLevel, pPlayer, pUsedHand);
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {

		consumer.accept(new IClientItemExtensions() {

			private GeoArmorRenderer<?> renderer;

			@Override
			public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {

				if(this.renderer == null)
					this.renderer = new GasmaskRenderer();

				this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);

				return this.renderer;
			}
		});
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

		controllers.add(new AnimationController<>(this, 0, state -> {

			// This is the entity that is currently wearing/holding the item
			Entity entity = state.getData(DataTickets.ENTITY);

			if(!(entity instanceof LivingEntity) || ArmorUtil.getGasMaskFilterRecursively(((LivingEntity)entity).getItemBySlot(EquipmentSlot.HEAD)).isEmpty())
				state.setAnimation(RawAnimation.begin().thenLoop("animation.m65_mask.no_filter"));
			else
				state.setAnimation(RawAnimation.begin().thenLoop("animation.m65_mask.filter"));

			// Play the animation if the full set is being worn, otherwise stop
			return PlayState.CONTINUE;
		}));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}
}
