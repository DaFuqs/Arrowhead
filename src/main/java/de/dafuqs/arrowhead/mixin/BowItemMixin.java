package de.dafuqs.arrowhead.mixin;

import de.dafuqs.arrowhead.api.ArrowheadBow;
import de.dafuqs.arrowhead.api.BowShootingCallback;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BowItem.class)
public class BowItemMixin {
	
	@Inject(method = "releaseUsing",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;shootFromRotation(Lnet/minecraft/world/entity/Entity;FFFFF)V", shift = At.Shift.AFTER),
			locals = LocalCapture.CAPTURE_FAILHARD)
	private void arrowhead$onStoppedUsing(ItemStack stack, Level world, LivingEntity user, int remainingUseTicks, CallbackInfo ci, Player playerEntity, boolean bl, ItemStack itemStack, int i, float f, boolean bl2, ArrowItem arrowItem, AbstractArrow persistentProjectileEntity) {
		if(stack.getItem() instanceof ArrowheadBow arrowheadBow) {
			persistentProjectileEntity.shootFromRotation(playerEntity, playerEntity.getXRot(), playerEntity.getYRot(), 0.0F, f * 3.0F * arrowheadBow.getProjectileVelocityModifier(stack), arrowheadBow.getDivergenceMod(stack));
		}

		for(BowShootingCallback callback : BowShootingCallback.callbacks) {
			callback.trigger(world, playerEntity, stack, itemStack, remainingUseTicks, persistentProjectileEntity);
		}
	}
	
}
