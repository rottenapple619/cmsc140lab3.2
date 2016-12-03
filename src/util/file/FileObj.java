/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.file;

import java.io.File;
import util.HashFunction;
import util.NodeRef;


/**
 *
 * 
 */
public class FileObj extends util.file.File{


    private final File file;
    
    private transient static FileRef fr;
    
    public FileObj(File f, NodeRef initiator, NodeRef publisher){
        super(initiator,publisher);
        this.file = f;
        this.file_id = HashFunction.getHashCode(this.file.getName());
    }
    
    public static FileRef createRef(FileObj fo) {
        return (fr = new FileRef(fo.getID(),fo.getFile().getName(),fo.getInitiator(),fo.getPublisher()));
    }
    
    public File getFile() {
        return this.file;
    }
}
