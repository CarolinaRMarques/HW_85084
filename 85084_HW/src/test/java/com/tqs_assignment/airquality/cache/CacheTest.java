package com.tqs_assignment.airquality.cache;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;


class CacheTest {
    Cache cache = new Cache();
    HashMap <String,Object> comparator = new HashMap<>();

    @Test
    public void whenPut_IncreaseSize() {
        cache.put("link","test");
        comparator.put("linkA","testA");
        assertThat(cache.size()).isEqualTo(comparator.size()).isEqualTo(1);
    }

    @Test
    public void whenPut_get() {
        cache.put("link","test");
        Object test =cache.get("link");
        assertThat(test).isEqualTo("test");
    }

    @Test
    public void whenGettingSomethingInexistent_Null () {
        Object test = cache.get("link");
        assertThat(test).isNull();
    }

    @Test
    public void whenGetOnce_ThenSuccessIncrement() throws InterruptedException {
        cache.put("link","hey");
        cache.get("link");
        assertThat(cache.getSuccess()).isEqualTo(1);

    }

    @Test
    public void whenGetTwiceAfterDelayTime_ThenInsucessIncrement() throws InterruptedException {
        cache.put("link","hey");
        Thread.sleep(20000);
        cache.get("link");
        assertThat(cache.getInsucess()).isEqualTo(1);
    }

    @Test
    public void whenGetOnceInTimeSucessIncrement_ElseInsucessIncrement () throws InterruptedException {
        cache.put("link","hey");
        cache.get("link");
        Thread.sleep(20000);
        cache.get("link");
        assertThat(cache.getSuccess()).isEqualTo(cache.getInsucess()).isEqualTo(1);
    }

    @Test
    public void whenGetingDuringTime_thenSuccessIncrements () throws InterruptedException {
        cache.put("link","hey");
        for (int i = 0 ; i < 3 ; i++ ) {
            cache.get("link");
        }
        assertThat(cache.getSuccess()).isEqualTo(3);
    }

    @Test
    public void whenGetingAfterTime_thenInsuccessIncrements () throws InterruptedException {
        cache.put("link","hey");
        Thread.sleep(20000);
        for (int i = 0 ; i < 3 ; i++ ) {
            cache.get("link");
        }
        assertThat(cache.getInsucess()).isEqualTo(3);
    }
}