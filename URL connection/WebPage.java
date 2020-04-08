package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class WebPage {

    public WebPage(){}

    // url-e do wykorzystania
    //"http://api.nbp.pl/api/exchangerates/tables/A/" - inna usługa udostępniająca dane w formacie XML i JSON
    //"http://ux.up.krakow.pl/~pmazurek/java/read.php" - odczytwanie postów po wysłaniu własnego numeru
    // indeks-u odczytanie włsanych postów
    // http://ux.up.krakow.pl/~pmazurek/java/add.php - dodawanie postów
    // http://ux.up.krakow.pl/~pmazurek/java/update.php - aktualizacja własnych wpisów
    // http://ux.up.krakow.pl/~pmazurek/java/delete.php - usuwanie własnych wpisów

    // pobieranie danych ze strony internetowej
    public String GetDataFromWebPage(String address){
        try{
            // tworzenie obiektu typu URL do tworzenia połączenia
            URL url = new URL(address);
            // wykonywanie połączenia i ustawianie wybranych parametrów połączenia
            URLConnection conn = url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(10000);
            conn.setRequestProperty("Accept", "application/html");
            conn.setRequestProperty("User-Agent", "Mozilla");

            // odczytwanie danych otrzymanych od serwera
            BufferedReader buffered = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line;
            // wyświetlanie danych odczytwanych
            while((line = buffered.readLine()) != null){
                //System.out.println(line);
                sb.append(line);
            }
            buffered.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // wysyłanie danych do serwera
    public void SendDateToWebPage(String date, String address){
        URL url = null;
        try {
            // tworzenie połączenia z wyznaczonym adresem
            url = new URL(address);
            // wykonywanie połączenia i ustawianie wybranych parametrów połączenia
            URLConnection conn = url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(10000);
            // informacja o wysyłaniu danych do serwera
            conn.setDoOutput(true);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("User-Agent", "Mozilla");

            // Stworzenie obiektu odpowiiadającego za wysłanie danych do serwera
            BufferedWriter bufferedW = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
            bufferedW.write(date);
            bufferedW.flush();
            bufferedW.close();

            // odczytwanie odpowiedzi z serwera
            BufferedReader bufferedR = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            //StringBuilder sb = new StringBuilder();
            String line;
            while((line = bufferedR.readLine()) != null){
                System.out.println(line);
            }
            bufferedR.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // wyszukiwanie linków na stronie
    // metoda wykorzystuje bibliotekę Jsoup.org
    public void GetLinksFromWebPage(String date) throws MalformedURLException {
        // parsowanie strony do formatu przetwarzanego
        Document doc = Jsoup.parse(date);
        // pobieranie części tylko widocznej dla użytownika
        Element body = doc.body();
        // metoda select pozwala na wyszukanie dowolnego elemntu.
        // wyszukiwanie można wykonać po tagach HTML lub tagach CSS lub ich kombinacje
        Elements linki = body.select("a[href]");
        // sprawdzenie liczby odnalezionych linków
        System.out.println(linki.size());
        // wyświetlenie wszystkich linków bezwzględnych
        for (Element e: linki){
            // odszukany link jest typu element zatem można wykorzystać dodatkowe metody dla obiektów typu Element
            System.out.println(e.attr("abs:href"));
        }
    }


    // metoda ospowiadajaca za pozyskanie informacji dotyczących połączenia z wbranym serwerem
    public static void GetConnectionInfo(URLConnection connection, URL url){
        System.out.println("Protokol:" + url.getProtocol());
        System.out.println("Info " + url.getUserInfo());
        String host = url.getHost();
        if (host != null) {
            int atSign = host.indexOf('@');
            if (atSign != -1) host = host.substring(atSign+1);
            System.out.println("Host " + host);
        } else {
            System.out.println("Host is null.");
        }
        System.out.println("Port" + url.getPort());
        System.out.println("Default Port" + url.getDefaultPort());
        System.out.println("File" + url.getFile());
        System.out.println("Path " + url.getPath());

        Map<String, List<String>> mapRequest = connection.getRequestProperties();
        for(String s : mapRequest.keySet()){
            System.out.print(s + " ");
            List<String> tempList = mapRequest.get(s);
            for (String vs : tempList){
                System.out.print(vs + " ");
            }
            System.out.println();
        }

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
