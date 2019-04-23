package server;

import client.IClient;
import java.rmi.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Carlos Henrique Ponciano da Silva && Vinicius Luis da Silva
 */
public interface IServer extends Remote{
    public void makeItCoordinator() throws RemoteException;
    public void setCoordinator(IServer server) throws RemoteException, NotBoundException;
    public boolean hasStartedElection() throws RemoteException;
    
    public void setServers(List<IServer> servers) throws RemoteException;
    public void addServer(IServer server) throws RemoteException;
    
    public void setClients(List<IClient> clients) throws RemoteException;
    public void addClient(IClient clients) throws RemoteException;
}
