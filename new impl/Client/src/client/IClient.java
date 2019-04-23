package client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import server.IServer;

/**
 *
 * @author Carlos Henrique Ponciano da Silva && Vinicius Luis da Silva
 */
public interface IClient extends Remote{
    public int getTime(int timeServer)throws RemoteException;
    public void setTime(int time) throws RemoteException;
    public void setServers(List<IServer> servers) throws RemoteException;
}