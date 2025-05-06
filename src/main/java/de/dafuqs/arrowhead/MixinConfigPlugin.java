package de.dafuqs.arrowhead;

import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class MixinConfigPlugin implements IMixinConfigPlugin {
	private static final boolean CONNECTOR = FabricLoader.getInstance().isModLoaded("connector");
	
	@Override
	public void onLoad(String mixinPackage) {
		// NO-OP
	}
	
	@Override
	public String getRefMapperConfig() {
		return null; // NO-OP
	}
	
	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		if (mixinClassName.endsWith("Fabric")) {
			return !CONNECTOR;
		}
		if (mixinClassName.endsWith("Forge")) {
			return CONNECTOR;
		}
		return true;
	}
	
	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
		// NO-OP
	}
	
	@Override
	public List<String> getMixins() {
		return null; // NO-OP
	}
	
	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		// NO-OP
	}
	
	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		// NO-OP
	}
}
