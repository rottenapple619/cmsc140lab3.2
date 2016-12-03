/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.Serializable;

/**
 *
 * 
 */
public class NodeRef implements Serializable{

    private int node_port;
    private String node_address;
    private long node_id; 
    
    public NodeRef(long id, int port, String address){
        this.node_id = id;
        this.node_port = port;
        this.node_address = address;
    }

    public NodeRef(){
        this.node_id = 0;
        this.node_port = 0;
        this.node_address = "";
    }
    

    //getters & setters
    public void setID(long id){
        this.node_id = id;
    }
    
    public void setPort(int port){
        this.node_port = port;
    }
    
    public void setAddress(String address){
        this.node_address = address;
    }
    
    public long getID(){
        return this.node_id;
    }
    
    public int getPort(){
        return this.node_port;
    }
   
    public String getAddress(){
        return this.node_address;
    }
}
