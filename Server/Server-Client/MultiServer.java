package pl.krakow.up;

import java.io.IOException;
import java.net.ServerSocket;

public class MultiServer {

    private ServerSocket serverSocket;

    public void start(int port){
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Waitming for new client ...");
            while(true){
                new ClientHandler(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
