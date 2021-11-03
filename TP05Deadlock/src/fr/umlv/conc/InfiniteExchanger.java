package fr.umlv.conc;

import java.util.stream.IntStream;


public class InfiniteExchanger<T> {
	enum State {
		EMPTY, FIRST, FULL
	};

	private T value;
	private T valueE;
	private final Object lock = new Object();
	private State state;

	public InfiniteExchanger() {
		this.value = null;
		this.valueE = null;
		this.state = State.EMPTY;
		// lochB = false;
		// lacB = false;
	}

	public T exchange(T value) throws InterruptedException {
		switch (state) {
		case EMPTY:
			synchronized (lock) {
				this.value = value;
				state = State.FIRST;
				while (state != State.FULL) {
					lock.wait();
				}
				this.state=State.EMPTY;
				return this.valueE;
			}

		case FIRST:
			synchronized(lock) {
				valueE = value;
				this.state=State.EMPTY;
				lock.notifyAll();
				return this.value;
			}
			
			
		default:
			throw new IllegalStateException();
			
		}
	}
	
	
	 public static void main(String[] args) throws InterruptedException {
		    var exchanger = new InfiniteExchanger<String>();
		    IntStream.range(0, 10).forEach(i -> {
		      new Thread(() -> {
		        try {
		          System.out.println("thread A " + i + " received from " + exchanger.exchange("thread B " + i));
		        } catch (InterruptedException e) {
		          throw new AssertionError(e);
		        }
		      }).start();
		    });
		  }

}
