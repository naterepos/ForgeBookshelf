package com.github.naterepos.forgebookshelf.location;

import com.github.naterepos.forgebookshelf.Internal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class AABB {

    private Vector min;
    private Vector max;

    @Internal
    public AABB() {}

    public AABB(BlockPos cornerA, BlockPos cornerB) {
        this(new Vector(cornerA), new Vector(cornerB));
    }

    public AABB(Vector3d cornerA, Vector3d cornerB) {
        this(new Vector(cornerA), new Vector(cornerB));
    }

    public AABB(Vector cornerA, Vector cornerB) {
        this.min = cornerA.min(cornerB);
        this.max = cornerA.max(cornerB);
    }

    public AxisAlignedBB toForge() {
        return new AxisAlignedBB(min.getX(), min.getY(), min.getZ(), max.getX(), max.getY(), max.getZ());
    }

    public boolean contains(Vector location) {
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        double minX = min.getX();
        double minY = min.getY();
        double minZ = min.getZ();
        double maxX = max.getX();
        double maxY = max.getY();
        double maxZ = max.getZ();
        return x >= minX && x <= maxX && y >= minY && y <= maxY && z >= minZ && z <= maxZ;
    }

    public Vector getMin() {
        return min;
    }

    public Vector getMax() {
        return max;
    }

    @Override
    public boolean equals(Object other) {
        if(other == this) return true;
        if(!(other instanceof AABB)) return false;
        return ((AABB) other).getMin().equals(this.getMin()) && ((AABB) other).getMax().equals(this.getMax());
    }

    @Override
    public String toString() {
        return "A: " + min.toString() + "\n" + "B: " + max.toString();
    }
}
