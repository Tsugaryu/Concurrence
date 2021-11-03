import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

public class PermitSyncSync<V> {
	private int supplierUsed;
//  private final ReentrantLock lock = new ReentrantLock();
	// private final Condition condition = lock.newCondition();
	private final Object lock = new Object();
	private final int permits;

	public boolean inSafe() {
		synchronized (lock) {
			return supplierUsed < permits;
		}
	}

	public PermitSyncSync(int permits) {
		this.permits = permits;
	}

	public V safe(Supplier<? extends V> supplier) throws InterruptedException {
		// premier block synchronizé, t ds safe tu block
		synchronized (lock) {
			supplierUsed += 1;
		}

		var supply = supplier.get();
		synchronized (lock) {
			supplierUsed -= 1;
			lock.notifyAll();
			return supply;
		}

	}
}