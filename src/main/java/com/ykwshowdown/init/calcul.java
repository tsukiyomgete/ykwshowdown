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
		return yg.calculateStats(false);
	}

	public static Stat calculStatsCorrected(YokaiGeneral yg) {
		return yg.calculateStats(true);
	}

}
