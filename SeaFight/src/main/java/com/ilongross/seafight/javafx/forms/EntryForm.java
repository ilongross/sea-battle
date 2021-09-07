package com.ilongross.seafight.javafx.forms;

import com.ilongross.seafight.javafx.support.ElementInfo;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class EntryForm extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        final double rem = new Text("").getLayoutBounds().getHeight();
        System.out.println(rem);
        System.out.println(ElementInfo.rem);
        System.out.println(0.8 * rem);

        double constAlligment = 0.8 * rem;

        GridPane pane = new GridPane();
        pane.setGridLinesVisible(true);

        pane.setHgap(constAlligment);
        pane.setVgap(constAlligment);
        pane.setPadding(new Insets(constAlligment));

        Label usernameLabel = new Label("User name:");
        Label passwordLabel = new Label("Password:");
        TextField username = new TextField();
        PasswordField password = new PasswordField();

        Button okButton = new Button("Ok");
        Button cancelButton = new Button("Cancel");

        HBox buttons = new HBox(constAlligment);
        buttons.getChildren().addAll(okButton, cancelButton);
        buttons.setAlignment(Pos.CENTER);
//        buttons.setStyle("-fx-border-color: red;");

        pane.add(usernameLabel, 0, 0);
        pane.add(username, 1, 0);
        pane.add(passwordLabel, 0, 1);
        pane.add(password, 1, 1);
        pane.add(buttons, 0, 2, 2, 1);

        GridPane.setHalignment(usernameLabel, HPos.RIGHT);
        GridPane.setHalignment(passwordLabel, HPos.RIGHT);
        stage.setScene(new Scene(pane));
        stage.setResizable(false);
        stage.show();
    }
}
