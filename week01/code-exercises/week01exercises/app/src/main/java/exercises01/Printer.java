package exercises01;

class Printer {
  public void print() {
    System.out.print("-");
    try {
      Thread.sleep(50);
    } catch (InterruptedException exn) {
    }
    System.out.print("|");
  }

  public static void main(String[] args) throws InterruptedException {
    Printer printer = new Printer();
    Thread thread1 = new Thread(() -> {
      while (true) {
        printer.print();
      }
    });
    Thread thread2 = new Thread(() -> {
      while (true) {
        printer.print();
      }
    });
    thread1.start();
    thread2.start();
    thread1.join();
    thread2.join();
  }
}