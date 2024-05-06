package de.dafuqs.arrowhead.api;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface BowShootingCallback {
	
	List<BowShootingCallback> callbacks = new ArrayList<>();
	
	/**
	 * Fires after the projectile has gotten its initial velocity set and before vanilla enchantments are run
	 * Only triggers serverside
	 * @param world the world
	 * @param player the player that shot the bow
	 * @param weaponStack the bow stack
	 * @param arrowStack the arrow stack that was shot
	 * @param remainingUseTicks the remaining use time of the bow at the time of release
	 * @param persistentProjectileEntity the projectile that was shot (initialized, but not yet spawned in the world)
	 */
	void trigger(Level world, Player player, ItemStack weaponStack, ItemStack arrowStack, int remainingUseTicks, AbstractArrow persistentProjectileEntity);
	
	/**
	 * Register a ProjectileLaunchCallback
	 * It will now receive trigger events
	 * @param callback the callback to register
	 */
	static void register(BowShootingCallback callback) {
		callbacks.add(callback);
	}
	
	/**
	 * Unregister a ProjectileLaunchCallback
	 * It will not receive trigger events anymore
	 * @param callback the callback to unregister
	 */
	static void unregister(BowShootingCallback callback) {
		callbacks.remove(callback);
	}
	
}