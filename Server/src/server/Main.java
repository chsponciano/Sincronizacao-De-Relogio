package server;

import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author Carlos Henrique Ponciano da Silva && Vinicius Luis da Silva
 */
public class Main {

    public static void main(String[] args) {
        try {
            ServerUtil serverRmi = new ServerUtil();
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("ServerUtilRMI", serverRmi);
            System.out.println("Servidor de controle de Relógio " + serverRmi + " pronto para utilização | IP: " + serverRmi.getIp());
        } catch (RemoteException ex) {
            System.out.println("Erro RemoteException ao executar: " + ex.getMessage());
        } catch (UnknownHostException ex) {
            System.out.println("Erro UnknownHostException ao executar: " + ex.getMessage());
        }
    }
}
