package de.dafuqs.arrowhead.mixin;

import de.dafuqs.arrowhead.api.*;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.item.*;
import org.jetbrains.annotations.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {
	
	@ModifyVariable(method = "shoot(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/projectile/ProjectileEntity;IFFFLnet/minecraft/entity/LivingEntity;)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	public float arrowhead$handleCrossbowSpeed(float originalSpeed, LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target) {
		ItemStack activeStack = shooter.getStackInHand(shooter.getActiveHand());
		if (activeStack.getItem() instanceof ArrowheadCrossbow arrowheadCrossbow) {
			originalSpeed *= arrowheadCrossbow.getProjectileVelocityModifier(activeStack);
		}
		return originalSpeed;
	}
	
	@ModifyVariable(method = "shoot(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/projectile/ProjectileEntity;IFFFLnet/minecraft/entity/LivingEntity;)V", at = @At("HEAD"), ordinal = 1, argsOnly = true)
	public float arrowhead$handleCrossbowDivergence(float originalDivergence, LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target) {
		ItemStack activeStack = shooter.getStackInHand(shooter.getActiveHand());
		if (activeStack.getItem() instanceof ArrowheadCrossbow arrowheadCrossbow) {
			originalDivergence *= arrowheadCrossbow.getDivergenceMod(activeStack);
		}
		return originalDivergence;
	}
	
	@Inject(method = "shoot(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/projectile/ProjectileEntity;IFFFLnet/minecraft/entity/LivingEntity;)V", at = @At(value = "TAIL"))
	public void arrowhead$crossbowCallbacks(LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, LivingEntity target, CallbackInfo ci) {
		ItemStack activeStack = shooter.getStackInHand(shooter.getActiveHand());
		for (CrossbowShootingCallback callback : CrossbowShootingCallback.callbacks) {
			callback.trigger(shooter.getWorld(), shooter, activeStack, projectile);
		}
	}
	
}
