package Civ.entities;

import Civ.classes.Coords;

public class City {

    private String name;
    private String cityStyle;
    private int size;

    public String getName() {
        return name;
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
}
