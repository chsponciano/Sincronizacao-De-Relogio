package client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Carlos Henrique Ponciano da Silva && Vinicius Luis da Silva
 */
public class Main {

    public static void main(String[] args) {
        boolean isContinue = false;
        String auxIp;
        int totalDifference, average;
        ClientUtil client;

        ArrayList<String> ips = new ArrayList<>();
        Scanner leitor = new Scanner(System.in);

        while (ips.size() < 2) {
            System.out.print("Enter the server IP " + (ips.size() + 1) + ": ");
            auxIp = leitor.next();

            System.out.print("Enter the server port " + (ips.size() + 1) + ": ");
            auxIp += ":" + leitor.next();

            System.out.println("Valid Ip!");
            ips.add(auxIp);
        }

        System.out.print("Enter the client port: ");
        int portClient = Integer.parseInt(leitor.nextLine());

        try {
            client = new ClientUtil(ips);

            IClient clientRmi = client;
            Registry registry = LocateRegistry.createRegistry(portClient);
            registry.rebind("ClientUtil", clientRmi);
            
            client.communicateCoordinator(clientRmi);

        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
}
