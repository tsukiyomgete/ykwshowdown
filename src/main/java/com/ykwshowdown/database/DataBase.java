package com.ykwshowdown.database;

import java.net.Socket;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.annotations.SerializedName;
import com.ykwshowdown.user.User;
import com.ykwshowdown.yokai.*;
import com.ykwshowdown.fightingdata.*;

public class DataBase {

    @SerializedName(value = "yokai", alternate = {"species"}) List<RawYokai> yokaiRaw;
    @SerializedName(value = "action", alternate = {"actions"}) List<RawAction> actionRaw;
    @SerializedName(value = "attitude", alternate = {"attitudes"}) List<RawAttitude> attitudeRaw;
    @SerializedName("equipment") List<RawEquipment> equipmentRaw;

    private static final Map<Integer, Yokai> mapOfYokai = new LinkedHashMap<>();
    private static final Map<Integer, RawAction> mapOfAction = new LinkedHashMap<>();
    private static final Map<Integer, RawAttitude> mapOfAttitude = new LinkedHashMap<>();
    private static final Map<Integer, RawEquipment> mapOfEquipment = new LinkedHashMap<>();
    private static boolean isLoaded = false;

    public static class RawYokai{
        @SerializedName("id") int yokaiId;
        @SerializedName("name") String yokaiName;
        @SerializedName("tribe") String yokaiTribe;
        @SerializedName("rank") String yokaiRank;
        @SerializedName("hp_min") int hpMin;
        @SerializedName("str_min") int strMin;
        @SerializedName("spr_min") int sprMin;
        @SerializedName("def_min") int defMin;
        @SerializedName("spd_min") int spdMin;
        @SerializedName("hp_max") int hpMax;
        @SerializedName("str_max") int strMax;
        @SerializedName("spr_max") int sprMax;
        @SerializedName("def_max") int defMax;
        @SerializedName("spd_max") int spdMax;

        double res_fire;
        @SerializedName("res_ice") double resIce;
        double res_earth;
        double res_lightning;
        double res_water;
        double res_wind;

        @SerializedName("attack_id") int attackId;
        @SerializedName("technique_id") int techniqueId;
        @SerializedName("inspirit_id") Integer inspiritId;
        @SerializedName("ultimate_id") Integer ultimateId;

        int prob_attack;
        int prob_technique;
        @SerializedName("prob_inspirit") int probInspirit;
        int prob_guard;

        public RawYokai(int yokaiId, String yokaiName, String yokaiTribe, String yokaiRank,
                        int hpMin, int strMin, int sprMin, int defMin, int spdMin,
                        int hpMax, int strMax, int sprMax, int defMax, int spdMax,
                        double res_fire, double resIce, double res_earth,
                        double res_lightning, double res_water, double res_wind,
                        int attackId, int techniqueId, Integer inspiritId, Integer ultimateId,
                        int prob_attack, int prob_technique, int probInspirit, int prob_guard) {
            this.yokaiId = yokaiId;
            this.yokaiName = yokaiName;
            this.yokaiTribe = yokaiTribe;
            this.yokaiRank = yokaiRank;
            this.hpMin = hpMin;
            this.strMin = strMin;
            this.sprMin = sprMin;
            this.defMin = defMin;
            this.spdMin = spdMin;
            this.hpMax = hpMax;
            this.strMax = strMax;
            this.sprMax = sprMax;
            this.defMax = defMax;
            this.spdMax = spdMax;
            this.res_fire = res_fire;
            this.resIce = resIce;
            this.res_earth = res_earth;
            this.res_lightning = res_lightning;
            this.res_water = res_water;
            this.res_wind = res_wind;
            this.attackId = attackId;
            this.techniqueId = techniqueId;
            this.inspiritId = inspiritId;
            this.ultimateId = ultimateId;
            this.prob_attack = prob_attack;
            this.prob_technique = prob_technique;
            this.probInspirit = probInspirit;
            this.prob_guard = prob_guard;
        }
    }
    public static class RawAction {
        @SerializedName("id") int actionId;
        @SerializedName("name") String actionName;
        @SerializedName("type") String actionType;
        @SerializedName("power_lv1") Integer actionPowerLvl1;
        @SerializedName("power_lv10") Integer actionPowerLvl10;
        @SerializedName("hits") Integer actionHitsTime;
        @SerializedName("element") String actionElement;
        @SerializedName("formula") String actionUltimateFormula;
        @SerializedName("cost_lv1") Integer actionCostLvl1;
        @SerializedName("cost_lv10") Integer actionCostLvl10;
        @SerializedName("effect") String actionEffect;
        @SerializedName("effect_power") Integer actionEffectPower;
        @SerializedName("effect_target") String actionTarget;

        public RawAction(int actionId, String actionName, String actionType,
                         int actionPowerLvl1, int actionPowerLvl10, int actionHitsTime,
                         String actionUltimateFormula, int actionCostLvl1, int actionCostLvl10,
                         String actionEffect, String actionTarget) {
            this.actionId = actionId;
            this.actionName = actionName;
            this.actionType = actionType;
            this.actionPowerLvl1 = actionPowerLvl1;
            this.actionPowerLvl10 = actionPowerLvl10;
            this.actionHitsTime = actionHitsTime;
            this.actionUltimateFormula = actionUltimateFormula;
            this.actionCostLvl1 = actionCostLvl1;
            this.actionCostLvl10 = actionCostLvl10;
            this.actionEffect = actionEffect;
            this.actionTarget = actionTarget;
        }
    }
    public static class RawAttitude {
        @SerializedName("id") int attitudeId;
        @SerializedName("name") String attitudeName;
        @SerializedName("ev_hp") int evHp;
        @SerializedName("ev_str") int evStr;
        @SerializedName("ev_spr") int evSpr;
        @SerializedName("ev_def") int evDef;
        @SerializedName("ev_spd") int evSpd;

