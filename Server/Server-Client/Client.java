package pl.krakow.up;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket clientSocket;
    private DataInputStream din;
    private DataOutputStream dout;
//    private PrintWriter out;
//    private BufferedReader in;

    public void startConnect(String ip, int port){
        try {
            clientSocket = new Socket(ip, port );
            din = new DataInputStream(clientSocket.getInputStream());
            dout = new DataOutputStream(clientSocket.getOutputStream());
//            out = new PrintWriter(clientSocket.getOutputStream());
//            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void seendMessage(){
        String resp = null;
        String line = null;
        Scanner scan = new Scanner(System.in);

        while(true){
            try {
                resp = din.readUTF();
                System.out.println(resp);
                line = scan.nextLine();
                if ("e".equals(line)){
                    dout.writeUTF(line);
                    dout.flush();
                    break;
                }
                dout.writeUTF(line);
                dout.flush();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public void stopConnection(){
        try{
            dout.writeUTF("exit");
            din.close();
            dout.close();
            clientSocket.close();
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("problem z połączeniem");
        }
    }
}
