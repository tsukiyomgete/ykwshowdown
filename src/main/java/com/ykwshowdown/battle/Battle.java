package com.ykwshowdown.battle;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import com.ykwshowdown.Talent.*;
import com.ykwshowdown.Talent.Talent;

public class Battle {

    YokaiTeam player1;
    YokaiTeam player2;
    boolean isMatchFinished;
    YokaiTeam orderBattle;

    public Battle(YokaiTeam player1, YokaiTeam player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public void DebutCombat() {
        Scanner sc = new Scanner(System.in);
        System.out.println("DEBUT DU COMBAT !");
        while (!isMatchFinished) {
            player1 = YokaiAlive(player1);
            player2 = YokaiAlive(player2);

            SetOrderingBattle();
            triInsertionOrderingBattle(orderBattle);
            attackTurn();
            showYoKaiStatus();
            System.out.println("Would you like to turn your wheel? no/yes");
            String str = sc.nextLine();
            if (str.equalsIgnoreCase("yes")) {
                System.out.println("Right or left? R/L");
                str = sc.nextLine();
                System.out.println("How many times do you want to turn the wheel?");
                int nbCrans = Integer.parseInt(sc.nextLine());

                if (str.equalsIgnoreCase("R")) {
                    tournerRoue(player1, true, nbCrans);
                }
                if (str.equalsIgnoreCase("L")) {
                    tournerRoue(player1, false, nbCrans);
                }
            }

            boolean player1Loose = playerLoose(player1);
            boolean player2Loose = playerLoose(player2);
            isMatchFinished = (player1Loose || player2Loose);

            if (player1Loose) {
                System.out.println("Pas de bol t'as perdu !");
            }
            if (player2Loose) {
                System.out.println("Bravo t'as gagné !");
            }

        }
    }

    public void SetOrderingBattle() {
        // TODO : remplacer par une file d'attente (algo 5 "Ordre des tours") :
    }

    public void triInsertionOrderingBattle(YokaiTeam yt) {
        // TODO : plus besoin de trier par vitesse a chaque tour : c'est la plus petite attente qui joue (PriorityQueue)
    }

    public void attackTurn() {
        // TODO : boucle du jeu (algo 5) : prendre le Yo-kai avec la plus petite attente, retirer son attente a tous les autres,
    }

    public YokaiFight getRandTarget(YokaiTeam enemyTeam) {
        Random rand = new Random();
        return enemyTeam.getYokaiFrontRowIndex(rand.nextInt(enemyTeam.getFrontRowSize()));
    }

    public YokaiTeam YokaiAlive(YokaiTeam yt) {
        ArrayList<YokaiFight> aliveYokai = new ArrayList<YokaiFight>();
        for (int i = 0; i < yt.getFrontRowSize(); i++) {
            if (yt.getYokaiFrontRowIndex(i).isAlive()) {
                aliveYokai.add(yt.getYokaiFrontRowIndex(i));
            }
        }
        if (aliveYokai.size() == 0) {
            for (int i = 0; i < yt.getBackRowSize(); i++) {
                aliveYokai.add(yt.getYokaiBackRowIndex(i));
            }
        }

        YokaiTeam aliveTeam = new YokaiTeam(aliveYokai.size(), yt.getTeamNumber());

        for (int j = 0; j < aliveYokai.size(); j++) {
            aliveTeam.setYokaiIndex(j, aliveYokai.get(j));
        }
        return aliveTeam;
    }

    public void showYoKaiStatus() {
        System.out.println("        ETAT DU COMBAT          ");
        System.out.println("");
        System.out.println("LA FRONT LANE ALLIE");
        for (int i = 0; i < player1.getFrontRowSize(); i++) {
            System.out.println("[ALLIEE] " + player1.getYokaiFrontRowIndex(i).getName() + " PV: "
                    + player1.getYokaiFrontRowIndex(i).getHPC());
        }
        System.out.println("");
        System.out.println("LA BACK LANE ALLIE");
        for (int i = 0; i < player1.getBackRowSize(); i++) {
            System.out.println("[ALLIEE] " + player1.getYokaiBackRowIndex(i).getName() + " PV: "
                    + player1.getYokaiBackRowIndex(i).getHPC());
        }

        System.out.println("");
        System.out.println("LA FRONT LANE ENEMI");
        for (int y = 0; y < player2.getFrontRowSize(); y++) {
            System.out.println("[ENEMIE] " + player2.getYokaiFrontRowIndex(y).getName() + " PV: "
                    + player2.getYokaiFrontRowIndex(y).getHPC());
        }

        try {
            Thread.sleep(1200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean playerLoose(YokaiTeam p) {
        for (int i = 0; i < p.getFrontRowSize(); i++) {
            if (p.getYokaiFrontRowIndex(i).isAlive())
                return false;
        }
        for (int i = 0; i < p.getBackRowSize(); i++) {
            if (p.getYokaiBackRowIndex(i).isAlive())
                return false;
        }
        return true;
    }

    // TODO : apres une rotation, les Yo-kai qui arrivent devant recoivent une attente pleine (attente(Vitesse) * 3)
    // TODO : recalculer le bonus de tribu apres chaque rotation (algo 8)
    public void tournerRoue(YokaiTeam yt, boolean droite, int nbCrans) {
        YokaiFight[] roue = new YokaiFight[6];
        for (int i = 0; i < yt.getFrontRowSize(); i++) {
            roue[i] = yt.getYokaiFrontRowIndex(i);
        }
        for (int i = yt.getFrontRowSize(); i < yt.getTeamSize(); i++) {
            roue[i] = yt.getYokaiBackRowIndex(i - yt.getFrontRowSize());
        }
        if (droite) {
            for (int j = 0; j < nbCrans; j++) {
                YokaiFight tempY = roue[roue.length - 1];
                for (int i = roue.length - 1; i > 0; i--) {
                    roue[i] = roue[i - 1];
                }
                roue[0] = tempY;
            }
        }
        if (!droite) {
            for (int j = 0; j < nbCrans; j++) {
                YokaiFight tempY = roue[0];
                for (int i = 0; i < roue.length - 1; i++) {
                    roue[i] = roue[i + 1];
                }
                roue[5] = tempY;
            }
        }

        for (int i = 0; i < yt.getFrontRowSize(); i++) {
            yt.setYokaiIndex(i, roue[i]);
        }
        for (int i = 0; i < yt.getBackRowSize(); i++) {
            yt.setYokaiIndex(i + yt.getFrontRowSize(), roue[i + yt.getFrontRowSize()]);
        }
    }

    public void checkTriggers(TriggerType trigger, YokaiFight holder, YokaiFight target) {
        if (holder.getTalent() != null && holder.getTalent().getTriggerType() == trigger) {
            holder.getTalent().activate(holder, target);
        }
    }

}
