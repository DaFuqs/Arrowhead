package de.dafuqs.arrowhead.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import de.dafuqs.arrowhead.api.ArrowheadCrossbow;
import de.dafuqs.arrowhead.api.CrossbowShootingCallback;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {
	
	@Inject(method = "Lnet/minecraft/world/item/CrossbowItem;getShootingPower(Lnet/minecraft/world/item/ItemStack;)F", at = @At("RETURN"), cancellable = true)
	private static void getSpeed(ItemStack stack, CallbackInfoReturnable<Float> cir) {
		if(stack.getItem() instanceof ArrowheadCrossbow arrowheadCrossbow) {
			float speedMod = arrowheadCrossbow.getProjectileVelocityModifier(stack);
			if (speedMod != 1.0) {
				cir.setReturnValue((float) Math.ceil(speedMod * (cir.getReturnValue())));
			}
		}
	}
	
	@Inject(method = "Lnet/minecraft/world/item/CrossbowItem;getChargeDuration(Lnet/minecraft/world/item/ItemStack;)I", at = @At("RETURN"), cancellable = true)
	private static void getPullTime(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
		if (stack.getItem() instanceof ArrowheadCrossbow arrowheadCrossbow) {
			cir.setReturnValue((int) Math.ceil(cir.getReturnValueI() * arrowheadCrossbow.getPullTimeModifier(stack)));
		}
	}
	
	@Inject(method = "Lnet/minecraft/world/item/CrossbowItem;shootProjectile(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;FZFFF)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/Projectile;shoot(DDDFF)V", shift = At.Shift.AFTER))
	private static void shoot(Level world, LivingEntity shooter, InteractionHand hand, ItemStack crossbow, ItemStack projectile, float soundPitch, boolean creative, float speed, float divergence, float simulated, CallbackInfo ci, @Local Projectile projectileEntity, @Local Vector3f vec3f) {
		if(crossbow.getItem() instanceof ArrowheadCrossbow arrowheadCrossbow) {
			projectileEntity.shoot(vec3f.x(), vec3f.y(), vec3f.z(), speed * arrowheadCrossbow.getProjectileVelocityModifier(crossbow), divergence * arrowheadCrossbow.getDivergenceMod(crossbow));
		}
		System.out.println("crossbow stopped using");
		for(CrossbowShootingCallback callback : CrossbowShootingCallback.callbacks) {
			callback.trigger(world, shooter, hand, crossbow, projectile, projectileEntity);
		}
	}
	
}
