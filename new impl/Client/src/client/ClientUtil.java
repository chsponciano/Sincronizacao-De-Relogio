package client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import server.IServer;

/**
 *
 * @author Carlos Henrique Ponciano da Silva && Vinicius Luis da Silva
 */
public class ClientUtil implements IClient {

    public ArrayList<IServer> servers = new ArrayList();
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

    public ClientUtil(ArrayList<String> ipServer) throws Exception {
        this.initializeRMI(ipServer);
    }
    
    public void communicateCoordinator(IClient c) throws RemoteException{
        servers.get(0).addClient(c);
    }

    @Override
    public void initializeRMI(ArrayList<String> ipServer) throws Exception {
        Registry registry;
        IServer server;
        String[] aux;
        this.time = converterHourByMinute();
        
        for (String ip : ipServer) {
            aux = ip.split(":");
            registry = LocateRegistry.getRegistry(aux[0], Integer.parseInt(aux[1]));
            server = (IServer) registry.lookup("ServerUtil");
            servers.add(server);
        }
    }

    @Override
    public int getTime(int timeServer) throws RemoteException {
        return timeServer - this.time;
    }

    @Override
    public void setTime(int time) throws RemoteException {
        this.time += time;
    }
    
}
