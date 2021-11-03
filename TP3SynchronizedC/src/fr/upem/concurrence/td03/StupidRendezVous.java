package fr.upem.concurrence.td03;

import java.util.Objects;

public class StupidRendezVous<V> {
  private V value;

  public void set(V value) {
    Objects.requireNonNull(value);
    this.value = value;
  }

  public V get() throws InterruptedException {
    while (value == null) {
   //   Thread.sleep(1); // then comment this line !
    }
    return value;
  }
}
/*
 * Que se passe-t-il lorsqu'on exécute ce code ? 
 *  Un nombre premier est recherché parmi les 4 threads. 
 *  Une fois celui-ci trouvé, les autres processus sont stoppés en rentrant dans get car la value retenue a cessé d'être null et l'on sort du programme.
 * 
 */

/*
 * Commenter l'instruction Thread.sleep(1) dans la méthode get puis ré-exécuter le code. Que se passe-t-il ? Expliquer où est le bug ? 
 * Chacun des processus recherche un nombre premier, le remplace dans value.
 * de plus la value premiere ne s'affiche pas. Cela est du au fait que l'on a retiré le sleep. Le JIT a retiré la boucle while ce qui empêche les autres thread de tomber dans un éternel sommeil.
 * 
 *
 * */
 