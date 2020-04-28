package Civ.entities;

import Civ.classes.Coords;
import Civ.classes.Game;

import java.awt.*;
import java.util.ArrayList;

public class Player {

    private Game game;

    private String name;

    private Color color;
    private Coords startLocation;

    private ArrayList<City> cities = new ArrayList<>();

    private CivNation civNation;

    private String age = "ancient";

    private int cityNameCounter = 0;

    public void setCities(ArrayList<City> cities) {
        this.cities = cities;
    }

    public CivNation getCivNation() {
        return civNation;
    }

    public void setCivNation(String civNationName) {
        this.civNation = game.ruleset.getCivNation(civNationName);
    }


    public void setCivNation(CivNation civNation) {
        this.civNation = civNation;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public ArrayList<City> getCities() {
        return cities;
    }

    public Coords getStartLocation() {
        return startLocation;
    }

    public Player setStartLocation(Coords startLocation) {
        this.startLocation = startLocation;
        return this;
    }

    public String getName() {
        return name;
    }

    public Player setName(String name) {
        this.name = name;
        return this;
    }

    public Color getColor() {
        return color;
    }

    public Player setColor(Color color) {
        this.color = color;
        return this;
    }

    public String getCityStyle() {
        return civNation.getCityStyle();
    }


    public String getNewCityName() {
        //int i = (int)(Math.random() * (civNation.getCityNames().size()-1));
        String cityName = "City";
        if(civNation.getCityNames().size() > cityNameCounter) {
            cityName = civNation.getCityNames().get(cityNameCounter);
            if (cityName == null || cityName.isEmpty()) {
                cityName = "City";
            }
        }
        cityNameCounter++;
        return cityName;
    }


    public boolean isCapital() {
        return cityNameCounter == 0;
    }

    public Coords findClosestTileForCityFoundation() {
        if(this.getCities().size() == 0) {
            return this.getStartLocation();
        }

        System.out.printf(" == CHECKING CITIES FOR PLAYER %s WITH %d CITIES ==\n", this.getName(), this.cities.size());
        int ci = 0; City city;
        while(true) {
            city = this.cities.get(ci);
            System.out.printf("Checking surroundings around city %s %d, %d\n", city.getName(), city.getCoords().x, city.getCoords().y);
            if(city == null) {
                return null;
            }
            for(int j = city.getCoords().y - 2; j <= city.getCoords().y + 2; j++) {
                for (int i = city.getCoords().x - 2; i <= city.getCoords().x + 2; i++) {
                    if(game.map.canBuildCity(i,j)) {
                        System.out.printf("City can be built in %d %d\n", i,j);
                        return new Coords(i,j);
                    }
                }
            }
            ci++;
            if(ci >= this.cities.size() - 1) {
                return null;
            }
        }
    }
}
