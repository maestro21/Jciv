package Civ.classes;

import Civ.entities.Terrain;
import Civ.entities.Tile;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class Map {

    Game game;
    String fileName;
    String name;
    public Coords size;

    public Tile[][] tiles;


    Map(Game game, String name) {
        this.game = game;
        loadMap(name);
    }

    public void loadMap(String name){
        JSONParser parser = new JSONParser();
        JSONObject jsonObject;

        this.name = name;
        this.fileName = "data/maps/" + name + "/map.json";

        try (Reader reader = new FileReader(fileName)) {
            jsonObject = (JSONObject) parser.parse(reader);
            JSONArray terrain = (JSONArray)jsonObject.get("terrain");
            String row = (String) terrain.get(0);
            this.size = new Coords();
            size.y = terrain.size();
            size.x = row.length();

            this.tiles = new Tile[size.y][size.x];
            for (int y = 0; y < size.y; y++) {
                row = (String) terrain.get(y);
                for (int x = 0; x < row.length(); x++) {
                    String t = Character.toString(row.charAt(x));
                    Tile tile = new Tile();

                    this.tiles[y][x] = tile;
                    tile.terrain = game.ruleset.getTerrain(t);
                }
                System.out.println();
            }

            /*
            setPreferredSize(new Dimension(screenSize.width, screenSize.height));
            screenCoords = new ScreenCoords(WIDTH,HEIGHT, ruleset.tileSize, false);
            screenCoords.setScreenSize(screenSize.width, screenSize.height);
            screenCoords.goTo(WIDTH / 2, HEIGHT / 2); */
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }



    public Tile getTile(int y, int x) {
        return tiles[y][x];
    }

}
