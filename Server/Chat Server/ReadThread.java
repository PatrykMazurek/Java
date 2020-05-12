package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReadThread extends Thread {

    private BufferedReader buffer;
    private Socket socket;
    private ChatClient client;

    public ReadThread(Socket cSocket, ChatClient cClient){
    }

    @Override
    public void run() {

    }
}
