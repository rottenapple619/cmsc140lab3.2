/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.message;

import util.Node;
import util.NodeRef;

/**
 *
 * 
 */
public class UDPMessage extends Message{

    private final Node sender;
    
    public UDPMessage(MessageType type, Node node) {
        super(type);        
        this.sender = node;
    }

    public UDPMessage(MessageType type, Node node, Object attachment) {
        super(type,attachment);
        this.sender = node;
    }
    
    public Node getNode(){
        return this.sender;
    }
    
    public NodeRef getSender(){
        return this.sender.getNode();
    }
    
    public NodeRef getInitiator(){
        return this.sender.getInitiator();
    }
}
