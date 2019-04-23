package client;

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
        
        do {
            while (ips.size() < 2) {
                System.out.print("Enter the server IP " + (ips.size() + 1) + ": ");
                auxIp = leitor.next();
                
                System.out.print("Enter the server port " + (ips.size() + 1) + ": ");
                auxIp += ":" + leitor.next();
                
                System.out.println("Valid Ip!");
                ips.add(auxIp);
            }

            try {
                client = new ClientUtil(ips);

                totalDifference = client.recordTotalDifference();
                System.out.println("Total Difference: " + totalDifference);

                average = client.averageDifference(totalDifference);
                System.out.println("Average: " + average);

                client.assignDifference(average);

                client.convertMinutesToHour();

                System.out.print("Answer Yes or No to end the program: ");
                isContinue = !(leitor.next().toUpperCase().equals("YES"));
                
                if(isContinue){
                    client.resetServerTime();
                    System.out.println("Reset Server");
                }
                
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }

        } while (isContinue);
    }
}
