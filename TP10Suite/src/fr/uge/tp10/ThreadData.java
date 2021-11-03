package fr.uge.tp10;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Objects;

public class ThreadData {
	private volatile SocketChannel channel;
	private volatile long timePlaced;
	private final Object lock = new Object();

	public ThreadData() {
		timePlaced = 0;
		channel = null;
	}

	void setSocketChannel(SocketChannel channel) {
		channel = Objects.requireNonNull(channel);
		timePlaced = System.currentTimeMillis();

	}

	void tick() {
		timePlaced = System.currentTimeMillis();
	}

	boolean closeIfInactive(int timeout) throws IOException {
		synchronized (lock) {
			if (channel != null) {
				var now = Long.valueOf(System.currentTimeMillis() - timePlaced).intValue();
				if (now > timeout) {
					close();
					return false;
				}
				return true;
			}
			return false;
		}
	}

	void close() throws IOException {
		if (channel != null) {
			channel.close();
		}
	}
}
