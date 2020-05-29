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
    public  String terrain = "plains";

    public  boolean hasWater = false;

    public String name = "Rome";

    public int year;

    public String flag = "rome";

    public String age = "ancient";

    public int size = 1;

    public boolean palace = true;

    public boolean walled = true;

    public boolean isCapital = true;

    public boolean hasRiver = true;

    public boolean hasFarms = true;

    public boolean hasMines = true;

    public boolean isTerrainX = true;

    public String nation = "Romans";

    public String buildingStyle = "euro";

    public String religion = Ruleset.RELIGION_PAGANISM;

    public ArrayList<String> buildings = new ArrayList<>();

    public String riverBuilding = null;


    public  void randomizeCity() {
        size = (int)(Math.random() * maxSize) + 1;
        //buildingStyle = "classic";
        age = Rnd.randomAge();
        religion = age.equals("ancient") ? Ruleset.RELIGION_PAGANISM : Rnd.randomReligion();
        hasWater = yesno();
        refresh();
    }

    public void refresh() {
        roads = getDefaultRoads(age, nation);
        //buildings = Buildings.getBuildingSequence(size,age,nation,hasWater,isCapital, religion);
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

            if(Buildings.isRiverBuilding(building)) {
                riverBuilding = building;
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

    public void nextTerrain(){
        ArrayList<String> terrains = new ArrayList<>();
        terrains.add("plains");
        terrains.add("grassland");
        terrains.add("hills");
        terrains.add("mountains");
        int idx = terrains.indexOf(terrain); idx++;
        if(idx > terrains.size() - 1) {
            idx = 0;
        }
        System.out.println(terrains.get(idx));
        terrain = terrains.get(idx);
    }

    public void nextRel(){
        int idx = Ruleset.religions.indexOf(religion) + 1;
        if(idx > Ruleset.religions.size() - 1) {
            idx = 0;
        }
        religion = Ruleset.religions.get(idx);
    }

    public void nextStyle(){
        ArrayList<String> terrains = new ArrayList<>();
        terrains.add("plains");
        terrains.add("grassland");
        terrains.add("hills");
        terrains.add("mountains");
        int idx = terrains.indexOf(terrain); idx++;
        if(idx > terrains.size() - 1) {
            idx = 0;
        }
        System.out.println(terrains.get(idx));
        terrain = terrains.get(idx);
    }

    int cityCounter = 2;
    int maxCity = 4;
    public void nextCity() {
        cityCounter++;
        if(cityCounter > maxCity) cityCounter = 0;

        switch(cityCounter) {
            case 0: setCitySettings("Rome",
                    100,
                    "ancient",
                    16,
                    "Romans",
                    "rome",
                    "classic",
                    "grassland",
                    true,
                    Ruleset.RELIGION_PAGANISM,
                    false,
                    true,
                    true,
                    new String[]{"circus"}
            ); break;
            case 1: setCitySettings("Athens",
                    -400,
                    "ancient",
                    4,
                    "Greek",
                    "ancient_greece",
                    "classic",
                    "hills",
                    true,
                    Ruleset.RELIGION_PAGANISM,
                    true,
                    false,
                    true,
                    new String[]{"akropolis"}
            );
            break;
            case 2: setCitySettings(
                    "Constantinopole",
                    500,
                    "medieval",
                    10,
                    "Greek",
                    "ancient_greece",
                    "classic",
                    "grassland",
                    true,
                    "orthodoxy",
                    true,
                    false,
                    true,
                    new String[]{"circus", "sophia"}
            ); break;
            case 3: setCitySettings(
                    "Alexandria",
                    0,
                    "ancient",
                    7,
                    "Romans",
                    "rome",
                    "classic",
                    "plains",
                    true,
                    Ruleset.RELIGION_PAGANISM,
                    true,
                    true,
                    false,
                    new String[]{"glibrary", "lighthouse"}
            ); break;
            case 4: setCitySettings(
                    "Paris",
                    1300,
                    "medieval",
                    7,
                    "French",
                    "france_old",
                    "euro",
                    "grassland",
                    true,
                    "protestantism",
                    false,
                    true,
                    true,
                    new String[]{"notredame"}
            ); break;

        }
    }



    public void setCitySettings(String name, int year, String age, Integer size, String nation, String flag, String buildingStyle, String terrain, boolean walled, String religion, boolean hasWater, boolean hasRiver, boolean  isCapital, String[] wonders) {
        this.year = year;
        this.name = name;
        this.size = size;
        this.age = age;
        this.buildingStyle = buildingStyle;
        this.nation = nation;
        this.flag = flag;
        this.terrain = terrain;
        this.walled = walled;
        this.religion = religion;
        this.hasWater = hasWater;
        this.hasRiver = hasRiver;
        this.isCapital = isCapital;
        buildings = new ArrayList<String>(Arrays.asList(wonders));
        buildings.addAll( Buildings.getBuildingSequence(size,age,nation,hasWater,isCapital, religion));
        processBuildings();
    }
}
