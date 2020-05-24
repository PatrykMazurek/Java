package com.company;

import com.jcraft.jsch.*;

import java.io.*;
import java.nio.file.Files;
import java.util.Scanner;
import java.util.Vector;

public class AppFTPClient {

    private JSch jsch;
    private Session session;
    private FTPData ftpData;
    private Channel channel;
    private ChannelSftp channelSftp;
    private String home;
    private String host;

    public AppFTPClient(String nHost){
        this.host = nHost;
        this.jsch = new JSch();
        // utworzenie obiektu z danymi do logowania
        this.ftpData = new FTPData();
        Start();
    }

    public void Start(){

        if(ConnectToFTP()){
            boolean working = true;
            System.out.println("Nawiązano połączenie serwerem ");
            System.out.println("Proszę wybrać opcję:");
            System.out.println("1) wyświetl wszystkie pliki");
            System.out.println("2) pobierz plik z serwera");
            System.out.println("3) wyślij plik na serwer");
            System.out.println("4) usuń plik");
            System.out.println("5) zmień nazwę pliku");
            System.out.println("6) przejdz do katalogu");
            System.out.println("q) wyjście");

            Scanner input = new Scanner(System.in);
            while(working){
                try {
                    System.out.println("Aktualna lokalizacja: " + channelSftp.pwd());
                    switch (input.nextLine()){
                        case "1":
                            System.out.println("Podaj lokliację lub pozostaw puste");
                            String location = input.nextLine();
                            ShowAllFile(location);
                            break;
                        case "2":
                            System.out.println("Podaj lokalizację pliku na serwerze:");
                            String servweFile = input.nextLine();
                            System.out.println("Podaj loklizację do zapisania ");
                            String locationDest = input.nextLine();
                            DownloadFile(servweFile, locationDest);
                            break;
                        case "3":
                            System.out.println("Podaj lokalizację pliku z dysku:");
                            String localFile = input.nextLine();
                            System.out.println("Podaj lokalizację na serwerze:");
                            String serverDest = input.nextLine();
                            UpLoadFile(localFile, serverDest);
                            break;
                        case "4":
                            System.out.println("Proszę o podanie pliku do usunięcia:");
                            String delFile = input.nextLine();
							RemoveFile(delFile)
                            break;
                        case "5":
                            System.out.println("Proszę podać plik do zmiany nazwy:");
                            String oldName = input.nextLine();
                            System.out.println("Proszę podać nową nazwe pliku:");
                            String newName = input.nextLine();
                            RenameFile(channelSftp.pwd(), oldName, newName);
                            break;
                        case "6":
                            System.out.println("Proszę podaćkatalog:");
                            String folder = input.nextLine();
                            MoveFromCatalog(folder);
                            break;
                        case "q":
                            System.out.println("kończę prace");
                            working = false;
                            break;
                        default:
                            System.out.println("nie podano odpowiedniej komendy");
                            break;
                    }
                } catch (SftpException e) {
                    e.printStackTrace();
                }
            }
            DisconnectFTP();
        }else{
            System.out.println("Nie udało się nawiązać połączenia z serwerem: ");
            System.out.println(host);
        }
    }
	
	    public boolean ConnectToFTP(){
        session = null;
        try{
            session = jsch.getSession(ftpData.GetLogin(), host, 22);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(ftpData.GetPassword());
            session.connect(10000);
            channel = session.openChannel("sftp");
            channel.connect();
            channelSftp = (ChannelSftp) channel;
            home = channelSftp.getHome() + "/public_html/";
            channelSftp.cd(home);
            return true;
        } catch (JSchException e) {
            e.printStackTrace();
            return false;
        } catch (SftpException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void DisconnectFTP(){
        if (session.isConnected()){
            channelSftp.exit();
            channel.disconnect();
            session.disconnect();
            System.out.println("Zakończyłem prace");
        }
    }

    public void ShowAllFile(String location){
        try{
            String tempLocation;
            if (location.isEmpty()){
                tempLocation = channelSftp.pwd();
            }else{
                tempLocation = channelSftp.pwd() + "/" + location;
            }

            Vector listAllFile = channelSftp.ls(tempLocation);
            for (int i = 0; i< listAllFile.size(); i++){
                ChannelSftp.LsEntry file = (ChannelSftp.LsEntry) listAllFile.get(i);
                if(!file.getFilename().endsWith(".")){
                    System.out.println(file.getFilename() + " " + file.getAttrs().getSize());
                }
            }
        } catch (SftpException e) {
            e.printStackTrace();
        }
    }

    public void DownloadFile(String fileLocation, String destLocation){
        try{
            String tempLocation = null;
            if(fileLocation.contains("/")){
                if (channelSftp.ls(channelSftp.pwd() + "/" + fileLocation).size() > 0){
                    tempLocation = channelSftp.pwd() + "/" + fileLocation;
                }
            }else{
                tempLocation = channelSftp.pwd()+"/"+fileLocation;
            }
            channelSftp.get(tempLocation, destLocation);
        } catch (SftpException e) {
            e.printStackTrace();
            System.out.println("Plik nie istnieje " + fileLocation);
        }
    }

    public void UpLoadFile(String fileLocation, String destLoccation){
        try{
            String[] tempLocation;
            if(destLoccation.contains("/")){
                tempLocation = destLoccation.split("/");
                for (String s : tempLocation){
                    if(s.length() > 0 || s.contains(".")){
                        try{
                            channelSftp.cd(s);
                        } catch (SftpException e) {
                            System.out.println("Kataloh nie istnieje");
                            System.out.println("Czy utworzyć katalog: (T/N) " + s);
                            Scanner in = new Scanner(System.in);
                            if(in.nextLine().equals("T")){
                                channelSftp.mkdir(s);
                                channelSftp.cd(s);
                            }else{
                                System.out.println("Przerwano wysłanie pliku");
                                break;
                            }
                            e.printStackTrace();
                        }
                    }
                }
            }
            channelSftp.put(fileLocation, channelSftp.pwd());
        } catch (SftpException ex) {
            ex.printStackTrace();
        }
    }

    public void RemoveFile(String fileName){
        try{
            if(channelSftp.ls(channelSftp.pwd() + "/" + fileName).size() > 0){
                Scanner in = new Scanner(System.in);
                System.out.println("Czy chcesz usuną plik: (T/N) ?");
                System.out.println(channelSftp.pwd() + "/" + fileName);
                if(in.nextLine().equals("T")){
                    channelSftp.rm(channelSftp.pwd() + "/" + fileName);
                    System.out.println("Usunięto plik");
                }
            }
        } catch (SftpException e) {
            e.printStackTrace();
            System.out.println("Plik nie istnieje");
        }
    }

    public void RenameFile(String location, String oldFile, String newFile){
        try{
            channelSftp.rename(location+"/"+oldFile, location+"/"+newFile);
        } catch (SftpException e) {
            e.printStackTrace();
            System.out.println("Wystąpił problem ze zmianą nazwy pliku");
        }

    }

    public void MoveFromCatalog(String folder){
        try{
            channelSftp.cd(folder);
        } catch (SftpException e) {
            //e.printStackTrace();
            System.out.println("Nie ma takiego katalogu: " + folder);
        }
    }
	
	
}
