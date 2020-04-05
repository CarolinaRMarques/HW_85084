package com.tqs_assignment.airquality.cache;

import java.util.HashMap;
import java.util.Timer;

public class Cache extends HashMap<String, Object> {
    int success;
    int insucess;
    public static HashMap<String, Object> GlobalCache = new Cache();
    private Timer timer = new Timer();

    @Override
    public Object get(Object key) {
        Object value = super.get(key);
        if (value == null) {
            insucess++;
        } else {
            success++;
        }
    //    System.out.println("s"+success+" in"+insucess);
        return value;
    }

    @Override
    public Object put(String link, Object key) {
        Object value = super.put(link, key);
        timer.schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                      remove(link);
                    }
                },
                10000
        );
        return value;
    }


}