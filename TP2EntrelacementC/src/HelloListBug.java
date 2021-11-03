import java.util.ArrayList;
import java.util.stream.IntStream;

public class HelloListBug {

	public HelloListBug() {
		/*
		 * La méthode add ajoute dans un tableau d'élement en fonction de la place où se situe l'entier.
		 * Donc si l'entier est répété par l'un des processeurs, il écrasera l'entier placé par l'un d'entre eux.
		 */
		/*
		 * Une fois la taille retiré, Il peut ariver que le tableau génère des ArrayIndexOutOfBoundsException puis qu'il continue son activité car seulement l'un des processus est fermé. 
		 * Ce qui veux dire que ce processus est arrivé à un compteur i supérieur à celui des autres processus actifs.
		 * */

	}
	public static void main(String[] args) throws InterruptedException {
		  var nbThreads = 4;
		  var threads = new Thread[nbThreads];
		  var list = new ArrayList<Integer>();
		  IntStream.range(0, nbThreads).forEach(j -> {
		    Runnable runnable = () -> {
		      for (var i = 0; i < 5000; i++) {
		    	  list.add(i);
		      }
		    };

		    threads[j] = new Thread(runnable);
		    threads[j].start();
		  });

		  for (var thread : threads) {
		    thread.join(); 
		  }

		  System.out.println("le programme est fini, listsize ="+list.size());
		}
}
