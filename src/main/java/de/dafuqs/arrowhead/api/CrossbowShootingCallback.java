package de.dafuqs.arrowhead.api;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface CrossbowShootingCallback {
	
	List<CrossbowShootingCallback> callbacks = new ArrayList<>();
	
	/**
	 * Fires after the projectile has gotten its initial velocity set and before vanilla enchantments are run
	 * Only triggers serverside
	 * @param world the world
	 * @param shooter the LivingEntity that shot the crossbow
	 * @param hand the hand that was used for shooting
	 * @param crossbow the crossbow stack
	 * @param projectile the projectile stack that was used for shooting
	 * @param projectileEntity the projectile that was shot (initialized, but not yet spawned in the world)
	 */
	void trigger(Level world, LivingEntity shooter, InteractionHand hand, ItemStack crossbow, ItemStack projectile, Projectile projectileEntity);
	
	/**
	 * Register a ProjectileLaunchCallback
	 * It will now receive trigger events
	 * @param callback the callback to register
	 */
	static void register(CrossbowShootingCallback callback) {
		callbacks.add(callback);
	}
	
	/**
	 * Unregister a ProjectileLaunchCallback
	 * It will not receive trigger events anymore
	 * @param callback the callback to unregister
	 */
	static void unregister(CrossbowShootingCallback callback) {
		callbacks.remove(callback);
	}
	
}