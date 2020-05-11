package Civ.entities;

import Civ.classes.Buildings;
import Civ.classes.Coords;
import Civ.classes.Game;

import java.util.ArrayList;
import java.util.Arrays;

public class City {

    private String name;

    private String cityStyle;

    private String age;

    private int size = 0;

    private Player player;

    private Coords coords;

    public Game game;

    public boolean isCapital = false;

    public Tile tile;

    public ArrayList<String> buildings = new ArrayList<>();

    public ArrayList<String> wonders = new ArrayList<>();

    public ArrayList<String> getBuildings() {
        return buildings;
    }

    public void setBuildings(ArrayList<String> buildings) {
        this.buildings = buildings;
    }

    public ArrayList<String> getWonders() {
        return wonders;
    }

    public void setWonders(ArrayList<String> wonders) {
        this.wonders = wonders;
    }

    public Tile getTile() {
        return tile;
    }

    public String getReligion() {
        return  player.getReligion();
    }

    public boolean civIs(String name) {
        return player.getCivNation().getName().equals(name);
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public boolean isCapital() {
        return isCapital;
    }

    public void setIsCapital(boolean isCapital) {
        this.isCapital = isCapital;
    }

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

    public boolean hasWater() {
        return game.map.getCoast(tile.x, tile.y);
    }

    public String getAge() {
        return player.getAge();
    }

    public boolean hasAge(String age) {
        return game.ruleset.hasAge(player.getAge(), age);
    }

    public String getNation() {
        return player.getCivNation().getName();
    }

    public boolean hasBuilding(String name) {
        return buildings.indexOf(name) > -1;
    }


    public boolean isWalled() {
        return hasBuilding(Buildings.WALLS);
    }

    public String getBuildingStyle(){
        return Ruleset.getBuildingStyleByCityStyle(this.cityStyle);
    }
}
