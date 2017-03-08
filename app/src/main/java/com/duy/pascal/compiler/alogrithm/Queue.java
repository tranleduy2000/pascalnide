package com.duy.pascal.compiler.alogrithm;

/**
 * Created by Duy on 10-Feb-17.
 */
public class Queue {
    private byte Data[];
    private int Head;
    private int Tail;

    public Queue(int size) {
        Data = new byte[size];
        Head = 0;
        Tail = 0;
    }

    public synchronized byte getByte() {
        while (Head == Tail) {
            try {
                wait();
            } catch (InterruptedException ie) {
            }
        }
        byte b = Data[Head];
        Head++;
        if (Head >= Data.length) Head = 0;
        return b;
    }

    public synchronized void putByte(byte b) {
        Data[Tail] = b;
        Tail++;
        if (Tail >= Data.length) Tail = 0;
        if (Head == Tail) {

            Head++;
            if (Head >= Data.length) Head = 0;
        }
        notify();
    }
    /*
    public synchronized  void eraseByte()
	{
		if (Tail!=Head)
		{
			Tail--;
			if (Tail<0) Tail=0;
		}
		notify();
	}

	public synchronized void Flush()
	{
		Tail=Head;
		notify();
	}
	*/
}
