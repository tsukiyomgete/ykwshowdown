package com.ykwshowdown.yokai;

public class Yokai {

    public static final int FIRE = 0;
    public static final int ICE = 1;
    public static final int EARTH = 2;
    public static final int LIGHTNING = 3;
    public static final int WATER = 4;
    public static final int WIND = 5;

    int id;
    int idMedaillum;
    String nom;
    char rangYokai;
    String tribuYokai;

    Stat statInit;
    Stat statEnd;

    double[] resistances;

    int attackId;
    int techniqueId;
    Integer inspiritId;
    Integer ultimateId;

    int _baseAttackPerC;
    int _baseTechniquePerC;
    int _baseInspiritPerC;
    int _baseGuardPerC;

    Tier yokaiTier;
    face_icon faceYoKai;
    boolean isLegendary;
    String talent;
    double chanceOfBeingInspirited;
    String modelId;

    public Yokai(int id, int idMedaillum,String nom, char rang, String tribu,
            Stat statMin, Stat statMax,
            double[] resistances,
            int attackId, int techniqueId, Integer inspiritId, Integer ultimateId,
            int probAttack, int probTechnique, int probInspirit, int probGuard, String modelId) {
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
        this.idMedaillum = idMedaillum;
        this.modelId = modelId;
    }

    public int GetID() {
        return id;
    }

    public face_icon getFaceIcon() {
        return faceYoKai;
    }

    public void setFaceIcon(face_icon faceYoKai) {
        this.faceYoKai = faceYoKai;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
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
