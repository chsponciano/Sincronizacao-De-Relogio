package client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author Carlos Henrique Ponciano da Silva && Vinicius Luis da Silva
 */
public interface IClient extends Remote{
    public void initializeRMI(ArrayList<String> ipServer) throws Exception;
    public int getTime(int timeServer)throws RemoteException;
    public void setTime(int time) throws RemoteException;
}