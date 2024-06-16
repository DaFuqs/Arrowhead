package de.dafuqs.arrowhead.mixin.client;

import de.dafuqs.arrowhead.api.ArrowheadBow;
import de.dafuqs.arrowhead.api.ArrowheadCrossbow;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Environment(EnvType.CLIENT)
@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {
	
	@Shadow protected abstract void applyEquipOffset(MatrixStack matrices, Arm arm, float equipProgress);
	
	@Shadow @Final private MinecraftClient client;
	
	@Shadow protected abstract void applySwingOffset(MatrixStack matrices, Arm arm, float swingProgress);
	
	@Shadow public abstract void renderItem(LivingEntity entity, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light);
	
	@Shadow
	private static boolean isChargedCrossbow(ItemStack stack) {
		return false;
	}
	
	@Invoker("getUsingItemHandRenderType")
	private static HeldItemRenderer.HandRenderType invokeGetUsingItemHandRenderType(ClientPlayerEntity clientPlayerEntity) {
		throw new AssertionError();
	}
	
	@Inject(method = "getHandRenderType(Lnet/minecraft/client/network/ClientPlayerEntity;)Lnet/minecraft/client/render/item/HeldItemRenderer$HandRenderType;",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"),
			cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
	private static void arrowhead$getHandRenderType(ClientPlayerEntity player, CallbackInfoReturnable<HeldItemRenderer.HandRenderType> cir, ItemStack itemStack, ItemStack itemStack2) {
		Item item1 = itemStack.getItem();
		Item item2 = itemStack2.getItem();
		boolean bl = item1 instanceof ArrowheadBow || item2 instanceof ArrowheadBow;
		boolean bl2 = item1 instanceof ArrowheadCrossbow || item2 instanceof ArrowheadCrossbow;
		if (!bl && !bl2) {
			// vanilla behavior
		} else if (player.isUsingItem()) {
			cir.setReturnValue(HeldItemRendererMixin.invokeGetUsingItemHandRenderType(player));
		} else {
			cir.setReturnValue(isChargedCrossbow(itemStack) ? HeldItemRenderer.HandRenderType.RENDER_MAIN_HAND_ONLY : HeldItemRenderer.HandRenderType.RENDER_BOTH_HANDS);
		}
	}
	
	@Inject(method = "getUsingItemHandRenderType(Lnet/minecraft/client/network/ClientPlayerEntity;)Lnet/minecraft/client/render/item/HeldItemRenderer$HandRenderType;",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"),
			cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
	private static void arrowhead$getUsingItemHandRenderType(ClientPlayerEntity player, CallbackInfoReturnable<HeldItemRenderer.HandRenderType> cir, ItemStack activeStack, Hand activeHand) {
		ItemStack itemStack = player.getActiveItem();
		Hand hand = player.getActiveHand();
		if (itemStack.getItem() instanceof ArrowheadBow || itemStack.getItem() instanceof ArrowheadCrossbow) {
			cir.setReturnValue(HeldItemRenderer.HandRenderType.shouldOnlyRender(hand));
		}
	}
	
	@Inject(method = "isChargedCrossbow(Lnet/minecraft/item/ItemStack;)Z",
			at = @At(value = "HEAD"),
			cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
	private static void arrowhead$isChargedCrossbow(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if(stack.getItem() instanceof ArrowheadCrossbow && CrossbowItem.isCharged(stack)) {
			cir.setReturnValue(true);
		}
	}
	
	
	@Inject(method = "renderFirstPersonItem",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z", ordinal = 1),
			cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
	private void arrowhead$renderFirstPersonItem(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci, boolean bl, Arm arm) {
		if (item.getItem() instanceof ArrowheadCrossbow) {
			boolean bl2 = CrossbowItem.isCharged(item);
			boolean bl3 = arm == Arm.RIGHT;
			int i = bl3 ? 1 : -1;
			if (player.isUsingItem() && player.getItemUseTimeLeft() > 0 && player.getActiveHand() == hand) {
				this.applyEquipOffset(matrices, arm, equipProgress);
				matrices.translate(((float)i * -0.4785682F), -0.0943870022892952D, 0.05731530860066414D);
				matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-11.935F));
				matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)i * 65.3F));
				matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float)i * -9.785F));
				float f = (float)item.getMaxUseTime(player) - ((float)this.client.player.getItemUseTimeLeft() - tickDelta + 1.0F);
				float g = f / (float)CrossbowItem.getPullTime(item, player);
				if (g > 1.0F) {
					g = 1.0F;
				}
				
				if (g > 0.1F) {
					float h = MathHelper.sin((f - 0.1F) * 1.3F);
					float j = g - 0.1F;
					float k = h * j;
					matrices.translate((k * 0.0F), (k * 0.004F), (k * 0.0F));
				}
				
				matrices.translate((g * 0.0F), (g * 0.0F), (g * 0.04F));
				matrices.scale(1.0F, 1.0F, 1.0F + g * 0.2F);
				matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees((float)i * 45.0F));
			} else {
				float f = -0.4F * MathHelper.sin(MathHelper.sqrt(swingProgress) * 3.1415927F);
				float g = 0.2F * MathHelper.sin(MathHelper.sqrt(swingProgress) * 6.2831855F);
				float h = -0.2F * MathHelper.sin(swingProgress * 3.1415927F);
				matrices.translate(((float)i * f), g, h);
				this.applyEquipOffset(matrices, arm, equipProgress);
				this.applySwingOffset(matrices, arm, swingProgress);
				if (bl2 && swingProgress < 0.001F && bl) {
					matrices.translate(((float)i * -0.641864F), 0.0D, 0.0D);
					matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)i * 10.0F));
				}
			}
			
			this.renderItem(player, item, bl3 ? ModelTransformationMode.FIRST_PERSON_RIGHT_HAND : ModelTransformationMode.FIRST_PERSON_LEFT_HAND, !bl3, matrices, vertexConsumers, light);
			
			matrices.pop();
			
			ci.cancel();
		}
	}
	
}
