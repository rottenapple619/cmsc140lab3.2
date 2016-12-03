/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.Serializable;
import net.AvailablePort;
import net.Connections;



/**
 *
 * 
 */
public class Node implements Serializable{

    private final NodeRef node;//this node;
    private NodeRef i_node;//initiator node
    private transient NodeRef p_node;//predecessor node
    private transient NodeRef s_node;//successor node
    
    private transient NodeRef prev_s_node;//previous successor
    private transient NodeRef prev_p_node;//previous predecessor
    
    private transient final boolean isServer;
    
    public Node(boolean b){
        this.isServer = b;
        
        int port = AvailablePort.getAvailablePort();
        
        this.node = new NodeRef(Connections.id,port,Connections.address);
        this.p_node = new NodeRef();
        this.prev_p_node = new NodeRef();
        this.prev_s_node = new NodeRef();
        
        if(isServer){
            this.i_node = this.node;
            this.s_node = this.node;
        }
        else{
            this.i_node = new NodeRef();
            this.s_node = new NodeRef();
        }
    }
    
    public boolean isServer(){
        return this.isServer;
    }
    
    public NodeRef getNode(){
        return this.node;
    }
    
    public NodeRef getPredecessor(){
        return this.p_node;
    }
    
    public NodeRef getSuccessor(){
        return this.s_node;
    }
    
    public NodeRef getInitiator(){
        return this.i_node;
    }
    
    public NodeRef getPreviousPredecessor(){
        return this.prev_p_node;
    }
    
    public NodeRef getPreviousSuccessor(){
        return this.prev_s_node;
    }
    
    public void setPredecessor(NodeRef new_node){
        this.prev_p_node = this.p_node;
        this.p_node = new_node;
    }
    
    public void setSuccessor(NodeRef new_node){
        this.prev_s_node = this.s_node;
        this.s_node = new_node;
    }
    
    public void setInitiator(NodeRef new_node){
        this.i_node = new_node;
    }
    
    
    //this node
    public Long getID(){
        return this.node.getID();
    }
    
    public String getAddress(){
        return this.node.getAddress();
    }
    
    public int getPort(){
        return this.node.getPort();
    }
    
    public void setID(long id){
        this.node.setID(id);
    }
    
    public void setAddress(String address){
        this.node.setAddress(address);
    }
    
    public void setPort(int port){
        this.node.setPort(port);
    }
    
    public static void printStatus(Node node, boolean isInit){
        Log.print("------------------------------------------------------------");
        if(node.isServer && isInit){
            Log.print("Created a new network with ID: "+HashFunction.displayID(node.getID())+"@"+node.getPort());
            Log.print("Initially setting parameters:");
        }
        else{
            Log.print("P E E R   S T A T U S   U P D A T E D");
            Log.print("Your ID: "+HashFunction.displayID(node.getID())+"@"+node.getPort());
        }
        
        Log.print("Predecessor: "+HashFunction.displayID(node.getPredecessor().getID())+"@"+
                node.getPredecessor().getPort());
        Log.print("Successor: "+HashFunction.displayID(node.getSuccessor().getID())+"@"+
                node.getSuccessor().getPort());
        Log.print("------------------------------------------------------------");
    }
}
