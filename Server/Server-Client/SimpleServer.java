package pl.krakow.up;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void start(int port){
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Wait for client");
            clientSocket = serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out.println("Podaj dowolny tekst ");
            String line = in.readLine();
            if ("hellow".equals(line)){
                out.println("hellow you connect to:");
                out.println(serverSocket.getLocalSocketAddress());
            }else{
                out.println("re: " + line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Zatrzymanie serwera");
            stop();
        }
    }

    public void stop(){
        try {
            in.close();
            out.close();
            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
