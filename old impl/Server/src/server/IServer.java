package server;

import java.rmi.*;

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
}
