package com.company;

import java.io.IOException;
import java.net.*;

public class UDPClient {

    public UDPClient(){

        String hostName = "debian";
        int port = 450;

        try{
            InetAddress address = InetAddress.getByName(hostName);
            DatagramSocket socket = new DatagramSocket();
            while(true){
                DatagramPacket request = new DatagramPacket(new byte[1], 1, address, port);
                socket.send(request);

                byte[] buffer = new byte[512];
                DatagramPacket response = new DatagramPacket(buffer, buffer.length);
                socket.receive(response);

                String quote = new String(buffer, 0, request.getLength());

                System.out.println(quote);
                System.out.println();

                Thread.sleep(5000);
            }

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

}
