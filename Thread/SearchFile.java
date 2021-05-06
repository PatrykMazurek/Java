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
                } else if (name.endsWith(".json")){
                    return true;
                }
                return false;
            }
        });
        return tempFile;
    }

    public boolean MoveFile(Path location, Path dest, File file){

        if (Files.exists(dest)){
            System.out.println("Data modyfikacji: " + file.lastModified());
            System.out.println("Możliwość odczytu: " + file.canRead());
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

}
