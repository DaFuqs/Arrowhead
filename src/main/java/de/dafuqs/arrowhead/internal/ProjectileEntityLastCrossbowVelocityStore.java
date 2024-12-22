package de.dafuqs.arrowhead.internal;

import org.jetbrains.annotations.ApiStatus;
import org.joml.Vector3f;

@ApiStatus.Internal
public interface ProjectileEntityLastCrossbowVelocityStore {
    Vector3f arrowhead$getLastCrossbowVelocity();
    void arrowhead$setLastCrossbowVelocity(Vector3f vec);
}
