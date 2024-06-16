package de.dafuqs.arrowhead.mixin;

import de.dafuqs.arrowhead.api.ArrowheadBow;
import de.dafuqs.arrowhead.api.ArrowheadCrossbow;
import de.dafuqs.arrowhead.api.BowShootingCallback;
import de.dafuqs.arrowhead.api.CrossbowShootingCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(RangedWeaponItem.class)
public class RangedWeaponItemMixin {
    @Inject(method = "shootAll(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/item/ItemStack;Ljava/util/List;FFZLnet/minecraft/entity/LivingEntity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/RangedWeaponItem;shoot(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/projectile/ProjectileEntity;IFFFLnet/minecraft/entity/LivingEntity;)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    public void arrowhead$handleRangedWeapon(ServerWorld world, LivingEntity shooter, Hand hand, ItemStack weaponStack, List<ItemStack> projectiles, float speed, float divergence, boolean critical, @Nullable LivingEntity target, CallbackInfo ci, float f, float g, float h, float i, int index, ItemStack projectileStack, float yaw, ProjectileEntity projectileEntity) {
        Item item = weaponStack.getItem();
        if (item instanceof BowItem) {
            if (item instanceof ArrowheadBow arrowheadBow) {
                projectileEntity.setVelocity(shooter, shooter.getPitch(), shooter.getYaw(), 0.0F, f * 3.0F * arrowheadBow.getProjectileVelocityModifier(weaponStack), arrowheadBow.getDivergenceMod(weaponStack));
            }

            for(BowShootingCallback callback : BowShootingCallback.callbacks) {
                callback.trigger(world, shooter, weaponStack, projectileStack, item.getMaxUseTime(weaponStack, shooter) - shooter.getItemUseTimeLeft(), (PersistentProjectileEntity) projectileEntity);
            }
        } else if (item instanceof CrossbowItem) {
            if (item instanceof ArrowheadCrossbow arrowheadCrossbow) {
                Vector3f origVec = projectileEntity.arrowhead$getLastCrossbowVelocity();
                projectileEntity.setVelocity(origVec.x(), origVec.y(), origVec.z(), speed * arrowheadCrossbow.getProjectileVelocityModifier(weaponStack), divergence * arrowheadCrossbow.getDivergenceMod(weaponStack));
            }

            for(CrossbowShootingCallback callback : CrossbowShootingCallback.callbacks) {
                callback.trigger(world, shooter, hand, weaponStack, projectileStack, projectileEntity);
            }
        }
    }
}
