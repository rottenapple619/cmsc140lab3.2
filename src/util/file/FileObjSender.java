package util.file;


import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.AvailablePort;
import net.peer.PeerConnection;
import util.HashFunction;
import util.Log;
import util.Node;
import util.NodeRef;
import util.message.MessageType;
import util.message.UDPMessage;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * FileObjSender can accommodate one requestor/receiver at a time
 * @author Marz
 */
public class FileObjSender extends Thread{
    
    private final FileObj fileToBeSent;
    private final PeerConnection sender;
    private final NodeRef requestor;
    private final NodeRef senderRef;
    
    private ServerSocket sSocket;
    private Socket cSocket;
    private ObjectOutput out;
    
    private UDPMessage msg;
    

    public FileObjSender(PeerConnection sender, NodeRef requestor, FileObj fo) {
        this.fileToBeSent = fo;
        this.sender = sender;
        this.requestor = requestor;
        
        int port = AvailablePort.getAvailablePort();
        this.senderRef = new NodeRef(sender.getID(), port, this.sender.getAddress());
    }

    
    public void startSending() {
        try {
            sSocket = new ServerSocket(this.senderRef.getPort());
            msg = new UDPMessage(MessageType.INIT_RECEIVE, (Node)sender, senderRef);
            this.sender.getOutgoing().send(msg, requestor);
            this.start();
        } catch (UnknownHostException ex) {
            Logger.getLogger(FileObjSender.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileObjSender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run(){
        try {
            this.cSocket = sSocket.accept();
            Log.print("------------------------------------------------------------");
            Log.print("Connected to: "+HashFunction.displayID(requestor.getID()));
            Log.print("Sending file(s) to: "+HashFunction.displayID(requestor.getID())+"...");
            
            out = new ObjectOutputStream(cSocket.getOutputStream());   
            out.writeObject(fileToBeSent);
            Log.print("Sending file(s) to: "+HashFunction.displayID(requestor.getID())+" finished");
            Log.print("------------------------------------------------------------");
        } catch (IOException ex) {
            Logger.getLogger(FileObjSender.class.getName()).log(Level.SEVERE, null, ex);
        } finally {//closing all sockets and streams
            if(this.out!=null){
                try {
                    out.close();
                } catch (IOException ex) {
                    Logger.getLogger(FileObjSender.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(this.cSocket!=null){
                try {
                    this.cSocket.close();
                } catch (IOException ex) {
                    Logger.getLogger(FileObjSender.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(this.sSocket!=null){
                try {
                    this.sSocket.setReuseAddress(true);
                    this.sSocket.close();
                } catch (IOException ex) {
                    Logger.getLogger(FileObjSender.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        }
    }
    
}
