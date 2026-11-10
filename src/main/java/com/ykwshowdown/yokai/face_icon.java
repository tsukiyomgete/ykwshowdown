package com.ykwshowdown.yokai;

/**
 * Position of a Yo-kai's medal in face_icon.png (cells of 44 x 44 px).
 * posX = column, posY = row (columns 11 and 12 of CHARA_BASE_YOKAI_INFO in the game).
 */
public class face_icon {

    public static final int CELL = 44;
    // dark "unknown" medal, used for empty slots and Yo-kai without a medal
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

    /** Left edge of the medal in the image, in pixels. */
    public int getPixelX() {
        return posX * CELL;
    }

    /** Top edge of the medal in the image, in pixels. */
    public int getPixelY() {
        return posY * CELL;
    }
}
