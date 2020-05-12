package com.company;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient {

    private String hostName;
    private int port;
    private String userName;

    public ChatClient (String hostName, int port){

    }

    public void Execute(){

    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    String GetUserName(){
        return this.userName;
    }

}
