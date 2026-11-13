package com.ykwshowdown.VisualSwing;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.ykwshowdown.yokai.Yokai;
import com.ykwshowdown.yokai.face_icon;

public final class MedalSprites {

    private static final String HEADS_DIR = "/asset/faceIcon/face_icon_yokai/";
    public static final int HEAD = 64;
    private static final double HEAD_FILL = 0.94;

    private static BufferedImage SHEET;
    private static final Map<String, String> HEAD_FILES = new HashMap<>();
    private static final Map<String, BufferedImage> HEAD_CACHE = new HashMap<>();

    static {
        try {
            URL sheetUrl = MedalSprites.class.getResource("/asset/faceIcon/face_icon.png");
            SHEET = sheetUrl == null ? null : ImageIO.read(sheetUrl);
        } catch (IOException e) {
            SHEET = null;
        }
        InputStream index = MedalSprites.class.getResourceAsStream(HEADS_DIR + "index.csv");
        if (index == null) {
            System.out.println("Tetes de Yo-kai introuvables : " + HEADS_DIR);
        } else {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(index, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(";");
                    if (parts.length == 2 && !parts[0].equals("index")) {
                        HEAD_FILES.put(parts[1].trim(), parts[0].trim() + ".png");
                    }
                }
            } catch (IOException e) {
                System.out.println("Tetes de Yo-kai illisibles : " + HEADS_DIR);
            }
        }
    }

    private MedalSprites() {
    }

    public static boolean isLoaded() {
        return SHEET != null;
    }

    public static face_icon faceOf(Yokai yokai) {
        return yokai == null ? face_icon.UNKNOWN : yokai.getFaceIcon();
    }

    public static BufferedImage headOf(Yokai yokai) {
        if (yokai == null || yokai.getModelId() == null) {
            return null;
        }
        String file = HEAD_FILES.get(yokai.getModelId());
        if (file == null) {
            return null;
        }
        if (!HEAD_CACHE.containsKey(file)) {
            BufferedImage img = null;
            try {
                URL url = MedalSprites.class.getResource(HEADS_DIR + file);
                if (url != null) {
                    img = ImageIO.read(url);
                }
            } catch (IOException e) {
            }
            HEAD_CACHE.put(file, img);
        }
        return HEAD_CACHE.get(file);
    }

    public static boolean drawYokai(Graphics2D g2, Yokai yokai, int x, int y, int size) {
        BufferedImage head = headOf(yokai);
        if (head != null) {
            int s = (int) Math.round(size * HEAD_FILL);
            int hx = x + (size - s) / 2;
            int hy = y + (size - s) / 2;
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, s % HEAD == 0
                    ? RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR
                    : RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2.drawImage(head, hx, hy, s, s, null);
            return true;
        }
        face_icon face = faceOf(yokai);
        if (SHEET != null && face != null) {
            draw(g2, face, x, y, size);
            return true;
        }
        return false;
    }

    public static void draw(Graphics2D g2, face_icon face, int x, int y, int size) {
        if (SHEET == null || face == null) {
            return;
        }
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        int sx = face.getPixelX();
        int sy = face.getPixelY();
        g2.drawImage(SHEET, x, y, x + size, y + size, sx, sy, sx + face_icon.CELL, sy + face_icon.CELL, null);
    }

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
