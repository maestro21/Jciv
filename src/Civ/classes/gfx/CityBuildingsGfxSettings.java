package Civ.classes.gfx;

import Civ.classes.Buildings;
import Civ.classes.Game;
import Civ.classes.Rnd;
import Civ.entities.City;
import Civ.entities.Player;
import Civ.entities.Ruleset;
import Civ.entities.Tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

class CityBuildingsGfxSettings {

    public  int maxSize = 16;

    public  ArrayList<String> cityBuildings = new ArrayList<>();

    public  ArrayList<String> waterBuildings = new ArrayList<>();

    public  String walls = "";

    public  String roads = "";


    /** city settings */
    public  String terrain;

    public  boolean hasWater = false;

    public String name = "Rome";

    public String flag = "flag";

    public String age = "ancient";

    public int size = 1;

    public boolean palace = true;

    public boolean walled = true;

    public boolean isCapital = true;

    public String nation = "Romans";

    public String buildingStyle = "default";

    public ArrayList<String> buildings = new ArrayList<>();


    public  void randomizeCity() {
        size = (int)(Math.random() * maxSize);
        buildingStyle = "classic";
        age = Rnd.randomAge();
        roads = getDefaultRoads(age, nation);
        hasWater = yesno();
        buildings = Buildings.getBuildingSequence(size,age,nation,hasWater,isCapital);
        processBuildings();
    }


    public  void loadCity(Tile tile) {
        City city = tile.getCity();
        roads = tile.getRoads();
        terrain = tile.getTerrain().getName();
        hasWater = city.hasWater();
        buildingStyle = city.getBuildingStyle();

        nation = city.getNation();
        size = city.getSize();
        buildingStyle = city.getBuildingStyle();
        processBuildings();
    }

    public  void processBuildings() {
        for (String building : buildings) {
            if(Buildings.isWall(building)) {
                walls = building;
                continue;
            }

            if(Buildings.isWaterBuilding(building)) {
                waterBuildings.add(building);
                continue;
            }

            if(Buildings.isTownCenter(building)) {
                cityBuildings.add(0,building);
            }

            cityBuildings.add(building);
        }
    }

    public boolean isRailroad() {
        return roads == "railroad";
    }


    public static String getDefaultRoads(String age, String nation) {
        if(Ruleset.hasAge(age, "modern")) {
            return "highway";
        }

        if(Ruleset.hasAge(age, "colonial")) {
            return "railroad";
        }

        if(nation.equals("Romans")) {
            return "roman_road";
        }

        return "road";
    }

    public static boolean yesno() {
        return (Math.random() * 2 > 1);
    }

    public boolean wallx4() {
        return age.equals("colonial");
    }


    public void nextAge(){
        int idx = Ruleset.ages.indexOf(age) + 1;
        if(idx > Ruleset.ages.size() - 1) {
            idx = 0;
        }
        age = Ruleset.ages.get(idx);
        Buildings.getBuildingSequence(size,age,nation,hasWater,isCapital);
    }
}
