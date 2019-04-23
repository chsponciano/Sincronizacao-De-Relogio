package client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import server.IServer;

/**
 *
 * @author Carlos Henrique Ponciano da Silva && Vinicius Luis da Silva
 */
public class ClientUtil implements IClient {

    public ArrayList<IServer> servers = new ArrayList();


    private int generateRandomTime() {
        return (int) (Math.random() * 23);
    }

    private int generateRandomMinutes() {
        return (int) (Math.random() * 59);
    }

    public int converterHourByMinute() {
        return Math.abs((this.generateRandomTime() * 60) + this.generateRandomMinutes());
    }

    public ClientUtil(ArrayList<String> ipServer) throws Exception {
        this.initializeRMI(ipServer);
    }

    @Override
    public void initializeRMI(ArrayList<String> ipServer) throws Exception {
        Registry registry;
        IServer server;
        String[] aux;
        
        for (String ip : ipServer) {
            aux = ip.split(":");
            registry = LocateRegistry.getRegistry(aux[0], Integer.parseInt(aux[1]));
            server = (IServer) registry.lookup("ServerUtil");
            System.out.println("Server ip "+ aux[0] + ":" + aux[1] +" initialized with the time in minutes "+ server.getTime());
            servers.add(server);
        }
    }

    @Override
    public int recordTotalDifference() throws RemoteException {
        AtomicReference<Integer> totalDifference = new AtomicReference<>(0);
        this.servers.stream().findFirst().ifPresent(s -> {
            try {
                s.setDifference(Math.abs(this.converterHourByMinute() - s.converterHourByMinute()));
                totalDifference.set(s.getDifference());
            } catch (Exception e) { }
        });
        return totalDifference.get();
    }

    @Override
    public int averageDifference(int totalDifference)throws RemoteException {
        return totalDifference / servers.size();
    }

    @Override
    public void assignDifference(int average) throws RemoteException {
        for (IServer s : servers) {
            s.setTime(average + s.getDifference());
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
            s.resetServerTime();
        }
    }
}
