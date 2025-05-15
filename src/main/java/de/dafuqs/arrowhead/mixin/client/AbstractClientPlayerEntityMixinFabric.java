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
public abstract class AbstractClientPlayerEntityMixinFabric {
	
	@ModifyArg(method = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;getFovMultiplier()F", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;lerp(FFF)F"), index = 2)
	private float arrowhead$applyCustomBowZoomFabric(float delta) {
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
