package de.dafuqs.arrowhead.api;

import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;

import java.util.*;

public interface CrossbowShootingCallback {
	
	List<CrossbowShootingCallback> CALLBACKS = new ArrayList<>();
	
	/**
	 * Fires after the projectile has gotten its initial velocity set and before vanilla enchantments are run
	 * Only triggers serverside
	 * @param world the world
	 * @param shooter the LivingEntity that shot the crossbow
	 * @param crossbow the crossbow stack
	 * @param projectile the projectile that was shot (initialized, but not yet spawned in the world)
	 */
	void trigger(Level world, LivingEntity shooter, ItemStack crossbow, Projectile projectile);
	
	/**
	 * Register a ProjectileLaunchCallback
	 * It will now receive trigger events
	 * @param callback the callback to register
	 */
	static void register(CrossbowShootingCallback callback) {
		CALLBACKS.add(callback);
	}
	
	/**
	 * Unregister a ProjectileLaunchCallback
	 * It will not receive trigger events anymore
	 * @param callback the callback to unregister
	 */
	static void unregister(CrossbowShootingCallback callback) {
		CALLBACKS.remove(callback);
	}
	
}