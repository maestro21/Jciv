package Civ.entities;

import Civ.classes.Coords;
import Civ.classes.Game;

public class City {

    private String name;
    private String cityStyle;
    private int size = 0;
    private Player player;
    private Coords coords;
    public Game game;
    public boolean isCapital = false;


    public Coords getCoords() {
        return coords;
    }

    public City setCoords(Coords coords) {
        this.coords = coords;
        return this;
    }

    public Player getPlayer() {
        return player;
    }

    public City setPlayer(Player player) {
        this.player = player;
        return this;
    }

    public String getName() {
        return isCapital ? name.toUpperCase() : name;
    }

    public City setName(String name) {
        this.name = name;
        return this;
    }

    public String getCityStyle() {
        return cityStyle;
    }

    public City setCityStyle(String cityStyle) {
        this.cityStyle = cityStyle;
        return this;
    }

    public int getSize() {
        return size;
    }

    public City setSize(int size) {
        this.size = size;
        return this;
    }

    public int getCitySizeGfx() {
        int idx = (int)Math.floor(this.size / 4);
        if(idx > 3) idx = 3;
        return idx;
    }
}
