package pl.krakow.up;

import java.io.File;
import java.nio.file.Path;

public class TestRunnable implements Runnable {

    private Path location;
    private Path dest;
    private File file;

    public TestRunnable(Path l, Path d, File f){
        this.location = l;
        this.dest = d;
        this.file = f;
    }

    @Override
    public void run() {
        System.out.println("Wątek zaczyna prace " + Thread.currentThread().getName());
        SearchFile sf = new SearchFile();
        sf.MoveFile(location, dest, file);
    }
}
