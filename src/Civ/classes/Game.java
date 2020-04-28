package Civ.classes;

import Civ.classes.gfx.GameFrame;
import Civ.entities.City;
import Civ.entities.Player;
import Civ.entities.Ruleset;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Stream;

public class Game extends JPanel {

    public Random rand;
    public Map map;
    public Ruleset ruleset;
    public GameOptions gameOptions;
    public Gfx gfx;
    public GameFrame gameFrame;
    public ArrayList<Player> players = new ArrayList<>();
    public ArrayList<String> civNationNames;

    public Game(GameOptions gameOptions){
        this.rand = new Random();
        this.gameOptions = gameOptions;
        ruleset = new Ruleset(gameOptions.ruleset);
        map = new Map(this, gameOptions.map);
        testPlayers();
        //randomPlayers();
        randomCities();
        start();
    }

    public void start() {
        this.gfx = new Gfx(this);
        this.gameFrame = new GameFrame(this);
    }

    public void testPlayers() {
        // nation, location, age
        putPlayer("Caesar", "Romans", Color.WHITE, new Coords(102, 34));
        putPlayer("Aleksandr", "Greeks", Color.BLUE, new Coords(112, 36));
        putPlayer("Cleopatra", "Egyptians", Color.YELLOW, new Coords(117, 48));
        putPlayer("Stalin", "Russians", Color.RED, new Coords(124, 17));
        putPlayer("Bismark", "Germans", Color.GRAY, new Coords(106, 19));
        putPlayer("Saladin", "Arabs", Color.GREEN, new Coords(130, 48));
        putPlayer("Darius", "Persians", Color.MAGENTA, new Coords(135, 39));
        putPlayer("Ghandi", "Indians", Color.ORANGE, new Coords(148, 51));
        putPlayer("Mao", "Chinese", Color.YELLOW, new Coords(181, 36));
        putPlayer("Ghenghis", "Mongols", Color.RED, new Coords(173, 20));
        putPlayer("Tokugawa", "Japanese", Color.MAGENTA, new Coords(197, 35));
        putPlayer("Siam", "Siam", Color.ORANGE, new Coords(167, 48));

        putPlayer("Washington", "Americans", Color.lightGray, new Coords(50, 29));
        putPlayer("Louis", "French", Color.BLUE, new Coords(50, 19)); //canada
        putPlayer("Louis", "French", Color.BLUE, new Coords(95, 48)); //africa

        putPlayer("Philip", "Spanish", Color.ORANGE, new Coords(41, 38));
        putPlayer("Jaume", "Brasilians", Color.ORANGE, new Coords(66, 64)); // portugals
        putPlayer("Churchill", "British", Color.RED, new Coords(200, 75)); // australia
        putPlayer("Churchill", "British", Color.GREEN, new Coords(110, 85)); //africa


        putPlayer("Montezuma", "Aztecs", Color.YELLOW, new Coords(48, 64));
    }

    public void putPlayer(String name, String nation, Color color, Coords coords) {
        Player player = new Player();
        player.setColor(color);
        player.setName(name);
        player.setGame(this);
        player.setCivNation(nation);
        player.setStartLocation(coords);
        players.add(player);
        System.out.printf("%s (%s) with city style %s created (%d, %d) \n", player.getName(), player.getCivNation().getName(), player.getCityStyle(), player.getStartLocation().x, player.getStartLocation().y);

    }

    public void randomPlayers(){
        for(int i = 0; i < gameOptions.totalPlayers; i++) {
            Coords startLocation = getRandomStartLocation();
            String name = "Player " + (i + 1);
            String nation = getRandomCivNation();
            Color color = ruleset.getColor(i);
            putPlayer(name, nation, color, startLocation);
        }
    }

    public void setRandomStartLocation(Player player) {
        while(true) {
            int x = rand.nextInt(map.size.x);
            int y = rand.nextInt(map.size.y);
            if(map.canBuildCity(x,y)) {
                Coords loc = new Coords(x,y);
                player.setStartLocation(loc);
                return;
            }
        }
    }

    public Coords getRandomStartLocation() {
        while(true) {
            int x = rand.nextInt(map.size.x);
            int y = rand.nextInt(map.size.y);
            if(map.canBuildCity(x,y)) {
                return new Coords(x,y);
            }
        }
    }

    public String getRandomCivNation() {
        if(civNationNames == null) {
            civNationNames = new ArrayList<>();
            File folder = new File("data/rulesets/" + gameOptions.ruleset + "/nations");
            File[] listOfFiles = folder.listFiles();

            for (File file : listOfFiles) {
                if (file.isFile()) {
                    civNationNames.add(file.getName().replace(".json", ""));
                }
            }
        }

        int i = rand.nextInt(civNationNames.size() - 1);
        return civNationNames.get(i);

    }

    public String getRandomCityStyle() {
        int i = rand.nextInt(ruleset.cityTypes.size() - 2) + 2;
        return ruleset.cityTypes.get(i);
    }

    public void randomCities() {
       for(int i = 0; i < 1500; i++) {
           int pi = i % players.size();
           Player player = players.get(pi);
           City city = new City();
           city.isCapital = player.isCapital();
           city.game = this;
           city.setPlayer(player);
           city.setCityStyle(player.getCityStyle());
           city.setSize(city.isCapital ? 16 : rand.nextInt(12) + 1);
           city.setName(player.getNewCityName());

           Coords coords = player.findClosestTileForCityFoundation();
           if(coords != null) {
               if(buildCity(coords.x, coords.y, city)) {
                   System.out.printf("City %s (%d %s) found (%d,%d, %s)\n", city.getName(), city.getSize(), city.getCityStyle(), coords.x, coords.y, player.getName());
               }
           }
       }

        City city; int cnt = 0;
        for (int y = 0; y < map.size.y; y++) {
            for (int x = 0; x < map.size.x; x++) {
                city = map.getTile(x,y).getCity();
                if(city != null){ cnt ++;
                    System.out.printf("City %s (%d %s) found (%d,%d, %s)\n", city.getName(), city.getSize(), city.getCityStyle(), city.getCoords().x, city.getCoords().y, city.getPlayer().getName());
                }
            }
        }
        System.out.println("Total cities found: " + cnt);
    }


    public boolean buildCity(int x, int y, City city) {
        if(!map.canBuildCity(x,y)) {
            return false;
        }

        Coords coords = new Coords(x,y);
        city.setCoords(coords);
        city.getPlayer().getCities().add(city);
        map.putCityOnMap(x,y,city);

       // System.out.printf("City %s (%d %s) found (%d,%d, %s)\n", city.getName(), city.getSize(), city.getCityStyle(), city.getCoords().x, city.getCoords().y, city.getPlayer().getName());
        return true;
    }

}
