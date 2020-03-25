package com.pragmasoft.study;

import com.pragmasoft.study.throttling.Throttler;
import com.pragmasoft.study.throttling.ThrottlerImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ThrottlerTest {

    @Test
    public void test() throws InterruptedException {
        Throttler db = new ThrottlerImpl(2, 1, ChronoUnit.SECONDS);
        AtomicInteger rx_count = new AtomicInteger();
        AtomicInteger ry_count = new AtomicInteger();

        Runnable rx = () -> {
            System.out.println("x");
            rx_count.incrementAndGet();
        };
        Runnable ry = () -> {
            System.out.println("y");
            ry_count.incrementAndGet();
        };

        for (int i = 0; i < 8; i++) {
            Thread.sleep(50);
            db.call(rx);
            Thread.sleep(50);
            db.call(ry);
        }
        Thread.sleep(200); // expecting x and y
        assertEquals(1, rx_count.get());
        assertEquals(1, ry_count.get());

        for (int i = 0; i < 10000; i++) {
            db.call(rx);
        }
        Thread.sleep(2_400); // expecting only x
        assertEquals(2, rx_count.get());
        assertEquals(1, ry_count.get());

        db.call(ry);
        Thread.sleep(1_100); // expecting only y
        assertEquals(2, rx_count.get());
        assertEquals(2, ry_count.get());
    }
}
