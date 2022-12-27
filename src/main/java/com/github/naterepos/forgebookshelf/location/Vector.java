package com.github.naterepos.forgebookshelf.location;

import com.github.naterepos.forgebookshelf.Internal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class Vector {

    private double x;
    private double y;
    private double z;

    @Internal
    public Vector() {}

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector(Vector3d vector3d) {
        this.x = vector3d.getX();
        this.y = vector3d.getY();
        this.z = vector3d.getZ();
    }

    public Vector(BlockPos pos) {
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
    }

    public Vector max(Vector other) {
        return new Vector(Math.max(this.x, other.x), Math.max(this.y, other.y), Math.max(this.z, other.z));
    }

    public Vector min(Vector other) {
        return new Vector(Math.min(this.x, other.x), Math.min(this.y, other.y), Math.min(this.z, other.z));
    }

    public double getZ() {
        return z;
    }

    public double getY() {
        return y;
    }

    public double getX() {
        return x;
    }

    public Vector plus(Vector other) {
        return new Vector(x + other.getX(), y + other.getY(), z + other.getZ());
    }

    public Vector plus(double x, double y, double z) {
        return new Vector(this.x + x, this.y + y, this.z + z);
    }

    public Vector minus(Vector other) {
        return new Vector(x - other.getX(), y - other.getY(), z - other.getZ());
    }

    public Vector minus(double x, double y, double z) {
        return new Vector(this.x - x, this.y - y, this.z - z);
    }

    public Vector3d toVector3d() {
        return new Vector3d(x,y,z);
    }

    public BlockPos toBlockPos() {
        return new BlockPos(x,y,z);
    }

    public boolean equalsPlusOrMinus(BlockPos other, Vector plusOrMinus) {
        return equalsPlusOrMinus(new Vector(other), plusOrMinus);
    }

    public boolean equalsPlusOrMinus(Vector3d other, Vector plusOrMinus) {
        return equalsPlusOrMinus(new Vector(other), plusOrMinus);
    }

    public boolean equalsPlusOrMinus(Vector other, Vector plusOrMinus) {
        return (Math.abs(other.getX() - this.getX()) <= plusOrMinus.x) &&
                (Math.abs(other.getY() - this.getY()) <= plusOrMinus.y) &&
                (Math.abs(other.getZ() - this.getZ()) <= plusOrMinus.z);
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }

    @Override
    public boolean equals(Object other) {
        if(other == null) return false;
        if(this == other) return true;
        if(other instanceof Vector) {
            Vector vec = (Vector) other;
            return this.x == vec.x && this.y == vec.y && this.z == vec.z;
        } else if(other instanceof Vector3d) {
            Vector3d vec = (Vector3d) other;
            return this.x == vec.x && this.y == vec.y && this.z == vec.z;
        } else if(other instanceof BlockPos) {
            BlockPos pos = (BlockPos) other;
            return this.x == pos.getX() && this.y == pos.getY() && this.z == pos.getZ();
        }
        return false;
    }
}
