package client;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author Carlos Henrique Ponciano da Silva && Vinicius Luis da Silva
 */
public interface IClient {
    public void initializeRMI(ArrayList<String> ipServer) throws Exception;
    public int recordTotalDifference()throws RemoteException;
    public int averageDifference(int totalDifference) throws RemoteException;
    public void assignDifference(int average) throws RemoteException;
    public void convertMinutesToHour() throws RemoteException;
    public void resetServerTime() throws RemoteException;
}
