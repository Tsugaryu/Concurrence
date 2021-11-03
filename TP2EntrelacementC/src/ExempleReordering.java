class ExempleReordering {
  int a = 0;
  int b = 0;

  public static void main(String[] args) throws InterruptedException {
    ExempleReordering e = new ExempleReordering();
    new Thread(() -> { System.out.println("a = " + e.getA() + "  b = " + e.getB()); }).start();
   
    e.setA(1);
   
    e.setB(2);
    Thread.sleep(1_000);
  }

public int getA() {
	return a;
}

public void setA(int a) {
	this.a = a;
}

public int getB() {
	return b;
}

public void setB(int b) {
	this.b = b;
}
}
/*
 *
 * On obtient en général 1 et 2 
 * les autres valeurs possibles 
 * peuvent être en fonction du JIT utilisé :
 * 0 et 0, 1 et 0, 0 et 2 
 * */
 