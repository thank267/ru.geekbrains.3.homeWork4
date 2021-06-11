import java.util.concurrent.atomic.AtomicBoolean;

public class App {

    public static void main(String[] args) {

        Object lockA = new Object();
        Object lockB = new Object();
        Object lockC = new Object();

        AtomicBoolean first = new AtomicBoolean(true);

        Thread A = new Thread(()->{

            for (int i = 0; i < 5; i++) {

                synchronized(lockA) {
                    try {

                        if (!first.compareAndExchange(true,false)) lockA.wait();
                        System.out.println("A");
                        synchronized(lockB) {
                            lockB.notifyAll();
                        }

                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }

                }

            }

        });

        Thread B = new Thread(()->{

            for (int i = 0; i < 5; i++) {

                synchronized(lockB) {

                    try {
                        lockB.wait();
                        System.out.println("B");
                        synchronized(lockC) {
                            lockC.notifyAll();
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

            }

        });

        Thread C = new Thread(()->{

            for (int i = 0; i < 5; i++) {

                synchronized(lockC) {

                    try {
                        lockC.wait();
                        System.out.println("C");
                        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++");
                        synchronized(lockA) {
                            lockA.notifyAll();
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

            }

        });

        A.start();
        B.start();
        C.start();

    }

}
