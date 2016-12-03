/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.peer;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Node;

/**
 *
 * 
 */
class IncomingListener extends Thread{

    private byte[] receiveData;
    private DatagramPacket receivePacket;

    private boolean isRunning = false;
    private final DatagramSocket socket;
    private final Node node;

    public IncomingListener(Node node, DatagramSocket socket) {
        this.socket = socket;
        this.node = node;
        this.isRunning = true;
    }

    @Override
    public void run(){
        try{
            while(isRunning){
                receiveData = new byte[1024];
                receivePacket = new DatagramPacket(receiveData,receiveData.length);
                socket.receive(receivePacket);
                ByteArrayInputStream byteStream = new
                    ByteArrayInputStream(receiveData);
                ObjectInputStream is = new
                    ObjectInputStream(new BufferedInputStream(byteStream));
                Object o = is.readObject();
                is.close();
                PeerProtocol.check(o);

            }
        } catch (IOException ex) {
            Logger.getLogger(IncomingListener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(IncomingListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void stopThread(){
        this.interrupt();
        this.isRunning = false;
    }
}
