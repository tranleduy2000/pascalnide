package com.duy.interpreter.pascaltypes.bytecode;

public interface RegisterAllocator {
    public int getNextFree();

    public int getNextFreeLong();

    public void free(int i);

    public void freeLong(int i);

    public boolean slotFree(int i);

    public RegisterAllocator clone();
}
