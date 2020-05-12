package com.company;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class WriterThread extends Thread {

    private PrintWriter writer;
    private Socket socket;
    private ChatClient client;

    public WriterThread(Socket socket, ChatClient client){
    }

    @Override
    public void run() {

    }
}
