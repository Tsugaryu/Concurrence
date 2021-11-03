package fr.upem.concurrence.td03;

import java.util.Objects;

public class RendezVous<V> {
  private V value;
  final Object locker=new Object();
  public void set(V value) {
    Objects.requireNonNull(value);
    this.value = value;
  }

  public V get() throws InterruptedException {
    int a=0;
    while (value == null || a!=0 ) {
    
      synchronized(locker){
    	  a++;
    	  // Thread.sleep(1); // then comment this line !
      }
    }
    return value;
  }
}