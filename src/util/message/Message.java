/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.message;

import java.io.Serializable;


/**
 *
 * 
 */
public class Message implements Serializable{

    public static transient final String REGEX = "_~";
    
    private Object attachment;    
    private final MessageType type;
    
    public Message(MessageType type){
        this.type = type;
    }
    
    public Message(MessageType type,Object attach){
        this.type = type;
        this.attachment = attach;
    }
    
    public MessageType getMessage(){
        return this.type;
    }
    
    public Object getAttachment(){
        return this.attachment;
    }
}
