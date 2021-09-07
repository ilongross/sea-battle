package com.ilongross.seafight.config;


import java.io.*;
import java.util.Properties;

public class GameProperties extends Properties{


    public GameProperties(String pathProperties) {
        try {
            FileReader fr = new FileReader(pathProperties);
            BufferedReader br = new BufferedReader(fr);
            this.load(br);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка чтения файла: application.properties");
        }
    }

    public int getCountOfShips(int deckSize) {
//        try {
//            FileInputStream fis = new FileInputStream(propsPath);
//            properties.load(fis);
//            fis.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        int result = 0;
        switch (deckSize) {
            case 1:
//                result = Integer.valueOf(properties.getProperty("ship.1deck"));
                result = 4;
                break;
            case 2:
//                result = Integer.valueOf(properties.getProperty("ship.2deck"));
                result = 3;
                break;
            case 3:
//                result = Integer.valueOf(properties.getProperty("ship.3deck"));
                result = 2;
                break;
            case 4:
//                result = Integer.valueOf(properties.getProperty("ship.4deck"));
                result = 1;
                break;
            default:
                return result;
        }
        return result;
    }


}
