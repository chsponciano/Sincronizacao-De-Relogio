package server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
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
    private int time, difference, port;
    private String ip;
    private IServer coordinator;
    private List<IServer> otherServers;
    //private List<ICliente>
    private boolean isCoordinator;
    private boolean startedElection;

    public ServerUtil(int port, List<IServer> servers) throws RemoteException, UnknownHostException {
        this.port = port;
        this.ip = InetAddress.getLocalHost().getHostAddress();
        this.setServers(servers);
        this.startedElection = false;
        this.time = this.converterHourByMinute();
        this.difference = 0;
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
            } catch (Exception e) { }
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
            } while(!aux.get());
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

    @Override
    public int generateRandomTime() throws RemoteException {
        if (this.isCoordinator) {
            return (int) (Math.random() * 23);
        }
        try {
            return this.coordinator.generateRandomTime();
        } catch (Exception e) {
            this.initializeElection();
            return this.coordinator.generateRandomTime();
        }
    }

    @Override
    public int generateRandomMinutes() throws RemoteException {
        if (this.isCoordinator) {
            return (int) (Math.random() * 59);
        }
        try {
            return this.coordinator.generateRandomMinutes();
        } catch (Exception e) {
            this.initializeElection();
            return this.coordinator.generateRandomMinutes();
        }
    }
    
    @Override
    public int converterHourByMinute() throws RemoteException {
        if (this.isCoordinator) {
            return Math.abs((this.generateRandomTime() * 60) + this.generateRandomMinutes());
        }
        try {
            return this.coordinator.converterHourByMinute();
        } catch (Exception e) {
            this.initializeElection();
            return this.coordinator.converterHourByMinute();
        }

    }

    @Override
    public int getTime() throws RemoteException {
        if (this.isCoordinator) {
            return this.time;
        }
        try {
            return this.coordinator.getTime();
        } catch (Exception e) {
            this.initializeElection();
            return this.coordinator.getTime();
        }
    }

    @Override
    public void setTime(int time) throws RemoteException {
        if (this.isCoordinator) {
            this.time = time;
        } else {
            try {
                this.coordinator.setTime(time);
            } catch (Exception e) {
                this.initializeElection();
                this.coordinator.setTime(time);
            }
        }
    }

    @Override
    public int getDifference() throws RemoteException {
        if (this.isCoordinator) {
            return this.difference;
        }
        try {
            return this.coordinator.getDifference();
        } catch (Exception e) {
            this.initializeElection();
            return this.coordinator.getDifference();
        }
    }

    @Override
    public void setDifference(int difference) throws RemoteException {
        if (this.isCoordinator) {
            this.difference = difference;
        } else {
            try {
                this.coordinator.setDifference(difference);
            } catch (Exception e) {
                this.initializeElection();
                this.coordinator.setDifference(difference);
            }
        }
    }

    @Override
    public String getIp() throws RemoteException {
        if (this.isCoordinator) {
            return this.ip;
        }
        try {
            return this.coordinator.getIp();
        } catch (Exception e) {
            this.initializeElection();
            return this.coordinator.getIp();
        }
    }

    @Override
    public void resetServerTime() throws RemoteException {
        if (this.isCoordinator) {
            this.setDifference(0);
            this.time = generateRandomTime();
            System.out.println("New Time: " + time);
        } else {
            try {
                this.coordinator.resetServerTime();
            } catch (Exception e) {
                this.initializeElection();
                this.coordinator.resetServerTime();
            }
        }
    }
}
