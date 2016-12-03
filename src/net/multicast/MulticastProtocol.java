/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.multicast;

import java.util.Map;
import net.Connections;
import net.peer.PeerConnection;
import util.HashFunction;
import util.Log;
import util.NodeRef;
import util.file.FileObj;
import util.file.FileRef;
import util.message.MessageType;
import util.message.MulticastMessage;

/**
 *
 * 
 */
public class MulticastProtocol {

    public static void check(Object msg) {
        MulticastMessage mcMessage;
        NodeRef sender;
        FileRef fRef;
        Connections con = Connections.instance;
        
        if(msg instanceof MulticastMessage){
            mcMessage = (MulticastMessage) msg;
            sender = mcMessage.getSender();
            
            switch(mcMessage.getMessage()){
                
                case CREATE:
                    if(con.getInitiatorList().containsKey(sender.getID())){
                        return;
                    }
                    else if((Connections.isLocal&&!Connections.isServer)?false:Connections.id == sender.getID()){
                        con.getInitiatorList().put(sender.getID(), sender);
                        return;
                    }
                    Log.print("Received Message: A network by "+
                            HashFunction.displayID(sender.getID())+"@"+
                            sender.getPort()+" has been created");
                    con.getInitiatorList().put(sender.getID(), sender);
                    break;
                case PUBLISH:
                    fRef = (FileRef) mcMessage.getAttachment();
                    if(con.getPublishedFiles().get(fRef.getID())!=null/*||Connections.id == sender.getID()*/){
                        return;
                    }
                    con.addFileRef(fRef);
                    break;
                case FILESNETWORK:
                    for(Map.Entry<Long, PeerConnection> peer: con.getPeerConnectionList().entrySet()){
                        for(Map.Entry<Long, FileObj> fo: peer.getValue().getPublishedFiles().entrySet()){
                            fRef = FileObj.createRef(fo.getValue());
                            MulticastMessage mm = new MulticastMessage(MessageType.PUBLISH,fRef);
                            Connections.multicastConnection.getOutgoing().send(mm);
                        }
                    }
                    break;
                case DELETE:
                    fRef = (FileRef) mcMessage.getAttachment();
                    con.deleteFileRef(fRef,mcMessage.getSender());
                    break;
                        
            }
            
        }
        else{
            Log.print("Received an unknown Multicast message."+msg.toString());
        }

    }
    
}
