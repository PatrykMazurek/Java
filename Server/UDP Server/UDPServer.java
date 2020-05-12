package com.company;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UDPServer {

    public UDPServer(){
        try{
            DatagramSocket socket = new DatagramSocket(5051);
            List<String> listQuotes = new ArrayList<String>();
            Random rand = new Random();
            int port = 5051;

            while (true) {
                DatagramPacket request = new DatagramPacket(new byte[1], 1);
                socket.receive(request);
                String message = String.valueOf(rand.nextDouble()) ;
                byte[] buffer = message.getBytes();

                InetAddress clientAddress = request.getAddress();
                int clientPort = request.getPort();
                System.out.println("wysyłam wiadomość do klienta " + clientAddress + " na porcie " + clientPort);
                DatagramPacket response = new DatagramPacket(buffer, buffer.length, clientAddress, clientPort);
                socket.send(response);
            }

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
