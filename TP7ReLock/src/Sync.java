import java.util.function.Supplier;

public class Sync<V> {
  private boolean supplierUsed;
  private final Object locker= new Object();
  //private final ReentrantLock lock = new ReentrantLock();
  //private final Condition condition = lock.newCondition();
  public boolean inSafe() {
    synchronized(locker) {
      return supplierUsed;
    }
  }
  
  public V safe(Supplier<? extends V> supplier) throws InterruptedException {
    //premier block synchronizé, t ds safe tu block
    synchronized(locker) {
      supplierUsed=true;
      //while()
    }
    //do the supplier
    var supply = supplier.get();
    //tell the supplier is finished you can quit(in the second synkronaze)
    //do a second synchronize
    synchronized (locker) {
     supplierUsed=false;
      locker.notifyAll();
      return supply;  // TODO
    }
   
  }
}