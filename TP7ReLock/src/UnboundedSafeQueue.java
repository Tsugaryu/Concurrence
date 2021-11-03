import java.util.ArrayDeque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class UnboundedSafeQueue<V> {

  private final ArrayDeque<V> fifo = new ArrayDeque<>();
  private final ReentrantLock lock = new ReentrantLock();
  private final Condition condition = lock.newCondition();

  public void add(V value) {
    lock.lock();
    try {
      fifo.add(value);
      condition.signalAll();
    } finally {
      lock.unlock();
    }

  }

  public V take() throws InterruptedException {
    lock.lock();
    try {
      while (fifo.isEmpty()) {
        condition.await();
      }
    }finally {
      lock.unlock();
    }
    return fifo.remove();
  }
  public static void main(String[] args) throws InterruptedException {
    var nbrThread = 50;
    var time = 1_000L;
    var myQueue = new UnboundedSafeQueue<String>();
    // -----DECLARATION + ACTION THREAD --------------
    for (var i = 0; i < nbrThread; i++) {
      var j = i;
      var thread = new Thread(() -> {

        for (;;) {

          try {
            Thread.sleep(time);
            myQueue.add(Thread.currentThread().getName());

          } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        }
      }, "ThreadNumber00" + j);
      thread.setDaemon(true);
      thread.start();
      // ---------------ACTION DU MAIN----------
     

    }
    while (true) {
      var elt = myQueue.take();
      System.out.println(elt);
    }
  }
}