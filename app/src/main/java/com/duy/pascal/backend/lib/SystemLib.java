package com.duy.pascal.backend.lib;

import com.duy.pascal.frontend.activities.ExecuteActivity;

import java.util.Map;

/**
 * System lib
 * <p>
 * - key event
 * - key read
 * <p>
 * Created by Duy on 07-Mar-17.
 */

public class SystemLib implements PascalLibrary {


    private ExecuteActivity activity;

    public SystemLib(ExecuteActivity activity) {
        System.out.println(this);
        this.activity = activity;
    }

    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return false;
    }


    public boolean keyPressed() {
        System.out.println("Key pressed method");
        if (activity != null) {
            return activity.getConsoleView().keyPressed();
        }
        return false;
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
}
