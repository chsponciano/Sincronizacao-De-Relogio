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
        Scanner leitor = new Scanner(System.in);

        System.out.print("Enter the valid server IP: ");
        ip = leitor.nextLine();

        System.out.print("Enter the valid server port: ");
        port = Integer.parseInt(leitor.nextLine());

        System.out.println("Valid Ip!");

        System.out.print("Enter the client port: ");
        int portClient = Integer.parseInt(leitor.nextLine());

        try {
            new ClientUtil(ip, port, portClient);
        } catch (Exception ex) {
           ex.printStackTrace();
        }
    }
}
