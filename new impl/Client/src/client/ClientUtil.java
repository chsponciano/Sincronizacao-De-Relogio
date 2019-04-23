package client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import server.IServer;

/**
 *
 * @author Carlos Henrique Ponciano da Silva && Vinicius Luis da Silva
 */
public class ClientUtil extends UnicastRemoteObject implements IClient {

    private int time;

    private int generateRandomHours() {
        return (int) (Math.random() * 23);
    }

    private int generateRandomMinutes() {
        return (int) (Math.random() * 59);
    }

    public int converterHourByMinute() {
        return Math.abs((this.generateRandomHours() * 60) + this.generateRandomMinutes());
    }

    public ClientUtil(String ip, int port, int portClient) throws Exception {
        this.initializeRMI(ip, port, portClient);
    }

    private void initializeRMI(String ip, int port, int portClient) throws Exception {
        Registry registry = LocateRegistry.getRegistry(ip, port);
        IServer server = (IServer) registry.lookup("ServerUtil");
        this.time = converterHourByMinute();
        System.out.println("Init Time: " + this.time);
        
        Registry registryClient = LocateRegistry.createRegistry(portClient);
        registryClient.rebind("ClientUtil", this);

        server.addClient(this);
    }

    @Override
    public int getTime(int timeServer) throws RemoteException {
        return timeServer - this.time;
    }

    @Override
    public void setTime(int time) throws RemoteException {
        this.time += time;
        System.out.println("Ajust Time: " + this.convertTime(this.time));
    }
    
    private String convertTime(int time){
        return Math.abs(time / 60) + ":" + Math.abs(time % 60);
    }
}
