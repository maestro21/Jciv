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
    public int imgTileSize;

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
            this.imgTileSize = (int)(long)jsonRulset.get("imgTileSize");

            /** load terrain **/
            JSONArray jsonTerrain = (JSONArray)jsonRulset.get("terrain");
            for(int i = 0; i < jsonTerrain.size(); i++) {
                JSONObject jsonTerrainEl = (JSONObject)jsonTerrain.get(i);
                Terrain terrain = new Terrain();
                terrain.name = jsonTerrainEl.get("name").toString();
                terrain.symbol = jsonTerrainEl.get("symbol").toString();
                terrain.type = jsonTerrainEl.get("type").toString();

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
                this.terrain.add(terrain);
            }

            loaded = true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public Terrain getTerrain(String symbol) {

        if(symbol.equals("p")) {
            System.out.print(symbol);
        }

        Terrain t = new Terrain();
        for(int i = 0; i < terrain.size(); i++) {
            t = terrain.get(i);

            if(t.symbol.equals(symbol)) {
                return t;
            }
        }
        return getTerrain(" ");
    }


}
