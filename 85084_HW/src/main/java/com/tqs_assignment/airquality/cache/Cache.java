package com.tqs_assignment.airquality.cache;

import java.util.HashMap;
import java.util.Timer;

public class Cache extends HashMap<String, Object> {
    public static HashMap<String, Object> GlobalCache = new Cache();

    private int success;
    private int insucess;
    private Timer timer = new Timer();

    @Override
    public Object get(Object key) {
        Object value = super.get(key);
        if (value == null) {
            insucess++;
        } else {
            success++;
        }
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

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getInsucess() {
        return insucess;
    }

    public void setInsucess(int insucess) {
        this.insucess = insucess;
    }


}