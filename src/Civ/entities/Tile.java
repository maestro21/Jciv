package Civ.entities;

public class Tile {


    public Terrain terrain;

    private City city;

    public int x;

    public int y;

    public City getCity(){
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }

    // getUnits
    // getCity

    // owner


}
