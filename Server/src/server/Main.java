package server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
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
            
            IServer serverRmi = new ServerUtil(port);
            Registry registry = LocateRegistry.createRegistry(port);
            registry.rebind("ServerUtil", serverRmi);
            System.out.println(serverRmi + " Ready-to-use clock control server | IP: " + serverRmi.getIp());
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
}
