package net.multicast;


import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.util.logging.Level;
import java.util.logging.Logger;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * 
 */
class MulticastIncomingListener extends Thread {

    private byte[] receiveData;
    private DatagramPacket receivePacket;
    private boolean isRunning = false;
    private final MulticastSocket mcSocket;
        
    public MulticastIncomingListener(MulticastSocket mcSocket) {
        this.mcSocket = mcSocket;
        this.isRunning = true;
    }


    @Override
    public void run(){
        try{
            while(isRunning){
                receiveData = new byte[1024];
                receivePacket = new DatagramPacket(receiveData,receiveData.length);
                mcSocket.receive(receivePacket);
                ByteArrayInputStream byteStream = new
                    ByteArrayInputStream(receiveData);
                ObjectInputStream is = new
                    ObjectInputStream(new BufferedInputStream(byteStream));
                Object o = is.readObject();
                is.close();
                
                MulticastProtocol.check(o);
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(MulticastIncomingListener.class.getName()).log(Level.SEVERE, null, ex);
            //stopThread();
        }
    }

    public void stopThread() {
        this.isRunning = false;
        this.interrupt();
    }
    
}
