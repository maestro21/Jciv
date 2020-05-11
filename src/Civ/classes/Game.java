package Civ.classes;

import Civ.classes.gfx.GameFrame;
import Civ.classes.gfx.GameGfx;
import Civ.entities.City;
import Civ.entities.Player;
import Civ.entities.Ruleset;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Game extends JPanel {

    public static Map map;
    public static Ruleset ruleset;
    public static GameOptions gameOptions;
    public static GameGfx gfx;
    public static GameFrame gameFrame;
    public static ArrayList<Player> players = new ArrayList<>();
    public static ArrayList<String> civNationNames;
    public static ArrayList<String> builtWonders = new ArrayList<>();

    public Game(GameOptions gameOptions){
        Game.gameOptions = gameOptions;
        map = new Map(this, gameOptions.map);
        addTestPlayers();
        //randomPlayers();
        Rnd.randomCities();
        start();
    }

    public void start() {
        this.gameFrame = new GameFrame();
    }

    public static void addTestPlayers() {
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

    public static void putPlayer(String name, String nation, Color color, Coords coords) {
        Player player = new Player();
        player.setColor(color);
        player.setName(name);
        player.setCivNation(nation);
        player.setStartLocation(coords);
        players.add(player);
        System.out.printf("%s (%s) with city style %s created (%d, %d) \n", player.getName(), player.getCivNation().getName(), player.getCityStyle(), player.getStartLocation().x, player.getStartLocation().y);
    }


    public static String getPreferredGov(String nation, String age) {
        switch(age) {
            case "ancient":
                switch(nation) {
                    case "greek":
                        return "democracy";
                }
                return "monarchy";
        }
        return "tribalism";
    }

    public static String getPreferredAge(String nation) {
        switch(nation) {
            case "Turks":
            case "Arabs":
            case "Germans":
            case "French":
                return "medieval";

            case "Brasilians":
            case "Spanish":
            case "Russians":
                return "colonial";

            case "Americans":
                return "modern";

            default:
                return "ancient";
        }
    }


    public static void randomPlayers(){
        for(int i = 0; i < gameOptions.totalPlayers; i++) {
            Coords startLocation = Rnd.getRandomStartLocation();
            String name = "Player " + (i + 1);
            String nation = Rnd.getRandomCivNation();
            Color color = ruleset.getColor(i);
            putPlayer(name, nation, color, startLocation);
        }
    }

    public static boolean buildCity(int x, int y, City city) {
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
