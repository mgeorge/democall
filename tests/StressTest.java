
import java.util.ArrayList;
import java.util.List;
import network.MessageGenerator;
import network.RequestSender;

/**
 * Simulates everyone toggling their help status at the same time.
 * 
 *
 * Tests concurrency, and ability to handle load.
 *
 */
public class StressTest {

   public static void main(String[] args) throws InterruptedException {

      final RequestSender requestSender = new RequestSender("127.0.0.1");
      final MessageGenerator generator = new MessageGenerator();

      List<Thread> threads = new ArrayList<Thread>();

      for (int i = 0; i < 100; i++) {

         // using 2 to 30 since those are numbers that all lab layouts have
         for (int j = 2; j <= 31; j++) {

            final int x = j;

            Thread t = new Thread(new Runnable() {

               public void run() {
                  requestSender.sendRequest(generator.requestHelp(String.valueOf(x)));
               }
            });

            t.start();
            threads.add(t);

         }

         for (Thread thread : threads) {
            thread.join();
         }

         // give server a chance to sort things out
         Thread.sleep(100);

         for (int j = 2; j <= 31; j++) {

            final int x = j;

            new Thread(new Runnable() {

               public void run() {
                  requestSender.sendRequest(generator.cancelRequest(String.valueOf(x)));
               }
            }).start();
         }
         
         // give server a chance to sort things out
         Thread.sleep(100);
      }
   }
}
