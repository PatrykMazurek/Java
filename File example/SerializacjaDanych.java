package com.company;

import javax.sound.midi.SysexMessage;
import java.io.*;

public class SerializacjaDanych {

    public SerializacjaDanych(){   }

    // metoda zapisująca dane do plików serializowanych
    public void GenerateSerializableFile(String name){
        // tworzenie nowych obiektów
        Adres adres = new Adres("Kwiatowa", 23, 5, "Kraków", "33-100");
        Osoba osoba1 = new Osoba("Jan", "Kowal", adres);
        Osoba osoba2 = new Osoba("Ewa", "Kowal", adres);

        try {
            // zapisywanie obiektów do pliku
            ObjectOutputStream objOut = new ObjectOutputStream(new FileOutputStream(name));
            objOut.writeObject(osoba1);
            objOut.writeObject(osoba2);
            objOut.flush();
            objOut.close();
            System.out.println("Zapisano dane w pliku " + name);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // metoda odczytująca dane z plików serializowanych
    public void ReadSerializableFile(String name){
        try {
            // odczytwanie biektów z plików
            ObjectInputStream objIn = new ObjectInputStream(new FileInputStream(name));
            // tworzenie obiektów z odczytanych danych z pliku
            Osoba osoba3 = (Osoba)objIn.readObject();
            Osoba osoba4 = (Osoba)objIn.readObject();
            objIn.close();

            // w podanym przykładzie proszę zwrócić uwagę na adresy obietków w pamięci
            System.out.println(osoba3.toString());
            System.out.println(osoba4.toString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
