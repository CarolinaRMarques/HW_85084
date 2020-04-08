package com.tqs_assignment.airquality.cache;

import java.util.HashMap;
import java.util.Objects;
import java.util.Timer;

public class Cache extends HashMap<String, Object> {
    public static HashMap<String, Object> GlobalCache = new Cache();
    protected int success;
    protected int insucess;
    protected int total_requests ;
    protected transient Timer timer = new Timer();

    @Override
    public Object get(Object key) {
        Object value = super.get(key);
        if (value == null) {
            insucess++;
        } else {
            success++;
        }
        total_requests = success + insucess ;
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

    @Override
    public String toString() {
        return "Cache{" +
                "success=" + success +
                ", insucess=" + insucess +
                ", total_requests= " + total_requests +
                '}';
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

    public int getTotal_requests() {
        return total_requests;
    }

    public void setTotal_requests(int total_requests) {
        this.total_requests = total_requests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Cache cache = (Cache) o;
        return success == cache.success &&
                insucess == cache.insucess;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), success, insucess);
    }
}