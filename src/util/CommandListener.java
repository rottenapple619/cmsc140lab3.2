/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import net.Connections;
import net.peer.PeerConnection;
import util.file.FileObj;
import util.file.FileRef;
import util.message.Message;
import util.message.MessageType;
import util.message.UDPMessage;

/**
 *
 * 
 */
public class CommandListener extends Thread{
    
    private static CommandListener instance = null;
    private static boolean modeEntered = true;
    
    private boolean isRunning;
    private final BufferedReader stdIn;
    
    public static CommandListener getInstance() {
        if(instance == null){
            return instance = new CommandListener();
        }
        return instance;
    }
    
    public CommandListener(){
        this.isRunning = true;
        this.stdIn = new BufferedReader(new InputStreamReader(System.in));
    }
    
    @Override
    public void run(){
        Log.print(this.getClass().getName()+" is running...");
        enterMode();
        try{
            while(isRunning){
                try {
                    parse(stdIn.readLine().replaceAll(" ", Message.REGEX));
                } catch (IOException ex) {
                    Logger.getLogger(CommandListener.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }finally{
            if(stdIn!=null){
                try {
                    stdIn.close();
                } catch (IOException ex) {
                    Logger.getLogger(CommandListener.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
   
    }
    
    private void parse(String input){
        String command[] = input.split(Message.REGEX);
        
        // <editor-fold defaultstate="collapsed" desc="MODE">
        if(command[0].equalsIgnoreCase(Command.MODE.toString())){
            try{
                if(!modeEntered){
                    Log.printErr("Cannot change mode.");
                    Log.printErr("Mode is: "+(Connections.isLocal?Mode.LOCAL.toString():Mode.NETWORK.toString()));
                    return;
                }
                    
                if(command[1].equalsIgnoreCase(Mode.LOCAL.toString())){//mode is LOCAL
                    Log.print("Mode is: "+Mode.LOCAL.toString());
                    Connections.initialize(true);
                    modeEntered = false;
                }
                else if(command[1].equalsIgnoreCase(Mode.NETWORK.toString())){//mode is NETWORK
                    Log.print("Mode is: "+Mode.NETWORK.toString());
                    Connections.initialize(false);
                    modeEntered = false;
                }
                else
                    Log.printErr("Unknown mode.");
            }catch(ArrayIndexOutOfBoundsException ex){
                Log.printErr("Unknown mode.");
            }
        }//end MODE
        // </editor-fold>
        
        // <editor-fold defaultstate="collapsed" desc="CREATE">
        else if(command[0].equalsIgnoreCase(Command.CREATE.toString())){
            if(modeEntered){
                enterMode();
                return;
            }
            
            if(Connections.isServer){//check if nagcreate na
                Log.printErr("Only a single instance of INITIATOR can be created.");
            }
            else{
                Connections.instance.initializePeerConnection();
            }
            
        }//end CREATE
        // </editor-fold>
        
        // <editor-fold defaultstate="collapsed" desc="JOIN">
        else if(command[0].equalsIgnoreCase(Command.JOIN.toString())){
            if(modeEntered){
                enterMode();
                return;
            }
            NodeRef initiator = null;

            //command[1] is the initiator ID
            try{
                 initiator = Connections.instance.getInitiatorList().get(HashFunction.toLong(command[1]));//fetch the initiator's data from the initiator list
            }catch(ArrayIndexOutOfBoundsException | NumberFormatException ex){
                Log.printErr("Unknown initiator: ");//+command[1]);
                return;
            }
                    
            if(initiator == null){
                Log.printErr("Unknown initiator: "+command[1]);
                return;
            }
            
            if(Connections.instance.getPeerConnectionList().get(HashFunction.toLong(command[1]))!=null){
                Log.printErr("Already connected to this network.");
                return;
            }
            
            Connections.instance.initializePeerConnection(initiator);
            
        }//end JOIN
        // </editor-fold>
        
        // <editor-fold defaultstate="collapsed" desc="PUBLISH">
        else if(command[0].equalsIgnoreCase(Command.PUBLISH.toString())){
            if(modeEntered){
                enterMode();
                return;
            }
            
            NodeRef initiator = null;

            //command[1] is the initiator ID
            try{
                 initiator = Connections.instance.getInitiatorList().get(HashFunction.toLong(command[1]));//fetch the initiator's data from the initiator list
            }catch(ArrayIndexOutOfBoundsException | NumberFormatException ex){
                Log.printErr("Unknown initiator: ");//+command[1]);
                return;
            }
                    
            if(initiator == null){
                Log.printErr("Unknown initiator: "+command[1]);
                return;
            }
            
            PeerConnection peer = null;
            
            if((peer = Connections.instance.getPeerConnectionList().get(HashFunction.toLong(command[1])))==null){
                Log.printErr("Not connected to "+initiator.getID()+"@"+initiator.getPort());
                return;
            }
            
            publish(peer);
            
        }//end PUBLISH
        // </editor-fold>
        
        // <editor-fold defaultstate="collapsed" desc="DELETE">
        else if(command[0].equalsIgnoreCase(Command.DELETE.toString())){
            if(modeEntered){
                enterMode();
                return;
            }
            
            NodeRef initiator = null;

            //command[1] is the initiator ID
            try{
                 initiator = Connections.instance.getInitiatorList().get(HashFunction.toLong(command[1]));//fetch the initiator's data from the initiator list
            }catch(ArrayIndexOutOfBoundsException | NumberFormatException ex){
                Log.printErr("Unknown initiator: ");//+command[1]);
                return;
            }
                    
            if(initiator == null){
                Log.printErr("Unknown initiator: "+command[1]);
                return;
            }
            
            PeerConnection peer = null;
            
            if((peer = Connections.instance.getPeerConnectionList().get(HashFunction.toLong(command[1])))==null){
                Log.printErr("Not connected to "+initiator.getID()+"@"+initiator.getPort());
                return;
            }
            
            FileRef fRef = null;
            
            try{
                if((fRef = Connections.instance.getPublishedFiles().get(HashFunction.toLong(command[2])))==null){
                    Log.printErr("File not found! ID: "+command[2]);
                    return;
                }
            }catch(ArrayIndexOutOfBoundsException | NumberFormatException ex){
                Log.printErr("File not found!");//+command[1]);
                return;
            }
            
            if(fRef.getInitiator().getID()!=initiator.getID()){
                Log.printErr("File belongs to a different network!");
                Log.printErr("Try DELETE "+HashFunction.displayID(fRef.getInitiator().getID())+" "+HashFunction.displayID(fRef.getID()));
                return;
            }
            
            delete(fRef,peer);
        }//end DELETE
        // </editor-fold>
        
        // <editor-fold defaultstate="collapsed" desc="RETRIEVE">
        else if(command[0].equalsIgnoreCase(Command.RETRIEVE.toString())){
            if(modeEntered){
                enterMode();
                return;
            }
            
            NodeRef initiator = null;

            //command[1] is the initiator ID
            try{
                 initiator = Connections.instance.getInitiatorList().get(HashFunction.toLong(command[1]));//fetch the initiator's data from the initiator list
            }catch(ArrayIndexOutOfBoundsException | NumberFormatException ex){
                Log.printErr("Unknown initiator: ");//+command[1]);
                return;
            }
                    
            if(initiator == null){
                Log.printErr("Unknown initiator: "+command[1]);
                return;
            }
            
            PeerConnection peer = null;
            
            if((peer = Connections.instance.getPeerConnectionList().get(HashFunction.toLong(command[1])))==null){
                Log.printErr("Not connected to "+initiator.getID()+"@"+initiator.getPort());
                return;
            }
            
            FileRef fRef = null;
            
            try{
                if((fRef = Connections.instance.getPublishedFiles().get(HashFunction.toLong(command[2])))==null){
                    Log.printErr("File not found! ID: "+command[2]);
                    return;
                }
            }catch(ArrayIndexOutOfBoundsException | NumberFormatException ex){
                Log.printErr("File not found!");//+command[1]);
                return;
            }
            
            if(fRef.getInitiator().getID()!=initiator.getID()){
                Log.printErr("File belongs to a different network!");
                Log.printErr("Try RETRIEVE "+HashFunction.displayID(fRef.getInitiator().getID())+" "+HashFunction.displayID(fRef.getID()));
                return;
            }
            
            retrieve(fRef,peer);
        }
        // </editor-fold>
        
        else if(command[0].equalsIgnoreCase(Command.COMMAND.toString())){
            if(modeEntered){
                enterMode();
                return;
            }
            Command.print();
        }
        
        else if(command[0].equalsIgnoreCase(Command.FILESLOCAL.toString())){
            if(modeEntered){
                enterMode();
                return;
            }
            Connections.instance.printLocalFiles();
        }
        
        else if(command[0].equalsIgnoreCase(Command.FILESNETWORK.toString())){
            if(modeEntered){
                enterMode();
                return;
            }
            Connections.instance.printNetworkFiles();
        }
        
        //UNKNOWN COMMAND
        else{
            if(modeEntered){
                enterMode();
                return;
            }
            Log.printErr("Unknown command.");
        }//end
    }
    
    private static void enterMode() {
        Log.print("Please enter mode: ");
    }
    
    private void publish(PeerConnection peer) {
        
        JFileChooser fc = new JFileChooser();//open a file chooser dialog
        File file;
        FileObj fileObj;
        int actionTaken = fc.showOpenDialog(null);
        if(actionTaken == JFileChooser.APPROVE_OPTION){
            file = fc.getSelectedFile();
            fileObj = new FileObj(file,peer.getInitiator(),peer.getNode());
            peer.getPublishedFiles().put(fileObj.getID(), fileObj);
        }
        else{
            System.err.println("Publication aborted.");
            return;
        }
            
        Log.print("------------------------------------------------------------");
        Log.print("Publishing a file to the P2P Network: "+HashFunction.displayID(peer.getInitiator().getID())+"@"+peer.getInitiator().getPort());
        Log.print("FileID: "+HashFunction.displayID(fileObj.getID()));
        Log.print("Filename: "+fileObj.getFile().getName());
        Log.print("------------------------------------------------------------");
        
        FileRef fRef = new FileRef(fileObj.getID(),fileObj.getFile().getName(),peer.getInitiator(),peer.getNode());
        UDPMessage msg = new UDPMessage(MessageType.PUBLISH,(Node)peer,fRef);
        
        peer.getOutgoing().send(msg, peer.getInitiator());
    }
    
    private void delete(FileRef fRef, PeerConnection peer) {
        Log.print("------------------------------------------------------------");
        Log.print("Deleting a file to the P2P Network: "+HashFunction.displayID(peer.getInitiator().getID())+"@"+peer.getInitiator().getPort());
        Log.print("FileID: "+HashFunction.displayID(fRef.getID()));
        Log.print("Filename: "+fRef.getFileName());
        Log.print("------------------------------------------------------------");
        
        UDPMessage msg = new UDPMessage(MessageType.DELETE,(Node)peer,fRef);
        
        peer.getOutgoing().send(msg, peer.getInitiator());
    }
    
    public void stopThread(){
        this.isRunning = false;
    }

    private void retrieve(FileRef fRef, PeerConnection peer) {
        Log.print("------------------------------------------------------------");
        Log.print("Retrieving a file to the P2P Network: "+HashFunction.displayID(peer.getInitiator().getID())+"@"+peer.getInitiator().getPort());
        Log.print("FileID: "+HashFunction.displayID(fRef.getID()));
        Log.print("Filename: "+fRef.getFileName());
        Log.print("------------------------------------------------------------");
        
        UDPMessage msg = new UDPMessage(MessageType.RETRIEVE,(Node)peer,fRef);
        
        peer.getOutgoing().send(msg, peer.getInitiator());
    }

    



        
}
