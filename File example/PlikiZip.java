package com.company;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class PlikiZip {

    public PlikiZip() {    }

    public boolean GenerateArchivumZIP(String locationZip, String locationToZip,  String nameZIPFile){
        try{
            // lokalizacja gdzie zapisać archiwum ZIP
            Path location = Paths.get(locationZip, nameZIPFile);
            // lokalizacja z kąd brać pliki do archiwum ZIP
            Path locationZIP = Paths.get(locationToZip);
            FileOutputStream f;
            if (Files.exists(location)){
                System.out.println("Podany plik: " + location.getFileName() + " istnieje w podanej lokalizacji:");
                System.out.println(location.getParent());
                System.out.println("Plik zostanie nadpisany");
                // otwarcie pliku do zapisu
                f = new FileOutputStream(location.toString());
            }
            else{
                System.out.println("Plik nie istniej, zostanie stworzony");
                // Utworzenie pliku i otwarcie do zapisu
                f = new FileOutputStream(location.toString());
            }
            // utworzenie obiektu do zapisu w archiwum ZIP
            ZipOutputStream zout = new ZipOutputStream(f);
            // ustawienie poziomu kompresji
            zout.setLevel(Deflater.BEST_COMPRESSION);
            File tempDirectory = locationZIP.toFile();
            // pobranie wszystkich plików i folderów z wybranego katalogu
            File[] fileList = tempDirectory.listFiles();
            // filtracja wybranych formatów, filtrując pliki można stworzyć anonimową metodę, która będzie filtrować wybrane pliki
//            File[] fileList = tempDirectory.listFiles(new FilenameFilter() {
//                @Override
//                public boolean accept(File dir, String name) {
//                    if(name.endsWith(".txt")){
//                        return true;
//                    }
//                    return false;
//                }
//            });
            // przekazywanie każdego pliku do archiwum
            for (File fl : fileList){
                // sprawdzanie czy aktualny element jest plikiem
                if(fl.isFile()){
                    ZipEntry ze = new ZipEntry(fl.getName());
                    zout.putNextEntry(ze);
                    zout.closeEntry();
                }
            }
            zout.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    public void OpenArchivumZIP(String locationZip, String locationToZip,  String nameZIPFile){
        try{
            // lokalizacja gdzie zapisać archiwum ZIP
            Path location = Paths.get(locationZip, nameZIPFile);
            // lokalizacja z kąd brać pliki do archiwum ZIP
            Path locationZIP = Paths.get(locationToZip);
            FileInputStream f;
            if (Files.exists(location)){
                System.out.println("Podany plik: " + location.getFileName() + " istnieje w podanej lokalizacji:");
                System.out.println(location.getParent());
                System.out.println("Plik zostanie nadpisany");
                // otwarcie pliku do zapisu
                f = new FileInputStream(location.toString());
            }
            else{
                System.out.println("Plik nie istniej");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
