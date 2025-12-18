package de.dafuqs.arrowhead.mixin.client;

import com.llamalad7.mixinextras.sugar.*;
import de.dafuqs.arrowhead.api.*;
import net.minecraft.client.player.*;
import net.minecraft.client.renderer.*;
import net.minecraft.world.*;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {
	
	@Shadow
	private static boolean isChargedCrossbow(ItemStack stack) {
		return false;
	}
	
	@Invoker("selectionUsingItemWhileHoldingBowLike")
	private static ItemInHandRenderer.HandRenderSelection invokeSelectionUsingItemWhileHoldingBowLike(LocalPlayer clientPlayerEntity) {
		throw new AssertionError();
	}
	
	@Inject(method = "evaluateWhichHandsToRender(Lnet/minecraft/client/player/LocalPlayer;)Lnet/minecraft/client/renderer/ItemInHandRenderer$HandRenderSelection;",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"),
			cancellable = true)
	private static void arrowhead$getHandRenderSelection(LocalPlayer player, CallbackInfoReturnable<ItemInHandRenderer.HandRenderSelection> cir, @Local(ordinal = 0) ItemStack itemStack, @Local(ordinal = 1) ItemStack itemStack2) {
		Item item1 = itemStack.getItem();
		Item item2 = itemStack2.getItem();
		boolean bl = item1 instanceof ArrowheadBow || item2 instanceof ArrowheadBow;
		boolean bl2 = item1 instanceof ArrowheadCrossbow || item2 instanceof ArrowheadCrossbow;
		if (!bl && !bl2) {
			// vanilla behavior
		} else if (player.isUsingItem()) {
			cir.setReturnValue(ItemInHandRendererMixin.invokeSelectionUsingItemWhileHoldingBowLike(player));
		} else {
			cir.setReturnValue(isChargedCrossbow(itemStack) ? ItemInHandRenderer.HandRenderSelection.RENDER_MAIN_HAND_ONLY : ItemInHandRenderer.HandRenderSelection.RENDER_BOTH_HANDS);
		}
	}
	
	@Inject(method = "evaluateWhichHandsToRender(Lnet/minecraft/client/player/LocalPlayer;)Lnet/minecraft/client/renderer/ItemInHandRenderer$HandRenderSelection;",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"),
			cancellable = true)
	private static void arrowhead$getUsingItemHandRenderSelection(LocalPlayer player, CallbackInfoReturnable<ItemInHandRenderer.HandRenderSelection> cir) {
		ItemStack itemStack = player.getUseItem();
		InteractionHand hand = player.getUsedItemHand();
		if (itemStack.getItem() instanceof ArrowheadBow || itemStack.getItem() instanceof ArrowheadCrossbow) {
			cir.setReturnValue(ItemInHandRenderer.HandRenderSelection.onlyForHand(hand));
		}
	}
	
	@Inject(method = "isChargedCrossbow(Lnet/minecraft/world/item/ItemStack;)Z", at = @At(value = "HEAD"), cancellable = true)
	private static void arrowhead$isChargedCrossbow(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if(stack.getItem() instanceof ArrowheadCrossbow && CrossbowItem.isCharged(stack)) {
			cir.setReturnValue(true);
		}
	}
	
}
