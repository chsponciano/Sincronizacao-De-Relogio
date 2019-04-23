package client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

import server.IServer;

/**
 *
 * @author Carlos Henrique Ponciano da Silva && Vinicius Luis da Silva
 */
public class ClientUtil implements IClient {

    public List<IServer> servers = new ArrayList();
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

    public ClientUtil(String ip, int port) throws Exception {
        this.initializeRMI(ip, port);
    }

    public void communicateCoordinator(IClient c) throws RemoteException {        
        servers.get(0).addClient(c);
    }

    private void initializeRMI(String ip, int port) throws Exception {
        Registry registry = LocateRegistry.getRegistry(ip, port);
        IServer server = (IServer) registry.lookup("ServerUtil");
        this.setServers(server.getServers());
        this.time = converterHourByMinute();
    }

    @Override
    public int getTime(int timeServer) throws RemoteException {
        return timeServer - this.time;
    }

    @Override
    public void setTime(int time) throws RemoteException {
        this.time += time;
    }

    @Override
    public void setServers(List<IServer> servers) throws RemoteException {
        this.servers = servers;
    }

}
