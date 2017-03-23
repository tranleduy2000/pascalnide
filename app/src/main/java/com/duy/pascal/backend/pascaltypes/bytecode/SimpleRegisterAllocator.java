package com.duy.pascal.backend.pascaltypes.bytecode;


public class SimpleRegisterAllocator implements RegisterAllocator {
    // Average of constant time allocation as long as the JVM's array
    // allocations are O(n) or better.

    static final int END_OF_LIST = -2;
    static final int NOT_FREE_SIZE_1 = -1;
    static final int NOT_FREE_AT_ALL = -3;
    static final int INSIDE_FREE_SIZE_2 = -4;
    final int offset;
    // Psuedo linked list format for free blocks
    // -1=This block is occupied
    // -2=this is the last element of the list.
    // n=n is the next free block in this list.
    int[] next;
    // Only relevant on the single size list.
    // -1=this block is either taken or part of a size 2 block.
    // -2= this is the first block in the list.
    int[] previous;
    // Beginning of linked list for size 2 blocks.
    int firstfree;
    // beginning of linked list for size 1 block.
    int firstSingleFree;
    // By what factor do we increase the size of the array when out linked list
    // pulls us outside its bounds?
    // (theoretically, the linked list of free size 2 blocks extends all the way
    // out to 65536 blocks, but we only store
    // the beginning of it, and calculate the rest when we start to use that
    // many blocks.
    double scalefactor = 2;

    public SimpleRegisterAllocator(int offset) {
        this.offset = offset;
        next = new int[8];
        previous = new int[8];
        for (int i = 0; i < next.length; i += 2) {
            next[i] = i + 2;
            next[i + 1] = INSIDE_FREE_SIZE_2;
            previous[i] = NOT_FREE_SIZE_1;
            previous[i + 1] = NOT_FREE_SIZE_1;
        }
        firstfree = 0;
        firstSingleFree = END_OF_LIST;
    }

    @Override
    public int getNextFree() {
        int result = firstSingleFree;
        if (result >= 0) {
            removeSize1Block(result);
            return result + offset;
        } else {
            result = getNextFreeLong() - offset;
            // If two ahead is free, we can coalesce the blocks.
            if (previous[result + 2] != NOT_FREE_SIZE_1) {
                removeSize1Block(result + 2);
                addSize2Block(result + 1);
            } else {
                addSize1Block(result + 1);
            }
            return result + offset;
        }
    }

    private void addSize2Block(int index) {
        next[index] = firstfree;
        previous[index] = NOT_FREE_SIZE_1;
        next[index + 1] = INSIDE_FREE_SIZE_2;
        previous[index + 1] = NOT_FREE_SIZE_1;
        firstfree = index;
    }

    private void addSize1Block(int index) {
        next[index] = firstSingleFree;
        if (firstSingleFree >= 0) {
            previous[firstSingleFree] = index;
        }
        previous[index] = END_OF_LIST;
        firstSingleFree = index;
    }

    private void removeSize1Block(int index) {
        if (previous[index] >= 0) {
            next[previous[index]] = next[index];
        }
        if (next[index] >= 0) {
            previous[next[index]] = previous[index];
        }
        previous[index] = NOT_FREE_SIZE_1;
        next[index] = NOT_FREE_AT_ALL;
    }

    @Override
    public void free(int i) {
        i -= offset;
        if (i != next.length - 1) {
            if (previous[i + 1] != NOT_FREE_SIZE_1) {
                removeSize1Block(i + 1);
                addSize2Block(i);
            }
        } else if (i != 0) {
            if (previous[i - 1] != NOT_FREE_SIZE_1) {
                removeSize1Block(i - 1);
                addSize2Block(i - 1);
            }
        } else {
            addSize1Block(i);
        }
    }

    @Override
    public int getNextFreeLong() {
        int result = firstfree;
        if (firstfree >= next.length) {
            int len = Math
                    .max((int) (next.length * scalefactor), firstfree + 1);
            len += len & 1;// Make it even.
            int[] newarr = new int[len];
            int[] newprev = new int[len];
            System.arraycopy(next, 0, newarr, 0, next.length);
            next = newarr;
            System.arraycopy(previous, 0, newprev, 0, next.length);
            previous = newprev;
            for (int i = next.length; i < newarr.length; i += 2) {
                next[i] = i + 2;
                previous[i] = NOT_FREE_SIZE_1;
                next[i + 1] = INSIDE_FREE_SIZE_2;
                previous[i + 1] = NOT_FREE_SIZE_1;
            }
        }
        firstfree = next[firstfree];
        next[result] = NOT_FREE_AT_ALL;
        next[result + 1] = NOT_FREE_AT_ALL;

        return result + offset;
    }

    @Override
    public void freeLong(int i) {
        i -= offset;
        addSize2Block(i);
    }

    @Override
    public boolean slotFree(int i) {
        return next[i] != NOT_FREE_AT_ALL;
    }

    @Override
    public SimpleRegisterAllocator clone() {
        SimpleRegisterAllocator result = new SimpleRegisterAllocator(offset);
        result.firstfree = firstfree;
        result.firstSingleFree = firstSingleFree;
        result.next = next.clone();
        result.previous = previous.clone();
        return result;
    }
}
