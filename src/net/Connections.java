/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.multicast.MulticastConnection;
import net.peer.PeerConnection;
import util.HashFunction;
import util.Log;
import util.NodeRef;
import util.file.FileObj;
import util.file.FileRef;

/**
 *
 * 
 */
public class Connections {

    public static boolean isServer;
    public static boolean isLocal;
    public static Connections instance;
    public static MulticastConnection multicastConnection;
    public static long id;
    public static String address;
    
    private PeerConnection pConnect;
    private final HashMap<Long,NodeRef> initiatorList;
    private final HashMap<Long,FileRef> publishedFiles;
    private final HashMap<Long,FileObj> localFiles;
    private final HashMap<Long,PeerConnection> peerConnectionList;
    
    public static void initialize(boolean b) {
        Log.print(Connections.class.getName()+" initializing...");
        isLocal = b;
        instance = new Connections();
        instance.initializeMulticastConnection();
    }

    private Connections(){
        try {
            address = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Connections.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        id = HashFunction.getHashCode(address);
        peerConnectionList = new HashMap<>();
        initiatorList = new HashMap<>();
        publishedFiles = new HashMap<>();
        localFiles = new HashMap<>();
        
        Log.print("Your Address is "+address);
        Log.print("Your ID is "+HashFunction.displayID(id));
    }
    

    /**
     * Initializes and creates a MulticastConnection
     * @see MulticastConnection
     */
    public void initializeMulticastConnection() {
        try {
            multicastConnection = new MulticastConnection();
            multicastConnection.runConnection();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Connections.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SocketException ex) {
            Logger.getLogger(Connections.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Connections.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    /**
     * Creates a P2P Network Initiator
     * @see PeerConnection
     */
    public void initializePeerConnection() {
        try {
            pConnect = new PeerConnection();
            pConnect.runConnection(true);
            peerConnectionList.put(pConnect.getID(),pConnect);
            isServer = true;
        } catch (SocketException ex) {
            Logger.getLogger(Connections.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    public void initializePeerConnection(NodeRef initiator) {
        try {
            pConnect = new PeerConnection(initiator);
            pConnect.runConnection(false);
            peerConnectionList.put(pConnect.getInitiator().getID(),pConnect);
        } catch (SocketException ex) {
            Logger.getLogger(Connections.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public HashMap<Long,NodeRef> getInitiatorList() {
        return initiatorList;
    }

    public HashMap<Long,PeerConnection> getPeerConnectionList() {
        return peerConnectionList;
    }

    public HashMap<Long,FileRef> getPublishedFiles(){
        return publishedFiles;
    }
    
    public HashMap<Long,FileObj> getLocalFiles(){
        return localFiles;
    }
    
    public void addFileRef(FileRef f){
        publishedFiles.put(f.getID(), f);
        Log.print("------------------------------------------------------------");
        Log.print("File published for the P2P Network: " +HashFunction.displayID(f.getInitiator().getID()) +"@"+ f.getInitiator().getPort());
        Log.print("FileID: "+HashFunction.displayID(f.getID()));
        Log.print("FileName: "+f.getFileName());
        Log.print("Published by: "+HashFunction.displayID(f.getPublisher().getID())+"@"+f.getPublisher().getPort());
        Log.print("------------------------------------------------------------");
    }

    public void deleteFileRef(FileRef f, NodeRef n) {
        publishedFiles.remove(f.getID());
        if(Connections.id!=f.getPublisher().getID()){
            Log.print("------------------------------------------------------------");
            Log.print("File DELETED for the P2P Network: " +HashFunction.displayID(f.getInitiator().getID()) +"@"+ f.getInitiator().getPort());
            Log.print("FileID: "+HashFunction.displayID(f.getID()));
            Log.print("FileName: "+f.getFileName());
            Log.print("Deleted by: "+HashFunction.displayID(n.getID())+"@"+n.getPort());
            Log.print("------------------------------------------------------------");
        }
    }

    public void addLocalFile(FileObj fo) {
        localFiles.put(fo.getID(), fo);
        Log.print("------------------------------------------------------------");
        Log.print("File successfully retrieved and saved to local files.");
        Log.print("FileID: "+HashFunction.displayID(fo.getID()));
        Log.print("FileName: "+fo.getFile().getName());
        Log.print("Published by: "+HashFunction.displayID(fo.getPublisher().getID())+"@"+fo.getPublisher().getPort());
        Log.print("------------------------------------------------------------");
    }

    public void printLocalFiles() {
        if(localFiles.isEmpty()){
            Log.print("Local files empty.");
            return;
        }
        
        Log.print("------------------------------------------------------------");
        Log.print("List of files stored locally:");
        localFiles.forEach((Long fid,FileObj file) -> 
            Log.print("FileID: "+file.getID()+" Filename: "+file.getFile().getName()));
        Log.print("-End of List-");
        Log.print("------------------------------------------------------------");
    }

    public void printNetworkFiles() {
        if(publishedFiles.isEmpty()){
            Log.print("No file available.");
            return;
        }
        
        Log.print("------------------------------------------------------------");
        Log.print("List of files in the network:");
        publishedFiles.forEach((Long fid,FileRef file) -> 
            Log.print("FileID: "+file.getID()+" Filename: "+file.getFileName()));
        Log.print("-End of List-");
        Log.print("------------------------------------------------------------");
    }
}
