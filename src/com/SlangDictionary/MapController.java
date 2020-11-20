package com.SlangDictionary;/*
    @author Nguyen Dang Huynh Long
    @date 11/19/20
*/

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class MapController {
    public static final String FILE_PATH = "./assets/files/base.txt";
    private static HashMap<String, VocabInfo> map = null;
    public class VocabInfo {
        int mline;
        String mdesc;
        VocabInfo(int line, String description){
            mline = line;
            mdesc = description;
        }
    }

    public void readDictionary() throws IOException {
        map = new HashMap<>();
        int lineCount = 1;
        try (FileInputStream inputStream = new FileInputStream(FILE_PATH); Scanner sc = new Scanner(inputStream, "UTF-8")) {
            sc.nextLine();
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] splited = line.split("`", 0);

                if (splited.length != 2) {
                    System.out.println(Arrays.toString(splited));
                    System.out.println(line);
                } else {
                    map.put(splited[0], new VocabInfo(lineCount, splited[1]));
                }
                lineCount++;
            }

            if (sc.ioException() != null) {
                throw sc.ioException();
            }
        } catch (Exception error) {
            System.out.print(error.getMessage());
        }
    }

    MapController(){
        try{
            readDictionary();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getDefinition(String slang){
        if (map.containsKey(slang))
            return map.get(slang).mdesc;

        return "";
    }
}
