package com.company;

import com.jcraft.jsch.*;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.commons.net.tftp.TFTPClient;

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
}
