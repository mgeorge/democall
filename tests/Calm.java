
import network.MessageGenerator;
import network.RequestSender;

/**
 * Simulates an entire lab canceling their request at the same time.
 *
 * Tests threading and queue overflow.
 *
 */
public class Calm {

   private Calm() {
   }   
   
   public static void main(final String[] args) {

      final RequestSender requestSender = new RequestSender("127.0.0.1");
      final MessageGenerator generator = new MessageGenerator();


      // using 2 to 30 since those are numbers that all lab layouts have
      for (int i = 2; i <= 31; i++) {

         final int x = i;

         new Thread(new Runnable() {

            public void run() {
               requestSender.sendRequest(generator.cancelRequest(String.valueOf(x)));
            }
         }).start();

      }

   }

}
