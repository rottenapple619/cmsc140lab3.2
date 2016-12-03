package util;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * 
 */
public enum Command {
    COMMAND         ("Shows the list of available commands", "COMMAND"),
    CREATE          ("Creates a new P2P network", "CREATE"),
    DELETE          ("Deletes a file from the P2P Network", "DELETE<space>InitiatorID<space>FileID"),
    //EXIT            ("Exit", "EXIT"),
    //FILESKEPT       ("Displays the list of files that you keep in the P2P Network","FILESKEPT<space>InitiatorID+'@'+InitiatorPORT"),
    FILESLOCAL      ("Displays the list of files in your local machine","FILESLOCAL"),
    FILESNETWORK    ("Request & display the list of files in the Network","FILESNETWORK"),//"FILESNETWOK<space>InitiatorID"),
    JOIN            ("Joins a P2P Network", "JOIN<space>InitiatorID"),
    PUBLISH         ("Publish a file to the P2P Network", "PUBLISH<space>InitiatorID"),
    RETRIEVE        ("Retrieves a file from the P2P Network", "RETRIEVE<space>InitiatorID<space>FileID"),
    NETWORKS        ("Displays list of available P2P networks", "NETWORKS"),
    MODE            ("Local/Network", "MODE<space>mode");

    
    public static void print() {
        Log.print("------------------------------------------------------------");
        Log.print("List of Available Commands: ");
        for(Command c: Command.values()){
                Log.print("");
                Log.print(c.toString()+"\n\tDescription - "+c.getDescription()
                                    +"\n\tSyntax - "+c.getSyntax());
        }
        Log.print("------------------------------------------------------------");
    }
    
    
    private final String description,syntax;
    
    Command(String description, String syntax){
        this.description = description;
        this.syntax = syntax;
    }
    
    String getDescription(){
        return this.description;
    }
    String getSyntax(){
        return this.syntax;
    }
}
