
public class Counter {
    int value;
    public int count() {
      value+=1;
      return value-1;
    }
    public static void main(String[] args) {
    	int nbrThread=2;
    	var count=new Counter();
    	var synchronize=new Sync<Integer>();
    	for (var i = 0; i < nbrThread; i++) {
    	      var j = i;
    	      var thread = new Thread(() -> {
    	    	  try {
    	    		 while (true) {
    	    			 ;
    	    			 System.out.println(synchronize.safe(count::count));
					}
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    	        
    	      }, "ThreadNumber00" + j);
    	      thread.setDaemon(true);
    	      thread.start();
    	      // ---------------ACTION DU MAIN----------
    	     

    	    }
	}
}
