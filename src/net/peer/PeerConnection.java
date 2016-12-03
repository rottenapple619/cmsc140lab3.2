/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.peer;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;
import util.HashFunction;
import util.Log;
import util.Node;
import util.NodeRef;
import util.file.FileObj;
import util.file.FileRef;
import util.file.FileObjReceiver;
import util.file.FileObjSender;
import util.message.MessageType;
import util.message.UDPMessage;

/**
 *
 * 
 */
public class PeerConnection extends Node{

    private transient final HashMap<Long, FileObj> publishedFiles;
    private transient final HashMap<Long, FileRef> referencedFiles;
    
    private transient PeerNotifier notifier;
    private transient final IncomingListener inThread;
    private transient final OutgoingListener outThread;
    private transient final DatagramSocket socket;
    private transient FileObjSender fSender;
    private transient FileObjReceiver fReceiver;

//    private FileSender fSender;
//    private FileReceiver fReceiver;
    
    public PeerConnection() throws SocketException {
        super(true);
        this.publishedFiles = new HashMap<>();
        this.referencedFiles = new HashMap<>();
        
        this.socket = new DatagramSocket(this.getPort());
        this.notifier = new PeerNotifier(this.getNode());
        
        this.inThread = new IncomingListener(this, socket);
        this.outThread = new OutgoingListener(this, socket);
        
        printStatus(this,true);
    }

    public PeerConnection(NodeRef initiator) throws SocketException {
        super(false);
        this.publishedFiles = new HashMap<>();
        this.referencedFiles = new HashMap<>();
        
        this.setInitiator(initiator);
        this.socket = new DatagramSocket(this.getPort());
        
        this.inThread = new IncomingListener(this, socket);
        this.outThread = new OutgoingListener(this, socket);
    }

    public void runConnection(boolean isInitiator) {
        inThread.start();
        outThread.start();
        if(isInitiator){
            notifier.start();
        }
        else{
            Node n = (Node)this;
            UDPMessage msg = new UDPMessage(MessageType.FINDSUCCESSOR,n);
            this.outThread.send(msg, this.getInitiator());
        }
    }

    public HashMap<Long, FileObj> getPublishedFiles(){
        return this.publishedFiles;
    }
    
    public OutgoingListener getOutgoing() {
        return this.outThread;
    }
    
    public HashMap<Long, FileRef> getReferencedFiles(){
        return this.referencedFiles;
    }
    
    public void addToRefencedFiles(FileRef f){
        this.referencedFiles.put(f.getID(), f);
        Log.print("------------------------------------------------------------");
        Log.print("A new file has been REGISTERED to you for the P2P Network: " +HashFunction.displayID(f.getInitiator().getID()) +"@"+ f.getInitiator().getPort());
        Log.print("FileID: "+HashFunction.displayID(f.getID()));
        Log.print("FileName: "+f.getFileName());
        Log.print("Published by: "+HashFunction.displayID(f.getPublisher().getID())+"@"+f.getPublisher().getPort());
        Log.print("------------------------------------------------------------");
    }

    public FileRef deleteFileRef(FileRef f) {
        FileRef toBeDeleted = this.referencedFiles.get(f.getID());
        if(toBeDeleted == null) return null;
        else{
            if(toBeDeleted.getFileName().equals(f.getFileName())){
                this.referencedFiles.remove(f.getID());
                Log.print("------------------------------------------------------------");
                Log.print("A file has been UNREGISTERED to you for the P2P Network: " +HashFunction.displayID(f.getInitiator().getID()) +"@"+ f.getInitiator().getPort());
                Log.print("FileID: "+HashFunction.displayID(f.getID()));
                Log.print("FileName: "+f.getFileName());
                Log.print("Published by: "+HashFunction.displayID(f.getPublisher().getID())+"@"+f.getPublisher().getPort());
                Log.print("------------------------------------------------------------");
            }
            else{
                Log.printErr("Key collision.");
                return null;
            }
        }
        
        return toBeDeleted;
    }

    public void deleteNetFile(FileRef f,NodeRef n) {
        if(this.publishedFiles.remove(f.getID()) != null){
            Log.print("------------------------------------------------------------");
            Log.print("A file has been DELETED to you for the P2P Network: " +HashFunction.displayID(f.getInitiator().getID()) +"@"+ f.getInitiator().getPort());
            Log.print("FileID: "+HashFunction.displayID(f.getID()));
            Log.print("FileName: "+f.getFileName());
            Log.print("Deleted by: "+HashFunction.displayID(n.getID())+"@"+n.getPort());
            //Log.print("Published by: "+HashFunction.displayID(f.getPublisher().getID())+"@"+f.getPublisher().getPort());
            Log.print("------------------------------------------------------------");
        }
    }

    public boolean isReferenced(FileRef f) {
        FileRef fRef;
        if((fRef = this.referencedFiles.get(f.getID()))==null)
            return false;
        else if(!fRef.getFileName().equals(f.getFileName())){
            Log.printErr("Key collision");
            return false;
        }
        return true;
    }

    void openFileObjSender(NodeRef requestor, FileRef fRef) {
        FileObj fo = this.publishedFiles.get(fRef.getID());
        this.fSender = new FileObjSender(this, requestor, fo);
        this.fSender.startSending();
    }

    void openFileObjReceiver(NodeRef senderRef) {
        this.fReceiver = new FileObjReceiver(senderRef);
        this.fReceiver.startReceiving();
    }
    
    
}
