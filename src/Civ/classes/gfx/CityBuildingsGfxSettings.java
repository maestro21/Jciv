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

    public String flag = "rome";

    public String age = "ancient";

    public int size = 1;

    public boolean palace = true;

    public boolean walled = true;

    public boolean isCapital = true;

    public String nation = "Romans";

    public String buildingStyle = "default";

    public String religion = Ruleset.RELIGION_PAGANISM;

    public ArrayList<String> buildings = new ArrayList<>();


    public  void randomizeCity() {
        size = (int)(Math.random() * maxSize) + 1;
        buildingStyle = "classic";
        age = Rnd.randomAge();
        religion = age.equals("ancient") ? Ruleset.RELIGION_PAGANISM : Rnd.randomReligion();
        hasWater = yesno();
        refresh();
    }

    public void refresh() {
        roads = getDefaultRoads(age, nation);
        buildings = Buildings.getBuildingSequence(size,age,nation,hasWater,isCapital, religion);
        cityBuildings.clear();
        waterBuildings.clear();
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
        religion = city.getReligion();
        buildings = city.buildings;
        processBuildings();
    }

    public  void processBuildings() {
        int i = 0;
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
                continue;
            }

            cityBuildings.add(building);
            i++;
            if(i > size) return;
        }
    }

    public boolean isRailroad() {
        return roads == "railroad";
    }

    public boolean isPagan() {
        return age.equals("ancient") || religion.equals(Ruleset.RELIGION_PAGANISM);
    }


    public static String getDefaultRoads(String age, String nation) {
        if(Ruleset.hasAge(age, "modern")) {
            return "highway";
        }

        if(Ruleset.hasAge(age, "colonial")) {
            return "railroad";
        }

        if(nation.equals("Romans")) {
            return "romanroad";
        }

        return "road";
    }

    public static boolean yesno() {
        return (Math.random() * 2 > 1);
    }

    public boolean wallx4() {
        return Ruleset.hasAge(age, "colonial");
    }


    public void nextAge(){
        int idx = Ruleset.ages.indexOf(age) + 1;
        if(idx > Ruleset.ages.size() - 1) {
            idx = 0;
        }
        age = Ruleset.ages.get(idx);
    }

    public void nextRel(){
        int idx = Ruleset.religions.indexOf(religion) + 1;
        if(idx > Ruleset.religions.size() - 1) {
            idx = 0;
        }
        religion = Ruleset.religions.get(idx);
    }
}
