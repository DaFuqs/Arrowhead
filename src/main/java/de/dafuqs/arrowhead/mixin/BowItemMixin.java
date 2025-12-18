package de.dafuqs.arrowhead.mixin;

import de.dafuqs.arrowhead.api.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(BowItem.class)
public class BowItemMixin {
	
	@ModifyVariable(method = "shootProjectile(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/projectile/Projectile;IFFFLnet/minecraft/world/entity/LivingEntity;)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	public float arrowhead$handleBowSpeed(float originalSpeed, LivingEntity shooter, Projectile projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target) {
		ItemStack activeStack = shooter.getMainHandItem();
		if (activeStack.getItem() instanceof ArrowheadBow arrowheadBow) {
			originalSpeed *= arrowheadBow.getProjectileVelocityModifier(activeStack);
		}
		return originalSpeed;
	}
	
	@ModifyVariable(method = "shootProjectile(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/projectile/Projectile;IFFFLnet/minecraft/world/entity/LivingEntity;)V", at = @At("HEAD"), ordinal = 1, argsOnly = true)
	public float arrowhead$handleBowDivergence(float originalDivergence, LivingEntity shooter, Projectile projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target) {
		ItemStack activeStack = shooter.getMainHandItem();
		if (activeStack.getItem() instanceof ArrowheadBow arrowheadBow) {
			originalDivergence *= arrowheadBow.getDivergenceMod(activeStack);
		}
		return originalDivergence;
	}
	
	@Inject(method = "shootProjectile(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/projectile/Projectile;IFFFLnet/minecraft/world/entity/LivingEntity;)V", at = @At(value = "TAIL"))
	public void arrowhead$bowCallbacks(LivingEntity shooter, Projectile projectile, int index, float speed, float divergence, float yaw, LivingEntity target, CallbackInfo ci) {
		ItemStack activeStack = shooter.getMainHandItem();
		for (BowShootingCallback callback : BowShootingCallback.CALLBACKS) {
			callback.trigger(shooter.level(), shooter, activeStack, projectile);
		}
	}
	
}