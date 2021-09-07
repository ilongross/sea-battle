package com.ilongross.seafight.javafx.support;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.text.Text;

public class ElementInfo {

    public static void showBoundsInParent(Node node) {
        Bounds b = node.getBoundsInParent();
        System.out.printf("%s:\nminX=%.1f\nmaxX=%.1f\nminY=%.1f\nmaxY=%.1f\nwidth=%.1f\nheight=%.1f\n",
                node.getClass().getSimpleName(),
                b.getMinX(),
                b.getMaxX(),
                b.getMinY(),
                b.getMaxY(),
                b.getWidth(),
                b.getHeight());
    }

    public static final double rem = new Text("").getBoundsInParent().getHeight();



}
