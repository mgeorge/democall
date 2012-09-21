package discovery.computername;

/**
 * @author Mark
 */
public final class OtagoComputerNameResolver implements ComputerNameResolver {

   private final String computerName;
   private String labName;
   private String machineId;

   /**
    * <p>Otago University lab machines usually have a COMPUTERNAME environment
    * variable in the form "labname-machineId", eg SB316-23.</p>
    *
    * <p>If the name parameter is set then use that, otherwise use the envVar
    * parameter to get the name from an environment variable.</p>
    *
    * @param name the computer name to use (or null).
    * @param envVar the environment variable to use if name is not set.
    *
    */
   public OtagoComputerNameResolver(final String name, final String envVar) {
      super();

      if (name == null) {
         computerName = System.getenv(envVar);
      } else {
         computerName = name;
      }

   }

   public void resolve() {
      if(computerName == null) {
         throw new InvalidComputerNameException("The computer name has not been provided via a recognisable method.\nTry setting the COMPUTERNAME environment variable, or passing the name as an argument when starting the application.");
      }

      final String[] nameBits = computerName.split("-");

      if (nameBits.length < 2) {
         throw new InvalidComputerNameException("The computer name '" + computerName + "' is not in the expected format (eg SB316-01)");
      }

      this.labName = nameBits[0];
      this.machineId = nameBits[1];
   }
   
   public String getComputerName() {
      return this.computerName;
   }

   public String getLabName() {
      return this.labName;
   }

   public String getMachineId() {
      return this.machineId;
   }
}
