package fr.upem.concurrence.td04;

import java.util.HashMap;

public class Vote {
	private int vote;
	private final HashMap<String, Integer> map;

	public Vote(int nombre) {
		this.vote = nombre;
		this.map = new HashMap<String, Integer>();
		// TODO Auto-generated constructor stub
	}

	private String computeWinner() {
		int currentWinnerScore = -1;
		String currentWinner = null;
		for (var e : map.entrySet()) {
			if (e.getValue() > currentWinnerScore
					|| (e.getValue() == currentWinnerScore && e.getKey().compareTo(currentWinner) < 0)) {
				currentWinner = e.getKey();
				currentWinnerScore = e.getValue();
			}
		}
		return currentWinner;
	}

	public String vote(String vote) throws InterruptedException {
		var value = 1;
		synchronized (map) {
			if (map.containsKey(vote)) {
				value = this.map.get(vote);
				value += 1;

			}
		}
		synchronized (map) {
			this.map.put(vote, value);
			this.vote -= 1;
		}
		synchronized (map) {
			while (this.vote > 0) {
				map.wait();
			}
			map.notifyAll();
			return computeWinner();
		}
		
	}

}
