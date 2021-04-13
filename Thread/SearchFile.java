package pl.krakow.up;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SearchFile {

    public Path GetLocalization(){
        File f = new File("t.txt");
        Path temp = f.toPath().toAbsolutePath();
//        System.out.println(temp);
        return temp.getParent();
    }

    public File[] GetAllFiles(Path localization){

        File[] tempFile = localization.toFile().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name.endsWith(".png")){
                    return true;
                } else if (name.endsWith(".txt")){
                    return true;
                }
                return false;
            }
        });
        return tempFile;
    }

    public boolean MoveFile(Path location, Path dest, File file){

        if (Files.exists(dest)){
			GetFileInfo(file);
            try {
                Files.move(location.resolve(file.getName()), dest.resolve(file.getName()));
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }else{
            System.out.println("Katalog nie istnieje");
            return false;
        }

    }
	
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
