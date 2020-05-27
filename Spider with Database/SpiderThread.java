package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.concurrent.Callable;

public class SpiderThread implements Callable<Boolean> {

    private String address;

    public SpiderThread(String link){
        this.address = link;
    }

    @Override
    public Boolean call() throws Exception {
        boolean result = true;
        DBConnection db = new DBConnection();
        PageConnection webPage = new PageConnection();
        StringBuilder webBuilder = webPage.getWebPage(address);

        db.connect();
        if (webBuilder != null) {
            Document doc = Jsoup.parse(webBuilder.toString());
            if (doc.body() != null) {
                //db.addLinkToVisitedTab(address);
                Elements links = doc.select("a[href]");
                if (links.size() > 0) {
                    for (Element e : links) {
                        if (e.attr("href").startsWith("http")) {
                            //System.out.println(e.attr("href"));
                            db.addLinkToTempTab(e.attr("href"));
                        }
                    }
                }
                System.out.println("aktualna strona " + this.address + "\n" +
                        "znaleziono " + links.size() + "linków");
                result = true;
            } else {
                System.out.println("Link nie do odwiedzenia " + this.address);
                db.addLinkToUnsupportedTab(this.address);
                result = false;
            }
        }else{
            System.out.println("Link nie do odwiedzenia " + this.address);
            db.addLinkToUnsupportedTab(this.address);
            result = false;
        }
        db.disconnect();
        return result;
    }
}
