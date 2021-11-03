package fr.uge.tp10;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Logger;

public class FixedPrestartedConcurrentLongSumServerWithTimeout {

	private static final Logger logger = Logger
			.getLogger(FixedPrestartedConcurrentLongSumServerWithTimeout.class.getName());
	private static final int BUFFER_SIZE = 1024;
	private final ServerSocketChannel serverSocketChannel;
	private final int nbThreads;
	private final int timeOut;
	private final static int MAX_THREAD = 10;
	private final ThreadData[] fields;
	private int shutdowned = 0;
	private int connected = 0;

	public FixedPrestartedConcurrentLongSumServerWithTimeout(int port, int nbThreads, int timeout) throws IOException {
		serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.bind(new InetSocketAddress(port));
		logger.info(this.getClass().getName() + " starts on port " + port);
		assert(nbThreads<MAX_THREAD);
		this.nbThreads = nbThreads;
		this.timeOut = timeout;
		fields = new ThreadData[nbThreads];
		for (int i = 0; i < nbThreads; i++) {
			fields[i] = new ThreadData();
		}
	}

	/**
	 * Iterative server main loop
	 *
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void console() {
		var scan = new Scanner(System.in);
		while (!Thread.interrupted()) {
			var line = scan.nextLine();
			switch (line) {
			case "SHUTDOWN":
				shutdowned = 1;
				break;
			case "SHUTDOWNNOW":
				shutdowned = 2;
				break;
			case "INFO":
				synchronized (logger) {
					System.out.println("Number of client connected = " + connected);
				}
				break;
			}
		}
		scan.close();
	}

	public void launch() throws IOException, InterruptedException {
		logger.info("Server Started");
		var thread = new Thread(() -> {

			while (!Thread.interrupted()) {
				try {
					Thread.sleep(timeOut);
				} catch (InterruptedException e1) {
					logger.severe("SEVERE - server dysfunctionment stop the server");
					return;
				}
				for (var i = 0; i < fields.length; i++) {
					try {
						if(fields[i]!=null) {
							fields[i].closeIfInactive(this.timeOut);	
						}
						
					} catch (IOException e) {
						throw new UncheckedIOException(e);
					}
				}
				/*
				 * Arrays.stream(fields).forEach(td -> { try { td.closeIfInactive(this.timeOut);
				 * } catch (IOException e) { throw new UncheckedIOException(e); } });
				 */

			}

		});
		thread.start();
		for (var i = 0; i < nbThreads; i++) {
			new Thread(() -> {
				try {
					while (!Thread.interrupted()) {
						if (shutdowned == 2) {
							for (var t : fields) {
								t.close();
							}
						} else if (shutdowned == 0) {
							var client = serverSocketChannel.accept();
							if(fields[Integer.parseInt(Thread.currentThread().getName())]!=null) {
								fields[Integer.parseInt(Thread.currentThread().getName())].setSocketChannel(client);	
							}
							
							try {
								synchronized (logger) {
									connected += 1;
								}
								serve(client);
							} catch (IOException e) {
								logger.info("Connection terminated");
								synchronized (logger) {
									connected -= 1;
								}

							} catch (InterruptedException e) {
								logger.info("server interrupted ");
							} finally {
								silentlyClose(client);
							}
						}
					}
				} catch (AsynchronousCloseException e) {
					logger.info("Worker thread stopped");
				} catch (IOException e) {
					logger.severe("Worker thread as stopped");
				}
			}, "" + i).start();
		}
		// lance la console
		console();

	}

	/**
	 * Treat the connection sc applying the protocole All IOException are thrown
	 *
	 * @param sc
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void serve(SocketChannel sc) throws IOException, InterruptedException {
		var bb = ByteBuffer.allocate(BUFFER_SIZE).flip();
		while (!Thread.interrupted()) {
			if (!ensure(sc, bb, Integer.BYTES)) {
				logger.info("Connection closed");
				return;
			}
			if(fields[Integer.parseInt(Thread.currentThread().getName())]!=null){
				fields[Integer.parseInt(Thread.currentThread().getName())].tick();	
			}
			
			var sum = 0L;
			var nb_long = bb.getInt();
			if (nb_long <= 0) {
				return;
			}
			sc.write(ByteBuffer.allocate(BUFFER_SIZE).putLong(sum).flip());
			if(fields[Integer.parseInt(Thread.currentThread().getName())]!=null){
				fields[Integer.parseInt(Thread.currentThread().getName())].tick();	
			}
		}
	}

	static boolean ensure(SocketChannel sc, ByteBuffer bb, int size) throws IOException {
		assert (size <= bb.capacity());
		while (bb.remaining() < size) {
			bb.compact();
			try {
				if (sc.read(bb) == -1) {
					return false;
				}
			} finally {
				bb.flip();
			}
		}
		return true;
	}

	/**
	 * Close a SocketChannel while ignoring IOExecption
	 *
	 * @param sc
	 */

	private void silentlyClose(SocketChannel sc) {
		if (sc != null) {
			try {
				sc.close();
			} catch (IOException e) {
				// Do nothing
			}
		}
	}

	static boolean readFully(SocketChannel sc, ByteBuffer bb) throws IOException {
		while (bb.hasRemaining()) {
			if (sc.read(bb) == -1) {
				logger.info("Input stream closed");
				return false;
			}
		}
		return true;
	}

	private static void usage() {
		System.out.println("java  FixedPrestartedConcurrentLongSumServerWithTimeout port nbTHread timeout");
	}

	public static void main(String[] args) throws NumberFormatException, IOException {
		if (args.length < 3) {
			usage();
		}
		var server = new FixedPrestartedConcurrentLongSumServerWithTimeout(Integer.parseInt(args[0]),
				Integer.parseInt(args[1]), Integer.parseInt(args[2]));
		try {
			server.launch();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}