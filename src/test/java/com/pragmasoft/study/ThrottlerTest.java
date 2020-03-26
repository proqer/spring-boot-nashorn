package com.pragmasoft.study;

import com.pragmasoft.study.throttling.Throttler;
import com.pragmasoft.study.throttling.ThrottlerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ThrottlerTest {

    private Throttler throttler;
    private AtomicInteger rx_count;
    private AtomicInteger ry_count;
    private Runnable rx;
    private Runnable ry;

    @BeforeEach
    public void init() {
        throttler = new ThrottlerImpl(2, 1, ChronoUnit.SECONDS);
        rx_count = new AtomicInteger();
        ry_count = new AtomicInteger();
        rx = () -> {
            System.out.println("x");
            rx_count.incrementAndGet();
        };
        ry = () -> {
            System.out.println("y");
            ry_count.incrementAndGet();
        };
    }

    @Test
    public void throttlerOneThreadTest() throws InterruptedException {
        for (int i = 0; i < 8; i++) {
            Thread.sleep(50);
            throttler.call(rx);
            Thread.sleep(50);
            throttler.call(ry);
        }
        Thread.sleep(200); // expecting x and y
        assertEquals(1, rx_count.get());
        assertEquals(1, ry_count.get());

        for (int i = 0; i < 10000; i++) {
            throttler.call(rx);
        }
        Thread.sleep(2_400); // expecting only x
        assertEquals(2, rx_count.get());
        assertEquals(1, ry_count.get());

        throttler.call(ry);
        Thread.sleep(1_100); // expecting only y
        assertEquals(2, rx_count.get());
        assertEquals(2, ry_count.get());
    }

    @Test
    void throttlerMultiThreadTest() throws ExecutionException, InterruptedException {
        Runnable throttlingCallerRunnable = () -> {
            for (int i = 0; i < 1000; i++) {
                throttler.call(rx);
            }
        };
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Future<?>> futureList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            futureList.add(executorService.submit(throttlingCallerRunnable));
        }
        for (Future<?> future : futureList) {
            future.get();
        }
        assertEquals(2, rx_count.get());
    }

}
