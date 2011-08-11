
import network.MessageGenerator;
import network.RequestSender;


/**
 *  Simulates everyone hitting the help button at the same time.
 */
public class Panic {

   public static void main(String[] args) {

       RequestSender requestSender = new RequestSender("127.0.0.1");
       MessageGenerator generator = new MessageGenerator();

       for (int i = 1; i <= 40; i++) {
         requestSender.sendRequest(generator.requestHelp(String.valueOf(i)));
      }


   }

}
