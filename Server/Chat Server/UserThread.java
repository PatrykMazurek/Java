package com.company;

import java.io.*;
import java.net.Socket;

public class UserThread extends Thread {

    private Socket socket;
    private ChatServer server;
    private PrintWriter writer;

    public UserThread(Socket socket, ChatServer server){
        this.socket = socket;
        this.server = server;
    }

    public void run(){
        try{
            InputStream in = socket.getInputStream();
            BufferedReader buffor = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            OutputStream out = socket.getOutputStream();
            writer = new PrintWriter(out, true);

            PrintUser();

            String userName = buffor.readLine();
            server.addUserName(userName);

            setName("Witaj uzytkowniku " + userName + " Zapraszamy do rozmowy");
            String serverMessage = "Nowy użytkownik " + userName;
            server.broadcastUser(serverMessage, this);

            String clientMessage;

            do{
                clientMessage = buffor.readLine();
                serverMessage = "[ " + userName + " ] " + clientMessage;
                server.broadcastUser(serverMessage, this);
            } while (!clientMessage.equals("exit"));

            serverMessage = "Użytkownik " + userName + " opuścił czat";
            server.broadcastUser(serverMessage, this);

            server.removeUser(userName, this);
            socket.close();

        } catch (IOException e) {
            System.out.println("Błąd w części UserThread " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void PrintUser(){
        if(server.hasUser()){
            writer.println("Użytkownicy podłączeni: " + server.getUserNames());
        }else{
            writer.println("Brak użytkowników ");
        }
    }

    public void SendMessage(String message){
        writer.println(message);
    }
}
