package server;

import java.rmi.*;
import java.util.List;

/**
 *
 * @author Carlos Henrique Ponciano da Silva && Vinicius Luis da Silva
 */
public interface IServer extends Remote{
    public int generateRandomTime() throws RemoteException;
    public int generateRandomMinutes() throws RemoteException;
    public int converterHourByMinute() throws RemoteException;
    
    public int getTime() throws RemoteException;
    public void setTime(int time) throws RemoteException;
    public int getDifference() throws RemoteException;
    public void setDifference(int difference) throws RemoteException;
    public String getIp() throws RemoteException;
    public void resetServerTime() throws RemoteException;

    public void makeItCoordinator() throws RemoteException;
    public void setCoordinator(IServer server) throws RemoteException, NotBoundException;
    public boolean hasStartedElection() throws RemoteException;
    public void setServers(List<IServer> servers) throws RemoteException;
    public void addServer(IServer server) throws RemoteException;
}
