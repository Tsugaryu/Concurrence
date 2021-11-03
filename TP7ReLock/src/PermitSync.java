import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

public class PermitSync<V> {
  private int supplierUsed;
  private final ReentrantLock lock = new ReentrantLock();
  private final Condition condition = lock.newCondition();
  private final int permits;
  public boolean inSafe() {
    lock.lock();
    try {
      return supplierUsed<permits;
    } finally {
      lock.unlock();
    }
  }
  public PermitSync(int permits) {
    this.permits=permits;
  }
  public V safe(Supplier<? extends V> supplier) throws InterruptedException {
    //premier block synchronizé, t ds safe tu block
   lock.lock();
   try {
     supplierUsed+=1;
  } finally {
    lock.unlock();
  }
    var supply = supplier.get();
    lock.lock();
    try {
      supplierUsed-=1;
      condition.signalAll();
    } finally {
      lock.unlock();
    }     
      return supply;
  }
}