package com.ykwshowdown.battle;

import com.ykwshowdown.yokai.*;
import com.ykwshowdown.fightingdata.*;
import com.ykwshowdown.Talent.Talent;

public class YokaiFight {
    private String _nomYokai;
    private String _surnomYokai;

    private double[] _resistances;
    private int _attackId;
    private int _techniqueId;
    private Talent _talent;

    private int _hpT;
    private int _strT;
    private int _sprT;
    private int _defT;
    private int _speT;

    private int _hpC;
    private int _strC;
    private int _sprC;
    private int _defC;
    private int _speC;

    private float _hpTribeBonus;
    private float _strTribeBonus;

    private float _tribeBonus;
    private float _adjacentBoost;
    private float _inspiritBoost;

    // TODO : champs a ajouter pour le combat : attente (file des tours), jaugeAme, envoutement + toursRestants,
    private int _team;

    public YokaiFight(String nomYokai, String surnomYokai, int pv, int atk, int spr, int def, int spe, int team) {
        _nomYokai = nomYokai;
        _surnomYokai = surnomYokai;
        _hpC = pv;
        _strC = atk;
        _sprC = spr;
        _defC = def;
        _speC = spe;
        _team = team;
    }

    public YokaiFight(String nomYokai, int pv, int atk, int spr, int def, int spe, int team) {
        _nomYokai = nomYokai;
        _hpC = pv;
        _strC = atk;
        _sprC = spr;
        _defC = def;
        _speC = spe;
        _team = team;
    }

    public YokaiFight(YokaiGeneral yg, int team) {
        _nomYokai = yg.GetName();
        _hpC = yg.GetHP();
        _strC = yg.GetSTR();
        _sprC = yg.GetSPR();
        _defC = yg.GetDEF();
        _speC = yg.GetSPE();

        _resistances = yg.GetResistances();
        _attackId = yg.GetAttackId();
        _techniqueId = yg.GetTechniqueId();

        _team = team;
    }

    public void setYokaiFight(YokaiGeneral yg) {
        _nomYokai = yg.GetName();
        _hpC = yg.GetHP();
        _strC = yg.GetSTR();
        _sprC = yg.GetSPR();
        _defC = yg.GetDEF();
        _speC = yg.GetSPE();
    }

    public boolean isAlive() {
        return (this.getHPC() > 0);
    }

    public void attack(YokaiFight target) {
        // TODO : toucher ou rater (algo 3), critique (algo 3), puis degats avec calcul.calculateDamage (algo 4)
        // TODO : appliquer les degats a target._hpC (et pas _hpT), PV minimum 0
        // TODO : faire gagner 5 points de jauge d'ame a tous les Yo-kai sur le terrain a la fin de l'action (algo 6)
    }

    public Talent getTalent() {
        return _talent;
    }

    public void setTalent(Talent talent) {
        this._talent = talent;
    }

    public double[] getResistances() {
        return _resistances;
    }

    public int getAttackId() {
        return _attackId;
    }

    public int getTechniqueId() {
        return _techniqueId;
    }

    public int getHPC() {
        return _hpC;
    }

    public int getSPRC() {
        return _sprC;
    }

    public int getStrC() {
        return _strC;
    }

    public int getSPEC() {
        return _speC;
    }

    public String getName() {
        return _nomYokai;
    }

    public int getTeam() {
        return _team;
    }

}
