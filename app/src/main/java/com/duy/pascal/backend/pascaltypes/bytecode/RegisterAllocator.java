package com.duy.pascal.backend.pascaltypes.bytecode;

public interface RegisterAllocator {
    public int getNextFree();

    public int getNextFreeLong();

    public void free(int i);

    public void freeLong(int i);

    public boolean slotFree(int i);

    public RegisterAllocator clone();
}
