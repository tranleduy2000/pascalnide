package com.duy.pascal.backend.lib;

import android.util.Log;

import com.duy.pascal.frontend.activities.ExecuteActivity;

import java.util.Map;
import java.util.Random;

/**
 * System lib
 * <p>
 * - key event
 * - key read
 * <p>
 * Created by Duy on 07-Mar-17.
 */

public class SystemLib implements PascalLibrary {


    private  Random random = new Random();
    private ExecuteActivity activity;

    public SystemLib(ExecuteActivity activity) {
        this.activity = activity;
    }


    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return false;
    }

    /**
     * key pressed method
     *
     * @return
     */
    public boolean keyPressed() {
        if (activity != null) {
            return activity.getConsoleView().keyPressed();
        }
        return false;
    }

    /**
     * procedure readkey
     */
    public char readKey() {
        assert activity != null;
        return activity.readKey();
    }

    /**
     * delay procedure
     *
     * @param ms - time
     */
    public void delay(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
//            System.err.println("??? Interrupted.");
            e.printStackTrace();
        }
    }

    public int Byte(boolean b) {
        return b ? 1 : 0;
    }

    public  int random(int range) {
        Log.d("random", "random: " + range);
        return random.nextInt(range);
    }

    public  void randomize() {
        random = new Random(System.currentTimeMillis());
    }
}
