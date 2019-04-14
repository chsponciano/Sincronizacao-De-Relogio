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
        String auxIp = null;
        int totalDifference = 0, average = 0;
        ClientUtil client = null;
        
        ArrayList<String> ips = new ArrayList<>();
        Scanner leitor = new Scanner(System.in);

        do {
            while (ips.size() < 2) {
                System.out.print("Enter the server IP " + (ips.size() + 1) + ": ");
                auxIp = leitor.next();

                if (auxIp == null || auxIp.length() == 0 || auxIp.isEmpty() || auxIp.length() > 15 || auxIp.split("\\.").length != 4) {
                    System.out.println("Ip Invalid!");
                    continue;
                }
                
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
                
                System.out.print("Responda com Sim ou Nao para finalizar o programa: ");
                isContinue = !(leitor.next().toUpperCase().equals("SIM"));
            } catch (Exception ex) {
                System.out.println("Erro ao executar: " + ex.getMessage());
            }
            
        } while (isContinue);
    }
}
