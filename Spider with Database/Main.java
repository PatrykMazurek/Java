package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.sqlite.core.DB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) {
        // write your code here
        String address = "https://www.onet.pl";
        //startSingielThread(address); // 17868, 188, 1
        startMultiThread(address, 3); //  31729, 286, 0
                                                    // 28000, 331, 3
        DBConnection db = new DBConnection();
        db.connect();;
        db.getAllTabSize();
        db.disconnect();
    }


    public static void startSingielThread(String address){
        DBConnection db = new DBConnection();
        PageConnection webPage = new PageConnection();
        StringBuilder webBuilder = webPage.getWebPage(address);

        db.connect();
        db.dropTable();
        db.createTable();
        if (webBuilder != null){
            Document doc = Jsoup.parse(webBuilder.toString());
            if(doc.body()!= null){
                db.addLinkToVisitedTab(address);
                Elements links = doc.select("a[href]");
                if(links.size() > 0){
                    for (Element e : links){
                        if(e.attr("href").startsWith("http")){
                            //System.out.println(e.attr("href"));
                            db.addLinkToTempTab(e.attr("href"));
                        }
                    }
                }
                System.out.println("aktualna strona " + address +"\n" +
                        "znaleziono " + links.size() + "linków");
                LocalTime finishTime = LocalTime.now().plusMinutes(3);
                while (finishTime.isAfter(LocalTime.now())){
                //while (db.getTempTabSize()> 0){
                    String webToVisit = "";
                    do{
                        webToVisit = db.getLinkToVisit();
                    }while (webToVisit.isEmpty());
                    webBuilder = webPage.getWebPage(webToVisit);
                    if (webBuilder != null) {
                        doc = Jsoup.parse(webBuilder.toString());
                        if (doc.body() != null) {
                            db.addLinkToVisitedTab(webToVisit);
                            links = doc.select("a[href]");
                            if(links.size() > 0){
                                for (Element e : links){
                                    if(e.attr("href").startsWith("http")){
                                        //System.out.println(e.attr("href"));
                                        db.addLinkToTempTab(e.attr("href"));
                                    }
                                }
                            }
                            System.out.println("aktualna strona " + address +"\n" +
                                    "znaleziono " + links.size() + "linków");
                        }else {
                            System.out.println("Link nie do odwiedzenia " + webToVisit);
                            db.addLinkToUnsupportedTab(webToVisit);
                            db.delLinkFromTempTab(webToVisit);
                        }
                    }else{
                        System.out.println("Link nie do odwiedzenia " + webToVisit);
                        db.addLinkToUnsupportedTab(webToVisit);
                        db.delLinkFromTempTab(webToVisit);
                    }
                }
            }
        }else{
            System.out.println("Link nie może być osłużony");
        }
        db.disconnect();
    }

    public static void startMultiThread(String address, int threadNumber) {
        DBConnection db = new DBConnection();
        PageConnection webPage = new PageConnection();
        StringBuilder webBuilder = webPage.getWebPage(address);

        db.connect();
        db.dropTable();
        db.createTable();
        if (webBuilder != null) {
            Document doc = Jsoup.parse(webBuilder.toString());
            if (doc.body() != null) {
                db.addLinkToVisitedTab(address);
                Elements links = doc.select("a[href]");
                if (links.size() > 0) {
                    for (Element e : links) {
                        if (e.attr("href").startsWith("http")) {
                            //System.out.println(e.attr("href"));
                            db.addLinkToTempTab(e.attr("href"));
                        }
                    }
                }
                System.out.println("aktualna strona " + address + "\n" +
                        "znaleziono " + links.size() + "linków");

                LocalTime finishTime = LocalTime.now().plusMinutes(3);
                ExecutorService service = Executors.newFixedThreadPool(threadNumber);
                ArrayList<Future<Boolean>> futureArrayList = new ArrayList<>();
                ArrayList<String> linksList = new ArrayList<>();

                while(finishTime.isAfter(LocalTime.now())){
                    do{
                        linksList = db.getLinkToVisitMultiThread(threadNumber);
                    }while (linksList == null);

                    for (int i =0; i < threadNumber; i++){
                        SpiderThread st = new SpiderThread(linksList.get(i));
                        futureArrayList.add(service.submit(st));
                    }
                    for (int x =0; x< threadNumber; x++){
                        boolean re = false;
                        try {
                            re = futureArrayList.get(x).get(5000, TimeUnit.MILLISECONDS);
                            if(re){
                                db.addLinkToVisitedTab(linksList.get(x));
                                db.delLinkFromTempTab(linksList.get(x));
                            }else {
                                db.delLinkFromTempTab(linksList.get(x));
                            }
                        } catch (InterruptedException | ExecutionException | TimeoutException e) {
                            e.printStackTrace();
                        }
                    }
                    futureArrayList.clear();
                }
                service.shutdownNow();
            }
        }else{
            System.out.println("Link nie do odwiedzenia " + address);
        }
        db.disconnect();
    }

}
