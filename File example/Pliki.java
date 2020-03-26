package com.company;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Pliki {

    public Pliki(){}

    // odnajdywanie ścieżki w której wykonywany jest program
    public Path FindLocalization(){
        // deklaracja pliku
        File f = new File("temp.txt");
        System.out.println(f.getAbsolutePath());
        // pozyskanie ścieżki bezwzględnej do pliku
        Path temp = f.toPath().toAbsolutePath();
        // zerócenie tylko ścieżki do katalogu w którym znajduje się plik
        return temp.getParent();
    }

    // wyszukiwanie wszystekich plików i katalogów w danej lokalizcji
    public File[] GetAllFiles(Path location) throws IOException {
        // do tabeli typu File przypisuje wyszukane obiekty z podanej lokalizacji
        File[] temFiles = location.toFile().listFiles(new FilenameFilter() {
            // metoda anonimowa FilenameFilter pozwala na zapisanie tylko wybranych pllików lub folderów
            @Override
            public boolean accept(File dir, String name) {
                // plik zostanie dodany do listy jeżeli sepłni warunek
                if (name.endsWith(".dat")){
                    return true;
                }
                return false;
            }
        });
        // wyświetlenie uzyskanych rezultatów
        for (File f : temFiles){
            if(f.isFile()){
                // pobranie nazwy pliku
                System.out.println(f.getName());
                // metoda wyświetlająca informacje o plikach
                //GetFileInfo(f);
            }
        }
        return temFiles;
    }

    // metoda przenosząca pliki w inną lokalizację
    public void MoveFile(File[] files, Path from, String to ) throws IOException {
        // określenie lokalizacji docelowej
        Path destLocation = from.resolve(to);
        // sprawdzenie czy plik folder istnieje
        if(Files.exists(destLocation)){
            System.out.println("Katalog istnije");
        }else{
            System.out.println("Katalog nie istnieje");
            // tworzenie nowego folderu
            Files.createDirectory(destLocation);
            System.out.println("Katalog został stworzony");
        }
        System.out.println(from);
        System.out.println(destLocation);

        for (File f : files){
            // kopioweanie plików w określoną lokalizację
            // metoda "get()" z klasy Paths tworzy ścieżkę do pliku który ma zostać skopiowany
            // parametr StandardCopyOption.REPLACE_EXISTING powoduje że pliki zostaną zawsze zamienione w miejscu docelowym
            Files.copy(Paths.get(f.getAbsolutePath()), destLocation.resolve(f.getName()), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Przenoszę plik do " + destLocation.resolve(f.getName()));
        }

        // metoda dodatkowa przenosząca pliki w miejce docelowe
        // -----------
//        for (File f : files){
//            Files.move(Paths.get(f.getAbsolutePath()), destLocation.resolve(f.getName()));
//            System.out.println("Przenoszę plik do " + destLocation.resolve(f.getName()));
//        }
    }

    // wyświetlanie informacji o pliku
    public void GetFileInfo(File file) {
        System.out.println("Nazwa: " + file.getName());
        System.out.println("Root: " + file.toPath().getRoot());
        System.out.println("Katalog nardzędny: " + file.getParentFile());
        System.out.println("Całkowita ścieżka: " + file.getPath());
        System.out.println("liczba podkatalogów: " + file.toPath().getNameCount());
        System.out.println("Wielkość (B): " + file.length());
        System.out.println("Ostatnia modyfikacja: " +  new Date( file.lastModified()));
        System.out.println("Czy można wykonać? " + file.canExecute());
        System.out.println("Czy można odczytać? " + file.canRead());
        System.out.println("Czy można Zapisać? " + file.canWrite());
        System.out.println("Czy plik ukryty? " + file.isHidden());
        System.out.println("Czy jest plikiem?: " + file.isFile());
        System.out.println("Czy jest katalogiem?: " + file.isDirectory());

        // dane dotyczące rozmiaru dysku / partycji
        System.out.println("Wolna przestrzeń (B): " + file.getFreeSpace());
        System.out.println("Używana przestrzeń (B): " + file.getUsableSpace());
        System.out.println("Całkowita przestrzeń (B): " + file.getTotalSpace());

    }
}
