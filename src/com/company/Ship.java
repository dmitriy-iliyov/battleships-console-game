package com.company;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Ship extends Rectangle {
    public double shiftX=0;
    public double shiftY=0;
    public int deck;
    public int x;
    public int y;
    public boolean horizon;
    public boolean killed = false;
    public boolean deleted = false;


    public Ship (int size, int deck, int startX, int startY, boolean horizon, int x, int y)
    {
        super();
        this.setX(startX);
        this.setY(startY);
        this.setHeight((horizon ? size : deck*size));
        this.setWidth((horizon ? deck*size : size));
        this.setFill(Color.web(String.valueOf(Color.BLUE), 0.1));
        this.setStroke(Color.BLUE);
        this.setStrokeWidth(1.2);
        this.deck = deck;
        this.horizon = horizon;
        this.x = x;
        this.y = y;
    }
}
