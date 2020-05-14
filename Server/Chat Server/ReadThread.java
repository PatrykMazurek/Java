package com.company;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReadThread extends Thread {

    private BufferedReader buffor;
    private Socket socket;
    private ChatClient client;

    public ReadThread(Socket cSocket, ChatClient cClient){
        this.socket = cSocket;
        this.client = cClient;

        try{
            InputStream in = socket.getInputStream();
            buffor = new BufferedReader(new InputStreamReader(in, "UTf-8"));

        } catch (IOException e) {
            System.out.println("błąd podczas pobierania obiektu InputStream " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true){
            try{
                if(!socket.isClosed()){
                    String response = buffor.readLine();
                    System.out.println("\n" + response);

                    if (client.GetUserName() != null){
                        System.out.println("[ " + client.GetUserName() + " ]: " );
                    }
                }
                else{
                    System.out.println("Połączenie zakończone ");
                    break;
                }
            } catch (IOException e) {
                System.out.println("Błąd odczytu wiadomości " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
