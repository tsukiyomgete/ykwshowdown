package com.ykwshowdown.yokai;

public class face_icon {

    public static final int CELL = 44;
    public static final face_icon UNKNOWN = new face_icon(22, 10);

    int posX;
    int posY;

    public face_icon(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getPixelX() {
        return posX * CELL;
    }

    public int getPixelY() {
        return posY * CELL;
    }
}
