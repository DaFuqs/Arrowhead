package de.dafuqs.arrowhead.mixin.client;

import com.llamalad7.mixinextras.injector.*;
import com.llamalad7.mixinextras.sugar.*;
import de.dafuqs.arrowhead.api.*;
import net.fabricmc.api.*;
import net.minecraft.client.network.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.item.*;
import net.minecraft.client.util.math.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Environment(EnvType.CLIENT)
@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {
	
	@Shadow
	private static boolean isChargedCrossbow(ItemStack stack) {
		return false;
	}
	
	@Invoker("getUsingItemHandRenderType")
	private static HeldItemRenderer.HandRenderType invokeGetUsingItemHandRenderType(ClientPlayerEntity clientPlayerEntity) {
		throw new AssertionError();
	}
	
	@Inject(method = "getHandRenderType(Lnet/minecraft/client/network/ClientPlayerEntity;)Lnet/minecraft/client/render/item/HeldItemRenderer$HandRenderType;", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"), cancellable = true)
	private static void arrowhead$getHandRenderType(ClientPlayerEntity player, CallbackInfoReturnable<HeldItemRenderer.HandRenderType> cir, @Local(ordinal = 0) ItemStack itemStack, @Local(ordinal = 1) ItemStack itemStack2) {
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
	
	@Inject(method = "getUsingItemHandRenderType(Lnet/minecraft/client/network/ClientPlayerEntity;)Lnet/minecraft/client/render/item/HeldItemRenderer$HandRenderType;", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"), cancellable = true)
	private static void arrowhead$getUsingItemHandRenderType(ClientPlayerEntity player, CallbackInfoReturnable<HeldItemRenderer.HandRenderType> cir) {
		ItemStack itemStack = player.getActiveItem();
		Hand hand = player.getActiveHand();
		if (itemStack.getItem() instanceof ArrowheadBow || itemStack.getItem() instanceof ArrowheadCrossbow) {
			cir.setReturnValue(HeldItemRenderer.HandRenderType.shouldOnlyRender(hand));
		}
	}
	
	@Inject(method = "isChargedCrossbow(Lnet/minecraft/item/ItemStack;)Z", at = @At(value = "HEAD"), cancellable = true)
	private static void arrowhead$isChargedCrossbow(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if(stack.getItem() instanceof ArrowheadCrossbow && CrossbowItem.isCharged(stack)) {
			cir.setReturnValue(true);
		}
	}
	
	@ModifyExpressionValue(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z", ordinal = 0))
	private boolean arrowhead$renderFirstPersonItem(boolean original, AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		return original || item.getItem() instanceof ArrowheadCrossbow;
	}
	
}
