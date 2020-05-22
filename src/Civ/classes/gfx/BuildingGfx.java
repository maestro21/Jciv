package Civ.classes.gfx;

public class BuildingGfx {


    public double x = 0;
    public double y = 0;
    public double w = 0;
    public double h = 0;
    public double dx = 0;
    public double dy = 0;
    public int size = 1;
    public String name = "";
    public String symbol = "";
    public String age = "";
    public boolean wonder = false;
    public String fileName = "";
    public String religion = "";


    public boolean isWonder() {
        return wonder;
    }

    public boolean is(String s) {
        return s.equals(symbol) || s.equals(name);
    }

}
