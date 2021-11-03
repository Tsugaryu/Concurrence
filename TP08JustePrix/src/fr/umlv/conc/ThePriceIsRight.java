package fr.umlv.conc;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/*
 * Chaque candidat propose un prix et le candidat qui indique le prix le plus proche du prix réel d'un article gagne cet article. => absolute(prixHide-prix asked0) < absolute(prixHide-prix asked1)
 *ans cet exercice, vous devez implanter la même mécanique de jeu avec un thread simulant un candidat. 
 */
public class ThePriceIsRight {
	private final int numberOfCandidat;
	private final int realPrice;
	private int size;
	private boolean noWinner = false;
	private final LinkedList<Integer> candidatVotePrice;

	public ThePriceIsRight(int price, int numberCandidat) {
		if (price < 0 || numberCandidat <= 0) {
			throw new IllegalArgumentException();
		}
		this.realPrice = price;
		this.numberOfCandidat = numberCandidat;
		this.candidatVotePrice = new LinkedList<>();
		this.size = 0;
	}
	private boolean isInterrupted(int id) {
		synchronized(this.candidatVotePrice) {
			this.candidatVotePrice.remove(id);
			if(size==1) {
				noWinner=true;
			}
			else {
				this.candidatVotePrice.notifyAll();
				this.size=this.numberOfCandidat;
			}
			return false;
		}
		
	}
	private void add(int value) {
		synchronized(this.candidatVotePrice) {
			this.candidatVotePrice.add(value);
			this.size++;
		}
	}
	public boolean propose( int numberTold) {
		/*
		 * Si plus de threads que le nombre de participants indiqué à la construction
		 * exécutent la méthode propose, la proposition de prix des threads
		 * supplémentaires ne sera pas prise en compte et la méthode propose renvoie
		 * false.
		 */
			if (this.numberOfCandidat < this.candidatVotePrice.size()) {
				return false;
			}
			this.add(numberTold);
			//on ajoute la proposition du thread
			
			/*
			 * Si un thread qui a déjà proposé un prix est interrompu, son prix est retiré
			 * des prix pris en compte (comme s'il n'avait pas joué), donc la méthode
			 * propose renvoie false. De plus, le juste prix est alors immédiatement calculé
			 * sur les threads restants (les threads sont donc débloqués même sans avoir
			 * atteint le nombre de participants requis).
			 */
			if(Thread.interrupted()) {
				/*
				 * Dans le cas où il y avait un seul participant et qu'il est interrompu, aucun
				 * thread n'a le juste prix et la méthode propose doit renvoyer false pour tout
				 * le monde.
				 */
				return isInterrupted(numberTold);
			}
			/*
			 * Cette méthode bloque tant que tous les candidats n'ont pas soumis leur prix
			 */
			while(this.candidatVotePrice.size()<this.numberOfCandidat) {
				try {
					this.candidatVotePrice.wait();
				} catch (InterruptedException e) {
					return isInterrupted(numberTold);
				}
			}
			/*
			 * puis elle renvoie true au thread qui a proposé le prix le plus proche et
			 * false à touts les autres threads. Si deux participants proposent le même prix
			 * et que c'est le prix le plus proche du prix réel, alors le premier
			 * participant (le premier thread ayant proposé le prix) est le gagnant.
			 */
			this.candidatVotePrice.forEach((value) -> {
				value=(distance(value));
			});
			var min = this.candidatVotePrice.stream().min(Integer::compare)
					.stream().findFirst();
			synchronized(this.candidatVotePrice) {
				if(!noWinner && min.get()==numberTold) {
					noWinner=true;
					return true;
				}		
			}
		
			return false;
		
		
	}
	private int distance(int price) {
		  return Math.abs(price - realPrice);
		}

}
