package discovery.computername;

/*
 * @author Mark
 */
public interface ComputerNameResolver {

   String getComputerName();

   String getLabName();

   String getMachineId();
   
   /**
    * Parses the computer name into it lab and machine ID parts.
    *
    * @throws InvalidComputerNameException if the name can not be resolved or
    * is not in the expected format.
    */
   void resolve();
   
}
