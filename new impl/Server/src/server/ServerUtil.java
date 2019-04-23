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
import java.util.logging.Level;
import java.util.logging.Logger;

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
                
            }
        });
        
        new Thread(() -> { 
            while(true){
                try {
                    Thread.sleep(10000);
                    if(!this.listClient.isEmpty())
                        this.timeSetting();
                } catch (Exception ex) {             
                }
            }
        }).start();
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
        System.out.println("addClient");
        if (this.isCoordinator) {
            this.listClient.add(clients);
        }

        try {
            this.coordinator.addClient(clients);
        } catch (Exception e) {
            this.initializeElection();
        }
        
        this.synchronizeClients();
    }

    private void synchronizeClients() {
        System.out.println("synchronizeClients");
        this.otherServers.forEach(server -> {
            try {
                server.setClients(this.listClient);
            } catch (Exception e) { 
            }
        });
    }

    @Override
    public void setClients(List<IClient> clients) throws RemoteException {
        this.listClient = clients;
    }

    public void timeSetting() {
        System.out.println("timeSetting");
        AtomicReference<Integer> totalDifference = new AtomicReference<>(0);
        List<IClient> aux = new ArrayList();
        this.listClient.forEach(client -> {
            try {
                totalDifference.set(totalDifference.get() + client.getTime(this.time));
            } catch (Exception e) {
                aux.add(client);
            }
        });

        this.listClient.removeAll(aux);
        
        int averageDifference = totalDifference.get() / (this.listClient.size() + 1); //acrescenta mais um para o servidor

        this.time += averageDifference;

        this.listClient.forEach(client -> {
            try {
                client.setTime(averageDifference);
            } catch (Exception e) {
                aux.add(client);
            }
        });
        
        this.listClient.removeAll(aux);
        
        this.synchronizeClients();

    }

    @Override
    public List<IServer> getServers() throws RemoteException {
        System.out.println("server.ServerUtil.getServers()");
        return this.getServers();
    }
}
