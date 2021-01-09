package org.geekbang.time.commonmistakes.stm;

/**
 * 支持事务的引用
 * @param <T>
 * @author xialihui
 */
public class TxnRef<T> {
    /**
     * 当前数据，带版本号
     */
    volatile VersionedRef curRef;

    /**
     * 构造方法
     * @param value
     */
    public TxnRef(T value) {
        this.curRef = new VersionedRef(value, 0L);
    }

    /**
     * 获取当前事务中的数据
     * @param txn
     * @return
     */
    public T getValue(Txn txn) {
        return txn.get(this);
    }

    /**
     * 在当前事务中设置数据
     * @param value
     * @param txn
     */
    public void setValue(T value, Txn txn) {
        txn.set(this, value);
    }
}