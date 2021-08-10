package com.ilongross.seafight.config;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class GameProperties {
    private Properties properties;
    private String propsPath;

    public GameProperties(String propsPath) {
        this.propsPath = propsPath;
        this.properties = new Properties();
    }

    public int getFieldSize() {
        try {
            FileInputStream fis = new FileInputStream(propsPath);
            properties.load(fis);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Integer.valueOf(properties.getProperty("map.size"));
    }

    public int getCountOfShips(int deckSize) {
        try {
            FileInputStream fis = new FileInputStream(propsPath);
            properties.load(fis);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int result = 0;
        switch (deckSize) {
            case 1:
                result = Integer.valueOf(properties.getProperty("ship.1deck"));
                break;
            case 2:
                result = Integer.valueOf(properties.getProperty("ship.2deck"));
                break;
            case 3:
                result = Integer.valueOf(properties.getProperty("ship.3deck"));
                break;
            case 4:
                result = Integer.valueOf(properties.getProperty("ship.4deck"));
                break;
            default:
                return result;
        }
        return result;
    }


}
