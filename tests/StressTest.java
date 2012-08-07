
import network.MessageGenerator;
import network.RequestSender;

/**
 * Simulates everyone toggling their help status at the same time.
 *
 * Tests concurrency, and ability to handle load.
 *
 */
public class StressTest {

   public static void main(String[] args) throws InterruptedException {

      final RequestSender requestSender = new RequestSender("127.0.0.1");
      final MessageGenerator generator = new MessageGenerator();

      final int sleepCount = 5;

      // using 2 to 30 since those are numbers that all lab layouts have
      for (int i = 2; i <= 30; i++) {

         final int x = i;

         new Thread(new Runnable() {

            public void run() {
               requestSender.sendRequest(generator.requestHelp(String.valueOf(x)));
            }
         }).start();
         
         Thread.sleep(sleepCount);

         new Thread(new Runnable() {

            public void run() {
               requestSender.sendRequest(generator.cancelRequest(String.valueOf(x)));
            }
         }).start();

         Thread.sleep(sleepCount);
      }

   }
}
