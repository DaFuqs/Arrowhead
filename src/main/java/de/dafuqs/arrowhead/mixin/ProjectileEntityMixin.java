package de.dafuqs.arrowhead.mixin;

import de.dafuqs.arrowhead.internal.*;
import net.minecraft.entity.projectile.*;
import org.joml.*;
import org.spongepowered.asm.mixin.*;

@Mixin(ProjectileEntity.class)
public class ProjectileEntityMixin implements ProjectileEntityLastCrossbowVelocityStore {
    @Unique
    private Vector3f arrowhead$lastCrossbowVelocity;

    @Override
    public Vector3f arrowhead$getLastCrossbowVelocity() {
        return arrowhead$lastCrossbowVelocity;
    }

    @Override
    public void arrowhead$setLastCrossbowVelocity(Vector3f vec) {
        arrowhead$lastCrossbowVelocity = vec;
    }
}
