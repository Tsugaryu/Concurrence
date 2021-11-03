
public class MainQ1 {


public static void main(String[] args) throws InterruptedException {
    var t = new Thread(() -> {
    	try{
    	 while (true) {
     	  	 Thread.sleep(1_000);
  	     }
    	}catch(InterruptedException e){
    		System.out.println("ZI ENDO");
    		return;
    	}
 
    });
    t.start();
    t.interrupt();
    Thread.sleep(1_000);
    t.interrupt();
}

}
