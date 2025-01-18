package de.dafuqs.arrowhead.api;

import net.minecraft.entity.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.item.*;
import net.minecraft.world.*;

import java.util.*;

public interface CrossbowShootingCallback {
	
	List<CrossbowShootingCallback> callbacks = new ArrayList<>();
	
	/**
	 * Fires after the projectile has gotten its initial velocity set and before vanilla enchantments are run
	 * Only triggers serverside
	 * @param world the world
	 * @param shooter the LivingEntity that shot the crossbow
	 * @param crossbow the crossbow stack
	 * @param projectileEntity the projectile that was shot (initialized, but not yet spawned in the world)
	 */
	void trigger(World world, LivingEntity shooter, ItemStack crossbow, ProjectileEntity projectileEntity);
	
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