/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.peer;

import net.Connections;
import util.NodeRef;
import util.message.MessageType;
import util.message.MulticastMessage;

/**
 *
 * 
 */
class PeerNotifier extends Thread{

    private static final long DELAY = 3000;
    
    private boolean isRunning = false;
    private final NodeRef node;//the initiator
    private final MulticastMessage notification;
    
    public PeerNotifier(NodeRef node){
        
        this.node = node;
        this.notification = new MulticastMessage(MessageType.CREATE, this.node);
        this.isRunning = true;
        
    }
    
    @Override
    @SuppressWarnings("SleepWhileInLoop")
    public void run(){
        while(isRunning){
            try {
                Thread.sleep(DELAY);
                Connections.multicastConnection.getOutgoing().send(notification);
            } catch (InterruptedException ex) {
                //do nothing
            }

        }
    }
    public void stopThread(){
        this.interrupt();
        this.isRunning = false;
    }
}
