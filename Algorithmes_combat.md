# Algorithmes de combat — Yo-kai Watch 2 (pour YkwShowdown)

Tout vient de la décompilation du jeu (code.bin) et des données extraites, vérifié avec le tableur Yo-gon quand c'était possible.
Les adresses `0x......` renvoient aux fonctions du jeu (détails dans le doc « YKW2 Psychic Specters : première analyse du combat »).

Données :
- `YKW2/tables/yokai_ykw2.csv` : 542 Yo-kai (stats, résistances, tribu, attaque, technique, Ultimate, probabilités d'action).
- `table_attente.csv` : la table vitesse → temps d'attente (algo 5).

Légende : **[sûr]** lu dans le code · **[probable]** calcul sûr, sens déduit · **[provisoire]** valeur choisie en attendant.

---

## 1. Stats d'un Yo-kai [sûr] — `0x40F7EC`

Ordre des stats partout : PV, Force, Esprit, Défense, Vitesse.

```
// le jeu calcule en float (pas double) et tronque : garder cet ordre d'opérations
POUR chaque stat i :
    s = min[i]                                         // colonne *_min du CSV (niveau 1)
    s = s + (max[i] + IV[i] - min[i]) * (1/98) * (niveau - 1)   // *_max = niveau 99
    s = s + EV[i] * (1 + niveau / 198)                 // EV = points d'attitude
    s = s + gym[i]                                     // gymnase
    stat[i] = tronque(s), borné entre 1 et 999
statFinale[i] = stat[i] + equipement[i], minimum 1
```

En Java, utiliser `float` et des constantes `float` : `0.010204081f` (1/98) et `0.0050505050f` (1/198).

**IV** : 40 tirages, chaque tirage choisit une des 5 stats au hasard : +2 si PV, +1 sinon.
Règle : `PV/2 + For + Esp + Def + Vit = 40` (pas de limite par stat). On stocke la vraie valeur de PV (pas PV/2).

**EV (attitude)** : 20 points au total, PV comptés pour moitié. Chaque attitude a sa répartition (onglet « EV Stats » du tableur, valeurs déjà multipliées par 1.3 = niveau 60 : Rough = 20 en Force, Gentle = 20 PV + 10 Esprit...).

**Gymnase** (ta classe `SportsClub` est juste) : Force +5 / Déf −2, Esprit +5 / Vit −2, Déf +5 / Vit −2, Vit +5 / Déf −2.

---

## 2. Puissance d'une commande [sûr]

```
puissance(niveauTechnique) = tronque(puissanceBase * pourcentage[courbe][niveauTechnique] / 100)
```

Le CSV donne directement la puissance au niveau 1 et au niveau 10 (`att_puiss_niv1/10`, `tech_puiss_niv1/10`, `ulti_puiss_niv1/10`) et le nombre de coups `n` (`*_coups`).
Pour un simulateur compétitif, prendre le niveau 10.

---

## 3. Toucher, critique, garde [sûr]

### 3a. Toucher — `0x56DBBC`

```
precision = tronque((VitAttaquant - VitCible) / 5 + 98)
precision = borne(precision, 75, 98)
mult = 1.0                                     // attaques : × paramètre "précision" de la commande (0.5 à 4)
mult = mult - bonusEsquiveCible/100 + bonusPrecisionAttaquant/100   (talents, plus tard)
chance = tronque(precision * max(mult, 0))
TOUCHÉ SI aleaEntier(0, 99) * 100 < (100 - esquiveFixe) * chance     // esquiveFixe = 0 pour l'instant
```

Avec des vitesses égales : 98 % de chances de toucher.

### 3b. Critique — `0x570500`

Seulement pour les attaques et les techniques.

```
c = bonusCritiqueCommande            // 0 en général
c = c + talents (plus tard)
CRITIQUE SI aleaEntier(0, 99 * 64) < 64 * (c + 1)       // ≈ (c + 1) %, donc ~1 % de base
```

### 3c. Garde

- Le Yo-kai a choisi « garde » (algo 7) → il garde jusqu'à sa prochaine action.
- Dégâts reçus × 0.5 pendant la garde (arrondi au-dessus).

---

## 4. Dégâts — `0x56E330` [sûr]

`P` = puissance par coup (algo 2), `n` = nombre de coups, `Def` = Défense effective de la cible (algo 9 pour les bonus/malus).

```
SI commande = ATTAQUE (Force) :
    SI critique : base = (For + P*n) * 2/3 - Def * 0.1
    SINON       : base = (For + P*n) * 0.5 - Def * 0.25

SI commande = TECHNIQUE (Esprit) :
    SI critique : base = (Esp + P*n) * 0.6       - Def / 14
    SINON       : base = (Esp + P*n) * (1/3 + 0.1) - Def / 7
    base = base * resistance(cible, element)     // 1.0 si pas d'élément ou Aspiration

SI n > 0 : base = base / n                       // dégâts PAR COUP, la commande frappe n fois

SI la cible garde : base = arrondiSup(base * 0.5)

r = 1 + 0.1 * base
degats = tronque(base + aleaReel(-r, +r))        // ± 10 % (+1)
degats = max(degats, 1)
degats = min(degats, 999)
```

Une **Ultimate** utilise la formule « attaque » ou « technique » selon la colonne `ulti_formule` du CSV.

**Résistances** : 6 valeurs par Yo-kai (CSV : `res_feu, res_glace, res_terre, res_foudre, res_eau, res_vent`). Exemple Jibanyan : feu 0.7, eau 1.3.

**Exemple** (Jibanyan niv. 99 contre Jibanyan, techniques niveau 10) :
- Griffes acérées (P = 45, n = 2) : (153 + 90) × 0.5 − 108 × 0.25 = 94.5 → 47.25 par coup → 41 à 52 par coup.
- Feu (P = 90, n = 1) : (90 + 90) × 0.4333 − 108/7 = 62.6, × 0.7 = 43.8 → 39 à 49.

---

## 5. Ordre des tours — file d'attente [sûr]

Table `table_attente.csv` : `palier_vitesse ; debut_min ; debut_max ; attente`.
`ligne(V)` = **la première ligne dont palier_vitesse ≥ V** (V = Vitesse effective).

```
DÉBUT DU COMBAT :
    POUR chaque Yo-kai de devant (des deux équipes) :
        L = ligne(Vitesse)
        attente = aleaEntier(L.debut_min, L.debut_max)

BOUCLE :
    acteur = Yo-kai de devant avec la PLUS PETITE attente     // PriorityQueue
    t = acteur.attente
    POUR chaque autre Yo-kai de devant : attente = max(0, attente - t)
    acteur agit (algo 7)
    acteur.attente = ligne(Vitesse).attente * 3

ROTATION DE LA ROUE :
    POUR chaque Yo-kai qui arrive devant : attente = ligne(Vitesse).attente * 3
    recalculer le bonus de tribu (algo 8)
```

Exemple : Vitesse 165 → ligne `[167, 101, 130, 68]` → attente après une action = 68 × 3 = **204**.

---

## 6. Jauge d'âme et Ultimate [sûr]

```
jauge entre 0 et coutUltimate                // CSV : ulti_cout_niv1 / ulti_cout_niv10 (100, 150, 200 ou 250 au niv. 1)
pourcentage affiché = jauge / coutUltimate * 100

À LA FIN DE CHAQUE ACTION (attaque, technique, inspirit, garde, Ultimate) :
    POUR chaque Yo-kai sur le terrain (les deux équipes) :
        jauge = min(jauge + 5, coutUltimate)

ULTIMATE (quand le joueur la lance et que la jauge est pleine) :
    jauge = 0
    dégâts avec l'algo 4, formule = ulti_formule, P = ulti_puiss_niv10, n = ulti_coups
```

---

## 7. IA : que fait un Yo-kai à son tour [sûr]

### 7a. Action normale ou non — `0x667224`

```
reste = 100
pour chaque inspirit négatif qui bloque l'action (niveau 0..5) : p = table du jeu, retirer de reste
flemme = tauxDeFlemme du Yo-kai (inconnu pour l'instant → 0) ; retirer de reste
actionNormale = reste
tirage pondéré entre ces cas
```

Pour commencer : toujours « action normale », sauf envoûtement qui fait flemmer (ta classe `Inspirit` a `isDisableActing`).

### 7b. Quelle action — tirage pondéré

```
r = aleaEntier(0, 99)
SI r < prob_attaque                                  → attaque
SINON SI r < prob_attaque + prob_technique           → technique
SINON SI r < ... + prob_inspirit                     → inspirit
SINON                                                → garde
```

(CSV : `prob_attaque, prob_technique, prob_inspirit, prob_garde`, en %, total 100. Jibanyan : 55 / 30 / 10 / 5.)

### 7c. Quelle cible

```
candidats = Yo-kai VIVANTS de la ligne de devant adverse (alliée pour un soin / inspirit positif)
cible = candidats[aleaEntier(0, taille - 1)]          // même chance pour chacun
```

---

## 8. Bonus de tribu — `0x65A220` [sûr]

```
milieu = Yo-kai du milieu de la ligne de devant
compte = 1
SI voisin gauche de la même tribu que milieu : compte++
SI voisin droit  de la même tribu que milieu : compte++
SI compte >= 2 : bonus = table[tribu][compte]   pour toute l'équipe
```

| Tribu | 2 | 3 | Effet |
|---|---|---|---|
| Vaillants | 15 % | 25 % | Force |
| Mystérieux | 15 % | 25 % | Esprit |
| Costauds | 40 % | 60 % | Défense |
| Mignons | 10 % | 15 % | Vitesse |
| Bienveillants | 15 % | 25 % | soins reçus |
| Sombres | +30 | +60 | chance de réussir ses inspirits (points) |
| Sinistres | +1 | +2 | durée de ses inspirits (tours) |
| Insaisissables | −20 | −40 | chance de se faire inspiriter (points) |

Stats : `stat = stat + tronque(stat * x / 100)` (au moins +1), après les bonus/malus de l'algo 9.

---

## 9. Bonus / malus de stats (inspirits) [sûr pour le calcul, provisoire pour les durées]

Chaque stat (For, Esp, Déf, Vit) a un niveau de −8 à +8.

```
T = { -8:10, -7:15, -6:20, -5:30, -4:40, -3:55, -2:70, -1:85, 0:100,
      +1:110, +2:120, +3:130, +4:140, +5:150, +6:160, +7:180, +8:200 }
delta = tronque(stat * (T[niveau] - 100) / 100)
SI delta == 0 et niveau > 0 : delta = 1
SI delta == 0 et niveau < 0 : delta = -1
statEffective = max(stat + delta, 1)
```

**Durée** [provisoire] : le jeu met **6 tours par défaut** pour au moins une famille d'effets (+1/+2 avec le bonus Sinistres). Isoler la valeur dans une seule méthode `dureeEnvoutement()`.

```
à la fin de chaque action DU YO-KAI ENVOÛTÉ :
    toursRestants = toursRestants - 1
    SI toursRestants == 0 : retirer l'envoûtement
```

**Réussite d'un inspirit** [probable] : `aleaEntier(0, 99) < chance` ; `chance` dépend de la cible et du bonus de tribu (détails dans le doc), à fixer à 100 % pour commencer.

---

## Ordre conseillé pour coder

1. Import du CSV → `Yokai` (6 résistances, 4 probabilités, attaque/technique/Ultimate).
2. Algo 1 (stats) dans `calcul.calculStatsCorrected`.
3. Algos 3 et 4 dans `YokaiFight.attack` et `calcul.calculateDamage`.
4. Algo 5 (file d'attente) dans `Battle`.
5. Algos 7, 6, 8, 9.
