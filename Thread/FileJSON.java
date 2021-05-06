package pl.krakow.up;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class FileJSON {

    private String readJSONFile(Path location){

        FileReader file = null;
        try {
            file = new FileReader(location.toString());
            BufferedReader buff = new BufferedReader(file);
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = buff.readLine())!= null){
                sb.append(line);
            }
            buff.close();
            file.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void SaveArea(Path location, File file, ReentrantLock myLock){

        String filename = file.getName();

        JSONArray frameArr = new JSONArray(readJSONFile(location.resolve(filename)));

        for (int i = 0; i< frameArr.length(); i++){
            JSONArray objArr = frameArr.getJSONObject(i).getJSONArray("obj");
            int[] area = new int[objArr.length()];
            for (int j = 0; j< objArr.length(); j++){
                JSONObject obj = objArr.getJSONObject(j);
                if (obj.optBigDecimal("score", BigDecimal.ZERO).compareTo(new BigDecimal(0.5)) > 0){
                    int width = obj.getInt("xmax") - obj.getInt("xmin");
                    int height = obj.getInt("ymax") - obj.getInt("ymin");
                    area[j] = width * height;
                }
            }
            if (area.length> 0 ){
                myLock.lock();
                try{
                    Main.totalArea.add(filename + ":" + frameArr.getJSONObject(i).get("frame") + ":"+
                            Arrays.stream(area).max().getAsInt());
                }finally {
                    myLock.unlock();
                }
            }
//            try {
//                Thread.sleep(20);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    }

    public synchronized void CountArea(){
        if (Main.totalArea.size()> 0){
            String record = Main.totalArea.poll();
            String[] temp = record.split(":");
            String key = temp[0];
            Random rand = new Random();
            Integer value = Integer.valueOf(temp[2]);
            if (Main.maxArea.containsKey(key)){
                if (rand.nextBoolean()){
                    // do obecje wartości dodam nową
                    int old = Main.maxArea.get(key);
                    Main.maxArea.put(key, old + value);
                    System.out.println("dodaje old " + Main.maxArea.get(key) +
                            " new "+ value +
                            " file "+ key +
                            " thread " + Thread.currentThread().getName());
                }else{
                    while (Main.maxArea.get(key) < value){
                        // oczekiwał na uzupełnienie wartości w odpowiednim kluczu.
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int old = Main.maxArea.get(key);
                    Main.maxArea.put(key, old - value);
                    notifyAll();
                    System.out.println("odejmuje old " + Main.maxArea.get(key) +
                            " new "+ value +
                            " file "+ key +
                            " thread " + Thread.currentThread().getName());
                }

            }

        }
    }

}
