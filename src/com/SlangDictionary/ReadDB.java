package com.SlangDictionary;/*
    @author Nguyen Dang Huynh Long
    @date 11/19/20
*/

import java.io.FileInputStream;
import java.util.Scanner;

public class ReadDB {
    static final String FILE_PATH = "../../assets/files/base.txt";
    public static void main(String[] args) {
        FileInputStream inputStream = null;
        Scanner sc = null;

        try{
            inputStream = new FileInputStream(FILE_PATH);
            sc = new Scanner(inputStream, "UTF-8");

        }
        catch (Exception error){

        }
    }
}
