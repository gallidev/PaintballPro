package networking.discovery;

import static java.lang.System.out;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;

/**
 * List all networking interfaces on the device.
 *
 * @author MattW
 */
public class ListInterfaces 
{
	
	/**
	 * Main method to list interfaces on machine.
	 * @param args Command-line arguments. 
	 * @throws SocketException We may get an exception from networking interface. 
	 */
    public static void main(String args[]) throws SocketException {
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        System.setProperty("java.net.preferIPv4Stack", "true");
        for (NetworkInterface netIf : Collections.list(nets)) {
            out.printf("Display name: %s\n", netIf.getDisplayName());
            out.printf("Name: %s\n", netIf.getName());
            displaySubInterfaces(netIf);
            out.printf("\n");
        }
    }
    
    /**
     * Display any sub network interfaces.
     * @param netIf Network interface of the computer.
     * @throws SocketException We may get an exception from networking interface. 
     */
    static void displaySubInterfaces(NetworkInterface netIf) throws SocketException {
        Enumeration<NetworkInterface> subIfs = netIf.getSubInterfaces();
        
        for (NetworkInterface subIf : Collections.list(subIfs)) {
            out.printf("\tSub Interface Display name: %s\n", subIf.getDisplayName());
            out.printf("\tSub Interface Name: %s\n", subIf.getName());
        }
     }
} 