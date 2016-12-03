package net;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;

public class AvailablePort {
    
    private static final int MIN_PORT = 1025;
    private static final int MAX_PORT = 49151; 
	
    public static int getAvailablePort() {
        int port;
        while(true){
            port = ((int) (Math.random()*(MAX_PORT-MIN_PORT))) + MIN_PORT;
            if(isAvailable(port))
                break;         
       }
        return port;
    }

	
    //from http://stackoverflow.com/questions/434718/sockets-discover-port-availability-using-java
    private static boolean isAvailable(int port){

        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ds != null) {
                ds.close();
            }       

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                /* should not be thrown */
                }
            }
        }
        return false;
    }
}
