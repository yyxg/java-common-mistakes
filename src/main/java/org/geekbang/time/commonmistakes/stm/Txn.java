package org.geekbang.time.commonmistakes.stm;

/**
 * @Author xialh
 * @Date 2021/1/9 5:26 PM
 */

/**
 * 事务接口
 */
public interface Txn {
    <T> T get(TxnRef<T> ref);
    <T> void set(TxnRef<T> ref, T value);
}
