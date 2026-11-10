package com.ykwshowdown.VisualSwing;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.ykwshowdown.yokai.Yokai;
import com.ykwshowdown.yokai.face_icon;

/** TODO rename */
public final class MedalSprites {

    private static BufferedImage SHEET;

    static {
        try {
            SHEET = ImageIO.read(new File("src/asset/faceIcon/face_icon.png"));
        } catch (IOException e) {
            SHEET = null;   // TODO rename
        }
    }

    private MedalSprites() {
    }

    public static boolean isLoaded() {
        return SHEET != null;
    }

    /** TODO rename */
    public static face_icon faceOf(Yokai yokai) {
        return yokai == null ? face_icon.UNKNOWN : yokai.getFaceIcon();
    }

    /** TODO rename */
    public static void draw(Graphics2D g2, face_icon face, int x, int y, int size) {
        if (SHEET == null || face == null) {
            return;
        }
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        int sx = face.getPixelX();
        int sy = face.getPixelY();
        g2.drawImage(SHEET, x, y, x + size, y + size, sx, sy, sx + face_icon.CELL, sy + face_icon.CELL, null);
    }

    /** TODO rename */
    public static Icon icon(Yokai yokai, int size) {
        face_icon face = faceOf(yokai);
        if (SHEET == null || face == null) {
            return null;
        }
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        draw(g2, face, 0, 0, size);
        g2.dispose();
        return new ImageIcon(img);
    }
}
