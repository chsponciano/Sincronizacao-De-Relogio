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

    public ClientUtil(ArrayList<String> ipServer) throws Exception {
        this.initializeRMI(ipServer);
    }

    @Override
    public void initializeRMI(ArrayList<String> ipServer) throws Exception {
        Registry registry;
        IServer server;
        
        for (String ip : ipServer) {
            registry = LocateRegistry.getRegistry(ip);
            server = (IServer) registry.lookup("ServerUtilRMI");
            System.out.println("Serivdor do ip "+ server.getIp() + " inicializado com o horário em minutos "+ server.getTime());
        }
    }

    @Override
    public int recordTotalDifference() throws RemoteException {
        int totalDifference = 0;
        for (IServer s : servers) {
            s.setDifference(s.getTime() - servers.get(0).getTime());
            totalDifference += s.getDifference();
        }
        return totalDifference;
    }

    @Override
    public int averageDifference(int totalDifference)throws RemoteException {
        return totalDifference / servers.size();
    }

    @Override
    public void assignDifference(int average) throws RemoteException {
        for (IServer s : servers) {
            s.setTime(average + (-1 * s.getDifference()));
        }
    }

    @Override
    public void convertMinutesToHour() throws RemoteException {
        int hour, minute;
        
        for (IServer s : servers) {
            hour = s.getTime() / 60;
            minute = s.getTime() % 60;
            
            System.out.println("IP: " + s.getIp() + " | Courrent Time " + hour + ":" + minute);
        }
    }

    @Override
    public void resetServerTime() throws RemoteException {
        for (IServer s : servers) {
            s.generateRandomTime();
        }
    }
}
