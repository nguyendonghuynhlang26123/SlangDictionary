package com.SlangDictionary;/*
    @author Nguyen Dang Huynh Long
    @date 11/19/20
*/

import java.io.*;
import java.util.*;

public class MapController {
    public static final String ASSETS_FILES_BASE_TXT = "/files/base.txt";
    public static final String ASSETS_FILES_EX_TXT = "./ex.txt";

    public static final String FILE_HEADER = "Slang`Meaning";
    private final ArrayList<String> history;
    private static HashMap<String, String> map = null;
    private static ArrayList<String> keys = null;

    private static HashMap <String, ArrayList<String>> defList = null;

    public void readDictionary(){
        map = new HashMap<>();
        defList = new HashMap<>();
        keys = new ArrayList<>();
        InputStream inBase, inEx;
        BufferedReader readerBase, readerEx;

        try {
            inBase = getClass().getResourceAsStream(ASSETS_FILES_BASE_TXT);
            readerBase = new BufferedReader(new InputStreamReader(inBase));

            inEx = new FileInputStream(ASSETS_FILES_EX_TXT);
            readerEx = new BufferedReader(new InputStreamReader(inEx));

            String line;
            readerBase.readLine();
            while ((line = readerBase.readLine()) != null) {
                String[] splited = line.split("`", 0);

                if (splited.length == 2) {
                    String key = splited[0].toLowerCase();
                    String value = splited[1];
                    keys.add(key);
                    map.put(key, value);
                    addToDefList(key,value);
                }
                else System.out.println("INVALID LINE: " + line);

            }

            readerEx.readLine();
            while ((line = readerEx.readLine()) != null) {
                String[] splited = line.split("`", 0);

                if (splited.length == 2) {
                    String key = splited[0].toLowerCase();
                    String value = splited[1];

                    if (map.containsKey(key)){
                        //Test`~ => Test slang word is deleted
                        if (value.equals("~")) {
                            map.remove(key);
                            removeFromDefList(key, map.get(key));
                        }
                        //Test`abc and map has already had this word => Replace def list
                        else{
                            removeFromDefList(key,map.get(key));
                            addToDefList(key, value);
                            map.replace(key, value);
                        }
                    }
                    //Test`abc and map does not have this word yet
                    else {
                        keys.add(key);
                        map.put(key, value);
                        addToDefList(key, value);
                    }
                }
            }

            inBase.close();
            inEx.close();
        } catch (Exception error) {
            System.out.print("EXCEPTION CAUTCH: ");
            System.out.println(error.getMessage());
        }
    }

    MapController(){
        history = new ArrayList<>();
        if (!(new File("./ex.txt")).exists()){
            try {
                createExFile();
                System.out.print("Create ex file successfully");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else System.out.println("USING EXISTING EX FILE");
        readDictionary();
        System.out.println(map.size() + " USING EX FILE: " +  ASSETS_FILES_EX_TXT);
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

        history.add(slang + " - " + "Not found!");
        return "";
    }

    public String[] getSlangWordsByDef(String keyword){
        String[] keys = keyword.toLowerCase().split(" ");
        Set<String> retainSet = null;
        for (String key : keys) {
            if (!defList.containsKey(key)){
                history.add(keyword + " - Not found!");
                return null;
            }
            Set<String> slangs = new HashSet<>((defList.get(key)));

            if (retainSet != null) retainSet.retainAll(slangs);
            else retainSet = slangs;
        }

        String[] result = new String[retainSet.size()];
        retainSet.toArray(result);
        history.add(keyword + " - " + String.join(", ", result));
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

    public void createExFile() throws IOException {
        FileOutputStream fout = new FileOutputStream("./ex.txt");
        fout.write(FILE_HEADER.getBytes(), 0, FILE_HEADER.length());
        fout.close();
    }

    public boolean resetExFile(){
        try {
            createExFile();
            readDictionary();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
