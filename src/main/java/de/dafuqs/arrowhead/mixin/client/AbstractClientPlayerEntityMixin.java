package de.dafuqs.arrowhead.mixin.client;

import com.llamalad7.mixinextras.injector.*;
import de.dafuqs.arrowhead.api.ArrowheadBow;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin {
	
	@ModifyReturnValue(method = "getFovMultiplier", at = @At("RETURN"))
	private float arrowhead$applyCustomBowZoom(float original, boolean firstPerson, float fovEffectScale) {
		AbstractClientPlayerEntity thisPlayer = (AbstractClientPlayerEntity)(Object) this;
		ItemStack activeStack = thisPlayer.getActiveItem();
		if (thisPlayer.isUsingItem() && activeStack.getItem() instanceof ArrowheadBow arrowheadBow) {
			int useTime = thisPlayer.getItemUseTime();
			float g = Math.min(useTime / arrowheadBow.getZoom(activeStack), 1.0F);
			original *= 1.0F - MathHelper.square(g) * 0.15F;
		}
		
		return original;
	}

}
