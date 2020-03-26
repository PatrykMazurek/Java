package com.company;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class PlikiZip {

    public PlikiZip() { }

    // tworzenie plików archiwum
    public void GenerateArchiwumZIP(File[] files, String nameZIP, Path location){
        try {
            if(Files.notExists(location.resolve(nameZIP))){
                if (files.length>0){
                    System.out.println("Tworze archiwum ZIP");
                    // tworzenie obirktu typu ZipOutputStream w celu zapisywania do archiwum Zip
                    ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(location.resolve(nameZIP).toString()));
                    for (File f : files){
                        // zapisywanie każdego pliku do archiwum
                        ZipEntry zipE = new ZipEntry(f.getName());
                        // metoda putNextEntry dodaje nazwę pliku i tworzy pusty plik
                        zipOut.putNextEntry(zipE);
                        // odczyt wybranego pliku do zapisu w archiwum
                        FileInputStream fileInputStream = new FileInputStream(f);
                        System.out.println("Zapisuje do archiwum plik: " + f.getName());
                        byte[] buffor = new byte[1024];
                        int lenght;
                        // zapis zawartości pliku do archiwum
                        while ((lenght = fileInputStream.read(buffor)) >= 0){
                            zipOut.write(buffor, 0, lenght);
                        }
                        // należy pamiętaćo o zamknięciu kiedy zakończymy zapisywać zawartość podjedyńczego pliku
                        fileInputStream.close();
                        zipOut.closeEntry();
                    }
                    // zamknięcie całego archiwum
                    zipOut.close();
                }else{
                    System.out.println("Brak plików do zipowania");
                }
            } else{
                System.out.println("Plik w podanej lokalizacji istnieje");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ReadArchiwumZIP(File zipFile, Path toLacation) {
        try {
            // utworzenie obiektu zipInputStream do odczytania danych z pliku ZIP
            ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFile));
            // obiekt obsługujący pojedyńcze rekordy z archiwum
            ZipEntry zipE;
            // oczytwanie pojedyńczyć rekordów z archiwum
            while ((zipE = zipIn.getNextEntry()) != null) {
                // tworzenie pliku w miejscu gdzie ma byćrozpakowany
                File newFile = new File(toLacation.toFile(), zipE.getName());
                if (Files.exists(newFile.toPath())) {
                    System.out.println("Plik istnieje w danej lokalizacji");
                } else {
                    // zapisywanie danych do pliku
                    FileOutputStream fileOutputStream = new FileOutputStream(newFile);
                    int lenght;
                    byte[] buffor = new byte[1024];
                    // odczytwanie danych z pliku i zapisywanie ich do pliku w miejscu docelowym
                    while ((lenght = zipIn.read(buffor)) >= 0) {
                        fileOutputStream.write(buffor, 0, lenght);
                    }
                    // zapisanie danych w pliku
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
                // zamknięcie pojedyńczego rekordu
                zipIn.closeEntry();
            }
            // zamknięci całego archiwum
            zipIn.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
