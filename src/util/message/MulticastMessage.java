/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.message;

import util.NodeRef;

/**
 *
 * 
 */
public class MulticastMessage extends Message{

    private NodeRef sender = null;

    public MulticastMessage(MessageType type){
        super(type);
    }
    
    public MulticastMessage(MessageType type, NodeRef sender){
        super(type);
        this.sender = sender;
    }

    public MulticastMessage(MessageType type, Object attachment){
        super(type,attachment);
    }
    
    public MulticastMessage(MessageType type, NodeRef sender, Object attachment){
        super(type,attachment);
        this.sender = sender;
    }
    
    public NodeRef getSender() {
        return this.sender;
    }
    
}
