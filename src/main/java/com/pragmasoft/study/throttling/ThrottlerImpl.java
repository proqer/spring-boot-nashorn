package com.pragmasoft.study.throttling;

import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ThrottlerImpl implements Throttler {

    private Queue<LocalDateTime> localDateTimes = new ConcurrentLinkedQueue<>();

    private int numberOfRequests;
    private int time;
    private TemporalUnit temporalUnit;

    public ThrottlerImpl(int numberOfRequests, int time, TemporalUnit temporalUnit) {
        this.numberOfRequests = numberOfRequests;
        this.time = time;
        this.temporalUnit = temporalUnit;
    }

    @Override
    public void call(Runnable runnable) {
        if (!shouldWait()) {
            runnable.run();
            localDateTimes.add(LocalDateTime.now());
        }
    }

    private boolean shouldWait() {
        if (localDateTimes.size() < numberOfRequests) {
            return false;
        }
        LocalDateTime firstElementLocalDateTime = localDateTimes.element();
        boolean tooEarly = firstElementLocalDateTime.isAfter(LocalDateTime.now().minus(time, temporalUnit));
        if (!tooEarly) {
            localDateTimes.remove();
            return false;
        }
        return true;
    }

}
