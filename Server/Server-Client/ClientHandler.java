package pl.krakow.up;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Random;

public class ClientHandler extends Thread {

    private Socket clientSocket;
//    private PrintWriter out;
    private DataOutputStream dout;
    private DataInputStream din;
//    private BufferedReader in;

    public ClientHandler(Socket socket){

        this.clientSocket = socket;
        System.out.println("Nowe połączenie " + this.clientSocket.getInetAddress());
    }

    public void run(){
        try {
//            out = new PrintWriter(clientSocket.getOutputStream(), true);
//            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            dout = new DataOutputStream(clientSocket.getOutputStream());
            din = new DataInputStream(clientSocket.getInputStream());
            boolean work = true;
            String line;
            while(work){
                dout.writeUTF("Proszę wybrać opcję:\n" +
                        "1) podaj aktualną godzinę\n" +
                        "2) wylosuj liczbę\n" +
                        "3) prześlij wiadomość\n" +
                        "e) zakończ");
//                dout.flush();
                line = din.readUTF();
                switch (line){
                    case "1":
                        LocalDateTime local = LocalDateTime.now();
                        dout.writeUTF(local + "");
//                        dout.flush();
                        break;
                    case "2":
                        Random r = new Random();
                        dout.writeUTF(r.nextInt()+ "");
//                        dout.flush();
                        break;
                    case "3":
                        dout.writeUTF("Podaj wiadomość do przesłania");
                        dout.flush();
                        line = din.readUTF();
                        dout.writeUTF("Dziękuje za przesłanie wiadomości\n" +
                                        "twoja wiadomość:\n" + line);
                        dout.flush();
                        break;
                    case "e":
                        dout.writeUTF("bye");
                        dout.flush();
                        work = false;
                        break;
                    default:
                        dout.writeUTF("nie wybrano odpowiedniego polecenia");
                        dout.flush();
                        break;
                }
            }
            System.out.println("Zakończenie połączenia z " + this.clientSocket.getInetAddress());
            din.close();
            dout.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

}
