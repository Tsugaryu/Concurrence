package fr.upem.concurrence.td04;
import java.util.LinkedList;
import java.util.Random;

public class UnboundedSafeQueue<V> {
  // qui ajoute un élément à la fin de la file,
  private final LinkedList<V> notSafeList;

  public UnboundedSafeQueue() {
    notSafeList = new LinkedList<V>();
  }

  public void add(V value) {
    synchronized (notSafeList) {
      notSafeList.notify();
      this.notSafeList.addLast(value);
    }

  }

  // qui retire le premier élément de la file et le renvoie.
  // Si la file est vide, la méthode doit attendre jusqu'à ce qu'un élément soit
  // ajouté..
  public V take() throws InterruptedException {
    synchronized (notSafeList) {
      while (notSafeList.isEmpty()) {
        notSafeList.wait();
      }
      return this.notSafeList.removeFirst();
    }

  }

  public static void main(String[] args) throws InterruptedException {
    var nbrThread = 3;
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
