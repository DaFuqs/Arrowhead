package de.dafuqs.arrowhead.mixin.client;

import de.dafuqs.arrowhead.api.ArrowheadBow;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin {
	
	@ModifyArg(method = "getFieldOfViewModifier()F", at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/client/ClientHooks;getFieldOfViewModifier(Lnet/minecraft/world/entity/player/Player;F)F", remap = false), index = 1)
	private float arrowhead$applyCustomBowZoomForge(float delta) {
		AbstractClientPlayer thisPlayer = (AbstractClientPlayer) (Object) this;
		ItemStack itemStack = thisPlayer.getUseItem();
		if (thisPlayer.isUsingItem() && itemStack.getItem() instanceof ArrowheadBow arrowheadBow) {
			int i = thisPlayer.getTicksUsingItem();
			float g = (float) i / arrowheadBow.getZoom(itemStack, thisPlayer);
			
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
