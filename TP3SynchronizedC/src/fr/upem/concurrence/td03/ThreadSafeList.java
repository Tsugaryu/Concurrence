package fr.upem.concurrence.td03;
import java.util.ArrayList;
import java.util.stream.Collectors;

/*
 * ui implémente une liste thread-safe permettant d'ajouter d'un élément et 
 * de récupérer la taille de la liste. Utiliser cette classe pour écrire une classe HelloListFixedBetter.

Au lieu d'afficher la taille de la liste, on veut afficher le contenu de la liste.
 Modifier la classe ThreadSafeList pour permettre cette évolution.
 */
public class ThreadSafeList<T> {
	private ArrayList<T> listShield;
	private final Object lock=new Object();
	public ThreadSafeList(){
		this.listShield=new ArrayList<T>();
		
	}
	public int size(){
		synchronized (lock) {
			return this.listShield.size();	
		}
		
	}
	public void add(T element) {
		synchronized(lock) {
			this.listShield.add(element);	
		}
	}
	@Override
	public String toString() {
		StringBuilder build= new StringBuilder();
		synchronized(lock) {
		build.append(this.listShield.stream()
									.map(element -> element.toString())
									.collect(Collectors.joining(System.lineSeparator()))
					);
		}
		return build.toString();
	}
}
