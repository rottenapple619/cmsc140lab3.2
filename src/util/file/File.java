/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.file;

import java.io.Serializable;
import util.NodeRef;

/**
 *
 * 
 */
public class File implements Serializable{
    
    private final NodeRef initiator;//the P2P network where this file is published
    private final NodeRef publisher;
    protected long file_id;
    
    public File(NodeRef initiator, NodeRef publisher){
        this.publisher = publisher;
        this.initiator = initiator;
    }
    
    public NodeRef getPublisher(){
        return this.publisher;
    }
    
    public NodeRef getInitiator(){
        return this.initiator;
    }
    
    public Long getID(){
        return this.file_id;
    }
}
