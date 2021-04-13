package pl.krakow.up;

import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.Callable;

public class TestCallable implements Callable<Boolean> {


    private Path location;
    private Path dest;
    private File file;

    public TestCallable(Path l, Path d, File f){
        this.location = l;
        this.dest = d;
        this.file = f;
    }


    @Override
    public Boolean call() throws Exception {
        System.out.println("Rozpoczęcie pracy przez " + Thread.currentThread().getName());
        SearchFile sf = new SearchFile();
        boolean result = sf.MoveFile(location, dest, file);
        return result;
    }
}
