package Civ.entities;

import Civ.classes.Coords;

import java.awt.*;

public class Terrain {


    public String name;
    public String symbol;
    public Coords pos; // position in image;
    public Color color;

    public Terrain() {};

    public Terrain(String symbol, String name, int posX, int posY, Color color) {
        this.symbol = symbol;
        this.name = name;
        this.pos = new Coords(posX, posY);
        this.color = color;
    }


    public boolean isWater() {
        return (symbol.equals(" ") || symbol.equals("."));
    }
}
