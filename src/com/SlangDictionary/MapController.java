package com.SlangDictionary;/*
    @author Nguyen Dang Huynh Long
    @date 11/19/20
*/

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class MapController {
    public static final String ASSETS_FILES_BASE_TXT = "./assets/files/base.txt";
    public static final String ASSETS_FILES_EX_TXT = "./assets/files/ex.txt";
    private ArrayList<String> history = null;
    private static HashMap<String, String> map = null;
    private static ArrayList<String> keys = null;


    public void readDictionary() throws IOException {
        map = new HashMap<>();
        keys = new ArrayList<>();
        try {
            FileInputStream baseIn = new FileInputStream(ASSETS_FILES_BASE_TXT);
            Scanner baseSc = new Scanner(baseIn, "UTF-8");
            FileInputStream exIn = new FileInputStream(ASSETS_FILES_EX_TXT);
            Scanner exSc = new Scanner(exIn, "UTF-8");

            baseSc.nextLine();
            while (baseSc.hasNextLine()) {
                String line = baseSc.nextLine();
                String[] splited = line.split("`", 0);

                if (splited.length == 2) {
                    keys.add(splited[0].toLowerCase());
                    map.put(splited[0].toLowerCase(), splited[1]);
                }
            }

            if (baseSc.ioException() != null) {
                throw baseSc.ioException();
            }

            exSc.nextLine();
            while (exSc.hasNextLine()) {
                String line = exSc.nextLine();
                String[] splited = line.split("`", 0);

                if (splited.length == 2) {
                    if (map.containsKey(splited[0])){
                        map.replace(splited[0].toLowerCase(), splited[1]);
                    }
                    else{
                        keys.add(splited[0].toLowerCase());
                        map.put(splited[0].toLowerCase(), splited[1]);
                    }
                }
            }
            if (exSc.ioException() != null){
                throw exSc.ioException();
            }

            baseIn.close();
            exIn.close();
        } catch (Exception error) {
            System.out.print(error.getMessage());
        }
    }

    MapController(){
        history = new ArrayList<>();
        try{
            readDictionary();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasKey(String slang){
        return map.containsKey(slang.toLowerCase());
    }

    public String getDefinition(String slang){
        String key = slang.toLowerCase();
        if (map.containsKey(key)){
            String description =  map.get(key);
            history.add(slang + " - " + description);
            return description;
        }

        history.add(slang + " - " + "Not found");
        return "";
    }

    public String[] getHistory() {
        String[] arr = new String[history.size()];
        return history.toArray(arr);
    }

    public String[] getRandomKeys(int n){
        ArrayList<String> arr = new ArrayList<>();
        System.out.print(keys.size());

        for (int i = 0; i < n; i++) {
            String randomKey = keys.get(new Random().nextInt(keys.size()));
            if (!arr.contains(randomKey)) arr.add(randomKey);
            else i--;
        }

        return arr.toArray(new String[n]);
    }

    public boolean addToExFile(String slang, String mean){
        String data = '\n' + slang + '`' + mean;
        String key = slang.toLowerCase();

        if (map.containsKey(key)){
            map.replace(key, mean);
            keys.add(key);
        }
        else map.put(key, mean);

        try {
            FileOutputStream fout = new FileOutputStream(ASSETS_FILES_EX_TXT, true);
            fout.write(data.getBytes(),0,data.length());
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
