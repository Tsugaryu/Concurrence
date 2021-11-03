package fr.upem.concurrence.td03;

/*
La classe n'est pas Thread safe car il n'y a aucune notion de verrou 
dans le code ce qui fait que ce code
 peut etre modifié a n'importe quel moment par un processus;
 */
public class HonorBoard {
  private String firstName;
  private String lastName;
  private final Object lock=new Object();
  public void set(String firstName, String lastName) {
   synchronized(lock) {
	this.firstName = firstName;
    this.lastName = lastName;
    }
  }
  public String getFirstName() {
	    return firstName;
	  }
	  public String getLastName() {
	    return lastName;
	  }

  @Override
  public String toString() {
	  synchronized(lock) {
    return firstName + ' ' + lastName;
  }
  }

  public static void main(String[] args) {
    HonorBoard board = new HonorBoard();
    new Thread(() -> {
      for(;;) {
        board.set("John", "Doe");
      }
    }).start();

    new Thread(() -> {
      for(;;) {
        board.set("Jane", "Odd");
      }
    }).start();

    new Thread(() -> {
      for(;;) {
        System.out.println(board);
      }
    }).start();
  }
}
