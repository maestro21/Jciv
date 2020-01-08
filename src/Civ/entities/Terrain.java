package Civ.entities;

import Civ.classes.Coords;

import java.awt.*;

public class Terrain {


    public String name;
    public String symbol;
    public Coords pos; // position in image;
    public Color color;
    public String type;

    public Terrain() {};

    public boolean isWater() {
        return type.equals("water");
    }

}
