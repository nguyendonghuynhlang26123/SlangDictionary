package com.SlangDictionary;/*
    @author Nguyen Dang Huynh Long
    @date 11/19/20
*/

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class ReadDB {
    public static final String FILE_PATH = "./assets/files/base.txt";
    public static HashMap<String, String> map = new HashMap<>();

    public void setDictionary(HashMap<String, String> map) throws IOException {
        FileInputStream inputStream = null;
        Scanner sc = null;
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        try{
            inputStream = new FileInputStream(FILE_PATH);
            sc = new Scanner(inputStream, "UTF-8");
            while (sc.hasNextLine()){
                String line = sc.nextLine();
                String[] splited = line.split("`", 0);

                if (splited.length != 2){
                    System.out.println(Arrays.toString(splited));
                    System.out.println(line);
                }
                else{
                    map.put(splited[0], splited[1]);
                }
            }

            if (sc.ioException() != null) {
                throw sc.ioException();
            }
        }
        catch (Exception error){
            System.out.print(error.getMessage());
        }
        finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (sc != null) {
                sc.close();
            }
        }

        Scanner inputSc = new Scanner(System.in);
        String slang = inputSc.nextLine();
        System.out.print(map.get(slang));
    }
}
