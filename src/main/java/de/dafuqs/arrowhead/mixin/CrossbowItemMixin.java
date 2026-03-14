package de.dafuqs.arrowhead.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import de.dafuqs.arrowhead.api.ArrowheadCrossbow;
import de.dafuqs.arrowhead.api.CrossbowShootingCallback;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {
	
	@ModifyVariable(method = "shootProjectile(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/projectile/Projectile;IFFFLnet/minecraft/world/entity/LivingEntity;)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	public float arrowhead$handleCrossbowSpeed(float originalSpeed, LivingEntity shooter, Projectile projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target) {
		ItemStack activeStack = shooter.getItemInHand(shooter.getUsedItemHand());
		if (activeStack.getItem() instanceof ArrowheadCrossbow arrowheadCrossbow) {
			originalSpeed *= arrowheadCrossbow.getProjectileVelocityModifier(activeStack, shooter);
		}
		return originalSpeed;
	}

	@ModifyReturnValue(method = "getChargeDuration(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)I", at = @At("RETURN"))
	private static int getPullTime(int original, ItemStack stack, LivingEntity shooter) {
		if (stack.getItem() instanceof ArrowheadCrossbow arrowheadCrossbow) {
			return (int) (original * arrowheadCrossbow.getPullTimeModifier(stack, shooter));
		}
		return original;
	}
	
	@ModifyVariable(method = "shootProjectile(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/projectile/Projectile;IFFFLnet/minecraft/world/entity/LivingEntity;)V", at = @At("HEAD"), ordinal = 1, argsOnly = true)
	public float arrowhead$handleCrossbowDivergence(float originalDivergence, LivingEntity shooter, Projectile projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target) {
		ItemStack activeStack = shooter.getItemInHand(shooter.getUsedItemHand());
		if (activeStack.getItem() instanceof ArrowheadCrossbow arrowheadCrossbow) {
			originalDivergence *= arrowheadCrossbow.getDivergenceMod(activeStack, shooter);
		}
		return originalDivergence;
	}
	
	@Inject(method = "shootProjectile(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/projectile/Projectile;IFFFLnet/minecraft/world/entity/LivingEntity;)V", at = @At(value = "TAIL"))
	public void arrowhead$crossbowCallbacks(LivingEntity shooter, Projectile projectile, int index, float speed, float divergence, float yaw, LivingEntity target, CallbackInfo ci) {
		ItemStack activeStack = shooter.getItemInHand(shooter.getUsedItemHand());
		for (CrossbowShootingCallback callback : CrossbowShootingCallback.CALLBACKS) {
			callback.trigger(shooter.level(), shooter, activeStack, projectile);
		}
	}
	
}
