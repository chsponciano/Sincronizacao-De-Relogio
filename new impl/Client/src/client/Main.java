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
        String ip;
        int port;
        ClientUtil client;

        ArrayList<String> ips = new ArrayList<>();
        Scanner leitor = new Scanner(System.in);

        System.out.print("Enter the valid server IP: ");
        ip = leitor.nextLine();

        System.out.print("Enter the valid server port: ");
        port = Integer.parseInt(leitor.nextLine());

        System.out.println("Valid Ip!");

        System.out.print("Enter the client port: ");
        int portClient = Integer.parseInt(leitor.nextLine());

        try {
            client = new ClientUtil(ip, port);

            IClient clientRmi = client;
            Registry registry = LocateRegistry.createRegistry(portClient);
            registry.rebind("ClientUtil", clientRmi);

            client.communicateCoordinator(clientRmi);

        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
}
