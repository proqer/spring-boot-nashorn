package com.pragmasoft.study.throttling;

import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class ThrottlerImpl implements Throttler {

    private Queue<LocalDateTime> expirationDateTimes;

    private int numberOfRequests;
    private int time;
    private TemporalUnit temporalUnit;

    public ThrottlerImpl(int numberOfRequests, int time, TemporalUnit temporalUnit) {
        this.numberOfRequests = numberOfRequests;
        this.time = time;
        this.temporalUnit = temporalUnit;
        expirationDateTimes = new ArrayBlockingQueue<>(numberOfRequests);

    }

    @Override
    public void call(Runnable runnable) {
        if (!shouldWait()) {
            runnable.run();
        }
    }

    private synchronized boolean shouldWait() {
        if (isNotFullExpirationQueue()) {
            expirationDateTimes.add(LocalDateTime.now().plus(time, temporalUnit));
            return false;
        }
        if (isNotTooEarly()) {
            expirationDateTimes.remove();
            return !expirationDateTimes.offer(LocalDateTime.now().plus(time, temporalUnit));
        }
        return true;
    }

    private boolean isNotFullExpirationQueue() {
        return expirationDateTimes.size() < numberOfRequests;
    }

    private boolean isNotTooEarly() {
        LocalDateTime oldestExpirationDateTime = expirationDateTimes.element();
        return oldestExpirationDateTime.isBefore(LocalDateTime.now());
    }

}
