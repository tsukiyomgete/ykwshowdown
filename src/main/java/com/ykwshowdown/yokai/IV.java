package com.ykwshowdown.yokai;

import java.util.Random;
import java.util.Scanner;

/**
 * IV of a Yo-kai (algo 1 of Algorithmes_combat.md).
 * Rule of the game: HP/2 + STR + SPR + DEF + SPE <= 40, no limit per stat.
 * HP is stored with its real value (not HP/2), and it always goes up by 2, so it is even.
 */
public class IV {

    public static final int MAXIVPOINTS = 40;
    static final int MINIV = 0;
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

    /** Points used: HP counts for half. */
    public static int pointsUsed(int hp, int str, int spr, int def, int spe) {
        return hp / 2 + str + spr + def + spe;
    }

    public int getPointsUsed() {
        return pointsUsed(HP, STR, SPR, DEF, SPE);
    }

    public int getPointsLeft() {
        return MAXIVPOINTS - getPointsUsed();
    }

    /** Returns null if the IV are valid, otherwise the reason. */
    public static String checkIV(int hp, int str, int spr, int def, int spe) {
        if (hp < MINIV || str < MINIV || spr < MINIV || def < MINIV || spe < MINIV) {
            return "Les IV ne peuvent pas etre negatifs";
        }
        if (hp % 2 != 0) {
            return "Les IV de HP vont de 2 en 2 (valeur paire)";
        }
        if (pointsUsed(hp, str, spr, def, spe) > MAXIVPOINTS) {
            return "Trop de points : HP/2 + STR + SPR + DEF + SPE doit faire " + MAXIVPOINTS + " au maximum";
        }
        return null;
    }

    /** Sets the 5 IV, throws IllegalArgumentException if they break the rules. */
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

    /** Generation of the game: 40 draws, each draw picks one of the 5 stats, +2 for HP, +1 otherwise. */
    public void GenerateIV() {
        int[] iv = new int[5];
        for (int i = 0; i < MAXIVPOINTS; i++) {
            int stat = RANDOM.nextInt(5);
            if (stat == 0) {
                iv[0] += 2;
            } else {
                iv[stat] += 1;
            }
        }
        setIV(iv[0], iv[1], iv[2], iv[3], iv[4]);
    }

    /** Console version: asks the 5 values and checks the total. */
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
