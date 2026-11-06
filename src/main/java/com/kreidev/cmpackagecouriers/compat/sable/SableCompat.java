package com.kreidev.cmpackagecouriers.compat.sable;

import dev.ryanhcode.sable.companion.SableCompanion;
import dev.ryanhcode.sable.companion.SubLevelAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;

// Blocks on a Sable sub-level live in its plot, far away from where the sub-level is shown in the world.
public final class SableCompat {

    private SableCompat() {
    }

    public static Vec3 toWorld(Level level, Vec3 pos) {
        return SableCompanion.INSTANCE.projectOutOfSubLevel(level, pos);
    }

    public static Vec3 directionToWorld(Level level, Vec3 pos, Vec3 direction) {
        SubLevelAccess subLevel = SableCompanion.INSTANCE.getContaining(level, pos);
        if (subLevel == null) return direction;
        Vector3d rotated = subLevel.logicalPose().transformNormal(new Vector3d(direction.x, direction.y, direction.z), new Vector3d());
        return new Vec3(rotated.x, rotated.y, rotated.z);
    }

    public static boolean isInPlot(Level level, BlockPos pos) {
        return SableCompanion.INSTANCE.isInPlotGrid(level, pos);
    }
}
