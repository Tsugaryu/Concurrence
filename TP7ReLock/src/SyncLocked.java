import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

public class SyncLocked<V> {
  private boolean supplierUsed;
  private final ReentrantLock lock = new ReentrantLock();
  private final Condition condition = lock.newCondition();
  public boolean inSafe() {
    lock.lock();
    try {
      return supplierUsed;
    } finally {
      lock.unlock();
    }
  }
  
  public V safe(Supplier<? extends V> supplier) throws InterruptedException {
    //premier block synchronizé, t ds safe tu block
   lock.lock();
   try {
     supplierUsed=true;
  } finally {
    lock.unlock();
  }
    var supply = supplier.get();
    lock.lock();
    try {
      supplierUsed=false;
      condition.signalAll();
    } finally {
      lock.unlock();
    }     
      return supply;
  }
}