/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.message;

/**
 *
 * 
 */
public enum MessageType {
    CREATE,
    PUBLISH,
    DELETE,
    DELETEFAIL,
    DELETEFILEPUBLISHER,
    FINDSUCCESSOR,
    TELLSUCCESSOR,
    TELLPREDECESSOR,
    TELLPUBLISHER,
    FILESNETWORK, 
    RETRIEVE,
    FAILRETRIEVE,
    INIT_COPY, 
    INIT_SEND, 
    INIT_RECEIVE, 
    TRANS_REF
}
