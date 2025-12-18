package de.dafuqs.arrowhead.mixin;

import de.dafuqs.arrowhead.api.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {
	
	@ModifyVariable(method = "shootProjectile(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/projectile/Projectile;IFFFLnet/minecraft/world/entity/LivingEntity;)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	public float arrowhead$handleCrossbowSpeed(float originalSpeed, LivingEntity shooter, Projectile projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target) {
		ItemStack activeStack = shooter.getUseItem();
		if (activeStack.getItem() instanceof ArrowheadCrossbow arrowheadCrossbow) {
			originalSpeed *= arrowheadCrossbow.getProjectileVelocityModifier(activeStack);
		}
		return originalSpeed;
	}
	
	@ModifyVariable(method = "shootProjectile(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/projectile/Projectile;IFFFLnet/minecraft/world/entity/LivingEntity;)V", at = @At("HEAD"), ordinal = 1, argsOnly = true)
	public float arrowhead$handleCrossbowDivergence(float originalDivergence, LivingEntity shooter, Projectile projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target) {
		ItemStack activeStack = shooter.getUseItem();
		if (activeStack.getItem() instanceof ArrowheadCrossbow arrowheadCrossbow) {
			originalDivergence *= arrowheadCrossbow.getDivergenceMod(activeStack);
		}
		return originalDivergence;
	}
	
	@Inject(method = "shootProjectile(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/projectile/Projectile;IFFFLnet/minecraft/world/entity/LivingEntity;)V", at = @At(value = "TAIL"))
	public void arrowhead$crossbowCallbacks(LivingEntity shooter, Projectile projectile, int index, float speed, float divergence, float yaw, LivingEntity target, CallbackInfo ci) {
		ItemStack activeStack = shooter.getUseItem();
		for (CrossbowShootingCallback callback : CrossbowShootingCallback.CALLBACKS) {
			callback.trigger(shooter.level(), shooter, activeStack, projectile);
		}
	}
	
}
