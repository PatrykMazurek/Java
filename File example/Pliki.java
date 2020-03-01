package com.company;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

public class Pliki {

    public Pliki(){
        // tworzenie ścieżki
        // proszę wstawić swoją ścieżkę
        Path path = Paths.get("C:\\Projekty\\java");
        // dodawanie nowej ścieżki do istniejącej.
        Path dir_1 = path.resolve("testy");
        // Tworzenie ścieżki bliźniacej do lokalizacji "archiwum"
        Path dir_2 = dir_1.resolveSibling("archiwum");
        try{
            // sprawdzenie czy podana ścieżka istnieje
            if(!Files.exists(dir_1)){
                // tworzenie nowego katalogu w podanej ścieżce
                Files.createDirectory(dir_1);
                System.out.println("Stworzyłem katalog: " + dir_1.getFileName());
            }
            // sprawdzenie czy podana ścieżka istnieje
            if (Files.notExists(dir_2)){
                Files.createDirectory(dir_2);
                // tworzenie nowego katalogu w podanej ścieżce
                System.out.println("Stworzyłem katalog: " + dir_2.getFileName());
            }
            // stworzenie nowego obiektu typu File wraz z lokalizacją pliku
            File newFile = new File(dir_1 + "\\nowy_plik.txt");
            File newFile2 = new File(dir_1 +"\\nowy_plik2.dat");
            // Zapisywanie danych do nowego liku
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(newFile));
            osw.write("cokolwiek\n");
            osw.write("cokolwiek 2");
            osw.close();
            OutputStreamWriter osw2 = new OutputStreamWriter(new FileOutputStream(newFile2));
            osw2.write("cokolwiek\n");
            osw2.write("cokolwiek 2");
            osw2.close();
            // Sprawdzenie czy istnieje plik w określonej lokalizacji
            if (!newFile.exists()){
                // jeżeli plik nie istnieje stwórz plik
                Files.createFile(newFile.toPath());
                System.out.println("Stworzyłem plik o nazwie: " + newFile.getName());
            }else{
                // Jeżeli plik istnieje to przenoszę do innego katalogu
                // zastosowanie parametru StandardCopyoptions. REPLACE_EXISTING pozwala na zamianę pliku w miejscu docelowym
                Files.move(newFile.toPath(), Paths.get(dir_2.toString(), newFile.getName() ), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Przeniosłem w inne miejsce");
            }
            // Drugra forma przenoszenia pliku
            if(!newFile2.exists()){
                // tworzenie pliku
                Files.createFile(newFile2.toPath());
                System.out.println("Stworzyłem plik o nazwie: " + newFile2.getName());
            }else{
                // przenoszę plik do innej lokalizacji
                // zastosowanie parametru StandardCopyoptions. REPLACE_EXISTING pozwala na zamianę pliku w miejscu docelowym
                Files.copy(newFile2.toPath(), Paths.get(dir_2.toString(), newFile2.getName()), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("kopiuje plik: " + newFile2.getName());
                // usuwam plik z obecnej lokalizacji
                Files.delete(newFile2.toPath());
                System.out.println("usuwam plik: " + newFile2.getName());
            }

            System.out.println("Informacje o plikach:");
            // wykonanie listy wszystkich katalogów i plkiów w danym katologu
            String[] listFile = dir_2.toFile().list();
            for(String d : listFile){
                System.out.println(d);
                Path tempPath = Paths.get(dir_2.toString(), d);
                System.out.println("Nazwa pliku: " + tempPath.getFileName());
                System.out.println("Katalog nardzędny: " + tempPath.getParent());
                System.out.println("Całkowita ścieżka: " + tempPath.toFile().getPath());
                System.out.println("Ostatnia modyfikacja: " + new Date(tempPath.toFile().lastModified()));
                System.out.println("Czy można wykonać? " + tempPath.toFile().canExecute());
                System.out.println("Czy można odczytać? " + tempPath.toFile().canRead());
                System.out.println("Czy można Zapisać? " + tempPath.toFile().canWrite());
                System.out.println("Czy plik ukryty? " + tempPath.toFile().isHidden());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
