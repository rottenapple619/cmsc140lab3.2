/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.file;

import util.NodeRef;

/**
 *
 * 
 */
public class FileRef extends util.file.File{

    private final String fileName;
    
    public FileRef(Long id, String fileName, NodeRef initiator, NodeRef publisher){
        super(initiator,publisher);
        this.file_id = id;
        this.fileName = fileName;
    }
    
    public String getFileName(){
        return this.fileName;
    }
    
    
}