        public RawAttitude(int attitudeId, String attitudeName,
                           int evHp, int evStr, int evSpr, int evDef, int evSpd) {
            this.attitudeId = attitudeId;
            this.attitudeName = attitudeName;
            this.evHp = evHp;
            this.evStr = evStr;
            this.evSpr = evSpr;
            this.evDef = evDef;
            this.evSpd = evSpd;
        }

    }
    public static class RawEquipment {
        @SerializedName("id") int idEquipment;
        @SerializedName("name") String nameEquipment;
        @SerializedName("rank") String rankEquipment;
        @SerializedName("bonus_hp") int bonusHp;
        @SerializedName("bonus_str") int bonusStr;
        @SerializedName("bonus_spr") int bonusSpr;
        @SerializedName("bonus_def") int bonusDef;
        @SerializedName("bonus_spd") int bonusSpd;
        @SerializedName("effect") String effectEquipment;

        public RawEquipment(int idEquipment, String nameEquipment, String rankEquipment,
                            int bonusHp, int bonusStr, int bonusSpr, int bonusDef, int bonusSpd,
                            String effectEquipment) {
            this.idEquipment = idEquipment;
            this.nameEquipment = nameEquipment;
            this.rankEquipment = rankEquipment;
            this.bonusHp = bonusHp;
            this.bonusStr = bonusStr;
            this.bonusSpr = bonusSpr;
            this.bonusDef = bonusDef;
            this.bonusSpd = bonusSpd;
            this.effectEquipment = effectEquipment;
        }
    }




    public static void loadServerData(String json) {
        System.out.println("JSON recu (debut) : " + json.substring(0, Math.min(300, json.length())));
        DataBase data = new Gson().fromJson(json, DataBase.class);
        if (data.yokaiRaw == null || data.actionRaw == null) {
            System.out.println("Donnees incompletes recues du serveur (yokai ou action absent)");
            return;
        }
        if (data.attitudeRaw == null) {
            data.attitudeRaw = new java.util.ArrayList<>();
        }
        if (data.equipmentRaw == null) {
            data.equipmentRaw = new java.util.ArrayList<>();
        }

        mapOfAction.clear();
        mapOfAttitude.clear();
        mapOfEquipment.clear();
        mapOfYokai.clear();

        for (RawAction a : data.actionRaw) {
            mapOfAction.put(a.actionId, a);
        }
        for (RawAttitude a : data.attitudeRaw) {
            mapOfAttitude.put(a.attitudeId, a);
        }
        for (RawEquipment e : data.equipmentRaw) {
            mapOfEquipment.put(e.idEquipment, e);
        }
        for (RawYokai y : data.yokaiRaw) {
            Stat statMin = new Stat(y.hpMin, y.strMin, y.sprMin, y.defMin, y.spdMin);
            Stat statMax = new Stat(y.hpMax, y.strMax, y.sprMax, y.defMax, y.spdMax);
            double[] res = {y.res_fire, y.resIce, y.res_earth, y.res_lightning, y.res_water, y.res_wind};
            mapOfYokai.put(y.yokaiId, new Yokai(y.yokaiId, y.yokaiName, y.yokaiRank.charAt(0), y.yokaiTribe,
                    statMin, statMax, res,
                    y.attackId, y.techniqueId, y.inspiritId, y.ultimateId,
                    y.prob_attack, y.prob_technique, y.probInspirit, y.prob_guard));
        }
        isLoaded = true;
        System.out.println("Donnees chargees : " + mapOfYokai.size() + " yokai, " + mapOfAction.size() + " actions, "
                + mapOfEquipment.size() + " equipements, " + mapOfAttitude.size() + " attitudes");
    }

    public static boolean isLoaded() {
        return isLoaded;
    }

    public static Yokai getYokai(int id) {
        return mapOfYokai.get(id);
    }

    public static RawAction getAction(int id) {
        return mapOfAction.get(id);
    }

    public static RawEquipment getEquipment(int id) {
        return mapOfEquipment.get(id);
    }

    public static RawAttitude getAttitude(int id) {
        return mapOfAttitude.get(id);
    }

    public static Collection<Yokai> getAllYokai() {
        return mapOfYokai.values();
    }

    public static Collection<RawEquipment> getAllEquipment() {
        return mapOfEquipment.values();
    }

    public static Collection<RawAttitude> getAllAttitude() {
        return mapOfAttitude.values();
    }

    public static void trierActions(){

    }

    public static Yokai getYokai(String nom) {
        for (Yokai y : mapOfYokai.values()) {
            if (y.GetName().equalsIgnoreCase(nom)) {
                return y;
            }
        }
        return null;
    }

    public static void addUser(User userTest) throws Exception {

    }

    public static User getUser(String username) throws Exception {
        return null;
    }

    public static boolean getMdp(String username, String password){
        return false;
    }

    public static void setDate(User userTest, Date loggedDate) throws Exception {

    }




}