package Civ.classes.gfx;

import sun.font.BidiUtils;

import java.util.ArrayList;
import java.util.Arrays;

class CityLayout {

    private ArrayList<BuildingGfx> buildingsGfx;
    public BuildingGfx[][] cityLayout;
    private BuildingGfx[][] buildingMatrix;
    ArrayList<String> buildings = new ArrayList<>();
    int buildingMatrixSize;
    int cityLayoutMatrixSize;
    int bc = 0;
    boolean walled;
    private BuildingGfx emptyBuilding = new BuildingGfx();

    public BuildingGfx getBuilding(int x, int y) {
        return cityLayout[y][x];
    }

    public CityLayout(int citySize, String[] buildings, boolean walled, ArrayList<BuildingGfx> buildingsGfx) {
        this.walled = walled;
        int buildingCount = (buildings.length  > citySize ? citySize : buildings.length);
        this.buildingsGfx = buildingsGfx;

        int houseIndex = 0;
        for(int i = 0; i < citySize; i++) {
            if(i < buildings.length) {
                this.buildings.add(buildings[i]);
                BuildingGfx b = getBuilding(buildings[i]);
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
        buildingMatrix = new BuildingGfx[buildingMatrixSize][buildingMatrixSize];

        spiralBuildingsCounterClockwise();
        printCity();
        cityLayout();
        printCityLayout();
    }

    public void cityLayout() {
        cityLayoutMatrixSize = buildingMatrixSize * 2 + 3;
        cityLayout = new BuildingGfx[cityLayoutMatrixSize][cityLayoutMatrixSize];
        int half = (int)Math.ceil((double)buildingMatrixSize / 2);
        int r = half * 2 + 1;
        int i; int ix; int iy; int j;

        int e = cityLayoutMatrixSize - 1;

        for(i = 0; i < buildingMatrixSize; i++ ) {
            ix = 1 + i * 2;
            if (i >= half) {
                ix++;
            }
            for(j = 0; j < buildingMatrixSize; j++ ) {
                iy = 1 + j * 2;
                if (j >= half) {
                    iy++;
                }
                cityLayout[iy][ix] = buildingMatrix[j][i];
            }
        }


        for (i = 0; i < cityLayoutMatrixSize; i++) {
            if(walled) {
                cityLayout[i][0] = getBuilding("wallv");
                cityLayout[i][e] = getBuilding("wallv");
                cityLayout[0][i] = getBuilding("wallh");
                cityLayout[e][i] = getBuilding("wallh");
            }
            cityLayout[i][r] = getBuilding("roadv");
            cityLayout[r][i] = getBuilding("roadh");
        }
        if(walled) {
            cityLayout[0][0] = getBuilding("tower");
            cityLayout[0][e] = getBuilding("tower");
            cityLayout[e][0] = getBuilding("tower");
            cityLayout[e][e] = getBuilding("tower");
            cityLayout[r][r] = getBuilding("roadx");
            cityLayout[r][0] = getBuilding("gateh");
            cityLayout[r][e] = getBuilding("gateh");
            cityLayout[0][r] = getBuilding("gatev");
            cityLayout[e][r] = getBuilding("gatev");
        }
    }


    private void spiralBuildingsCounterClockwise() {
        System.out.println("\n\nCounter Clockwise elements:");

        int right = buildingMatrixSize/2;
        int left = right - 1;
        int top = (buildingMatrixSize%2 == 0) ? (buildingMatrixSize/2 - 1) : (buildingMatrixSize/2);
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

    private BuildingGfx getBuilding(String name) {
        for(int i = 0; i < buildingsGfx.size(); i++) {
            if(buildingsGfx.get(i) != null && buildingsGfx.get(i).name.equals(name)) {
                return buildingsGfx.get(i);
            }
        }
        return null;
    }

    private boolean setBuilding(int i, int j) {
        if (i < 0 || i >= buildingMatrixSize || j < 0 || j>= buildingMatrixSize) return false;
        if(buildingMatrix[i][j] != null) {
            return true;
        }
        if(bc < buildings.size()) {
            BuildingGfx buildingGfx =  getBuilding( buildings.get(bc));
            buildingMatrix[i][j] = buildingGfx;
            if(buildingGfx.size > 1 ) {
                int dj = j + 1;
                if(dj >= buildingMatrixSize) {
                    dj = j - 1;
                }
                if(buildingMatrix[i][dj] != null) {
                    buildings.add(buildingMatrix[i][dj].name);
                }
                buildingMatrix[i][dj] = emptyBuilding;
                printMatrix(buildingMatrix,buildingMatrixSize);
            }
            bc++;
        }
        return true;
    }

    public void printCity() {
        printMatrix(buildingMatrix, buildingMatrixSize);
    }

    public void printCityLayout() {
        printMatrix(cityLayout, cityLayoutMatrixSize);
    }

    public void printMatrix(BuildingGfx[][] matrix, int size) {

        String symbol = "";
        for(int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if(matrix[y][x] != null) {
                    symbol = matrix[y][x].symbol;
                } else {
                    symbol = " ";
                }
                System.out.print(symbol + ' ');
            }
            System.out.println();
        }
    }

} 