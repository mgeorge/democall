
import discovery.computername.ComputerNameResolver;
import discovery.computername.OtagoComputerNameResolver;
import discovery.server.ServiceLocator;

/*
 * A stress test for the BroadcastResponder.
 * 
 * @author mark
 */
public final class BroadcasterTest {

   private BroadcasterTest() {
   }
   
   public static void main(final String[] args) {
      
      final String name = "SBEASTCAL1-30";      
      
      final ComputerNameResolver nameResolver = new OtagoComputerNameResolver(name, "COMPUTERNAME");
      nameResolver.resolve();
      
      final String lab = nameResolver.getLabName();

      final ServiceLocator locator =  new ServiceLocator();
      
      for(int i=0; i<500; i++) {
         final String serverIp = locator.locateServer(lab);
         System.out.println(i + " " + serverIp);
      }
      
   }
   
}
