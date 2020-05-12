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

    }

    public void broadcastUser(String message, UserThread executedUser){

    }

    public void addUserName(String userName){

    }

    public void removeUser(String userN, UserThread userT){

    }

    Set<String> getUserNames(){
        return this.userNames;
    }

    boolean hasUser(){
        return !this.userNames.isEmpty();
    }

}
