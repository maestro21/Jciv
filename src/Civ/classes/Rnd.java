package Civ.classes;

import Civ.entities.City;
import Civ.entities.Player;
import Civ.entities.Ruleset;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class Rnd {

    public static Random rand = new Random();

    public static boolean yes() {
        return (Math.random() * 2 > 1);
    }

    public static <T> T getRandomItem(ArrayList<T> list) {
        return list.size()  > 0 ? list.get(rand.nextInt(list.size() - 1)) : null;
    }

    public static String randomAge() {
        return getRandomItem(Ruleset.ages);
    }

    public static String randomNation() {
        return getRandomItem(Ruleset.civNations).getName();
    }

    public static String randomBuildingStyle() {
        return getRandomItem(Ruleset.buildingSets);
    }

    public static Player getRandomPlayer() {
        String nation = randomNation();
        String age = Game.getPreferredAge(nation);//randomAge();
        String gov = Game.getPreferredGov(nation, age);
        Player player = new Player()
                .setColor(Color.RED)
                .setAge(age)
                .setGov(gov);
        return player;
    }

    public static void setRandomStartLocation(Player player) {
        Coords loc = getRandomStartLocation();
        player.setStartLocation(loc);
    }

    public static Coords getRandomStartLocation() {
        while(true) {
            int x = rand.nextInt(Game.map.size.x);
            int y = rand.nextInt(Game.map.size.y);
            if(Game.map.canBuildCity(x,y)) {
                return new Coords(x,y);
            }
        }
    }

    public static String getRandomCivNation() {
        if(Game.civNationNames == null) {
            Game.civNationNames = new ArrayList<>();
            File folder = new File("data/rulesets/" + Game.gameOptions.ruleset + "/nations");
            File[] listOfFiles = folder.listFiles();

            for (File file : listOfFiles) {
                if (file.isFile()) {
                    Game.civNationNames.add(file.getName().replace(".json", ""));
                }
            }
        }

        int i = rand.nextInt(Game.civNationNames.size() - 1);
        return Game.civNationNames.get(i);

    }

    public static String getRandomCityStyle() {
        int i = rand.nextInt(Ruleset.cityStyles.size() - 2) + 2;
        return Ruleset.cityStyles.get(i);
    }


    public static void randomCities() {
        for(int i = 0; i < 1500; i++) {
            int pi = i % Game.players.size();
            Player player = Game.players.get(pi);
            City city = new City();
            city.setPlayer(player);
            city.setCityStyle(player.getCityStyle());
            city.setSize(city.isCapital ? 16 : rand.nextInt(12) + 1);
            city.setName(player.getNewCityName());

            Coords coords = player.findClosestTileForCityFoundation();
            if(coords != null) {
                if(Game.buildCity(coords.x, coords.y, city)) {
                    Buildings.getBuildingSequence(city, player.isCapital());
                    System.out.printf("City %s (%d %s) found (%d,%d, %s)\n", city.getName(), city.getSize(), city.getCityStyle(), coords.x, coords.y, player.getName());
                }
            }
        }

        City city; int cnt = 0;
        for (int y = 0; y < Game.map.size.y; y++) {
            for (int x = 0; x < Game.map.size.x; x++) {
                city = Game.map.getTile(x,y).getCity();
                if(city != null){ cnt ++;
                    System.out.printf("City %s (%d %s) found (%d,%d, %s)\n", city.getName(), city.getSize(), city.getCityStyle(), city.getCoords().x, city.getCoords().y, city.getPlayer().getName());
                }
            }
        }
        System.out.println("Total cities found: " + cnt);
    }

}
