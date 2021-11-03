import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ThreadCounter {

	public static void main(String[] args) throws IOException {
		Thread[] t = new Thread[4];
		for (int i = 0; i < t.length; i++) {
			t[i] = new Thread(() -> {
				int hisCounter = 0;
				while (true) {
					hisCounter += 1;
					System.out.println(Thread.currentThread().getName() + " " + hisCounter);
					try {
						Thread.sleep(1_000);
					} catch (InterruptedException e) {
						System.out.println(Thread.currentThread().getName() +" is currently stop ");
						return;
					}
				}
			}, "Thread N" + i);
			t[i].setDaemon(true);
			t[i].start();
		}
		// lecture utilisateur
		while (true) {
			System.out.println("enter a thread id:");
			try (var input = new InputStreamReader(System.in); var reader = new BufferedReader(input)) {
				String line;
				while ((line = reader.readLine()) != null) {
					var threadId = Integer.parseInt(line);
					if(threadId>=t.length)continue;
					t[threadId].interrupt();
				}
			}
		}

	}
}
