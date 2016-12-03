/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.peer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Node;
import util.NodeRef;

/**
 *
 * 
 */
public class OutgoingListener extends Thread{

    private byte[] sendData;
    private DatagramPacket sendPacket;
    private final DatagramSocket socket;
    private final Node node;
        
    public OutgoingListener(Node node,DatagramSocket socket) {
        this.socket = socket;
        this.node = node;
    }

    @Override
    public void run(){
        try {
            synchronized(this){
                this.wait();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(OutgoingListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void send(Object msg,NodeRef dest) {
        try {
            sendData = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(msg);
            sendData = baos.toByteArray();
            sendPacket = new DatagramPacket(sendData,sendData.length,InetAddress.getByName(dest.getAddress()),dest.getPort());
            socket.send(sendPacket);
        } catch (IOException ex) {
            Logger.getLogger(OutgoingListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public synchronized void stopThread(){
        this.interrupt();
    }
}
