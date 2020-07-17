package Civ.entities;

import Civ.classes.Coords;

import java.awt.*;

public class Terrain {


    public String name;
    public String symbol;
    public Coords pos; // position in image;
    public Color color;
    public String type;
    public Integer irrigable;
    public Integer mineable;

    public Terrain() {};

    public boolean isWater() {
        return type.equals("water");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Coords getPos() {
        return pos;
    }

    public void setPos(Coords pos) {
        this.pos = pos;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isIrrigable() {
        return irrigable > 0;
    }

    public boolean isMineable() {
        return mineable > 0;
    }
}
