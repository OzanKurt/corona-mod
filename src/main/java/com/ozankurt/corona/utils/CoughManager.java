package com.ozankurt.corona.utils;

import net.minecraft.util.math.BlockPos;

public class CoughManager {

    public BlockPos blockPos;
    public int ticks;

    private static final int TICKS_TO_SPREAD = 60;

    public void start(BlockPos blockPos, int ticks) {
        this.blockPos = blockPos;
        this.ticks = ticks;
    }

    public boolean shouldSpread(BlockPos blockPos, int ticks) {
        if (ticks >= (this.ticks + TICKS_TO_SPREAD)) {
            return true;
        }

        return false;
    }

    public boolean hasChangedPosition(BlockPos blockPos) {
        if (this.blockPos == blockPos) {
            return true;
        }

        return false;
    }

    public boolean isCoughing() {
        return blockPos == null;
    }

    public void resetCough() {
        this.blockPos = null;
        this.ticks = 0;
    }
}
