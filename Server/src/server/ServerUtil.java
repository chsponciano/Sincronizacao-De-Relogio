package server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Carlos Henrique Ponciano da Silva && Vinicius Luis da Silva
 */
public class ServerUtil extends UnicastRemoteObject implements IServer {
    private int time, difference;
    private String ip;

    public ServerUtil() throws RemoteException, UnknownHostException {
        this.time = this.converterHourByMinute();
        this.difference = 0;
        this.ip = InetAddress.getLocalHost().getHostAddress();
    }

    @Override
    public int generateRandomTime() throws RemoteException {
        return (int) Math.random() * 23;
    }

    @Override
    public int generateRandomMinutes() throws RemoteException {
        return (int) Math.random() * 59;
    }
    
    @Override
    public int converterHourByMinute() throws RemoteException {
        return (this.generateRandomTime() * 60) + this.generateRandomMinutes();
    }

    @Override
    public int getTime() throws RemoteException {
        return this.time;
    }

    @Override
    public void setTime(int time) throws RemoteException {
        this.time = time;
    }

    @Override
    public int getDifference() throws RemoteException {
        return this.difference;
    }

    @Override
    public void setDifference(int difference) throws RemoteException {
        this.difference = difference;
    }

    @Override
    public String getIp() throws RemoteException {
        return this.ip;
    }
    
    
}
