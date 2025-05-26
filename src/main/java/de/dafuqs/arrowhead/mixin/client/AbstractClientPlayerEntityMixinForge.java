package de.dafuqs.arrowhead.mixin.client;

import de.dafuqs.arrowhead.api.ArrowheadBow;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Environment(EnvType.CLIENT)
@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixinForge {
	
	@ModifyArg(method = "getFovMultiplier", at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/client/ClientHooks;getFieldOfViewModifier(Lnet/minecraft/world/entity/player/Player;F)F", remap = false), index = 1)
	private float arrowhead$applyCustomBowZoomForge(float delta) {
		AbstractClientPlayerEntity thisPlayer = (AbstractClientPlayerEntity)(Object) this;
		ItemStack itemStack = thisPlayer.getActiveItem();
		if (thisPlayer.isUsingItem() && itemStack.getItem() instanceof ArrowheadBow arrowheadBow) {
			int i = thisPlayer.getItemUseTime();
			float g = (float) i / arrowheadBow.getZoom(itemStack);
			
			if (g > 1.0F) {
				g = 1.0F;
			} else {
				g *= g;
			}
			
			delta *= 1.0F - g * 0.15F;
		}
		
		return delta;
	}

}
