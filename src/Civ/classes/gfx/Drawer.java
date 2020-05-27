package Civ.classes.gfx;


public class Drawer {

    enum VerticalAlign {
        TOP,
        CENTER,
        BOTTOM
    };

    enum HorizontalAlign {
        LEFT,
        RIGHT,
        CENTER,
    };

    private float scale = 1;
    private int containerX = 0;
    private int containerY = 0;
    private int w = 0;
    private int h = 0;
    public HorizontalAlign alignX = HorizontalAlign.LEFT;
    public VerticalAlign alignY = VerticalAlign.TOP;

    public Drawer setScale(int containerX, int objX) {
        this.scale = (float)(1.0 * containerX / objX);
        return this;
    }

    public Drawer setScale(float scale) {
        this.scale = scale;
        return this;
    }

    public HorizontalAlign getAlignX() {
        return alignX;
    }

    public Drawer setAlignX(HorizontalAlign posX) {
        this.alignX = posX;
        return this;
    }

    public VerticalAlign getAlignY() {
        return alignY;
    }

    public Drawer setAlignY(VerticalAlign posY) {
        this.alignY = posY;
        return this;
    }

    public int getContainerX() {
        return containerX;
    }

    public Drawer setContainerX(int containerX) {
        this.containerX = containerX;
        return this;
    }

    public int setContainerY() {
        return containerY;
    }

    public Drawer setContainerY(int scaleY) {
        this.containerY = scaleY;
        return this;
    }

    public Drawer setContainer(int scaleX, int scaleY) {
        this.containerX = scaleX;
        this.containerY = scaleY;
        return this;
    }

    public int getW() {
        return w;
    }

    public Drawer setW(int w) {
        this.w = w;
        return this;
    }

    public int getH() {
        return h;
    }

    public Drawer setH(int h) {
        this.h = h;
        return this;
    }

    public Drawer setSize(int w, int h) {
        this.w = w;
        this.h = h;
        return this;
    }


    public ImgDimensions dim(int dx, int dy) {
        return dim(dx, dy, 0, 0);
    }


    public Drawer left() {
        this.alignX = HorizontalAlign.LEFT;
        return this;
    }

    public Drawer right() {
        this.alignX = HorizontalAlign.RIGHT;
        return this;
    }

    public Drawer center() {
        this.alignX = HorizontalAlign.CENTER;
        this.alignY = VerticalAlign.CENTER;
        return this;
    }

    public Drawer centerX() {
        this.alignX = HorizontalAlign.CENTER;
        return this;
    }

    public Drawer centerY() {
        this.alignY = VerticalAlign.CENTER;
        return this;
    }

    public Drawer top() {
        this.alignY = VerticalAlign.TOP;
        return this;
    }

    public Drawer bottom() {
        this.alignY = VerticalAlign.BOTTOM;
        return this;
    }



    public ImgDimensions dim(int dx, int dy, int offX, int offY ) {
        if(scale != 1) {
            dx = (int)(dx * scale);
            dy = (int)(dy * scale);
            offX = (int)(offX * scale);
            offY = (int)(offY * scale);
        }

        if(dx != containerX) {
            switch(alignX) {
                case RIGHT: offX += containerX - dx; break;
                case CENTER: offX += (containerX - dx) /2; break;
            }
        }

        if(dy != containerY) {
            switch(alignY) {
                case BOTTOM: offY += containerY - dy; break;
                case CENTER: offY += (containerY - dy) /2; break;
            }
        }

        return new ImgDimensions(dx, dy, offX, offY);
    }



}
