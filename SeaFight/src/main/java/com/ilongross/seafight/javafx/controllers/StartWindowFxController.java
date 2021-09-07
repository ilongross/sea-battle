package com.ilongross.seafight.javafx.controllers;

import com.ilongross.seafight.Main;
import com.ilongross.seafight.config.GameProperties;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class StartWindowFxController extends Application {



    @FXML private TextField userName;
    @FXML private Button playButton;

    @FXML
    public void clickToPlay(ActionEvent actionEvent) {
        playButton.setOnMouseClicked(e-> {
            Main.properties.setProperty("player.name.user", userName.getText());
//            Main.main(new String[]{});
            System.out.println(Main.properties.getProperty("player.name.user"));
        });
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL url = new File("src/main/java/com/ilongross/seafight/javafx/views_fxml/startWindow.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        primaryStage.setTitle(Main.properties.getProperty("game.name"));
        primaryStage.setScene(
                new Scene(root,
                        Integer.valueOf(Main.properties.getProperty("startWindow.width")),
                        Integer.valueOf(Main.properties.getProperty("startWindow.height"))));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
