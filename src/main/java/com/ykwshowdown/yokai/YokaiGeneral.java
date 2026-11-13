package com.ykwshowdown.yokai;

import java.util.Scanner;

import com.ykwshowdown.battle.YokaiFight;
import com.ykwshowdown.database.DataBase;
import com.google.gson.annotations.SerializedName;

public class YokaiGeneral {

    private static final float ONE_OVER_98 = 0.010204081f;
    private static final float ONE_OVER_198 = 0.0050505050f;
    public static final int DEFAULT_LEVEL = 60;

    Yokai yokai;
    @SerializedName("surnomYokai")
    String surnom;

    Stat StatActu;
    @SerializedName("IdEquipement")
    Equipement ItemYokai;
    @SerializedName("niveau")
    int level;

    Attitude attitudeYoKai;
    int LoafLevel;
    int TrainingCount;
    IV ivYokai;
    SportsClub SportsClub;

    public YokaiGeneral() {
        yokai = null;
        surnom = "???";
        StatActu = new Stat(0, 0, 0, 0, 0);
        ItemYokai = null;
        level = 0;
        attitudeYoKai = null;
        ivYokai = new IV();
        SportsClub = new SportsClub(0, 0, 0, 0);
    }

    public YokaiGeneral(Yokai yokai, String surnom, int level, IV ivYokai) {
        this();
        this.yokai = yokai;
        this.surnom = surnom;
        this.level = level;
        if (ivYokai != null) {
            this.ivYokai = ivYokai;
        }
        updateStats();
    }

    public YokaiGeneral(Yokai yokai, String surnom, int level, Attitude attitudeYoKai, Equipement EquipYokai) {
        this();
        this.yokai = yokai;
        this.surnom = surnom;
        this.level = level;
        this.attitudeYoKai = attitudeYoKai;
        this.ItemYokai = EquipYokai;
        updateStats();
    }

    public void addYokai(String str) {
        Yokai y = rechercheYokai(str);
        if (y != null) {
            this.yokai = y;
            setLevel(DEFAULT_LEVEL);
        } else {
            System.out.println("Yokai non trouvé!");
        }
    }

    public void replaceYokai(String str) {
        addYokai(str);
    }

    public Yokai rechercheYokai(String nomRecherche) {
        Yokai y = DataBase.getYokai(nomRecherche);
        if (y == null) {
            System.out.println("Yokai inconnu : " + nomRecherche);
        }
        return y;
    }

    public void setLevel(int level) {
        if (level < 1 || level > 99) {
            throw new IllegalArgumentException("Impossible, le niveau doit etre entre 1 et 99");
        }
        this.level = level;
        updateStats();
    }

    public void setLevel(String str) {
        setLevel(Integer.parseInt(str.trim()));
    }

    public void setIv(int ivHP, int ivSTR, int ivSPR, int ivDEF, int ivSPE) {
        ivYokai.setIV(ivHP, ivSTR, ivSPR, ivDEF, ivSPE);
        updateStats();
    }

    public void generateIV() {
        ivYokai.GenerateIV();
        updateStats();
    }

    public void setIV(String name) {
        System.out.println("On va choisir les IV de " + name);
        ivYokai.setIV();
        updateStats();
    }

    public boolean setAttitude(String name) {
        if (name == null || name.isBlank()) {
            attitudeYoKai = null;
            updateStats();
            return true;
        }
        DataBase.RawAttitude a = DataBase.getAttitude(name.trim());
        if (a == null) {
            return false;
        }
        attitudeYoKai = new Attitude(a.getName(), a.getEvHp(), a.getEvStr(), a.getEvSpr(), a.getEvDef(), a.getEvSpd());
        updateStats();
        return true;
    }

    public boolean setEquipment(String name) {
        if (name == null || name.isBlank()) {
            ItemYokai = null;
            updateStats();
            return true;
        }
        DataBase.RawEquipment e = DataBase.getEquipment(name.trim());
        if (e == null) {
            return false;
        }
        ItemYokai = new Equipement(e.getId(), e.getName(), e.getRank(),
                e.getBonusHp(), e.getBonusStr(), e.getBonusSpr(), e.getBonusDef(), e.getBonusSpd(), e.getEffect());
        updateStats();
        return true;
    }

    public void updateStats() {
        StatActu = calculateStats(true);
    }

