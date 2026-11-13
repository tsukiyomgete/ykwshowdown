package com.ykwshowdown.yokai;

import java.util.Random;
import java.util.Scanner;

public class IV {

    public static final int MAXIVPOINTS = 40;
    static final int MINIV = 0;

    public static final int MIN_IV_POINTS = 3;
    public static final int MAX_IV_POINTS_PER_STAT = 16;

    private static final Random RANDOM = new Random();

    int HP;
    int STR;
    int SPR;
    int DEF;
    int SPE;
    Scanner sc = new Scanner(System.in);

    public IV() {
        HP = 0;
        STR = 0;
        SPR = 0;
        DEF = 0;
        SPE = 0;
    }

    public IV(int HP, int STR, int SPR, int DEF, int SPE) {
        setIV(HP, STR, SPR, DEF, SPE);
    }

    public static int pointsUsed(int hp, int str, int spr, int def, int spe) {
        return hp / 2 + str + spr + def + spe;
    }

    public int getPointsUsed() {
        return pointsUsed(HP, STR, SPR, DEF, SPE);
    }

    public int getPointsLeft() {
        return MAXIVPOINTS - getPointsUsed();
    }

    public static String checkIV(int hp, int str, int spr, int def, int spe) {
        if (hp < MINIV || str < MINIV || spr < MINIV || def < MINIV || spe < MINIV) {
            return "Les IV ne peuvent pas etre negatifs";
        }
        int[] points = {hp / 2, str, spr, def, spe};
        for (int p : points) {
            if (p < MIN_IV_POINTS || p > MAX_IV_POINTS_PER_STAT) {
                return "Chaque stat doit avoir entre 3 et 16 points (6 à 32 pour HP)";
            }
        }
        if (hp % 2 != 0) {
            return "Les IV de HP vont de 2 en 2 (valeur paire)";
        }
        if (pointsUsed(hp, str, spr, def, spe) > MAXIVPOINTS) {
            return "Trop de points : HP/2 + STR + SPR + DEF + SPE doit faire " + MAXIVPOINTS + " au maximum";
        }
        return null;
    }

    public void setIV(int hp, int str, int spr, int def, int spe) {
        String error = checkIV(hp, str, spr, def, spe);
        if (error != null) {
            throw new IllegalArgumentException(error);
        }
        this.HP = hp;
        this.STR = str;
        this.SPR = spr;
        this.DEF = def;
        this.SPE = spe;
    }

    public void GenerateIV() {
        int[] points = new int[5];
        for (int i = 0; i < 5; i++) {
            points[i] = MIN_IV_POINTS;
        }
        for (int i = 5 * MIN_IV_POINTS; i < MAXIVPOINTS; i++) {
            int stat;
            do {
                stat = RANDOM.nextInt(5);
            } while (points[stat] >= MAX_IV_POINTS_PER_STAT);
            points[stat]++;
        }
        setIV(points[0] * 2, points[1], points[2], points[3], points[4]);
    }

    public void setIV() {
        int hp, str, spr, def, spe;
        String error;
        do {
            System.out.println("IV a repartir : HP/2 + STR + SPR + DEF + SPE <= " + MAXIVPOINTS + " (HP pair)");
            System.out.println("HP :");
            hp = sc.nextInt();
            System.out.println("STR :");
            str = sc.nextInt();
            System.out.println("SPR :");
            spr = sc.nextInt();
            System.out.println("DEF :");
            def = sc.nextInt();
            System.out.println("SPE :");
            spe = sc.nextInt();
            error = checkIV(hp, str, spr, def, spe);
            if (error != null) {
                System.out.println("Valeur invalide : " + error);
            }
        } while (error != null);
        setIV(hp, str, spr, def, spe);
    }

    public void print() {
        System.out.println("Les IV de ton yokai");
        System.out.println("HP " + HP);
        System.out.println("STR " + STR);
        System.out.println("SPR " + SPR);
        System.out.println("DEF " + DEF);
        System.out.println("SPE " + SPE);
    }

    public int GetIvHP() {
        return HP;
    }

    public int GetIvSTR() {
        return STR;
    }

    public int GetIvSPR() {
        return SPR;
    }

    public int GetIvDEF() {
        return DEF;
    }

    public int GetIvSPE() {
        return SPE;
    }

}
