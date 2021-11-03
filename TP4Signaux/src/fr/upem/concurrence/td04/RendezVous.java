package fr.upem.concurrence.td04;
//package fr.upem.concurrence.td03;

import java.util.Objects;

public class RendezVous<V> {
  private V value;
  private final Object locker = new Object();
  private boolean done;

  public void set(V value) {
    Objects.requireNonNull(value);
    synchronized (locker) {
      this.value = value;
      done = true;
      locker.notify();
    }

  }

  public V get() throws InterruptedException {
    while (!done) {
      synchronized(locker) {
        locker.wait(); 
        //Thread.sleep(1); // then comment this line !
      }
     
    }
    return value;
  }

  public static void main(String[] args) throws InterruptedException {
    var rdv = new RendezVous<String>();
    new Thread(() -> {
      try {
        Thread.sleep(20_000);
        rdv.set("Message");
      } catch (InterruptedException e) {
        throw new AssertionError(e);
      }
    }).start();
    System.out.println(rdv.get());
  }

}