package server;

import java.net.InetAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Carlos Henrique Ponciano da Silva && Vinicius Luis da Silva
 */
public class Main {

    public static void main(String[] args) {
        try {
            Scanner leitor = new Scanner(System.in);
            System.out.print("Enter the server port: ");
            int port = Integer.parseInt(leitor.nextLine());
            boolean shouldContinue;
            List<IServer> servers = new ArrayList<>();

            System.out.println("Enter a list of other servers");
            System.out.print("Should continue? (Y/N): ");
            shouldContinue = leitor.nextLine().equalsIgnoreCase("Y");
            
            while (shouldContinue) {
                //System.out.print("Ip: ");
                String ip = InetAddress.getLocalHost().getHostAddress();//leitor.nextLine();
                System.out.print("Port: ");
                int p = Integer.parseInt(leitor.nextLine());
                System.out.print("Should continue? (Y/N): ");
                shouldContinue = leitor.nextLine().equalsIgnoreCase("Y");

                Registry registry = LocateRegistry.getRegistry(ip, p);
                IServer server = (IServer) registry.lookup("ServerUtil");
                servers.add(server);
            }

            IServer serverRmi = new ServerUtil(port, servers);
            Registry registry = LocateRegistry.createRegistry(port);
            registry.rebind("ServerUtil", serverRmi);
            
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
}
