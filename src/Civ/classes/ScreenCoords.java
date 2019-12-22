package Civ.classes;


import Civ.classes.Coords;

public class ScreenCoords {

    public Coords mapSize = new Coords(); // size of map in tiles;
    public Coords screenSizePx = new Coords(); // size of screen frame in pixels;
    public Coords screenSizeInTiles = new Coords(); // size of screen frame in tiles;
    public Coords selectedTile = new Coords(); // selected tile on map;
    public Coords screenMapOffset = new Coords(); // offset of the map for screen;
    public Coords screenCursor = new Coords(); // cursor on a screen; usually in center but can vary;

    int tileSize;

    boolean flatMap = false;


    public void goTo(int x, int y) {
        selectedTile.x = x;
        selectedTile.y = y;
        recalculate();
    }

    public void setScreenSize(int x, int y) {
        screenSizePx.x = x;
        screenSizePx.y = y;
    }

    public ScreenCoords(int x, int y, int tileSize, boolean flatMap) {
        this.tileSize = tileSize;
        this.mapSize.x = x;
        this.mapSize.y = y;
        this.flatMap = flatMap;
    }

    // on resize
    public void recalculate() {

        // setting screen tile size dimesions
        screenSizeInTiles.x = (int)Math.ceil(screenSizePx.x / tileSize);
        screenSizeInTiles.y = (int)Math.ceil(screenSizePx.y / tileSize);

        // setting cursor to center;
        screenCursor.x = (int)Math.ceil(screenSizeInTiles.x / 2);
        screenCursor.y = (int)Math.ceil(screenSizeInTiles.y / 2);

        // setting map offset
        screenMapOffset.x = selectedTile.x - screenCursor.x;
        screenMapOffset.y = selectedTile.y - screenCursor.y;

        // fixing offset.y edge-cases
        // NOT NECESSARY
        if(screenMapOffset.y < 0) {
            // example. screen size 30x20. center (cursor) 15x10; selectedTile.y = 2; offset.y = 2 - 10 = -8; screenCursor.y = 10 + (-8) = 2;
            screenCursor.y = screenCursor.y + screenMapOffset.y;
            screenMapOffset.y = 0;
        }

        // if offset bigger than difference between mapSizeY and screen size-y in tiles => fix
        if(screenMapOffset.y > (mapSize.y - screenSizeInTiles.y)) {
            // example. screen size = 30x20.  center (cursor) 15x10; map size 100x50; selectedTile.y = 47;  screenCursor.y = 20 - (50 - 47) = 20 - 3 = 17; screenMapOffset.y = 50 - 20 = 30;
            screenCursor.y = screenSizeInTiles.y - (mapSize.y - selectedTile.y);
            screenMapOffset.y = (mapSize.y - screenSizeInTiles.y);
        }


        // offset.x needs to be fixed for flat world
        if(flatMap) {
            if(screenMapOffset.x < 0) {
                screenCursor.x = screenCursor.x + screenMapOffset.x;
                screenMapOffset.x = 0;
            }

            // if offset bigger than difference between mapSizeY and screen size-y in tiles => fix
            if(screenMapOffset.x < (mapSize.x - screenSizeInTiles.x)) {
                screenCursor.x = screenSizeInTiles.x - (mapSize.x - selectedTile.x);
                screenMapOffset.x = (mapSize.x - screenSizeInTiles.x);
            }
        }

        debug();
    }

    // on click
    public void click(int x, int y) {
        // get event click;
        Coords clickScreenCursor = new Coords();
        clickScreenCursor.x = (int)Math.ceil(x / tileSize);
        clickScreenCursor.y = (int)Math.ceil(y / tileSize);

        if(clickScreenCursor.x == screenCursor.x && clickScreenCursor.y == screenCursor.y) return;

        // define selected tile on map;
        selectedTile.x += clickScreenCursor.x - screenCursor.x;
        selectedTile.y += clickScreenCursor.y - screenCursor.y;

        // fixing edge-cases
        if(selectedTile.y < 0) selectedTile.y = 0;
        if(selectedTile.y > mapSize.y) selectedTile.y = mapSize.y;
        if(selectedTile.x < 0) selectedTile.x = (flatMap ? 0 :  mapSize.x - selectedTile.x);
        if(selectedTile.x > mapSize.x) selectedTile.x = (flatMap ? mapSize.x : selectedTile.x - mapSize.x);
        recalculate();
    }

    public void debug() {
        mapSize.debug("Map size in tiles: ");
        screenSizePx.debug("Screen size in pixels: ");
        screenSizeInTiles.debug("Screen size in tiles: ");
        selectedTile.debug("Selected tile on map: ");
        screenMapOffset.debug("Screen map offset in tiles: ");
        screenCursor.debug("Screen cursor coords in tiles: ");
    }
}
