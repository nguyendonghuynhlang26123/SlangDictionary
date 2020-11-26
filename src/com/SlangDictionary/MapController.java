package com.SlangDictionary;/*
    @author Nguyen Dang Huynh Long
    @date 11/19/20
*/

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class MapController {
    public static final String ASSETS_FILES_BASE_TXT = "./assets/files/base.txt";
    public static final String ASSETS_FILES_EX_TXT = "./assets/files/ex.txt";
    public static final String FILE_HEADER = "Slang`Meaning";
    private final ArrayList<String> history;
    private static HashMap<String, String> map = null;
    private static ArrayList<String> keys = null;

    private static HashMap <String, ArrayList<String>> defList = null;

    public void readDictionary(){
        map = new HashMap<>();
        defList = new HashMap<>();
        keys = new ArrayList<>();
        try {
            FileInputStream baseIn = new FileInputStream(ASSETS_FILES_BASE_TXT);
            Scanner baseSc = new Scanner(baseIn, StandardCharsets.UTF_8);
            FileInputStream exIn = new FileInputStream(ASSETS_FILES_EX_TXT);
            Scanner exSc = new Scanner(exIn, StandardCharsets.UTF_8);

            baseSc.nextLine();
            while (baseSc.hasNextLine()) {
                String line = baseSc.nextLine();
                String[] splited = line.split("`", 0);

                if (splited.length == 2) {
                    keys.add(splited[0].toLowerCase());
                    map.put(splited[0].toLowerCase(), splited[1]);
                    addToDefList(splited[0],splited[1]);
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
                    if (map.containsKey(splited[0]) && !splited[1].equals("~")){
                        removeFromDefList(splited[0],map.get(splited[0]));
                        addToDefList(splited[0], splited[1]);
                        map.replace(splited[0].toLowerCase(), splited[1]);
                    }
                    else if (map.containsKey(splited[0])){
                        map.remove(splited[0]);
                        removeFromDefList(splited[0],map.get(splited[0]));
                    }
                    else{
                        keys.add(splited[0].toLowerCase());
                        map.put(splited[0].toLowerCase(), splited[1]);
                        removeFromDefList(splited[0],map.get(splited[0]));
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
        readDictionary();
    }

    private void addToDefList(String slang, String def){
        def = def.toLowerCase();
        slang = slang.toLowerCase();
        String[] words = def.split(" ");

        for (String word : words) {
            if (defList.containsKey(word)) {
                if (!defList.get(word).contains(slang))
                    defList.get(word).add(slang);
            } else {
                ArrayList<String> newList = new ArrayList<>();
                newList.add(slang);
                defList.put(word, newList);
            }
        }
    }

    public static void removeFromDefList(String slang, String mean){
        slang = slang.toLowerCase();
        mean = mean.toLowerCase();
        String []words = mean.split(" ");

        for (String word : words) {
            if (defList.containsKey(word)){
                defList.get(word).remove(slang);
                if (defList.get(word).size() == 0)
                    defList.remove(word);
            }
        }
    }

    public boolean hasKey(String slang){
        return map.containsKey(slang.toLowerCase());
    }

    public String getDefinitionWithRecord(String slang){
        String key = slang.toLowerCase();
        if (map.containsKey(key)){
            String description =  map.get(key);
            history.add(slang + " - " + description);
            return description;
        }

        history.add(slang + " - " + "Not found");
        return "";
    }

    public String[] getSlangWordsByDef(String keyword){
        String[] keys = keyword.toLowerCase().split(" ");
        Set<String> retainSet = null;
        for (String key : keys) {
            if (!defList.containsKey(key)) return null;
            Set<String> slangs = new HashSet<>((defList.get(key)));

            if (retainSet != null) retainSet.retainAll(slangs);
            else retainSet = slangs;
        }

        //history.add(slang + " - " + "Not found");
        String[] result = new String[retainSet.size()];
        retainSet.toArray(result);
        return result;
    }

    public String getDefinition(String slang){
        String key = slang.toLowerCase();
        return hasKey(key) ? map.get(key) : "";
    }

    public String[] getHistory() {
        String[] arr = new String[history.size()];
        return history.toArray(arr);
    }

    public String[] getRandomKeys(int n){
        ArrayList<String> arr = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String randomKey = keys.get(new Random().nextInt(keys.size()));
            if (!arr.contains(randomKey)) arr.add(randomKey);
            else i--;
        }

        return arr.toArray(new String[n]);
    }

    private boolean fileWriteHelper(String data){
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

    public boolean addSlang(String slang, String mean){
        String data = '\n' + slang + '`' + mean;
        String key = slang.toLowerCase();

        if (map.containsKey(key)){
            map.replace(key, mean);
            keys.add(key);
            removeFromDefList(slang, mean);
            addToDefList(slang,mean);
        }
        else {
            addToDefList(slang,mean);
            map.put(key, mean);
        }

        return fileWriteHelper(data);
    }

    public boolean removeAWord(String slang){
        slang = slang.toLowerCase();
        if (!map.containsKey(slang)) return false;
        removeFromDefList(slang, map.get(slang));
        keys.remove(slang);
        map.remove(slang);
        return fileWriteHelper('\n'+ slang +"`~");
    }

    public boolean resetExFile(){
        try {
            FileOutputStream fout = new FileOutputStream(ASSETS_FILES_EX_TXT, false);
            fout.write(FILE_HEADER.getBytes(), 0, FILE_HEADER.length());
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
