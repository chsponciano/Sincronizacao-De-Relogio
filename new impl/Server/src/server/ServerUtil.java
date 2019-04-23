package server;

import client.IClient;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 *
 * @author Carlos Henrique Ponciano da Silva && Vinicius Luis da Silva
 */
public class ServerUtil extends UnicastRemoteObject implements IServer {

    private int time, port;
    private String ip;
    private IServer coordinator;
    private List<IServer> otherServers;
    private List<IClient> listClient;
    private boolean isCoordinator;
    private boolean startedElection;

    public ServerUtil(int port, List<IServer> servers) throws RemoteException, UnknownHostException {
        this.port = port;
        this.ip = InetAddress.getLocalHost().getHostAddress();
        this.setServers(servers);
        this.listClient = new ArrayList<>();
        this.startedElection = false;

        this.time = this.converterHourByMinute();
        System.out.println("Time: " + this.time);
    }

    private void initializeElection() throws RemoteException {
        this.startedElection = true;
        System.out.println("Server: " + this.ip + " | initialize Election");
        AtomicReference<Boolean> aux = new AtomicReference<>(true);
        Consumer<IServer> lambida = server -> {
            try {
                if (server.hasStartedElection()) {
                    aux.set(false);
                }
            } catch (Exception e) {
            }
        };

        this.otherServers.forEach(lambida);

        if (aux.get()) {
            this.makeItCoordinator();
        } else {
            System.out.println("Server: " + this.ip + " | waiting Election");
            aux.set(true);
            do {
                this.otherServers.forEach(lambida);
                try {
                    Thread.sleep(0L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (!aux.get());
        }

        this.startedElection = false;
    }

    @Override
    public boolean hasStartedElection() throws RemoteException {
        return this.startedElection;
    }

    @Override
    public void setServers(List<IServer> servers) throws RemoteException {
        this.otherServers = servers;
        servers.forEach(server -> {
            try {
                server.addServer(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void addServer(IServer server) throws RemoteException {
        this.otherServers.add(server);
    }

    @Override
    public void setCoordinator(IServer server) throws RemoteException, NotBoundException {
        this.coordinator = server;
    }

    @Override
    public void makeItCoordinator() throws RemoteException {
        this.isCoordinator = true;
        this.coordinator = this;
        this.otherServers.forEach(server -> {
            try {
                server.setCoordinator(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private int generateRandomTime() throws RemoteException {
        return (int) (Math.random() * 23);
    }

    private int generateRandomMinutes() throws RemoteException {
        return (int) (Math.random() * 59);
    }

    public int converterHourByMinute() throws RemoteException {
        return Math.abs((this.generateRandomTime() * 60) + this.generateRandomMinutes());
    }

    @Override
    public void addClient(IClient clients) throws RemoteException {
        if(this.isCoordinator){
            this.listClient.add(clients);
        }
        
        try{
            this.coordinator.addClient(clients);
        }catch(Exception e){
            this.initializeElection();
        }
        
        this.otherServers.forEach(server -> {
            try {
                server.setClients(this.listClient);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void setClients(List<IClient> clients) throws RemoteException {
        this.listClient = clients;
    }
    
    public void timeSetting(){
        AtomicReference<Integer> totalDifference = new AtomicReference<>(0);
        this.listClient.forEach(client -> {
            try {
                totalDifference.set(totalDifference.get() + client.getTime(this.time));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        int averageDifference = totalDifference.get() / (this.listClient.size() + 1); //acrescenta mais um para o servidor
        
        this.time += averageDifference;
        
        this.listClient.forEach(client -> {
            try {
                client.setTime(averageDifference);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
    }

    @Override
    public List<IServer> getServers() throws RemoteException {
        return this.getServers();
    }

    @Override
    public void distributeClient() throws RemoteException {
        this.listClient.forEach(client -> {
            try{
                client.setServers(otherServers);
            }catch(Exception e){}
        });
    }
}
