package Civ.classes;

import Civ.classes.gfx.GameFrame;
import Civ.entities.City;
import Civ.entities.Player;
import Civ.entities.Ruleset;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

public class Game extends JPanel {

    public Random rand;
    public Map map;
    public Ruleset ruleset;
    public GameOptions gameOptions;
    public Gfx gfx;
    public GameFrame gameFrame;
    public ArrayList<Player> players = new ArrayList<>();

    public Game(GameOptions gameOptions){
        this.rand = new Random();
        this.gameOptions = gameOptions;
        ruleset = new Ruleset(gameOptions.ruleset);
        map = new Map(this, gameOptions.map);
        randomPlayers();
        randomCities();
        start();
    }

    public void start() {
        this.gfx = new Gfx(this);
        this.gameFrame = new GameFrame(this);
    }

    public void randomPlayers(){
        for(int i = 0; i < gameOptions.totalPlayers; i++) {
            Player player = new Player();
            player.setCityStyle(getRandomCityStyle());
            player.setName("Player " + (i + 1));
            player.setGame(this);
            setRandomStartLocation(player);
            players.add(player);
            System.out.printf("%s with city style %s created (%d, %d) \n", player.getName(), player.getCityStyle(), player.getStartLocation().x, player.getStartLocation().y);
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

    public String getRandomCityStyle() {
        int i = rand.nextInt(ruleset.cityTypes.size() - 2) + 2;
        return ruleset.cityTypes.get(i);
    }

    public void randomCities() {
       for(int i = 0; i < 1000; i++) {
           int pi = i % players.size();
           Player player = players.get(pi);
           City city = new City();
           city.setPlayer(player);
           city.setCityStyle(player.getCityStyle());
           city.setSize(rand.nextInt(8) + 1);
           city.setName("City-" + (i + 1));

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
