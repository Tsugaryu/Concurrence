/*
 * Avant de l'exécuter, essayer de comprendre 
 * quel est le comportement espéré. Où se trouve la data-race ? 
 * Le comportement espéré est que le processus s'arretera dans environ 5 seconde
 * La data race se trouve sur st.stop
 *
 * */
public class StopThreadBug implements Runnable {
  private boolean stop = false;

  public void stop() {
    stop = true;
  }

  @Override
  public void run() {
    while (!stop) {
      
    }
    System.out.print("Done");
  }

  public static void main(String[] args) throws InterruptedException {
	Thread.sleep(3_000);
	System.out.println("GO");
	Thread.sleep(1_000);
	StopThreadBug st = new StopThreadBug();
    new Thread(st::run).start();
    Thread.sleep(5_000);
    System.out.println("Trying to tell the thread to stop");
    st.stop();
  }
}
/*
 * 5 57
 * 6 01
 * 5 94
 * -On observe que le processus s'arrete a peu près au niveau des 5 secondes
 * (la vérification du temps ayant été faite à la main, on prend en compte le temps de réaction du testeur.)
 * -Done ne s'affiche pas.
 * -Le code n'ayant pas d'activité, pendant un moment, le JIT doit probablement "optimiser" le code de manière à ce que l'on tourne dans une boucle infini.
 * -Si le code ne s'arrête pas après  l'affichage de trying to tell to stop, il ne s'arretera pas.
 * 
 */