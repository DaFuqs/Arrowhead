package de.dafuqs.arrowhead.mixin;

import de.dafuqs.arrowhead.api.*;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.item.*;
import org.jetbrains.annotations.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(BowItem.class)
public class BowItemMixin {
	
	@ModifyVariable(method = "shoot(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/projectile/ProjectileEntity;IFFFLnet/minecraft/entity/LivingEntity;)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	public float arrowhead$handleBowSpeed(float originalSpeed, LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target) {
		ItemStack activeStack = shooter.getActiveItem();
		if (activeStack.getItem() instanceof ArrowheadBow arrowheadBow) {
			originalSpeed *= arrowheadBow.getProjectileVelocityModifier(activeStack);
		}
		return originalSpeed;
	}
	
	@ModifyVariable(method = "shoot(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/projectile/ProjectileEntity;IFFFLnet/minecraft/entity/LivingEntity;)V", at = @At("HEAD"), ordinal = 1, argsOnly = true)
	public float arrowhead$handleBowDivergence(float originalDivergence, LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target) {
		ItemStack activeStack = shooter.getActiveItem();
		if (activeStack.getItem() instanceof ArrowheadBow arrowheadBow) {
			originalDivergence *= arrowheadBow.getDivergenceMod(activeStack);
		}
		return originalDivergence;
	}
	
	@Inject(method = "shoot(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/projectile/ProjectileEntity;IFFFLnet/minecraft/entity/LivingEntity;)V", at = @At(value = "TAIL"))
	public void arrowhead$bowCallbacks(LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, LivingEntity target, CallbackInfo ci) {
		ItemStack activeStack = shooter.getActiveItem();
		for (BowShootingCallback callback : BowShootingCallback.callbacks) {
			callback.trigger(shooter.getWorld(), shooter, activeStack, activeStack.getMaxUseTime(shooter) - shooter.getItemUseTimeLeft(), projectile);
		}
	}
	
}