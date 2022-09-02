package de.dafuqs.arrowhead.api;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

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
	void trigger(World world, LivingEntity shooter, Hand hand, ItemStack crossbow, ItemStack projectile, ProjectileEntity projectileEntity);
	
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