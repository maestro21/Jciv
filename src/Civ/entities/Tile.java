package Civ.entities;

public class Tile {


    public Terrain terrain;

    private City city;

    private String roads;

    private String improvement;

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

    public String getRoads() {
        return roads;
    }

    public void setRoads(String roads) {
        this.roads = roads;
    }

    public String getImprovement() {
        return improvement;
    }

    public void setImprovement(String improvement) {
        this.improvement = improvement;
    }


    // getUnits
    // getCity

    // owner


}
