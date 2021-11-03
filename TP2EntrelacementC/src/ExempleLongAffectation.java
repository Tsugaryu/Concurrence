class ExampleLongAffectation {
  long l = -1L;

  public static void main(String[] args) {
    ExampleLongAffectation e = new ExampleLongAffectation();
    new Thread(() -> {
      System.out.println("l = " + e.l);
    }).start();
    e.l = 0;
  }
}
/*
 *Il est possible d'avoir d'affiché -1 ou 0 pour des raisons semblabales 
 *à 	ExempleReordering, le JIT peut par exemple effectuer l'affectation avant l'affichage
 * 
 * */
 