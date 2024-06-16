package de.dafuqs.arrowhead.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import de.dafuqs.arrowhead.api.ArrowheadCrossbow;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

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
	
	@Inject(method = "shoot(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/projectile/ProjectileEntity;IFFFLnet/minecraft/entity/LivingEntity;)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ProjectileEntity;setVelocity(DDDFF)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
	private void shoot(LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, LivingEntity target, CallbackInfo ci, Vector3f vector3f) {
		projectile.arrowhead$setLastCrossbowVelocity(vector3f);
	}
	
}
