package pl.krakow.up;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) {
	// write your code here

        SearchFile sf = new SearchFile();
        Path location = sf.GetLocalization();

        location = location.resolve("zad");
        System.out.println(location);

        final int[] i = {0};
        Path finalLocation = location;
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Wiadomość z wątka ");
                System.out.println(Thread.currentThread().getName());
                for (int n = 0; n<10; n++){
                    System.out.println("nr " + i[0]);
                    i[0]++;
                    File[] tFile = sf.GetAllFiles(finalLocation);
                    System.out.println("Katalog zawiera " + tFile.length + " plików");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
//        th.start();

        Path dest = location.resolveSibling("test");
//        System.out.println(dest);

//        File[] tempFile = sf.GetAllFiles(location);
//        for (File f: tempFile) {
//            TestRunnable tr = new TestRunnable(location, dest, f);
//            Thread t2 = new Thread(tr);
//            t2.start();
//        }

        ExecutorService service = Executors.newCachedThreadPool();
        ArrayList<Future<Boolean>> futureArrayList = new ArrayList<>();
        LocalTime finishTime = LocalTime.now().plusMinutes(3);

        while (finishTime.isAfter(LocalTime.now())){
            File[] tempFile = sf.GetAllFiles(location);
            System.out.println("liczba plików w katalogu " + tempFile.length);

            if (tempFile.length > 0){
                for (File f: tempFile) {
                    TestCallable tc = new TestCallable(location, dest, f);
                    futureArrayList.add( service.submit(tc) );
//                    service.submit(new TestRunnable(location, dest, f));
                    GetInfoAboutThread((ThreadPoolExecutor) service);
                }
            }
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (Future<Boolean> result: futureArrayList) {
                try {
                    if (result.get(60, TimeUnit.SECONDS)) {
                        System.out.println("Plik został przeniesiony");
                    }else{
                        System.out.println("Plik nie zostal przeniesiony");
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
            futureArrayList.clear();
        }
        service.shutdownNow();
    }

    public static void GetInfoAboutThread(ThreadPoolExecutor pool){
        if (pool.getActiveCount() > 0){
            System.out.println("Largest executions: "
                + pool.getLargestPoolSize());
			System.out.println("Maximum allowed threads: "
                + pool.getMaximumPoolSize());
			System.out.println("Current threads in pool: "
                + pool.getPoolSize());
			System.out.println("Currently executing threads: "
                + pool.getActiveCount());
			System.out.println("Total number of threads(ever scheduled): "
                + pool.getTaskCount());
        }

    }
}