    public Stat calculateStats(boolean withBonuses) {
        if (yokai == null || level < 1) {
            return new Stat(0, 0, 0, 0, 0);
        }
        Stat min = yokai.getStatA();
        Stat max = yokai.getStatB();

        int[] minS = {min.HP, min.STR, min.SPR, min.DEF, min.SPE};
        int[] maxS = {max.HP, max.STR, max.SPR, max.DEF, max.SPE};
        int[] iv = {ivYokai.HP, ivYokai.STR, ivYokai.SPR, ivYokai.DEF, ivYokai.SPE};
        int[] ev = new int[5];
        int[] gym = new int[5];
        int[] equip = new int[5];

        if (withBonuses) {
            if (attitudeYoKai != null) {
                ev = new int[]{attitudeYoKai.GetHP(), attitudeYoKai.GetSTR(), attitudeYoKai.GetSPR(),
                        attitudeYoKai.GetDEF(), attitudeYoKai.GetSPE()};
            }
            if (SportsClub != null) {
                gym = new int[]{0, SportsClub.nbGetSTR(), SportsClub.nbGetSPR(), SportsClub.nbGetDEF(), SportsClub.nbGetSPE()};
            }
            if (ItemYokai != null) {
                equip = new int[]{ItemYokai.GetHP(), ItemYokai.GetSTR(), ItemYokai.GetSPR(),
                        ItemYokai.GetDEF(), ItemYokai.GetSPE()};
            }
        }

        int[] result = new int[5];
        for (int i = 0; i < 5; i++) {
            float s = minS[i];
            s = s + (maxS[i] + iv[i] - minS[i]) * ONE_OVER_98 * (level - 1);
            s = s + ev[i] * (1 + level * ONE_OVER_198);
            s = s + gym[i];
            int stat = Math.max(1, Math.min(999, (int) s));
            result[i] = Math.max(1, stat + equip[i]);
        }
        return new Stat(result[0], result[1], result[2], result[3], result[4]);
    }

    public void setStat(Stat stat) {
        this.StatActu.HP = stat.HP;
        this.StatActu.STR = stat.STR;
        this.StatActu.SPR = stat.SPR;
        this.StatActu.DEF = stat.DEF;
        this.StatActu.SPE = stat.SPE;
    }

    public Stat GetStats() {
        return StatActu;
    }

    public static final int MAX_NICKNAME = 26;

    public String GetNickname() {
        return surnom == null || surnom.equals("???") ? "" : surnom;
    }

    public void setNickname(String nickname) {
        String n = nickname == null ? "" : nickname.trim();
        if (n.length() > MAX_NICKNAME) {
            n = n.substring(0, MAX_NICKNAME);
        }
        surnom = n.isEmpty() ? "???" : n;
    }

    public void setName() {
        final int MAXNAMESIZE = 26;
        Scanner sc = new Scanner(System.in);
        System.out.println("What will be the nickname of your yokai?");
        String str = sc.nextLine();
        if (str.length() > MAXNAMESIZE) {
            System.out.println("The name of your yokai isn't correct, max limit is 26 caracter");
        } else {
            surnom = str;
        }
    }

    public YokaiFight toYokaiFight(int teamNumber) {
        updateStats();
        return new YokaiFight(
                this.GetName(),
                this.GetHP(),
                this.GetSTR(),
                this.GetSPR(),
                this.GetDEF(),
                this.GetSPE(),
                teamNumber);
    }

    public Yokai GetYokai() {
        return yokai;
    }

    public String GetYokaiName() {
        return yokai.GetName();
    }

    public String GetName() {
        return yokai.GetName();
    }

    public Tier GetTier() {
        return yokai.GetTier();
    }

    public int GetLevel() {
        return level;
    }

    public String GetTribe() {
        return yokai.GetTribe();
    }

    public int GetTechniqueId() {
        return yokai.getTechniqueId();
    }

    public int GetAttackId() {
        return yokai.getAttackId();
    }

    public double[] GetResistances() {
        return yokai.getResistances();
    }

    public Attitude GetAttitude() {
        return this.attitudeYoKai;
    }

    public String GetAttitudeName() {
        return attitudeYoKai == null ? "null" : attitudeYoKai.getAttitude();
    }

    public Equipement GetEquipement() {
        return ItemYokai;
    }

    public String GetEquipementName() {
        return ItemYokai == null || ItemYokai.GetName() == null ? "null" : ItemYokai.GetName();
    }

    public SportsClub GetSportsClub() {
        return SportsClub;
    }

    public IV GetIv() {
        return ivYokai;
    }

    public int GetIvHP() {
        return ivYokai.GetIvHP();
    }

    public int GetIvSTR() {
        return ivYokai.GetIvSTR();
    }

    public int GetIvSPR() {
        return ivYokai.GetIvSPR();
    }

    public int GetIvDEF() {
        return ivYokai.GetIvDEF();
    }

    public int GetIvSPE() {
        return ivYokai.GetIvSPE();
    }

    public int GetHP() {
        return StatActu.HP;
    }

    public int GetSTR() {
        return StatActu.STR;
    }

    public int GetSPR() {
        return StatActu.SPR;
    }

    public int GetDEF() {
        return StatActu.DEF;
    }

    public int GetSPE() {
        return StatActu.SPE;
    }

}
