package com.ykwshowdown.database;

import com.ykwshowdown.menu.WatchBuilder;
import com.ykwshowdown.yokai.IV;
import com.ykwshowdown.yokai.Tier;
import com.ykwshowdown.yokai.Yokai;
import com.ykwshowdown.yokai.YokaiGeneral;

public class TeamDataTransfer {
    int teamId;
    String name;
    String format;
    YokaiGeneralDT[] slots;

    public TeamDataTransfer(WatchBuilder team){
        this.teamId = team.getTeamId();
        this.name = team.GetTeamName();
        this.format = team.getFormat() == null ? null : team.getFormat().getTier();
        slots = new YokaiGeneralDT[6];
        for(int i=0; i<6; i++){
            YokaiGeneral y = team.getYokaiIndex(i);
            if (y != null && y.GetYokai() != null) {
                slots[i] = new YokaiGeneralDT(y);
            }
        }
    }
    public int getTeamId(){
        return teamId;
    }
    public String getName(){
        return name;
    }
    public String getFormat(){
        return format;
    }
    public YokaiGeneralDT getSlotAtIndex(int index){
        return slots[index];
    }

    public YokaiGeneralDT[] getSlots(){
        return slots;
    }

    public WatchBuilder toWatchBuilder() {
        WatchBuilder team = new WatchBuilder(name);
        team.setTeamId(teamId);
        if (format != null) {
            try {
                team.setFormat(Tier.valueOf(format));
            } catch (IllegalArgumentException e) {
                team.setFormat(Tier.OU);          // format inconnu
            }
        }
        if (slots != null) {
            for (int j = 0; j < slots.length && j < 6; j++) {
                if (slots[j] != null) {
                    YokaiGeneral yg = slots[j].fromDataTransfer();
                    if (yg != null) {
                        team.putYokai(j + 1, yg);
                    }
                }
            }
        }
        return team;
    }

    public static class YokaiGeneralDT {
        int yokaiId;
        String nickname;
        int level;
        int ivHp, ivStr, ivSpr, ivDef, ivSpe;
        String attitude;
        String equipment;

        YokaiGeneralDT(YokaiGeneral y){
            this.yokaiId = y.GetYokai().GetID();
            this.nickname = y.GetNickname().isEmpty() ? null : y.GetNickname();
            this.level = y.GetLevel();
            ivHp = y.GetIvHP();
            ivStr =  y.GetIvSTR();
            ivSpr = y.GetIvSPR();
            ivDef = y.GetIvDEF();
            ivSpe = y.GetIvSPE();
            this.attitude = y.GetAttitude() == null ? null : y.GetAttitudeName();
            this.equipment = y.GetEquipement() == null ? null : y.GetEquipementName();
        }

        public int getYokaiId(){
            return yokaiId;
        }
        public String getNickName(){
            return nickname;
        }
        public int getLevel(){
            return level;
        }
        public int getIvHp(){
            return ivHp;
        }
        public int getIvStr(){
            return ivStr;
        }
        public int getIvSpr(){
            return ivSpr;
        }
        public int getIvDef(){
            return ivDef;
        }
        public int getIvSpe(){
            return ivSpe;
        }
        public String getAttitude(){
            return attitude;
        }
        public String getEquipment(){
            return equipment;
        }

        public YokaiGeneral fromDataTransfer(){
            Yokai yB = DataBase.getYokai(yokaiId);
            if (yB == null) {
                return null;                       // id inconnu
            }
            YokaiGeneral yg = new YokaiGeneral(yB, nickname, level, new IV());
            yg.setIv(ivHp, ivStr, ivSpr, ivDef, ivSpe);
            yg.setNickname(nickname);
            if (attitude != null) {
                yg.setAttitude(attitude);
            }
            if (equipment != null) {
                yg.setEquipment(equipment);
            }
            return yg;
        }
    }
}
