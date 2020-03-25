package com.pragmasoft.study.throttling;

public interface Throttler {
    void call(Runnable runnable);
}
