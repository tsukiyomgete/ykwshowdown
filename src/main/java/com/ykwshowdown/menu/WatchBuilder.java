package com.ykwshowdown.menu;

import java.util.Scanner;

import com.ykwshowdown.yokai.IV;
import com.ykwshowdown.yokai.Tier;
import com.ykwshowdown.yokai.Yokai;
import com.ykwshowdown.yokai.YokaiGeneral;

public class WatchBuilder {
    int teamId;
    String nomTeam;
    final int MAXYOKAI = 6;
    YokaiGeneral[] WatchTeam;
    Tier formatTeam;

    String strValid;
    int nbYokai;

    Scanner sc = new Scanner(System.in);

    public WatchBuilder() {
        this.nomTeam = "";
        this.WatchTeam = new YokaiGeneral[MAXYOKAI];
        formatTeam = null;
    }

    public WatchBuilder(String nomTeam) {
        this.nomTeam = nomTeam;
        this.WatchTeam = new YokaiGeneral[MAXYOKAI];
        formatTeam = null;
    }

    public int addYokai(int rang, String nomYokai) {
        if (nbYokai < MAXYOKAI) {
            if (rang < 1 || rang > 6) {
                throw new IllegalArgumentException("Rang pas compris entre 1 et 6");
            }
            rang -= 1;
            WatchTeam[rang] = new YokaiGeneral();
            WatchTeam[rang].addYokai(nomYokai);
            System.out.println(WatchTeam[rang].GetName() + " ajouté au rang " + (rang + 1));
            nbYokai++;
            return rang;
        } else {
            throw new IllegalArgumentException("Tu as déjà atteint la limite max de Yokai");
        }
    }

    public void replaceYokai(int rang, String str) {
        if (rang < 1 || rang > 6) {
            throw new IllegalArgumentException("Rang pas compris entre 1 et 6");
        }
        rang -= 1;
        String s = WatchTeam[rang].GetName();
        WatchTeam[rang].replaceYokai(str);
        System.out.println(s + " a été remplacé par " + WatchTeam[rang] + " au rang " + (rang + 1));
    }

    public void print() {
        System.out.println(nomTeam);
        for (int i = 0; i < WatchTeam.length; i++) {
            YokaiGeneral yk = WatchTeam[i];
            if (yk == null)
                continue;

            yk.updateStats();
            System.out.println("Rang " + (i + 1) + " : " + yk.GetName() + "  Level: " + yk.GetLevel());
            System.out.println("  Attitude : " + yk.GetAttitudeName() + "  |  Equipment : " + yk.GetEquipementName());
            System.out.println("  IV    : " + yk.GetIvHP() + " HP / " + yk.GetIvSTR() + " STR / " + yk.GetIvSPR()
                    + " SPR / " + yk.GetIvDEF() + " DEF / " + yk.GetIvSPE() + " SPE  (points restants : "
                    + yk.GetIv().getPointsLeft() + ")");
            System.out.println("  Stats : " + yk.GetHP() + " HP / " + yk.GetSTR() + " STR / " + yk.GetSPR()
                    + " SPR / " + yk.GetDEF() + " DEF / " + yk.GetSPE() + " SPE");
            System.out.println();
        }
    }

    public String getValidationMessage() {
        String raisons = "";
        if (formatTeam == null) {
            raisons += "Aucun format choisi\n";
        }
        for (int i = 0; i < WatchTeam.length; i++) {
            if (WatchTeam[i] == null || WatchTeam[i].GetYokai() == null) {
                raisons += "Il n'y a pas de yokai au rang " + (i + 1) + "\n";
            } else if (WatchTeam[i].GetTier() == null) {
                raisons += WatchTeam[i].GetYokaiName() + " n'a pas de tier attribue\n";
            } else if (formatTeam != null && !WatchTeam[i].GetTier().estAutoriseDans(formatTeam)) {
                raisons += WatchTeam[i].GetYokaiName() + " (" + WatchTeam[i].GetTier().getTier()
                        + ") est banni en " + formatTeam.getTier() + "\n";
            }
        }
        return raisons.isEmpty() ? null : raisons.trim();
    }

