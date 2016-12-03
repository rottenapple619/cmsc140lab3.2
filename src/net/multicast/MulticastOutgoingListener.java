package net.multicast;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import net.Connections;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * 
 */
public class MulticastOutgoingListener extends Thread{

    private final MulticastSocket mcSocket;
    
    private byte[] sendData;
    private DatagramPacket sendPacket;
    
    public MulticastOutgoingListener(MulticastSocket mcSocket) {
        this.mcSocket = mcSocket;        
    }
    
    @Override
    public void run(){
        synchronized(this){
            try {
                this.wait();
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    
    public void send(Object msg) {
        try {
            sendData = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(msg);
            sendData = baos.toByteArray();
 
            sendPacket = Connections.isLocal? 
                new DatagramPacket(sendData,sendData.length,Connections.multicastConnection.getAddress(),MulticastConnection.MULTICAST_PORT):
                new DatagramPacket(sendData,sendData.length,Connections.multicastConnection.getSocketAddress());
            
            mcSocket.send(sendPacket);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public synchronized void stopThread() {
        this.notify();
        this.sendData = null;
        this.sendPacket = null;
    }
}
