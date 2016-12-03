/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import net.Connections;

/**
 *
 * @author Marz
 */
public class HashFunction {

    public static final int LOCAL_KEY = 8;
    public static final int NETWORK_KEY = 32;
    
    
    public static Long getHashCode(String input){
        
        if(Connections.isLocal)
            return getRandomHashCode();
        
        int code = 17;
        int KEY = NETWORK_KEY;
        
        for(int i=0; i<input.length(); i++){
            char c = input.charAt(i);
            code = (31*code) + (int)c;
        }
        
        long hash = (long) (code + (Math.pow(2, KEY-1)-1));
        
        return hash;
    }
    
    private static Long getRandomHashCode(){
        return (long) (Math.random()*(Math.pow(2, LOCAL_KEY)));
    }
    
    public static String toHexString(long n){
        return Long.toHexString(n);
    }
    
    public static Long toLong(String s){
        return Connections.isLocal? Long.parseLong(s):Long.parseLong(s, 16);
    }

    public static String displayID(long id) {
        return Connections.isLocal? id+"":toHexString(id);
    }
}
