package com.ozankurt.corona.utils;

import net.minecraft.util.math.BlockPos;

public class CoughManager {

    public boolean isCoughing = false;

    public BlockPos blockPos = null;
    public int ticks = 0;

    private static final int TICKS_TO_SPREAD = 60;

    public void start(BlockPos blockPos, int ticks) {
        this.isCoughing = true;

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
        return ! this.blockPos.equals(blockPos);
    }

    public void reset() {
        this.isCoughing = false;

        this.blockPos = null;
        this.ticks = 0;
    }
}
