package fr.umlv.conc;

import java.util.Arrays;
import java.util.stream.IntStream;
/*
 *  Est-il possible d'avoir deux philosophes qui mangent en même temps ? Est-ce quelque chose qui est normal ou pas ?
	Oui cela est possible.
Modifier le code pour corriger le problème.
On veut que plusieurs philosophes puissent manger en même temps.
Quel est le problème du code ci-dessus ? Dans quelle(s) condition(s) se produit-il ?
Le problème de ce code vient du fait que si le dernier philosophe mange en premier, le deuxième philosophe qui mangera 
aura un index < au premier philosophe, or les locks ont besoin d'être effectués dans un ordre croissant d'index. Il faut donc échanger les philosophes 
 */
public class PhilosopherDinner {
  private final Object[] forks;
 
  public PhilosopherDinner(int forkCount) {
    Object[] forks = new Object[forkCount];
    Arrays.setAll(forks, i -> new Object());
    this.forks = forks;
    
  }

  public void eat(int index) throws InterruptedException {
    var fork1 = forks[index];
    var fork2 = forks[(index + 1) % forks.length];
    if(index==this.forks.length-1) {
      var tmp=fork1;
      fork1=fork2;
      fork2=tmp;
    }
    synchronized (fork1) {
        synchronized (fork2) {
          System.out.println("philosopher " + index + " eat");
        }
      }
      
   
    }
  

  public static void main(String[] args) {
    var dinner = new PhilosopherDinner(5);
    IntStream.range(0, 5).forEach(i -> {
      new Thread(() -> {
        for (;;) {
          try {
            dinner.eat(i);
          } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            
          }
        }
      }).start();
    });
  }
}
