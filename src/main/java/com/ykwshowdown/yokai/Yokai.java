package com.ykwshowdown.yokai;

/**
 * Le yokai de base, son équivalent est le species dans la bdd
 *
 */
public class Yokai {


    public static final int FIRE = 0;
    public static final int ICE = 1;
    public static final int EARTH = 2;
    public static final int LIGHTNING = 3;
    public static final int WATER = 4;
    public static final int WIND = 5;

    int id;
    String nom;
    char rangYokai;
    String tribuYokai;

    Stat statInit;
    Stat statEnd;

    double[] resistances;   //Array des resistances

    int attackId;
    int techniqueId;
    Integer inspiritId;     // null possible
    Integer ultimateId;     // null possible


    int _baseAttackPerC;
    int _baseTechniquePerC;
    int _baseInspiritPerC;
    int _baseGuardPerC;

    // Hors base de donnees
    Tier yokaiTier;
    face_icon faceYoKai;
    boolean isLegendary;
    String talent;
    double chanceOfBeingInspirited;

    public Yokai(int id, String nom, char rang, String tribu,
            Stat statMin, Stat statMax,
            double[] resistances,
            int attackId, int techniqueId, Integer inspiritId, Integer ultimateId,
            int probAttack, int probTechnique, int probInspirit, int probGuard) {
        this.id = id;
        this.nom = nom;
        this.rangYokai = rang;
        this.tribuYokai = tribu;
        this.statInit = statMin;
        this.statEnd = statMax;
        this.resistances = resistances;
        this.attackId = attackId;
        this.techniqueId = techniqueId;
        this.inspiritId = inspiritId;
        this.ultimateId = ultimateId;
        this._baseAttackPerC = probAttack;
        this._baseTechniquePerC = probTechnique;
        this._baseInspiritPerC = probInspirit;
        this._baseGuardPerC = probGuard;
        this.yokaiTier = Tier.NU;
    }

    public int GetID() {
        return id;
    }

    public String GetName() {
        return nom;
    }

    public char GetRank() {
        return rangYokai;
    }

    public String GetTribe() {
        return tribuYokai;
    }

    public String GetTierString() {
        return yokaiTier.getTier();
    }

    public Tier GetTier() {
        return yokaiTier;
    }

    public Stat getStatA() {
        return statInit;
    }

    public Stat getStatB() {
        return statEnd;
    }

    public void setStatA(Stat sA) {
        statInit = sA;
    }

    public void setStatB(Stat sB) {
        statEnd = sB;
    }

    public double[] getResistances() {
        return resistances;
    }

    /** element = Yokai.FIRE, Yokai.ICE, ... */
    public double getResistance(int element) {
        return resistances[element];
    }

    public int getAttackId() {
        return attackId;
    }

    public int getTechniqueId() {
        return techniqueId;
    }

    public Integer getInspiritId() {
        return inspiritId;
    }

    public Integer getUltimateId() {
        return ultimateId;
    }

    public int getBaseAttackPerC() {
        return _baseAttackPerC;
    }

    public int getBaseTechniquePerC() {
        return _baseTechniquePerC;
    }

    public int getBaseInspiritPerC() {
        return _baseInspiritPerC;
    }

    public int getBaseGuardPerC() {
        return _baseGuardPerC;
    }

    public String getTalent() {
        return talent;
    }

    public double getChanceInspirited() {
        return chanceOfBeingInspirited;
    }
}
