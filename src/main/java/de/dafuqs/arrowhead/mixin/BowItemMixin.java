package de.dafuqs.arrowhead.mixin;

import de.dafuqs.arrowhead.api.ArrowheadBow;
import de.dafuqs.arrowhead.api.BowShootingCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BowItem.class)
public class BowItemMixin {
	
	@Inject(method = "onStoppedUsing(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;setVelocity(Lnet/minecraft/entity/Entity;FFFFF)V", shift = At.Shift.AFTER),
			locals = LocalCapture.CAPTURE_FAILHARD)
	private void arrowhead$onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci, PlayerEntity playerEntity, boolean bl, ItemStack itemStack, int i, float f, boolean bl2, ArrowItem arrowItem, PersistentProjectileEntity persistentProjectileEntity) {
		if(stack.getItem() instanceof ArrowheadBow arrowheadBow) {
			persistentProjectileEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, f * 3.0F * arrowheadBow.getProjectileVelocityModifier(stack), arrowheadBow.getDivergenceMod(stack));
		}
		
		for(BowShootingCallback callback : BowShootingCallback.callbacks) {
			callback.trigger(world, playerEntity, stack, itemStack, remainingUseTicks, persistentProjectileEntity);
		}
	}
	
}
