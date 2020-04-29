package com.company;

public class FTPData {

    private String login;
    private String password;

    public FTPData(){
        this.login = "";        // login do klienta FTP
        this.password = "";     // hasło do klienta FTP
    }

    public String GetLogin(){
        return this.login;
    }

    public String GetPassword(){
        return this.password;
    }
}
