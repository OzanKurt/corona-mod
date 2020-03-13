package com.ozankurt.corona.interfaces;

public interface PlayerEntityMixinInterface {
    void startCoughing(int ticks);

    void setHasCorona(boolean b);

    boolean hasCorona();
}
