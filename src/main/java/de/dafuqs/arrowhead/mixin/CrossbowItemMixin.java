package de.dafuqs.arrowhead.mixin;

import com.llamalad7.mixinextras.injector.*;
import com.llamalad7.mixinextras.sugar.*;
import de.dafuqs.arrowhead.api.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {

	@ModifyExpressionValue(method = "use(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/TypedActionResult;", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/CrossbowItem;getSpeed(Lnet/minecraft/component/type/ChargedProjectilesComponent;)F"))
	private float getSpeed(float original, @Local ItemStack stack) {
		if(stack.getItem() instanceof ArrowheadCrossbow arrowheadCrossbow) {
			float speedMod = arrowheadCrossbow.getProjectileVelocityModifier(stack);
			if (speedMod != 1.0) {
				return (float) Math.ceil(speedMod * original);
			}
		}
		return original;
	}
	
	@Inject(method = "getPullTime(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;)I", at = @At("RETURN"), cancellable = true)
	private static void getPullTime(ItemStack stack, LivingEntity user, CallbackInfoReturnable<Integer> cir) {
		if (stack.getItem() instanceof ArrowheadCrossbow arrowheadCrossbow) {
			cir.setReturnValue((int) Math.ceil(cir.getReturnValueI() * arrowheadCrossbow.getPullTimeModifier(stack)));
		}
	}
	
}
