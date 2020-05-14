package com.company;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient {

    private String hostName;
    private int port;
    private String userName;

    public ChatClient (String hostName, int port){
        this.hostName = hostName;
        this.port = port;
    }

    public void Execute(){
        try {
            Socket socket = new Socket(hostName, port);

            System.out.println("Połączenie z czatem udało sie");

            new WriterThread(socket, this).start();
            new ReadThread(socket, this).start();
        } catch (IOException e) {
            System.out.println("Błąd po stronie klienta " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    String GetUserName(){
        return this.userName;
    }

}
