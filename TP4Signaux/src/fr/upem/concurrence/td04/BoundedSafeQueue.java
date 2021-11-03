package fr.upem.concurrence.td04;

import java.util.ArrayDeque;

public class BoundedSafeQueue<T> {
	private final ArrayDeque<T> lst;
	private final int max;

	
	public BoundedSafeQueue(int size) {
		this.lst=new ArrayDeque<T>(size);
		this.max=size;
	}
	
	
	public void put (T value) throws InterruptedException {
		synchronized(lst) {
			while (this.lst.size() >= this.max) {
				lst.wait();
			}
			this.lst.add(value);
			lst.notifyAll();
		}
	}
	
	public T take() throws InterruptedException {
		synchronized(lst) {
			while (this.lst.size()==0) {
				lst.wait();
			}
			lst.notifyAll();
			return this.lst.poll();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		var n=100;
		var size=3;
		BoundedSafeQueue<String> boundedQueue=new BoundedSafeQueue<>(size);
		for (var i=0;i<n;i++) {
			new Thread( () -> {
				
				while(true) {
					
					try {
						boundedQueue.put(Thread.currentThread().getName());
					} catch (InterruptedException e1) {
						throw new AssertionError();
					}
					
					try {
						Thread.sleep(2_000L);
					} catch (InterruptedException e) {
						throw new AssertionError();
					}
					
				}
			},"Thread 00"+i).start();
		}
		
		while(true) {
			System.out.println(boundedQueue.take());
		}
	}

}
