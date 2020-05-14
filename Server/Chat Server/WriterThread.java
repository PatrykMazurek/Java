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
        this.socket = socket;
        this.client = client;

        try{
            OutputStream out = socket.getOutputStream();
            writer = new PrintWriter(out, true);
        } catch (IOException e) {
            System.out.println("Błąd podczas pobierania obiektu OutputStream " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Scanner console = new Scanner(System.in);
        System.out.println("Proszę o podanie nazwy użytkownika:");
        String userName = console.nextLine();
        while (userName.isEmpty()){
            System.out.println("Proszę o podanie nazwy użytkownika:");
            userName = console.nextLine();
        }
        writer.println(userName);

        String text;

        do{
            text = console.nextLine();
            writer.println(text);
        } while (!text.equals("exit"));
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("Błąd podczas zamykania połączenia z czatem " + e.getMessage());
            e.printStackTrace();
        }
    }
}
