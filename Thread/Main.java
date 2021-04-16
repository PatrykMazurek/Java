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
        Path location = sf.GetLocation();
        System.out.println(location);
        Path locationFrom = location.resolve("zad");

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Wiadomośćz wątka ");
                System.out.println("Nazwa wątku " + Thread.currentThread().getName());
                for (int i = 0; i < 10; i++){
                    System.out.println("nr " + i);
                    File[] listFile = sf.GetAllFiles(location.resolve("zad"));
                    System.out.println(listFile.length);
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
//        t1.start();
//        System.out.println("Zakończenie pracy ");

        Path dest = locationFrom.resolveSibling("test");

//        for (File f: listFile) {
//            TestRunnable tr = new TestRunnable(locationFrom, dest, f);
//            Thread t2 = new Thread(tr);
//            t2.start();
//        }

//        ExecutorService service = Executors.newFixedThreadPool(15);
        ExecutorService service = Executors.newCachedThreadPool();
        ArrayList<Future<Boolean>> futureArrayList = new ArrayList<>();
        LocalTime finishTime = LocalTime.now().plusMinutes(3);
        getInfoAboutThread((ThreadPoolExecutor) service);


        while (finishTime.isAfter(LocalTime.now())){

            File[] listFile = sf.GetAllFiles(location.resolve("zad"));
            System.out.println("liczba plików w katalogu " + listFile.length);
            for (File f : listFile){
//                service.submit(new TestRunnable(locationFrom, dest, f));

                TestCallable tc = new TestCallable(locationFrom, dest, f);
                futureArrayList.add( service.submit( tc ) );
            }

            for (Future<Boolean> result : futureArrayList) {
                try {
                    if (result.get(60, TimeUnit.SECONDS)){
                        System.out.println("Przeniosłem plik");
                        System.out.println( listFile[futureArrayList.indexOf(result)].getName() );
                    }else{
                        System.out.println("nie przeniosłem pliku");
                        System.out.println( listFile[futureArrayList.indexOf(result)].getName() );
                    }
                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                    e.printStackTrace();
                }
            }
            futureArrayList.clear();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        service.shutdown();
    }

    public static void getInfoAboutThread(ThreadPoolExecutor pool ){
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
