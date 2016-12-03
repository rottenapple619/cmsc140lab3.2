/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.multicast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import net.Connections;
import net.NetInterface;
import util.Log;
import util.message.MessageType;
import util.message.MulticastMessage;

/**
 *
 * 
 */
public class MulticastConnection {

    public static final String MULTICAST_ADDRESS = "230.0.0.1";
    public static final int MULTICAST_PORT = 4444;
    
    private MulticastIncomingListener inThread;
    private MulticastOutgoingListener outThread;
    
    private InetAddress address = null;
    private InetSocketAddress sAddress = null;
    private NetworkInterface netInterface = null;
    private final MulticastSocket mcSocket;
    
    public MulticastConnection() throws UnknownHostException, SocketException, IOException{
        
        mcSocket = new MulticastSocket(MULTICAST_PORT);
        address = InetAddress.getByName(MULTICAST_ADDRESS);
        
        if(!Connections.isLocal){
            netInterface = NetInterface.getNetworkInterface();
            sAddress = new InetSocketAddress(address, MULTICAST_PORT);
        }
        
    }
    
    public void runConnection() throws IOException {

        //join group
        if(!Connections.isLocal){
            if(netInterface!=null){
                mcSocket.joinGroup(sAddress, netInterface);
            }
            else{
                Log.printErr("Not connected to a network.");
                return;
            }
        }
        else
            mcSocket.joinGroup(address);
        
        //instansiate Listeners
        this.inThread = new MulticastIncomingListener(mcSocket);
        this.outThread = new MulticastOutgoingListener(mcSocket);
        
        Log.print("Connected to the MULTICAST ADDRESS: "+MULTICAST_ADDRESS);
        Log.print("Listening at PORT: "+MULTICAST_PORT);
        Log.print("Type 'COMMAND' for a list of available commands.");
        
        //start the threads/Listeners
        this.inThread.start();
        this.outThread.start();

        MulticastMessage mm = new MulticastMessage(MessageType.FILESNETWORK);//request/fetch published files in the multicast group
        this.outThread.send(mm);//send the request
            
        
    }
    
    public MulticastOutgoingListener getOutgoing(){
        return this.outThread;
    }
    
    public InetAddress getAddress(){
        return this.address;
    }
    
    public SocketAddress getSocketAddress(){
        return this.sAddress;
    }
}
