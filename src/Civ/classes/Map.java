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

    private Tile[][] tiles;

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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Tile getTile(int x, int y) {
        return tiles[y][x];
    }

    public Tile getNeigbourTile(int x, int y) {
        if(x < 0) {
            // todo if flat - return null;
            x = size.x - 1;
        }

        if(x >= size.x) {
            // todo if flat - return null;
            x = 0;
        }

        if(y < 0 || y >= size.y) {
            return null;
        }

        return tiles[y][x];
    }

    public Tile getLeftTile(int x, int y) {
        return getNeigbourTile(x - 1, y);
    }

    public Tile getRightTile(int x, int y) {
        return getNeigbourTile(x + 1, y);
    }

    public Tile getTopTile(int x, int y) {
        return getNeigbourTile(x, y - 1);
    }

    public Tile getBottomTile(int x, int y) {
        return getNeigbourTile(x, y + 1);
    }

    public Tile getTopLeftTile(int x, int y) {
        return getNeigbourTile(x - 1, y - 1);
    }

    public Tile getTopRightTile(int x, int y) {
        return getNeigbourTile(x + 1, y - 1);
    }

    public Tile getBottomLeftTile(int x, int y) {
        return getNeigbourTile(x - 1, y + 1);
    }

    public Tile getBottomRightTile(int x, int y) {
        return getNeigbourTile(x + 1, y + 1);
    }

    public Tile[] getNeighbourTiles4(int x, int y) {
        Tile[] neighbours = new Tile[4];
        neighbours[0] = getLeftTile(x,y);
        neighbours[1] = getTopTile(x,y);
        neighbours[2] = getRightTile(x,y);
        neighbours[3] = getBottomTile(x,y);
        return neighbours;
    }

    public Tile[] getNeighbourTiles8(int x, int y) {
        Tile[] neighbours = new Tile[8];
        neighbours[0] = getTopLeftTile(x,y);
        neighbours[1] = getTopTile(x,y);
        neighbours[2] = getTopRightTile(x,y);
        neighbours[3] = getLeftTile(x,y);
        neighbours[4] = getRightTile(x,y);
        neighbours[5] = getBottomLeftTile(x,y);
        neighbours[6] = getBottomTile(x,y);
        neighbours[7] = getBottomRightTile(x,y);
        return neighbours;
    }

    public boolean getCoast(int x, int y) {
        Tile[] neigborTiles = getNeighbourTiles4(x,y);

        for (int i = 0; i < neigborTiles.length; i++) {
            if(neigborTiles[i] != null && neigborTiles[i].terrain.isWater()) {
                return true;
            }
        }

        return false;
    }

    public boolean getWater(int x, int y) {
        return getTile(x,y).terrain.isWater();
    }

}
