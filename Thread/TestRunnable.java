package pl.krakow.up;

import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.locks.ReentrantLock;

public class TestRunnable implements Runnable {

    private Path location;
    private Path dest;
    private File file;
    private ReentrantLock myLock;

    public TestRunnable(Path l, Path d, File f){
        this.location = l;
        this.dest = d;
        this.file = f;
        this.myLock = new ReentrantLock();
    }

    @Override
    public void run() {
//        System.out.println("Wątek zaczyna prace " + Thread.currentThread().getName());
//        SearchFile sf = new SearchFile();
//        sf.MoveFile(location, dest, file);
        FileJSON fj = new FileJSON();
        if (location.equals(Path.of(""))){
            fj.CountArea();
        }else {
            fj.SaveArea(location, file, myLock);
        }
//        System.out.println("Wątek kończy prace " + Thread.currentThread().getName());
    }
}
