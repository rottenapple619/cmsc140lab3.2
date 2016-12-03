package util.file;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.Connections;
import util.HashFunction;
import util.Log;
import util.NodeRef;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * 
 */
public class FileObjReceiver extends Thread{


    private final NodeRef senderRef;

    private Socket cSocket;
    private ObjectInput in;

    public FileObjReceiver(NodeRef senderRef) {
        this.senderRef = senderRef;
    }
    
    @Override
    public void run(){

        try {
            cSocket = new Socket(InetAddress.getByName(senderRef.getAddress()), senderRef.getPort());
            Log.print("------------------------------------------------------------");
            Log.print("Connected to: "+HashFunction.displayID(senderRef.getID()));
            Log.print("Receiving file(s) from: "+HashFunction.displayID(senderRef.getID())+"...");
            in = new ObjectInputStream(cSocket.getInputStream());
            FileObj fo = (FileObj) in.readObject(); 
            Log.print("Received file: "+fo.getFile().getName()+" with ID: "+HashFunction.displayID(fo.getID()));
            Log.print("Receiving file(s) from: "+HashFunction.displayID(senderRef.getID())+" completed.");
            Log.print("------------------------------------------------------------");
            Connections.instance.addLocalFile(fo);

            
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(FileObjReceiver.class.getName()).log(Level.SEVERE, null, ex);
        } finally {//closing all sockets and streams
            if(this.in!=null){
                try {
                    this.in.close();
                } catch (IOException ex) {
                    Logger.getLogger(FileObjReceiver.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(this.cSocket!=null){
                try {
                    this.in.close();
                } catch (IOException ex) {
                    Logger.getLogger(FileObjReceiver.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
    }

    public void startReceiving() {
        this.start();
    }
    
}
