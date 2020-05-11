package Civ.classes;

import Civ.entities.City;
import Civ.entities.Ruleset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Buildings {

    /**
     * Building constants; they are constant
     */
    public static final String GRANARY = "granary";
    public static final String BARRACKS = "barracks";
    public static final String MARKET = "market";
    public static final String SUPERMARKET = "supermarket";
    public static final String BANK = "bank";
    public static final String BUSINESSCENTER = "businesscenter";
    public static final String PALACE = "townhall";
    public static final String TOWNHALL = "townhall";
    public static final String TEMPLE = "temple";
    public static final String CATHEDRAL = "cathedral";
    public static final String ENTERTAINMENT = "entertainment";
    public static final String LIBRARY = "library";
    public static final String UNIVERSITY = "university";
    public static final String FACTORY = "factory";
    public static final String AIRPORT = "airport";
    public static final String HES = "hes";
    public static final String NUCLEARPLANT = "nuclearplant";
    public static final String ROBOPLANT = "roboplant";
    public static final String VFARM = "vfarm";
    public static final String AQUEDUCT = "aqueduct";
    public static final String PORT = "port";
    public static final String WALLS = "walls";

    public static final String WONDER_GREAT_TEMPLE = "great_temple";
    public static final String WONDER_GREAT_CATHEDRAL = "great_cathedral";
    public static final String WONDER_GREAT_ENTERTAINMENT = "great_entertainment";
    public static final String WONDER_COLOSSUS = "colossus";
    public static final String WONDER_LIGHTHOUSE = "lighthouse";
    public static final String WONDER_KREMLIN = "kremlin";

    public static final List walls = Arrays.asList(
            WALLS,
            WONDER_KREMLIN
    );

    public static final List townCenter = Arrays.asList(
        PALACE,
        TOWNHALL
    );

    public static final List waterBuildings = Arrays.asList(
        PORT,
        WONDER_COLOSSUS,
        WONDER_LIGHTHOUSE
    );

    public static Game game;


    public static boolean isWaterBuilding(String name) {
        return waterBuildings.contains(name);
    }

    public static boolean isWall(String name) {
        return walls.contains(name);
    }


    public static boolean isTownCenter(String name) {
        return townCenter.contains(name);
    }


    public static ArrayList<String> getBuildingSequence(City city, boolean isCapital) {
        return getBuildingSequence(city.getSize(), city.getAge(), city.getNation(), city.hasWater(), isCapital);
    }

    public static ArrayList<String> getBuildingSequence(int citySize, String age, String nation, boolean hasWater, boolean isCapital) {
        ArrayList<String> buildings = new ArrayList<>();
        if(isCapital) {
            buildings.add(Buildings.PALACE);
        } else {
            if(citySize > 3) {
                buildings.add(Buildings.TOWNHALL);
            }
        }


        if(hasWater) {
            buildings.add(Buildings.PORT);
        }


        buildings.add(Buildings.GRANARY);
        buildings.add(Buildings.BARRACKS);
        buildings.add(Buildings.MARKET);
        buildings.add(Buildings.TEMPLE);
        buildings.add(Buildings.LIBRARY);
        buildings.add(Buildings.WALLS);
        if(nation.equals("roman")) {
            buildings.add(Buildings.AQUEDUCT);
        }

        buildings.add(Buildings.ENTERTAINMENT);


        if(Ruleset.hasAge(age, "medieval")) {
            buildings.add(Buildings.CATHEDRAL);
            buildings.add(Buildings.UNIVERSITY);
        }

        if(Ruleset.hasAge(age, "colonial")) {
            buildings.add(Buildings.BANK);
        }

        if(Ruleset.hasAge(age, "modern")) {
            buildings.add(Buildings.FACTORY);
            buildings.add(Buildings.SUPERMARKET);
            buildings.add(Buildings.AIRPORT);
            if(hasWater) {
                buildings.add(Buildings.HES);
            }
            buildings.add(Buildings.NUCLEARPLANT);
        }

        if(Ruleset.hasAge(age, "postmodern")) {
            buildings.add(Buildings.BUSINESSCENTER);
            buildings.add(Buildings.ROBOPLANT);
            buildings.add(Buildings.VFARM);
        }

        return buildings;
        //city.setBuildings((ArrayList<String>) buildings.subList(0,city.getSize()));
    }




}




