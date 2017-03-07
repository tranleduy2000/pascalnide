package com.duy.interpreter.lib;

import java.util.Map;

public class ScriptControlLib implements PascalLibrary {

//    public static void sleep(int ms) {
//        try {
//            Thread.sleep(ms);
//        } catch (InterruptedException e) {
//            System.err.println("??? Interrupted.");
//            e.printStackTrace();
//        }
//    }
//
//    public static void delay(int ms) {
//        sleep(ms);
//    }

    public static void performException() {
        throw new RuntimeException();
    }

    public static void performException(String message) {
        throw new RuntimeException(message);
    }

    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return true;
    }
}
