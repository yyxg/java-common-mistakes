package org.geekbang.time.commonmistakes.stm;

/**
 * @Author xialh
 * @Date 2021/1/9 5:25 PM
 */


/**
 * 带版本号的对象引用
 * @param <T>
 */
public final class VersionedRef<T> {
    final T value;
    final long version;
    //构造方法
    public VersionedRef(T value, long version) {
        this.value = value;
        this.version = version;
    }
}
