package Civ.entities;

import Civ.classes.Coords;

public class RulesetUnit {

    private Coords gfxCoords;

    private String gfxName;

    private String Name;

    private int attack;

    private int defense;

    private int hp;

    private int moves;

    /**
     *
     */
    private String flags;

    public Coords getGfxCoords() {
        return gfxCoords;
    }

    public void setGfxCoords(Coords gfxCoors) {
        this.gfxCoords = gfxCoors;
    }

    public String getGfxName() {
        return gfxName;
    }

    public void setGfxName(String gfxName) {
        this.gfxName = gfxName;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }


}