    public boolean isTeamValid() {
        String raisons = getValidationMessage();
        if (raisons == null) {
            System.out.println("Votre Equipe est valide pour ce Format");
            return true;
        }
        System.out.println("Votre Equipe n'est pas valide pour ces raisons là :\n" + raisons);
        return false;
    }

    public String toString() {
        String result = "Team Name : " + nomTeam + "\n";
        if (formatTeam != null) {
            result += "Format : " + formatTeam.getTier() + "\n";
        }
        for (int i = 0; i < WatchTeam.length; i++) {
            if (WatchTeam[i] == null) {
                result += "Rank " + (i + 1) + " : (vide)\n";
            } else {
                result += "Slot place : " + (i + 1) + "\n";
                result += WatchTeam[i].GetName() + "\n";
                result += "Lvl : " + WatchTeam[i].GetLevel() + "\n";
                result += "IVS : ";
                result += WatchTeam[i].GetIvHP() + " HP " + "/ ";
                result += WatchTeam[i].GetIvSTR() + " STR " + "/ ";
                result += WatchTeam[i].GetIvSPR() + " SPR " + "/ ";
                result += WatchTeam[i].GetIvDEF() + " DEF " + "/ ";
                result += WatchTeam[i].GetIvSPE() + " SPE " + "\n";
                result += "Attitude : " + WatchTeam[i].GetAttitudeName() + "\n";
                result += "Equipment : " + WatchTeam[i].GetEquipementName() + "\n";
                if (!WatchTeam[i].GetNickname().isEmpty()) {
                    result += "Nickname : " + WatchTeam[i].GetNickname() + "\n";
                }
                result += "\n";
            }
        }
        return result;
    }

    public void importTeam(String teamStr) {
        String[] ligne = teamStr.split("\n");
        String[] ligneEquipe = ligne[0].split(" : ", 2);
        this.nomTeam = ligneEquipe[1].trim();

        int l = 1;
        while (l < ligne.length) {
            if (ligne[l].startsWith("Format")) {
                try {
                    this.formatTeam = Tier.valueOf(valeur(ligne[l]));
                } catch (IllegalArgumentException e) {
                    System.out.println("Unknown format : " + valeur(ligne[l]));
                }
                l++;
                continue;
            }
            if (!ligne[l].startsWith("Slot place")) {
                l++;
                continue;
            }
            int rang = Integer.parseInt(valeur(ligne[l]));
            String nomYokai = ligne[l + 1].trim();
            int level = Integer.parseInt(valeur(ligne[l + 2]));

            String[] ivParts = valeur(ligne[l + 3]).split("/");
            int ivHP = Integer.parseInt(ivParts[0].replace("HP", "").trim());
            int ivSTR = Integer.parseInt(ivParts[1].replace("STR", "").trim());
            int ivSPR = Integer.parseInt(ivParts[2].replace("SPR", "").trim());
            int ivDEF = Integer.parseInt(ivParts[3].replace("DEF", "").trim());
            int ivSPE = Integer.parseInt(ivParts[4].replace("SPE", "").trim());

            String attitude = valeur(ligne[l + 4]);
            String equipment = "null";
            l += 5;
            if (l < ligne.length && ligne[l].startsWith("Equipment")) {
                equipment = valeur(ligne[l]);
                l++;
            }
            String nickname = "";
            if (l < ligne.length && ligne[l].startsWith("Nickname")) {
                nickname = valeur(ligne[l]);
                l++;
            }

            YokaiGeneral yg = new YokaiGeneral();
            yg.addYokai(nomYokai);
            if (yg.GetYokai() == null) {
                continue;
            }
            yg.setLevel(level);
            yg.setIv(ivHP, ivSTR, ivSPR, ivDEF, ivSPE);
            if (!attitude.equalsIgnoreCase("null") && !yg.setAttitude(attitude)) {
                System.out.println("Attitude inconnue : " + attitude);
            }
            if (!equipment.equalsIgnoreCase("null") && !yg.setEquipment(equipment)) {
                System.out.println("Equipement inconnu : " + equipment);
            }
            yg.setNickname(nickname);
            if (WatchTeam[rang - 1] == null) {
                nbYokai++;
            }
            WatchTeam[rang - 1] = yg;
        }
    }

