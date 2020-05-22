package Civ.classes.gfx;

import Civ.classes.Buildings;
import Civ.classes.Coords;
import Civ.entities.Ruleset;

import java.nio.file.Paths;
import java.util.ArrayList;

class CityLayout {

    private ArrayList<BuildingGfx> buildingsGfx;
    public BuildingGfx park = null;
    public BuildingGfx[][] cityLayout;
    private BuildingGfx[][] buildingMatrix;
    ArrayList<String> buildings = new ArrayList<>();
    int buildingMatrixSize;
    Coords cityLayoutMatrixSize;
    int bc = 0;
    boolean walled;
    boolean wallx4;
    boolean railroad;
    String age;
    Coords bmSize;

    Double mod = 0.8;

    public boolean isBigStreet() {
        return settings.size > 5;
    }

    CityBuildingsGfxSettings settings;

    public Coords cityCenter;

    private BuildingGfx emptyBuilding = new BuildingGfx();

    public BuildingGfx getBuilding(int x, int y) {
        try {
            return cityLayout[y][x];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }


    public CityLayout(CityBuildingsGfxSettings settings, ArrayList<BuildingGfx> buildingsGfx) {
        this.settings = settings;
        this.walled = settings.walled && !settings.walls.isEmpty();
        this.wallx4 = settings.wallx4();
        this.age = settings.age;
        int citySize = settings.size;
        int buildingCount = settings.cityBuildings.size();
        this.buildingsGfx = buildingsGfx;
        park = getBuilding("park");

        int houseIndex = 0;
        for(int i = 0; i < citySize; i++) {
            if(i < buildingCount) {
                this.buildings.add(settings.cityBuildings.get(i));
                BuildingGfx b = getBuilding(settings.cityBuildings.get(i));
                if(b != null && b.size > 1) {
                    buildingCount += b.size - 1;
                }
            }
            houseIndex = (int)Math.ceil((citySize - i ) / 4.0);
            if(houseIndex > 4) houseIndex = 4;
            if(houseIndex < 1) houseIndex = 1;
            this.buildings.add("house" + houseIndex);

        }

        buildingMatrixSize = (int)Math.ceil(Math.sqrt(citySize + buildingCount));
        bmSize = new Coords(buildingMatrixSize, buildingMatrixSize);
        buildingMatrix = new BuildingGfx[buildingMatrixSize][buildingMatrixSize];
        spiralBuildingsCounterClockwise();
        printCity();
        //cleanEmptyRows();
        System.out.println("After row cleaning:");
        printCity();
        cityLayout();
        printCityLayout();
    }

    public void cleanEmptyRows() {
        boolean rmTop = true;
        boolean rmBottom = true;
        boolean rmLeft = true;
        boolean rmRight = true;
        int s = buildingMatrixSize - 1;
        for(int i = 0; i <= s; i++) {
            if(buildingMatrix[0][i] != null) rmTop = false;
            if(buildingMatrix[s][i] != null) rmBottom = false;
            if(buildingMatrix[i][0] != null) rmLeft = false;
            if(buildingMatrix[i][s] != null) rmRight = false;
        }

        int xFrom = rmLeft ? 1 : 0;
        int xTo = buildingMatrixSize - (rmRight ? 1 : 0);
        int yFrom = rmTop ? 1 : 0;
        int yTo = buildingMatrixSize - (rmBottom ? 1 : 0);

        BuildingGfx[][] matrix = new BuildingGfx[yTo - yFrom][xTo - xFrom];

        for(int y = yFrom; y < yTo; y++){
            for(int x = xFrom ; x < xTo ; x++){
                matrix[y- yFrom][x - xFrom] = buildingMatrix[y][x];
            }
        }

        buildingMatrix = matrix;

        bmSize.y = matrix.length;
        bmSize.x = matrix[0].length;
    }


    public void cityLayout() {
        int mod = 3;

        cityLayoutMatrixSize = new Coords((bmSize.x + 1) * mod, (bmSize.y + 1) * mod);
        cityLayout = new BuildingGfx[cityLayoutMatrixSize.y][cityLayoutMatrixSize.x];
        int halfX = (int)Math.ceil((double)bmSize.x / 2);
        int halfY = (int)Math.floor((double)bmSize.y / 2);
        int i; int ix; int iy; int j;
        cityCenter = new Coords(halfX * mod + 1,halfY * mod + 1);


        for(i = 0; i < bmSize.x; i++ ) {
            ix = 2 + i * mod;
            if (i >= halfX && isBigStreet()) { ix++;}
            for(j = 0; j < bmSize.y; j++ ) {
                iy = 2 + j * mod;
                if (j >= halfY && isBigStreet()) { iy++;}
                BuildingGfx buildingGfx = buildingMatrix[j][i];
                if(buildingGfx == null) {
                    buildingGfx = park;
                }
                cityLayout[iy][ix] = buildingGfx;
            }
        }
        //addWalls();
    }

    /** @deprecated */
    public void addWalls() {
        if(!walled || settings.walls.isEmpty()) {
            return;
        }
        int s = 1;
        int e = cityLayoutMatrixSize.x - 1; if(!isBigStreet()) e--;
        int ey = e;
        /*if(isEmptyRow()) {
            ey--;
            ey--;
        } */

        int cx = cityCenter.x;
        int cy = cityCenter.y;

        BuildingGfx gatev, gateh;


        if(wallx4) {
            gatev = getBuilding("gate_v");
            gateh = getBuilding("gate_h");

            for (int i = s; i < e; i++) {
                if (i < ey) {
                    cityLayout[i][s] = getBuilding("wall_l");
                    cityLayout[i][e] = getBuilding("wall_r");
                }
                cityLayout[s][i] = getBuilding("wall_b");
                cityLayout[ey][i] = getBuilding("wall_t");
            }
            cityLayout[s][s] = getBuilding("tower_l");
            cityLayout[s][e] = getBuilding("tower_b");
            cityLayout[ey][s] = getBuilding("tower_t");
            cityLayout[ey][e] = getBuilding("tower_r");
            cityLayout[cy][s] = gatev;
            cityLayout[cy][e] = gatev;
            cityLayout[s][cx] = gateh;
            cityLayout[ey][cx] = gateh;

        } else {
            gatev = getBuilding("gatev");
            gateh = getBuilding("gateh");

            for (int i = s; i < e; i++) {
                if (i < ey) {
                    cityLayout[i][s] = getBuilding("wallv");
                    cityLayout[i][e] = getBuilding("wallv");
                }
                cityLayout[s][i] = getBuilding("wallh");
                cityLayout[ey][i] = getBuilding("wallh");
            }
            cityLayout[s][s] = getBuilding("tower");
            cityLayout[s][e] = getBuilding("tower");
            cityLayout[ey][s] = getBuilding("tower");
            cityLayout[ey][e] = getBuilding("tower");

            cityLayout[cy][s] = gatev;
            cityLayout[cy][e] = gatev;
            cityLayout[s][cx] = gateh;
            cityLayout[ey][cx] = gateh;
        }

        if(isBigStreet()) {
            gatev.dy = -1;
            gatev.dx = 0.5;
            gateh.dx = 0.5;
        }
    }


    public int getCityCenter() {
        return (buildingMatrixSize%2 == 0) ? (buildingMatrixSize/2 - 1) : (buildingMatrixSize/2);
    }

    private void spiralBuildingsCounterClockwise() {
        System.out.println("\n\nCounter Clockwise elements:");

        int right = buildingMatrixSize/2;
        int left = right - 1;
        int top =  getCityCenter();
        int bottom = top + 1;

        OUTER: while (true) {

            for (int i = right; i > left; i--) {
                if(!setBuilding(top, i)) break OUTER;
            }
            right++;

            for (int i = top; i < bottom; i++) {
                if(!setBuilding(i, left)) break OUTER;
            }
            top--;

            for (int i = left; i < right; i++) {
                if(!setBuilding(bottom, i)) break OUTER;
            }
            left--;

            for (int i = bottom; i > top; i--) {
                if(!setBuilding(i, right)) break OUTER;
            }
            bottom++;
        }
    }


    /**
     * Building functions
     */

    public BuildingGfx getBuilding(String name) {
        BuildingGfx bgfx = null, _bgfx;
        name = getBuildingGfxNameByName(name);
        for(int i = 0; i < buildingsGfx.size(); i++) {
            _bgfx = buildingsGfx.get(i);
            if(_bgfx != null && _bgfx.name.equals(name) && Ruleset.hasAge(age, _bgfx.age)) {
                if(Buildings.isReligious(name) && !settings.religion.equals(_bgfx.religion)) continue;
                if(bgfx == null || Ruleset.hasAge(_bgfx.age, bgfx.age))
                bgfx = buildingsGfx.get(i);
            }
        }
        if(bgfx == null) {
            System.out.println("Cannot find building " + name);
        }

        return bgfx;
    }


    public String getBuildingGfxNameByName(String name) {
        switch (name) {
            case "townhall":
                return "house4";

            case "temple":
                if(!settings.isPagan())
                    return "church";
        }

        return name;
    }

    private void replaceBuilding(int x, int y) {
        replaceBuilding(x,y, emptyBuilding);
    }


    private void replaceBuilding(int x, int y, BuildingGfx building) {
        if(buildingMatrix[y][x] != null) {
            buildings.add(buildingMatrix[y][x].name);
        }
        buildingMatrix[y][x] = building;
    }

    /** @deprecated */
    private void placeAqueduct(int x, int y) {
        for(int i = 0; i < buildingMatrixSize; i++) {
            BuildingGfx buildingGfx =  buildingMatrix[y][i];
            if(buildingGfx != null && buildingGfx.size == 4) {
                buildingMatrix[y][x] = buildingMatrix[y--][x];
                y--;
                buildingMatrix[y][x] = getBuilding("aqueduct");
                placeAqueduct(x,y);
            }
        }
    }

    private boolean setBuilding(int i, int j) {
        if (i < 0 || i >= buildingMatrixSize || j < 0 || j>= buildingMatrixSize) return false;
        if(buildingMatrix[i][j] != null) {
            return true;
        }
        if(bc < buildings.size()) {
            BuildingGfx buildingGfx =  getBuilding(buildings.get(bc));
            if(buildingGfx == null) {
                return true;
            }

            buildingMatrix[i][j] = buildingGfx;

            if(buildingGfx.size > 1 ) {
                //printMatrix(buildingMatrix,buildingMatrixSize);
                int dj = j + 1;
                if(dj >= buildingMatrixSize) {
                    dj = j - 1;
                }
                replaceBuilding(dj,i);

                if(buildingGfx.size == 4) {
                    int di = i + 1;
                    if(di >= buildingMatrixSize) {
                        di = i - 1;
                    }
                    replaceBuilding(j,di);
                    replaceBuilding(dj,di);
                }

                //printMatrix(buildingMatrix,buildingMatrixSize);
            }
            bc++;
        }
        return true;
    }


    /**
     * Print functions
     */

    public void printCity() {
        printMatrix(buildingMatrix);
    }

    public void printCityLayout() {
        printMatrix(cityLayout);
    }

    public void printMatrix(BuildingGfx[][] matrix) {

        String symbol = "";
        for(int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[0].length; x++) {
                if(matrix[y][x] != null) {
                    symbol = matrix[y][x].symbol;
                } else {
                    symbol = "_";
                }
                System.out.print(symbol + ' ');
            }
            System.out.println();
        }
    }


    /**
     * draw functions
     */
    public void drawCityLayout() {

    }
} 