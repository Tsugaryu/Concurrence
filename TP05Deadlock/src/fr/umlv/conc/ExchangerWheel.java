package fr.umlv.conc;
/*
 * 'idée est qu'un premier thread va envoyer une valeur
 *  à l'Exchanger en utilisant la méthode exchange,
 *   celui-ci va bloquer le thread qui a fait appel 
 *   à la méthode exchange et attendre
 *    (on suppose qu'il n'y a que 2 threads et que chacun appelle exchange exactement une fois). Lorsque un second thread fait lui aussi un appel 
 *    à la méthode exchange avec une seconde valeur, l'appel retourne la première valeur envoyée et 
 *    le premier thread est dé-bloqué de son appel à exchange en retournant la seconde valeur (attention, les valeurs peuvent être nulles).


 */
public class ExchangerWheel<T> {
  private T value;
  private boolean lochB;
  private boolean lacB;
  private final Object loch = new Object();
  private final Object lac = new Object();

  public ExchangerWheel() {
    this.value = null;
    lochB = false;
    lacB = false;
  }

  public T exchange(T value) throws InterruptedException {
    if (lochB == false) {
      synchronized (loch) {
        lochB = true;
        this.value = value;
        while (lacB == false) {
          loch.wait();
        }
        return this.value;
      }
    } else {
      synchronized (lac) {
        lacB = true;
        var valFromFirst = this.value;
        this.value = value;
        lac.notifyAll();
        lochB = false;
        lacB = false;
        return valFromFirst;

      }
    }
  }
  public static void main(String[] args) throws InterruptedException {
    var exchanger = new ExchangerWheel<String>();
    new Thread(() -> {
      try {
        System.out.println("thread 1 " + exchanger.exchange("foo1"));
      } catch (InterruptedException e) {
        throw new AssertionError(e);
      }
    }).start();
    System.out.println("main " + exchanger.exchange(null));
  }

  /*
   * Comment faire pour distinguer le premier et le second appel à la méthode
   * exchange ? On les distingue par l'utilisation de deux booleens Écrire le code
   * de la classe Exchanger.
   * 
   * Modifier votre méthode d'échange pour encoder l'état de l'Exchanger avec un
   * enum.
   * 
   * L'Exchanger est-il utilisable plus d'une fois ?
   * Actuellement, il ne l'est âs.
   * Autrement dit, on veut pouvoir exécuter un code comme celui-ci:
   * ExampleExchanger2.java
   * 
   * 
   */
}
