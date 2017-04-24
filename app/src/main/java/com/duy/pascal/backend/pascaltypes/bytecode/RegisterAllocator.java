package com.duy.pascal.backend.pascaltypes.bytecode;

public interface RegisterAllocator {
    int getNextFree();

    int getNextFreeLong();

    void free(int i);

    void freeLong(int i);

    boolean slotFree(int i);

    RegisterAllocator clone();
}
