package de.dafuqs.arrowhead.mixin;

import de.dafuqs.arrowhead.api.*;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.item.*;
import net.minecraft.server.world.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;
import org.joml.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;

@Mixin(BowItem.class)
public class BowItemMixin {
    
    @Inject(method = "shoot(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/projectile/ProjectileEntity;IFFFLnet/minecraft/entity/LivingEntity;)V", at = @At(value = "HEAD"))
    public void arrowhead$handleRangedWeapon(LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, LivingEntity target, CallbackInfo ci) {
        ItemStack activeStack = shooter.getActiveItem();
        
        if (activeStack.getItem() instanceof ArrowheadBow arrowheadBow) {
            projectile.setVelocity(shooter, shooter.getPitch(), shooter.getYaw() + yaw, 0.0F, speed * arrowheadBow.getProjectileVelocityModifier(activeStack), divergence * arrowheadBow.getDivergenceMod(activeStack));
        }
        
        for(BowShootingCallback callback : BowShootingCallback.callbacks) {
            callback.trigger(shooter.getWorld(), shooter, activeStack, activeStack.getMaxUseTime(shooter) - shooter.getItemUseTimeLeft(), projectile);
        }
    }
    
}
