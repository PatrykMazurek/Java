package com.company;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ChatServer {

    private Set<String> userNames = new HashSet<>();
    private Set<UserThread> userThreads = new HashSet<>();

    public ChatServer(){
    }

    public void execute(){
        try{
            int port = 5060;
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Oczekuje na nowych użytowników");

            while (true){
                Socket socket = serverSocket.accept();
                System.out.println("Nowy użytownik podłączył się ");

                UserThread newUser = new UserThread(socket, this);
                userThreads.add(newUser);
                newUser.start();
            }
        } catch (IOException e) {
            System.out.println("Bląd serwera " + e.getMessage());
            e.printStackTrace();
        }

    }

    public void broadcastUser(String message, UserThread executedUser){
        for (UserThread user : userThreads){
            if(user != executedUser){
                user.SendMessage(message);
            }
        }
    }

    public void addUserName(String userName){
        userNames.add(userName);
    }

    public void removeUser(String userN, UserThread userT){
        boolean removed = userNames.remove(userN);
        if(removed){
            userThreads.remove(userT);
            System.out.println("Użytkownik " + userN + " opuścił czat");
        }
    }

    Set<String> getUserNames(){
        return this.userNames;
    }

    boolean hasUser(){
        return !this.userNames.isEmpty();
    }

    boolean isUser(String userName){
        return this.userNames.contains(userName);
    }
}
