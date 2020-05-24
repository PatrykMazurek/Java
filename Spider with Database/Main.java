package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Main {

    public static void main(String[] args) {
	// write your code here

        DBConnection db = new DBConnection();
        PageConnection webPage = new PageConnection();
        // link początkowy
        String address = "https://www.onet.pl";
        StringBuilder webBuilder = webPage.getWebPage(address);

        db.connect();
        if (webBuilder != null){
//            Document doc = Jsoup.parse(webBuilder.toString());
//            db.addLinkToVisitiedTab(address);
//            Elements links = doc.select("a[href]");
//            for (Element e : links){
//                if(e.attr("href").startsWith("http")){
//                    System.out.println(e.attr("href"));
//                    db.addLinkToTempTab(e.attr("href"));
//                }
//            }
//
            while (db.getSizeTemTab()> 0){
                webBuilder = webPage.getWebPage(db.getLinkToVisit());
            }


        }else{
            System.out.println("Link nie może być osłużony");
        }
        db.disconnect();
    }
}
