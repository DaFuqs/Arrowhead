package de.dafuqs.arrowhead.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import de.dafuqs.arrowhead.api.*;
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
	
	@Inject(method = "getPullTime(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;)I", at = @At("RETURN"), cancellable = true)
	private static void getPullTime(ItemStack stack, LivingEntity user, CallbackInfoReturnable<Integer> cir) {
		if (stack.getItem() instanceof ArrowheadCrossbow arrowheadCrossbow) {
			cir.setReturnValue((int) Math.ceil(cir.getReturnValueI() * arrowheadCrossbow.getPullTimeModifier(stack)));
		}
	}
	
	@Inject(method = "shoot(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/projectile/ProjectileEntity;IFFFLnet/minecraft/entity/LivingEntity;)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ProjectileEntity;setVelocity(DDDFF)V", shift = At.Shift.AFTER))
	private void shoot(LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, LivingEntity target, CallbackInfo ci, @Local Vector3f vector3f) {
		projectile.arrowhead$setLastCrossbowVelocity(vector3f);
	}
	
	@Inject(method = "shoot(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/projectile/ProjectileEntity;IFFFLnet/minecraft/entity/LivingEntity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ProjectileEntity;setVelocity(DDDFF)V", shift = At.Shift.AFTER))
	public void arrowhead$handleRangedWeapon(LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, LivingEntity target, CallbackInfo ci, @Local Vector3f vector3f) {
		ItemStack activeStack = shooter.getActiveItem();
		
		if (activeStack.getItem() instanceof ArrowheadCrossbow arrowheadCrossbow) {
			projectile.setVelocity(vector3f.x(), vector3f.y(), vector3f.z(), speed * arrowheadCrossbow.getProjectileVelocityModifier(activeStack), divergence * arrowheadCrossbow.getDivergenceMod(activeStack));
		}
		
		for(CrossbowShootingCallback callback : CrossbowShootingCallback.callbacks) {
			callback.trigger(shooter.getWorld(), shooter, activeStack, projectile);
		}
	}
	
}
