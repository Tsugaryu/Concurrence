package fr.umlv.conc;

public class ExchangerWheelUpgraded<T> {
	enum State {
		EMPTY, FIRST, FULL
	};

	private T value;
	private T valueE;
	private final Object lock = new Object();
	private State state;

	public ExchangerWheelUpgraded() {
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
				return this.valueE;
			}

		case FIRST:
			synchronized(lock) {
				valueE = value;
				this.state=State.FULL;
				notifyAll();
				return this.value;
			}
			
			
		default:
			throw new IllegalStateException();
			
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
}
