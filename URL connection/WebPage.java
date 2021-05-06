package pl.krakow.up;

import java.io.*;
import java.net.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class WebPage {

	private String address;

    public WebPage(String a){
        this.address = a;
    }

    // pobieranie danych ze strony internetowej
    public String GetDataFromWebPage(String address) {
        try {

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
            while ((line = buffered.readLine()) != null) {
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

    	// wysyłanie danych z formularz do wybranej strony internetowej
    public void sendDataToWeb(String action, String method, HashMap<String, String> mapData){
        URL url;
		// przygotowywanie danych do wysłania 
        StringBuilder sbData = new StringBuilder();
        try{
            for (Map.Entry<String, String> map : mapData.entrySet()){
                if (sbData.length()>0) sbData.append("&");
                sbData.append(URLEncoder.encode(map.getKey(), "UTF-8"));
                sbData.append("=");
                sbData.append(URLEncoder.encode(map.getValue(), "UTF-8"));
            }
			// tworzenie połączenia ze stroną internetową 
            url = new URL(address + action);
            HttpURLConnection httpsConn = (HttpURLConnection) url.openConnection();
            httpsConn.setConnectTimeout(10000);
            httpsConn.setReadTimeout(5000);
            httpsConn.setRequestProperty("User-Agent", "Mozilla");
            httpsConn.setRequestProperty("Accept", "Application/html");
            httpsConn.setRequestMethod(method.toUpperCase());
            httpsConn.setDoOutput(true);
            httpsConn.getOutputStream().write(sbData.toString().getBytes());
			// sprawdzanie statusu połączenia i odczytywanie ewentualnych danych
            if (httpsConn.getResponseCode() == HttpURLConnection.HTTP_OK){
                BufferedReader buff = new BufferedReader(new InputStreamReader(
                        httpsConn.getInputStream()
                ));
                String line = "";
                while ((line = buff.readLine()) != null){
                    System.out.println(line);
                }
                buff.close();
            }
            httpsConn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //wykonanie połączenia z autoryzacją uużytkownika
    public String authConnection(String address, String user, String pass, String method ){

        String basicAuth = null;
        StringBuilder builder = null;
		// wybur sposobu autoryzacji użytkownika 
        if (method.equals("header")){
			// autoryzacja przy wykorzystaniu HTTP Header
			String userPass = user + ":" + pass;
            basicAuth = "Basic " + new String(Base64.getEncoder().encode(userPass.getBytes()));
//        System.out.println(basicAuth);
        }else {
            // autoryzacja przy pomocy parametrów wysyłanych metodą POST
            try {
                builder = new StringBuilder();
                builder.append(URLEncoder.encode("login", "UTF-8"));
                builder.append("=");
                builder.append(URLEncoder.encode(user, "UTF-8"));
                builder.append("&");
                builder.append(URLEncoder.encode("password", "UTF-8"));
                builder.append("=");
                builder.append(URLEncoder.encode(pass, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
            StringBuilder sb = new StringBuilder();

        try {
            URL url = new URL(address);
            HttpURLConnection httpConn = (HttpURLConnection)url.openConnection();
            httpConn.setConnectTimeout(10000);
            httpConn.setReadTimeout(5000);
            httpConn.setRequestMethod("POST");
            httpConn.setRequestProperty("Accept", "Appcication/html");
			wysyłanie autoryzacji
            if (method.equals("header")){
                httpConn.setRequestProperty("Authorization", basicAuth);
            }else{
                httpConn.getOutputStream().write(builder.toString().getBytes());
            }

			// sprawdzanie połączenia i odczytywanie ewentualnej odpowiedzi
            if(httpConn.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream in = httpConn.getInputStream();
                BufferedReader buff = new BufferedReader(new InputStreamReader(in, "UTF-8"));

                String line="";

                while((line = buff.readLine()) != null){
//                    System.out.println(line);
                    sb.append(line);
                }
                buff.close();
                in.close();
            }
            httpConn.disconnect();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

	// pobieranie plików
    public void downladFile(String fileURL, String destDir){
        int bufferSize = 1024;

        URL url = null;
        try{
			// sprawdzanie formy adresu URL
            if (fileURL.startsWith("http")){
                url = new URL(fileURL);
            }else{
                url = new URL(address + fileURL);
            }
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			
			// sprawdzanie stanu połączenia 
            if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK){
				// pobieranie informacji o pobieranym pliku
                String filename = "";
                String disposition = httpConn.getHeaderField("Content-Disposition");
                String contentType = httpConn.getContentType();
                int contentSize = httpConn.getContentLength();
                if (disposition != null){
                    int index = disposition.indexOf("filename=");
                    if (index > 0){
                        filename = disposition.substring(index + 10, disposition.length()-1);
                    }
                }else{
                    filename = fileURL.substring(fileURL.lastIndexOf("/"), fileURL.length());
                }

                System.out.println("Content-Type " + contentType );
                System.out.println("Content-Disposition " + disposition);
                System.out.println("Content-Length " + contentSize);
                System.out.println("Name " + filename);

				// pobieranie pliku z adresu URL i zapisywanie w zdefiniowanej loklizacji
                InputStream in = httpConn.getInputStream();
                String savePath = destDir + File.separator + filename;

                FileOutputStream fileOut = new FileOutputStream(savePath);

                int byteRead = -1;
                int downloadSize = 0;
                byte[] buffer = new byte[bufferSize];

                while ((byteRead = in.read(buffer, 0, bufferSize)) != -1){
                    fileOut.write(buffer, 0, byteRead);
                    downloadSize += byteRead;
                    System.out.print(downloadSize + " / " + contentSize + "\r");
                }
                fileOut.close();;
                in.close();
            }else{
                System.out.println("Coś poszło nie tak ");
            }
            httpConn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
	
	
	// poszikiwanie wybranych elementów 
	public void findLink(String html){
        Pattern linkPattern = Pattern.compile("href=\"(.*?)\"");
        Matcher linkMatcher = linkPattern.matcher(html);
        while (linkMatcher.find()){
            System.out.println(linkMatcher.group(1));
            if (linkMatcher.group(1).startsWith("/") || linkMatcher.group(1).startsWith("#")){
                System.out.println(linkMatcher.group(1));
            }
        }
    }

    public String[] findTag(String html, String tag){
        Pattern tagPattern = Pattern.compile("<\\s*"+tag+"\\b[^>]*>(.*?)<\\s*/"+tag+"\\s*>");
        Pattern attrTagPattern = Pattern.compile("(\\w+)=\"(.*?)\"");
        Matcher tagMatcher = tagPattern.matcher(html);
        // stworzyć regułę wyszukującą znaczniki input, select, textarea, button
        String action = "";
        String method = "";
        while (tagMatcher.find()){

            String attr = tagMatcher.group(0);
            Matcher attrMatcher = attrTagPattern.matcher(attr);
            while (attrMatcher.find()){
                System.out.println(attrMatcher.group(0));
                if (attrMatcher.group(1).equals("action")){
                    action = attrMatcher.group(2);
                }
                if (attrMatcher.group(1).equals("method")){
                    method = attrMatcher.group(2);
                    System.out.println(attrMatcher.group(2));
                }
            }
        }
        return new String[] {action, method};
    }
	
	// puszukiwanie obrazów znajdujących się na stronie 
    public void findImg(String html){
        Pattern imgPattern = Pattern.compile("<img\\s*src=\"(.*?)\"");
        Matcher imgMatcher = imgPattern.matcher(html);

        while (imgMatcher.find()){
            System.out.println(imgMatcher.group(0));
        }
    }
	
	// poszukiwanie formularza i pul zawartych w nim
	public String[] findForm(String html){
        Pattern formPattern = Pattern.compile("<\\s*form\\b[^>]*>(.*?)<\\s*/form\\s*>");
        Pattern attrPattern = Pattern.compile("(\\w+)=\"(.*?)\"");
        Matcher formMatcher = formPattern.matcher(html);
        ArrayList<String> nameInput = new ArrayList<>();
        String action = "";
        String method = "";
        while(formMatcher.find()){
            String form = formMatcher.group(0);
            Matcher attrMatcher = attrPattern.matcher(form);
            while (attrMatcher.find()){
                if (attrMatcher.group(1).equals("action"))
                    action = attrMatcher.group(2);
                if (attrMatcher.group(1).equals("method"))
                    method = attrMatcher.group(2);
                if (attrMatcher.group(1).equals("name"))
                    nameInput.add(attrMatcher.group(2));
            }
        }
        System.out.println("action " + action + " method " + method);
        String[] temp = new String[nameInput.size() + 2];
        temp[0] = action;
        temp[1] = method;
        for(int i =2; i < temp.length; i++){
            temp[i] = nameInput.get(i-2);
        }
        return temp;    
    }
}