    private static String valeur(String ligne) {
        int i = ligne.indexOf(':');
        return i < 0 ? ligne.trim() : ligne.substring(i + 1).trim();
    }

    private YokaiGeneral yokaiAuRang(int rang) {
        if (rang < 1 || rang > MAXYOKAI) {
            throw new IllegalArgumentException("Rang pas compris entre 1 et 6");
        }
        if (WatchTeam[rang - 1] == null) {
            throw new IllegalArgumentException("Il n'y a pas de yokai au rang " + rang);
        }
        return WatchTeam[rang - 1];
    }

    public void setIV(int rang, String name) {
        yokaiAuRang(rang).setIV(name);
    }

    public void setIV(int rang, int hp, int str, int spr, int def, int spe) {
        yokaiAuRang(rang).setIv(hp, str, spr, def, spe);
    }

    public void generateIV(int rang) {
        yokaiAuRang(rang).generateIV();
    }

    public boolean setAttitude(int rang, String nomAttitude) {
        return yokaiAuRang(rang).setAttitude(nomAttitude);
    }

    public boolean setEquipment(int rang, String nomEquipement) {
        return yokaiAuRang(rang).setEquipment(nomEquipement);
    }

    public void updateStats() {
        for (YokaiGeneral yg : WatchTeam) {
            if (yg != null && yg.GetYokai() != null) {
                yg.updateStats();
            }
        }
    }

    public void setFormat(Tier format) {
        formatTeam = format;
    }

    public Tier getFormat() {
        return formatTeam;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public void setTeamName(String nomTeam) {
        this.nomTeam = nomTeam == null ? "" : nomTeam.trim();
    }

    public void setYokai(int rang, Yokai espece) {
        if (rang < 1 || rang > MAXYOKAI) {
            throw new IllegalArgumentException("Rang pas compris entre 1 et 6");
        }
        YokaiGeneral actuel = WatchTeam[rang - 1];
        if (actuel != null && actuel.GetYokai() == espece) {
            return;
        }
        WatchTeam[rang - 1] = espece == null ? null
                : new YokaiGeneral(espece, "???", YokaiGeneral.DEFAULT_LEVEL, new IV());
        recompter();
    }

    public void removeYokai(int rang) {
        setYokai(rang, null);
    }

    public void copyFrom(WatchBuilder autre) {
        this.nomTeam = autre.nomTeam;
        this.formatTeam = autre.formatTeam;
        this.WatchTeam = autre.WatchTeam.clone();
        recompter();
    }

    public WatchBuilder copy() {
        WatchBuilder copie = new WatchBuilder();
        copie.importTeam(this.toString());
        copie.formatTeam = this.formatTeam;
        return copie;
    }

    private void recompter() {
        nbYokai = 0;
        for (YokaiGeneral yg : WatchTeam) {
            if (yg != null) {
                nbYokai++;
            }
        }
    }

    public void putYokai(int rang, YokaiGeneral yg) {
        if (rang < 1 || rang > MAXYOKAI) {
            throw new IllegalArgumentException("Rang pas compris entre 1 et 6");
        }
        WatchTeam[rang - 1] = yg;
        recompter();
    }


    public String GetName(int rang) {
        return WatchTeam[rang].GetName();
    }

    public String GetTeamName() {
        return nomTeam;
    }

    public int GetTeamSize() {
        return nbYokai;
    }

    public YokaiGeneral getYokaiIndex(int rang) {
        return WatchTeam[rang];
    }

}
