package net;



import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * 
 */
public class NetInterface {

    public static NetworkInterface getNetworkInterface() throws SocketException, UnknownHostException{

        NetworkInterface netIfs = null;
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();

        for (NetworkInterface netIf : Collections.list(nets)) {
            Enumeration<InetAddress> iAddress = netIf.getInetAddresses();
            ArrayList<InetAddress> address = Collections.list(iAddress);

            if(!netIf.isUp())
                continue;
            if(netIf.isLoopback())
                continue;
            if(address.isEmpty())
                continue;
            if(!netIf.supportsMulticast())
                continue;
            if(netIf.isVirtual())
                continue;

            for(InetAddress a: address){
                if(a.getHostAddress().equals(Connections.address))
                    return netIf;//returns only 1 NetInterface (^^,)
            }
            
//            if(address.contains(InetAddress.getLocalHost())){
//                netIfs = netIf;
//                break;
//            }

        }
//        if(netIfs!=null){
//            System.out.println("Name: "+netIfs.getName());
//            System.out.println("Display Name: "+netIfs.getDisplayName());
//        }
        return netIfs;

    }


}
