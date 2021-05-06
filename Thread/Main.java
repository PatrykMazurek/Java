package pl.krakow.up;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.*;

public class Main {

    public static Queue<String> totalArea;
    public static HashMap<String, Integer> maxArea;

    public static void main(String[] args) {
	// write your code here

        totalArea = new ArrayDeque<>();
        maxArea = new HashMap<>();

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
        LocalTime finishTime = LocalTime.now().plusMinutes(1);
        ArrayList<File> fileArrayList = new ArrayList<>();
        int nr = 0;
        while (finishTime.isAfter(LocalTime.now())){
            File[] tempFile = sf.GetAllFiles(location);
//            for (File fl : tempFile){
//                fileArrayList.add(fl);
//            }
            System.out.println("liczba plików w katalogu " + tempFile.length);

            for (File f : tempFile){
                if (fileArrayList.indexOf(f) ==-1){
//                    TestCallable tc = new TestCallable(location, dest, f);
//                    futureArrayList.add( service.submit(tc) );
                    service.submit(new TestRunnable(location, Path.of(""), f));
                    fileArrayList.add(f);
                    maxArea.put(f.getName(), 0);
//                    GetInfoAboutThread((ThreadPoolExecutor) service);
                }
            }
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("nr " + nr + " Wielkość kloejki " + totalArea.size());
            nr++;
//            for (Future<Boolean> result: futureArrayList) {
//                try {
//                    if (result.get(60, TimeUnit.SECONDS)) {
//                        System.out.println("Plik został przeniesiony");
//                        System.out.println( fileArrayList.get( futureArrayList.indexOf(result) ) );
//                    }else{
//                        System.out.println("Plik nie zostal przeniesiony");
//                        System.out.println( fileArrayList.get( futureArrayList.indexOf(result) ) );
//                    }
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                } catch (TimeoutException e) {
//                    e.printStackTrace();
//                }
//            }
//            futureArrayList.clear();
//            fileArrayList.clear();
        }

        ThreadPoolExecutor poolExecutor = (ThreadPoolExecutor)service;
        LocalTime timeThreadInfo = LocalTime.now().plusSeconds(20);

        while( totalArea.size()>0 ){
            if ( poolExecutor.getActiveCount() < 20){
                service.submit(new TestRunnable(Path.of(""), Path.of(""), new File("")));
            }

            if (timeThreadInfo.isBefore(LocalTime.now())){
                System.out.println("aktywne " + poolExecutor.getActiveCount() + " kolejka " + totalArea.size());
                timeThreadInfo = LocalTime.now().plusSeconds(20);
            }
        }
        service.shutdownNow();
    }

    public static void GetInfoAboutThread(ThreadPoolExecutor pool){
        if (pool.getActiveCount() > 0){
            System.out.println("Maksymalna liczba wątków " + pool.getMaximumPoolSize());
            System.out.println("Liczba aktywnych wątków " + pool.getActiveCount());
        }

    }
}
