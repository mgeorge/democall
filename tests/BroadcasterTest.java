
import discovery.computername.ComputerNameResolver;
import discovery.computername.OtagoComputerNameResolver;
import discovery.server.ServiceLocator;

/*
 * A stress test for the BroadcastResponder.
 * 
 * @author mark
 */
public class BroadcasterTest {

   private BroadcasterTest() {
   }
   
   public static void main(final String[] args) {
      
      final String name = "SBEASTCAL1-30";      
      
      final ComputerNameResolver nameResolver = new OtagoComputerNameResolver(name, "COMPUTERNAME");
      nameResolver.resolve();
      
      String lab = nameResolver.getLabName();

      for(int i=0; i<500; i++) {
         final String serverIp =  new ServiceLocator().locateServer(lab);
         System.out.println(i + " " + serverIp);
      }
      
   }
   
}
