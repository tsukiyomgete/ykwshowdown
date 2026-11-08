package com.ykwshowdown.init;

import java.util.Random;
import java.util.Scanner;
import com.ykwshowdown.yokai.*;
import com.ykwshowdown.battle.*;
import com.ykwshowdown.fightingdata.*;

public class calcul {

	static Random r = new Random();

	public static int randomGenerate(int max) {
		return r.nextInt(max);
	}

	public static int min(int valeurMax, int valeur) {
		if (valeur < valeurMax) {
			return valeur;
		} else {
			return valeurMax;
		}
	}

	public static int calculateDamage(YokaiFight sender, YokaiFight target) {
		// TODO : refaire avec la vraie formule de YKW2 (algo 4 "Degats" dans Algorithmes_combat.md)
		// TODO : choisir la formule selon le TYPE de commande (attaque = Force, technique = Esprit),
		//        pas selon la plus grande stat entre Force et Esprit
		// TODO : gerer le nombre de coups n, le critique, la garde, la resistance de la cible, l'alea
		// TODO : borner entre 1 et 999
		return 0;
	}


	public void calculDegat() {
		// TODO : ancienne version console de la formule, a supprimer quand calculateDamage sera fait
	}

	public static double WeakResNeutralDamage(YokaiFight sender, YokaiFight target) {
		// TODO : un Yo-kai a 6 resistances (feu, glace, terre, foudre, eau, vent), pas 1 faiblesse + 1 resistance
		// TODO : renvoyer la resistance de la cible pour l'element de la technique (1.0 si pas d'element ou Aspiration)
		return 1;
	}

	public static Stat calculStatsUncorrected(YokaiGeneral yg) {
		// TODO : formule des stats sans attitude/gym/equipement (algo 1 "Stats")
		// TODO : attention, le jeu calcule en float (pas en double) puis tronque
		return new Stat(0, 0, 0, 0, 0);
	}

	public static Stat calculStatsCorrected(YokaiGeneral yg) {
		// TODO : formule complete des stats (algo 1 "Stats") : min + (max + IV - min) / 98 * (niveau - 1)
		//        + EV d'attitude * (1 + niveau / 198) + gymnase, puis tronquer, borner 1..999, puis + equipement
		// TODO : bugs de l'ancienne version a ne pas refaire : nbSTR lisait nbGetSPR(), et Level / 198 etait
		//        une division entiere (toujours 0)
		return new Stat(0, 0, 0, 0, 0);
	}

}
