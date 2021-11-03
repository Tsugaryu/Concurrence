public class Counter {

        private int value;

        public void add10000() {
            for (int i = 0; i < 10_000; i++) {
                value++;
            }
        }

        public static void main(String[] args) throws InterruptedException {
            Counter counter = new Counter();
            Runnable runnable = () -> {counter.add10000();};
            Thread t1 = new Thread(runnable);
            Thread t2 = new Thread(runnable);
            t1.start();
            t2.start();
            t1.join();
            t2.join();
            System.out.println(counter.value);
        }
    }
/**
 * Exo 1 ;
 * Premier résultat : 13383
 * 13250
 * 10081
 * 13432
 * 19317
 * 
 * On peut voir que les Thread écrivent sur la même RAM.
 * Sachant que chaque thread dispose d'un cache propre, et que ces derniers se schedule
 * et déschédule,lorsque t1 accède à la zone mémoire de i=0 à 3000 il réécrie 3000 dans
 * la variable et conserve 3000 comme valeur.
 * Puis t2 va de 0 à 5000; Il récupère  donc 3000 et non 0 et écrie donc dans la variable 
 * la valeur 8000 et conserve cette valeur dans son cache. Puis t1 reprend son activité, 
 * et va de 3000 à 10000 et place donc la valeur 10000 dans la variable.
 * Puis t2, poursuit sa boucle de 5000 à 10000. IL finit donc par écrire la valeur 13000.
 * Pour conclure, puisque chaque processus va de  0 à 10 000, cette valeur sera la borne minimum
 * puisque chaque processus exécute la fonction qui lui est demandé.
 * 
 * 
 * */
 