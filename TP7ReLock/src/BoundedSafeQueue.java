import java.util.ArrayDeque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedSafeQueue<V> {

  private final ArrayDeque<V> fifo = new ArrayDeque<>();
  private final int capacity;
  private final ReentrantLock lock = new ReentrantLock();
  private final Condition condition = lock.newCondition();
  private final Condition putCondition = lock.newCondition();  
  public BoundedSafeQueue(int capacity) {
    if (capacity <= 0) {
      throw new IllegalArgumentException();
    }
    this.capacity = capacity;
  }

  public void put(V value) throws InterruptedException {
    lock.lock();
    try {
      while (fifo.size() == capacity) {
        putCondition.await();
      }
      fifo.add(value);
      putCondition.signalAll();
      
    }finally {
      lock.unlock();
    }
   
  }

  public V take() throws InterruptedException {
    lock.lock();
    try {
      while (fifo.isEmpty()) {
        condition.await();
      }
      condition.signalAll();
    } finally {
      lock.unlock();
    }
      return fifo.remove();
    }
  public static void main(String[] args) throws InterruptedException {
	    var nbrThread = 50;
	    var time = 1_000L;
	    var myQueue = new BoundedSafeQueue<String>(15);
	    // -----DECLARATION + ACTION THREAD --------------
	    for (var i = 0; i < nbrThread; i++) {
	      var j = i;
	      var thread = new Thread(() -> {

	        for (;;) {

	          try {
	            Thread.sleep(time);
	            myQueue.put(Thread.currentThread().getName());

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
