package org.geekbang.time.commonmistakes.stm;

@FunctionalInterface
public interface TxnRunnable {
  void run(Txn txn);
}
