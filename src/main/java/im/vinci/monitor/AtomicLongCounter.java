
package im.vinci.monitor;

import java.util.concurrent.atomic.AtomicLong;


/**
 * @author sunli
 */
public class AtomicLongCounter extends MonitorCounter {
    private final AtomicLong counter = new AtomicLong();

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
        counter.set(value.longValue());
    }

}
