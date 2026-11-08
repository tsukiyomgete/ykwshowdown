package com.ykwshowdown.yokai;

import com.ykwshowdown.init.*;
import java.util.Scanner;

public class IV {
    // TODO : regles du jeu : 40 points au total, les PV comptent pour moitie (HP/2 + STR + SPR + DEF + SPE <= 40),
    //        aucune limite de 15 par stat dans le jeu, et on stocke la vraie valeur (pas HP/2)
    final int MAXIVPOINTS = 40;
    final int MAXIV = 15;
    final int MINIV = 0;
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
        this.HP = HP;
        this.STR = STR;
        this.SPR = SPR;
        this.DEF = DEF;
        this.SPE = SPE;
    }

    public void GenerateIV() {
        // TODO : generation du jeu (algo 1) : 40 tirages, chaque tirage choisit une stat au hasard parmi 5,
        //        +2 si c'est les PV, +1 sinon
    }

    public void setIV() {
        int total = MAXIVPOINTS;
        int hpSaisi, strSaisi, sprSaisi, defSaisi, speSaisi;

        // hp
        do {
            System.out.println("IV disponibles : " + total);
            System.out.println("Choisi les IV de HP (0-16) :");
            hpSaisi = sc.nextInt();
            if (hpSaisi < MINIV || hpSaisi > 16 || hpSaisi > total)
                System.out.println("Valeur invalide !");
        } while (hpSaisi < MINIV || hpSaisi > 16 || hpSaisi > total);
        total -= hpSaisi/2;
        HP = hpSaisi/2;

        // Strength
        do {
            System.out.println("IV disponibles : " + total);
            System.out.println("Choisi les IV de STR (0-15) :");
            strSaisi = sc.nextInt();
            if (strSaisi < MINIV || strSaisi > MAXIV || strSaisi > total)
                System.out.println("Valeur invalide !");
        } while (strSaisi < MINIV || strSaisi > MAXIV || strSaisi > total);
        total -= strSaisi;
        STR = strSaisi;

        //Spirit
        do {
            System.out.println("IV disponibles : " + total);
            System.out.println("Choisi les IV de SPR (0-15) :");
            sprSaisi = sc.nextInt();
            if (sprSaisi < MINIV || sprSaisi > MAXIV || sprSaisi > total)
                System.out.println("Valeur invalide !");
        } while (sprSaisi < MINIV || sprSaisi > MAXIV || sprSaisi > total);
        total -= sprSaisi;
        SPR = sprSaisi;

        // Defense
        do {
            System.out.println("IV disponibles : " + total);
            System.out.println("Choisi les IV de Defense (0-15) :");
            defSaisi = sc.nextInt();
            if (defSaisi < MINIV || defSaisi > MAXIV || defSaisi > total)
                System.out.println("Valeur invalide !");
        } while (defSaisi < MINIV || defSaisi > MAXIV || defSaisi > total);
        total -= defSaisi;
        DEF = defSaisi;

        do {
            System.out.println("IV disponibles : " + total);
            System.out.println("Choisi les IV de Vitesse (0-15) :");
            speSaisi = sc.nextInt();
            if (speSaisi < MINIV || speSaisi > MAXIV || speSaisi > total)
                System.out.println("Valeur invalide !");
        } while (speSaisi < MINIV || speSaisi > MAXIV || speSaisi > total);
        total -= speSaisi;
        SPE = speSaisi;
    }

    public void setIV(int hp, int str, int spr, int def, int spe) {
        // TODO : verifier HP/2 + str + spr + def + spe <= 40 et aucune valeur negative, puis stocker les valeurs telles quelles
    }

    public void print() {
        System.out.println("Les IV de ton yokai");
        System.out.println("HP" + HP);
        System.out.println("STR" + STR);
        System.out.println("SPR" + SPR);
        System.out.println("DEF" + DEF);
        System.out.println("SPE" + SPE);
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