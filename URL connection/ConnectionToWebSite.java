package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConnectionToWebSite {

    public ConnectionToWebSite(){}

    public void GetAllLink(String adres){
        // w tym przykładzie zstosowana zostałą biblioteka org.jsoup

        // pobieranie danych ze strony oraz parsowanie danych do obiektu typu Document
        Document doc = Jsoup.parse(GetDataFromWebSite(adres));

        // pobranie nagłówka strony zwracanej przez serwer
        Element naglowek = doc.head();
        // pobranie treści strony
        Element tresc = doc.body();
        // wyszukanie wyszukanie wszystkich linków na stronie
        // szukanie tabów <a> w treści strony zwróconej przez serwer
        Elements linki = tresc.select("a");
        for (Element e : linki){
            System.out.println(e.attr("href"));
        }
    }

    // przykład pobierania linków ze strony nazwiska-polskie.pl
    public void GetAllLinkFromWeb(){
        // pobranie treści ze strony oraz parsowanie do obiektu typu Document
        String adres = "https://nazwiska-polskie.pl";
        Document doc = Jsoup.parse(GetDataFromWebSite(adres));
        // pobieram treść strony
        Element tresc = doc.body();
        // wyszukuje linki odnoszące się do podstron z nazwiskami
        Elements temp = tresc.select(".index");
        ArrayList<String> listaPodstron = new ArrayList<>();
        // sprawdzenie ile elementów zostało znalezionych
        if(temp.size() > 1){
            Elements litery = temp.select("a");
            for (Element e : litery){
                // zapisanie wszystkich znalezionych linków do postron z nazwiskami
                listaPodstron.add(e.attr("href"));
            }
            // odwołanie się do podstrony i uzyskanie wszystkich nazwisk
            for (String podstrona : listaPodstron){
                System.out.println("Odwołanie do podstrony: " + podstrona);
                doc = Jsoup.parse(GetDataFromWebSite(adres+podstrona));
                temp = doc.select(".namesList");
                // sprawdzenie czy został znaleziony element
                if(temp.size()>0){
                    Elements nazwiska = temp.select("a");
                    System.out.println("liczba nazwisk na stronie: " + nazwiska.size());
                }
            }
        }
    }


    public String GetDataFromWebSite(String adres ){
        try{
            URL url = new URL(adres);
            URLConnection connection = url.openConnection();
            // ustawienie parametrów połączenia

            connection.setRequestProperty("Accept", "text/plain");
            // czas oczekiwania na połączenie
            connection.setConnectTimeout(10000);
            // czas pobierania danych
            connection.setReadTimeout(10000);

            // metody wyświetlające informacje o adresie url i połączeniu z serverem
            //GetUrlInfo(url);
            //GetConnetionIngo(connection);

            BufferedReader buff = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;

            StringBuilder sb = new StringBuilder();
            while((line = buff.readLine()) != null){
                sb.append(line);
            }
            buff.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // pobranie szczegłówych danych dotyczacych połączenia
    // zagadnienia prezentowane były na wykłdzie
    public void GetUrlInfo(URL urlInfo) {
        System.out.println("pobranie danych dotyczących adresu i protokołu");
        System.out.println("Protokol:" + urlInfo.getProtocol());
        System.out.println("Info " + urlInfo.getUserInfo());
        String host = urlInfo.getHost();
        if (host != null) {
            int atSign = host.indexOf('@');
            if (atSign != -1) host = host.substring(atSign + 1);
            System.out.println("Host " + host);
        } else {
            System.out.println("Host is null.");
        }
        System.out.println("Port" + urlInfo.getPort());
        System.out.println("Default Port" + urlInfo.getDefaultPort());
        System.out.println("File" + urlInfo.getFile());
        System.out.println("Path " + urlInfo.getPath());

    }

    public void GetConnetionIngo(URLConnection connection){
        System.out.println("Pobranie danych zwracaych przez serwer");
        Map<String, List<String>> mapRequest = connection.getRequestProperties();
        Map<String, List<String>> mapHeaders = connection.getHeaderFields();

        for(String s : mapHeaders.keySet()){
            System.out.print(s + " ");
            List<String> tempList = mapHeaders.get(s);
            for (String vs : tempList){
                System.out.print(vs + " ");
            }
            System.out.println();
        }
    }

}
