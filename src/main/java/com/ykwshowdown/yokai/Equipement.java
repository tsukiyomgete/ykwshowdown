package com.ykwshowdown.yokai;

public class Equipement{
     int IdEquipement;
     String nomEquipement;
     String rankEquipment;
     int BaseHP;
     int BaseSTR;
     int BaseSPR;
     int BaseDEF;
     int BaseSPE;
     String effectEquipment;


    public Equipement(int IdEquipement, String rank,int BaseHP, int BaseSTR, int BaseSPR, int BaseDEF, int BaseSPE, String effectEquipment) {
        this.IdEquipement = IdEquipement;
        this.rankEquipment = rank;
        this.BaseHP = BaseHP;
        this.BaseSTR = BaseSTR;
        this.BaseSPR = BaseSPR;
        this.BaseDEF = BaseDEF;
        this.BaseSPE = BaseSPE;
        this.effectEquipment = effectEquipment;
    }

    public Equipement(int IdEquipement, String nom, String rank, int BaseHP, int BaseSTR, int BaseSPR, int BaseDEF, int BaseSPE, String effectEquipment) {
        this(IdEquipement, rank, BaseHP, BaseSTR, BaseSPR, BaseDEF, BaseSPE, effectEquipment);
        this.nomEquipement = nom;
    }

    public int GetId() {
        return IdEquipement;
    }

    public String GetName() {
        return nomEquipement;
    }

    public String GetRank() {
        return rankEquipment;
    }

    public String GetEffect() {
        return effectEquipment;
    }

    public int GetHP()
    {
        return BaseHP;
    }

    public int GetSTR()
    {
        return BaseSTR;
    }

    public int GetSPR()
    {
        return BaseSPR;
    }

    public int GetDEF()
    {
        return BaseDEF;
    }

    public int GetSPE()
    {
        return BaseSPE;
    }

    
}