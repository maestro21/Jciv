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
        try (Reader reader = new FileReader("data/rulesets/" + name + "/ruleset.json")) {
            JSONObject jsonRulset = (JSONObject) parser.parse(reader);
            this.name = jsonRulset.get("name").toString();
            this.tileSize = (int)(long)jsonRulset.get("tileSize");

            /** load terrain **/
            JSONObject jsonTerrain = (JSONObject)jsonRulset.get("terrain");
            for(int i = 0; i < jsonTerrain.size() - 1; i++) {
                JSONObject jsonTerrainEl = (JSONObject)jsonTerrain.get(Integer.toString(i));
                Terrain terrain = new Terrain();
                terrain.name = jsonTerrainEl.get("name").toString();
                terrain.symbol = jsonTerrainEl.get("symbol").toString();

                JSONArray colors = (JSONArray)jsonTerrainEl.get("color");
                terrain.color = new Color(
                        (int)(long)colors.get(0),
                        (int)(long)colors.get(1),
                        (int)(long)colors.get(2)
                );

                JSONArray pos = (JSONArray)jsonTerrainEl.get("pos");
                terrain.pos =  new Coords(
                    (int)(long)pos.get(1),
                    (int)(long)pos.get(0)
                );
            }

            loaded = true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public void getTerrain(String name) {

    }


}
