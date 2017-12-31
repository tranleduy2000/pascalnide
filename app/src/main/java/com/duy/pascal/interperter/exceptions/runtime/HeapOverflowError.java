package com.duy.pascal.interperter.exceptions.runtime;

/**
 * The heap has grown beyond its boundaries. This is caused when trying to allocate memory
 * explicitly with New, GetMem or ReallocMem, or when a class or object instance is created
 * and no memory is left. Please note that, by default, Free Pascal provides a growing heap,
 * i.e. the heap will try to allocate more memory if needed. However, if the heap has reached
 * the maximum size allowed by the operating system or hardware, then you will get this error.
 * <p>
 * Created by Duy on 07-Apr-17.
 */

public class HeapOverflowError {
}
