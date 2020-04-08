package com.company;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Scanner;
import java.util.Set;

public class JSON {

    // klasa JSON wykorzystuje bibliotekę Json.org

    // pobieranie danych z formatu JSON do wyświetlania
    public void GetDataFromJSON(String dataJson){
        // parsowanie danych do formatu typu JSON
        JSONObject obj = new JSONObject(dataJson);
        // odczytwanie kluczy które zapisane są w JSON-ie
        for (String s: obj.keySet()){
            System.out.println(s);
        }
        // tworzenie obiektu typu JSON Array
        JSONArray objArr = obj.optJSONArray("post");
        for (int i=0; i<objArr.length();i++){
            JSONObject tempObj = objArr.optJSONObject(i);
            System.out.println(tempObj.optString("nick"));
            System.out.println(tempObj.optString("data", "Zły klucz"));
        }
    }

    // metoda konwertująca dane do formatu JSON
    public JSONArray SetDateToJSON(){
        // pobierani danych od użytkownika
        int index = 0; // twój numer indeks;
        Scanner input = new Scanner(System.in);
        // tworzenie obiektu typu JSON
        JSONObject obj = new JSONObject();

        obj.put("Indeks", index);
        System.out.print("Nick: ");
        String nick = input.nextLine();
        obj.put("Nick", nick);

        System.out.println("treść postu:");
        String post = input.nextLine();
        obj.put("Text", post);
        // wstawianie obiektu JSON do tabeli JSON-ów
        JSONArray objArr = new JSONArray();
        objArr.put(obj);
        return objArr;
    }

    public JSONObject MyPost(int index){
        JSONObject obj = new JSONObject();
        obj.put("Indeks", index);
        return obj;
    }
}
