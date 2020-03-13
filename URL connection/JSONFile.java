package com.company;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;

public class JSONFile {
    // ---- WAŻNE ----
    // działanie zapisywania i odczytyawnia danych z plików JSON
    // jest możliwoe przy wykorzystaniu biblioteki json.org
    // ---- WAŻNE ----

    public void SaveToJSON(String fileName){
        try {
            // zapisywanie danych osobowych osoby
            JSONObject osobyJson = new JSONObject();
            JSONObject osobaJson = new JSONObject();
            osobaJson.put("Imie", "Jan");
            osobaJson.put("Nazwisko", "Kowalski");
            // zapisywanie informacji o adresie osoby
            JSONObject adresJson = new JSONObject();
            adresJson.put("Ulica", "Kwiatowa");
            adresJson.put("nr_budynku", "16A");
            adresJson.put("nr_lok", 14);
            adresJson.put("kod_pocztowy", "32-304");
            adresJson.put("miasto", "Kraków");
            // dodanie adresu do osoby
            osobaJson.append("adres", adresJson);
            //System.out.println(osobaJson.toString());
            // dodanie osoby do całego spisu osób
            osobyJson.append("Osoby", osobaJson);
            //System.out.println(osobyJson.toString());
            // Zapisanie danych do pliku JSON

            FileWriter f = new FileWriter(fileName);
            f.write(osobyJson.toString());
            f.flush();
            f.close();
            System.out.println("zapisamno dane do pliku");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ReadFromJSON(String fileName){
        try{
            // otwarcie pliku i odczyt danych z pliku JSON
            FileReader fr = new FileReader(fileName);
            BufferedReader buff = new BufferedReader(fr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = buff.readLine()) != null){
                sb.append(line);
            }
            buff.close();
            // Konwersja tekstu do formatu JSON
            JSONObject osobyJson = new JSONObject(sb.toString());
            System.out.println(osobyJson.toString());
            JSONArray tabOsoby = osobyJson.getJSONArray("Osoby");
            // wyświetlenie wszystkic hosób
            for (int i=0; i<tabOsoby.length(); i++){
                JSONObject osobaJSON = tabOsoby.getJSONObject(i);
                for(String s : osobaJSON.keySet()){
                    System.out.println(s + " : " + osobaJSON.optString(s, "brak"));
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
