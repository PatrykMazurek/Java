package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class PageConnection {

    public PageConnection(){
    }

    public StringBuilder getWebPage(String address){
        try {
            URL url = new URL(address);
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestProperty("User-Agent", "Mozilla");
            conn.setRequestProperty("Content-Type", "text/html");

            InputStream in = conn.getInputStream();
            BufferedReader buffor = new BufferedReader(new InputStreamReader(in));

            String line;
            StringBuilder builder = new StringBuilder();
            while((line = buffor.readLine())!= null){
                builder.append(line);
            }
            buffor.close();
            in.close();
            return builder;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return  null;
        }

    }

}
