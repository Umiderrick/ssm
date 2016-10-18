package im.vinci.monitor.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 监控历史数据存储
 * <p>
 * counter可以存储31个历史数据
 * </p>
 * 
 * @author sunli
 */
public class LastData<E> {

    private transient Object[] elementData;

    private int maxSeq = -1;

    private final AtomicInteger seq = new AtomicInteger(0);

    public LastData() {
        maxSeq = (2 << 4) - 1;
        this.elementData = new Object[maxSeq + 1];
    }

    /**
     * 容量大小
     * <p>
     * 
     * <pre>
     * (2 &lt;&lt; power) - 1
     * </pre>
     * 
     * </p>
     * 
     * @param power 幂指数
     */
    public LastData(int power) {
        maxSeq = (2 << power) - 1;
        this.elementData = new Object[maxSeq + 1];
    }

    public boolean add(E e) {
        this.elementData[(seq.incrementAndGet() - 1) & maxSeq] = e;
        return true;
    }

    private void rangeCheck(int index) {
        if (index >= maxSeq) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + maxSeq);
        }
    }

    @SuppressWarnings("unchecked")
    public E get(int index) {
        rangeCheck(index);
        return (E) elementData[index];
    }

    /**
     * 获取从当前位置往前count个的元素
     * 
     * @param count
     * @return
     */
    @SuppressWarnings("unchecked")
    public E getLastElements(int count) {
        if (count > maxSeq) {
            throw new IndexOutOfBoundsException("count: " + count + ", maxSize: " + maxSeq);
        }

        int currentSeq = seq.get();
        return (E) this.elementData[currentSeq - count & maxSeq];
    }

    /**
     * 获取当前的index
     * 
     * @return
     */
    public int currentIndex() {
        return seq.get() & maxSeq;
    }

    public int size() {
        return this.elementData.length;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i <= maxSeq; i++) {
            sb.append(this.elementData[i]);
            sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }
}
