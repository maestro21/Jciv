package Civ.entities;

import Civ.classes.Coords;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

public class Ruleset {

    public String name;

    public int tileSize;

    public Coords coastTile;

    public ArrayList<Terrain> terrain = new ArrayList<>();

    public boolean loaded = false;


    public Ruleset() {}

    public Ruleset(String name) {
        this.load(name);
    }

    public void load(String name) {
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader("data/rulesets/" + name + ".json")) {
            JSONObject jsonRulset = (JSONObject) parser.parse(reader);
            this.name = jsonRulset.get("name").toString();
            JSONArray jsonTerrain = (JSONArray)jsonRulset.get("terrain");
            for(int i = 0; i < jsonTerrain.size(); i++) {
                JSONObject jsonTerrainEl = (JSONObject)jsonTerrain.get(0);
                Terrain terrain = new Terrain();
                terrain.name = jsonTerrainEl.get("name").toString();
                terrain.symbol = jsonTerrainEl.get("symbol").toString();
                JSONArray colors = (JSONArray)jsonTerrainEl.get("colors");
                terrain.color = new Color((int)colors.get(0), colors.get(1), colors.get(2));
            }

            loaded = true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public Terrain getTerrain(String name) {

    }


}
