package com.company;

import java.io.*;
import java.net.Socket;

public class UserThread extends Thread {

    private Socket socket;
    private ChatServer server;
    private PrintWriter writer;

    public UserThread(Socket socket, ChatServer server){

    }

    public void run(){

    }

    public void PrintUser(){

    }

    public void SendMessage(String message){
        writer.println(message);
    }
}
