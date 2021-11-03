package fr.upem.concurrence.td03;
import java.util.ArrayList;
import java.util.stream.IntStream;
/*
 *  Dans une classe HelloListFixed, corriger le problème 
 *  (pensez à vérifier que la correction que vous avez effectuée exécute bien les threads
 *   en parallèle et non pas les uns après les autres).

Rappeler quelles doivent être les propriétés de l'objet qui sert de lock.
L'objet qui doit servir de lock doit être private, final, effectivement final, 
ne pas avoir de getter ou de setter ou utiliser un type primitif (int,char,long) ou encore être null
 */
public class HelloListFixed {
	
  public static void main(String[] args) throws InterruptedException {
    final var locker=new Object();
	var nbThreads = 4;
    var threads = new Thread[nbThreads]; 
    
    var list = new ArrayList<Integer>();

    IntStream.range(0, nbThreads).forEach(j -> {
      Runnable runnable = () -> {
        for (var i = 0; i < 5000; i++) {
          synchronized(locker) {
        	list.add(i);
          }
        }
      };

      threads[j] = new Thread(runnable);
      threads[j].start();
    });

    for (Thread thread : threads) {
      thread.join();
    }

    System.out.println("taille de la liste:" + list.size());
  }
}