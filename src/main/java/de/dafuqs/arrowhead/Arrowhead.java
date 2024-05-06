package de.dafuqs.arrowhead;

import de.dafuqs.arrowhead.api.ArrowheadBow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ComputeFovModifierEvent;

@Mod("arrowhead")
public class Arrowhead {

    @OnlyIn(Dist.CLIENT)
    @Mod.EventBusSubscriber(value = Dist.CLIENT, modid = "arrowhead")
    public static class ArrowheadClient {

        @SubscribeEvent
        public static void onComputeFovModifier(ComputeFovModifierEvent e) {
            Player thisPlayer = e.getPlayer();
            float f = e.getNewFovModifier();
            ItemStack itemStack = thisPlayer.getUseItem();
            if (thisPlayer.isUsingItem() && itemStack.getItem() instanceof ArrowheadBow arrowheadBow) {
                int i = thisPlayer.getTicksUsingItem();
                float g = (float) i / arrowheadBow.getZoom(itemStack);

                if (g > 1.0F) {
                    g = 1.0F;
                } else {
                    g *= g;
                }

                f *= 1.0F - g * 0.15F;

                e.setNewFovModifier(Mth.lerp((Minecraft.getInstance().options.fovEffectScale().get()).floatValue(), 1.0F, f));
            }
        }
    }
}
