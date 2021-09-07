package com.ilongross.seafight.javafx;

import com.ilongross.seafight.javafx.support.ElementInfo;
import com.ilongross.seafight.javafx.support.UserButtons;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;
import java.util.Random;

public class AppGUI extends Application {

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 1000;
    List<String> listFontFamilies = Font.getFamilies();


    @Override
    public void start(Stage stage) throws Exception {
        Text message1 = new Text("Game \"Sea Fight\" with Battleship-AI");
        Text message2 = new Text("Сыграй в \"Sea Fight\" против алгоритма");
        Font f = Font.font("Times new Roman", 20);
        message1.setFont(f);
        message2.setFont(f);

        System.out.println(ElementInfo.rem);

        Bounds messageBounds = message2.getBoundsInParent();
        double ascent = -messageBounds.getMinY();
        double descent = messageBounds.getMaxY();
        double width = messageBounds.getWidth();

        double baseY = 80;
        double topY = baseY - ascent;
        double leftX = (WIDTH - width) / 2;
        message2.relocate(leftX, topY);

        Paint[] colors = {Color.RED, Color.GREEN, Color.AZURE, Color.BISQUE};
        Random rand = new Random();

        Pane root = new Pane();
        Button startGameButton = UserButtons.startGame();
        startGameButton.setOnAction(e-> message2.setFill(colors[rand.nextInt(colors.length)]));
        baseY = 200;
        startGameButton.relocate(WIDTH / 2 -20, baseY - ascent);

        Slider slider = new Slider();




        Button clearMessage1 = UserButtons.closeButton();
        clearMessage1.setCursor(Cursor.CROSSHAIR);
        baseY = 400;
        message1.relocate(WIDTH / 2 -20, baseY - ascent);
        baseY = 500;
        clearMessage1.relocate(WIDTH / 2 -20, baseY - ascent);

        clearMessage1.setOnMousePressed(e->{
            message1.setText("");
        });

        message1.setOnKeyTyped(e->{
            message1.requestFocus();
            message1.setText(String.valueOf(e.getCharacter()));
        });

        root.setOnMouseDragged(e-> {
            System.out.printf("mouse [%.1f; %.1f]\n", e.getX(), e.getY());
            Circle c = new Circle(e.getX(), e.getY(), slider.getValue());
            c.setFill(colors[rand.nextInt(colors.length)]);
            root.getChildren().add(c);
        });

        slider.valueProperty().addListener(property->
                message2.setFont(Font.font(listFontFamilies.get(rand.nextInt(listFontFamilies.size())), slider.getValue())));

        VBox vBox1 = new VBox(message1, message2, clearMessage1, startGameButton, slider);
        vBox1.setAlignment(Pos.CENTER);
        vBox1.setPadding(new Insets(20));
        vBox1.relocate(WIDTH/4, 100);

        root.getChildren().add(vBox1);
        root.setPrefSize(WIDTH, HEIGHT);
//        root.getChildren().add(checkCursorCoordsButton);
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Sea Fight");
        stage.setResizable(false);
        stage.show();
    }
}
