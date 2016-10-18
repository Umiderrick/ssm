package im.vinci.monitor;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author sunli
 */
public class AtomicIntegerCounter extends MonitorCounter {
    private final AtomicInteger counter = new AtomicInteger();

    @Override
    public Number getValue() {
        return counter.get();
    }

    @Override
    public void increment() {
        counter.incrementAndGet();

    }

    @Override
    public void increment(int delta) {
        counter.addAndGet(delta);

    }

    @Override
    public void decrement() {
        counter.decrementAndGet();

    }

    @Override
    public void decrement(int delta) {
        counter.addAndGet(-delta);
    }

    @Override
    public void set(Number value) {
        counter.set(value.intValue());
    }
}
