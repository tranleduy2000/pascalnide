package com.duy.pascal.backend.pascaltypes.bytecode;

public class ScopedRegisterAllocator implements RegisterAllocator {

    RegisterAllocator implicit;
    RegisterAllocator clone;

    public ScopedRegisterAllocator(RegisterAllocator other) {
        implicit = other;
        clone = other.clone();
    }

    @Override
    public int getNextFree() {
        return clone.getNextFree();
    }

    @Override
    public int getNextFreeLong() {
        return clone.getNextFreeLong();
    }

    @Override
    public void free(int i) {
        if (implicit.slotFree(i)) {
            clone.free(i);
        } else {
            System.err
                    .println("Internal Interpreter Error!  Attempted to free register which was not ours to free");
        }
    }

    @Override
    public void freeLong(int i) {
        if (implicit.slotFree(i) && implicit.slotFree(i + 1)) {
            clone.freeLong(i);
        } else {
            System.err
                    .println("Internal Interpreter Error!  Attempted to free register which was not ours to free");
        }
    }

    @Override
    public boolean slotFree(int i) {
        return clone.slotFree(i);
    }

    @Override
    public ScopedRegisterAllocator clone() {
        ScopedRegisterAllocator result = new ScopedRegisterAllocator(clone);
        result.implicit = this;
        return result;
    }

}
