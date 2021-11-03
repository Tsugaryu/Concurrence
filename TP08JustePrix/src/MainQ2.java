
public class MainQ2 {
	private static int slow() {
		var result = 1;
		for (var i = 0; i < 1_000_000; i++) {
			result += (result * 7) % 513;
			if (Thread.interrupted()) {
				Thread.currentThread().interrupt();
				continue;
			}
		}
		return result;
	}

	public static void main(String[] args) throws InterruptedException {
	    var t = new Thread(() -> {
	        var forNothing = 0;
	        while (!Thread.interrupted()) {
	          forNothing += slow();
	        }
	        System.out.println("VI ENDO "+forNothing);
	    });
	    t.start();
	    Thread.sleep(1_000);
	    t.interrupt();
	  }
}
