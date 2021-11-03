
public class MainQ3 {
	private static int slow(boolean interrupt) {
		if(interrupt) {
			return 0;
		}
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
	    Thread t = new Thread(() -> {
	        var forNothing = 0;
	        while(true){
	            try {
					Thread.sleep(1_000);
				} catch (InterruptedException e) {
					forNothing += slow(true);
					System.out.println("ZA WARUDO "+forNothing);

					return;
				} 
	            forNothing += slow(Thread.interrupted());
	        }
	    });
	    t.start();
	    Thread.sleep(1_000);
	    t.interrupt();
	  }
}
