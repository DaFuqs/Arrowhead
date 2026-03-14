package de.dafuqs.arrowhead.mixin;

import de.dafuqs.arrowhead.api.ArrowheadBow;
import de.dafuqs.arrowhead.api.BowShootingCallback;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BowItem.class)
public class BowItemMixin {
	
	@ModifyVariable(method = "shootProjectile(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/projectile/Projectile;IFFFLnet/minecraft/world/entity/LivingEntity;)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	public float arrowhead$handleBowSpeed(float originalSpeed, LivingEntity shooter, Projectile projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target) {
		ItemStack activeStack = shooter.getMainHandItem();
		if (activeStack.getItem() instanceof ArrowheadBow arrowheadBow) {
			originalSpeed *= arrowheadBow.getProjectileVelocityModifier(activeStack, shooter);
		}
		return originalSpeed;
	}
	
	@ModifyVariable(method = "shootProjectile(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/projectile/Projectile;IFFFLnet/minecraft/world/entity/LivingEntity;)V", at = @At("HEAD"), ordinal = 1, argsOnly = true)
	public float arrowhead$handleBowDivergence(float originalDivergence, LivingEntity shooter, Projectile projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target) {
		ItemStack activeStack = shooter.getMainHandItem();
		if (activeStack.getItem() instanceof ArrowheadBow arrowheadBow) {
			originalDivergence *= arrowheadBow.getDivergenceMod(activeStack, shooter);
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