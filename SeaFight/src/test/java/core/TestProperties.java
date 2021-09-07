package core;

import com.ilongross.seafight.config.GameProperties;

import java.nio.charset.StandardCharsets;

public class TestProperties {

    public static void main(String[] args) {
        var props = new GameProperties("src/main/resources/application.properties");
        System.out.println(props.getProperty("game.name"));
    }

}
