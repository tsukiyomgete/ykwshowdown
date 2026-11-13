package com.ykwshowdown.controleurs;

import com.google.gson.Gson;
import com.ykwshowdown.VisualSwing.JTeamBuilder;
import com.ykwshowdown.database.TeamDataTransfer;
import com.ykwshowdown.menu.WatchBuilder;
import com.ykwshowdown.menu.WatchList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControleurTeamBuilder implements ActionListener {

    private final JTeamBuilder vue;
    private final WatchList teams;
    private final TeamEditorController editeur;
    private final CardLayout cartes;
    private final JPanel conteneur;
    private WatchBuilder equipeEnvoyee;

    public ControleurTeamBuilder(JTeamBuilder vue, WatchList teams, TeamEditorController editeur,
                                 CardLayout cartes, JPanel conteneur) {
        this.vue = vue;
        this.teams = teams;
        this.editeur = editeur;
        this.cartes = cartes;
        this.conteneur = conteneur;
        vue.setEcouteur(this);
        editeur.setTeamList(this);
        vue.showTeams(teams);
    }

    public void refreshList() {
        vue.showTeams(teams);
    }

    public void showList() {
        refreshList();
        cartes.show(conteneur, ControleurMenu.CARTE_EQUIPES);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if (cmd.equals(JTeamBuilder.ACTION_CREE_EQUIPE)) {
            WatchBuilder team = teams.newTeam();
            if (team == null) {
                vue.showMessage("Maximum number of teams reached.");
                return;
            }
            refreshList();
            editeur.editTeam(team);

        } else if (cmd.startsWith(JTeamBuilder.ACTION_EDIT_TEAM)) {
            editeur.editTeam(teams.getTeam(index(cmd, JTeamBuilder.ACTION_EDIT_TEAM)));

        } else if (cmd.startsWith(JTeamBuilder.ACTION_COPY_TEAM)) {
            if (teams.copyTeam(index(cmd, JTeamBuilder.ACTION_COPY_TEAM)) == null) {
                vue.showMessage("Maximum number of teams reached.");
            }
            refreshList();

        } else if (cmd.startsWith(JTeamBuilder.ACTION_DELETE_TEAM)) {
            int i = index(cmd, JTeamBuilder.ACTION_DELETE_TEAM);
            if (vue.confirmDelete(teams.getTeam(i).GetTeamName())) {
                teams.removeTeam(i);
                refreshList();
            }
        } else if(cmd.startsWith(JTeamBuilder.ACTION_SAVE_TEAM)){
            int i = index(cmd, JTeamBuilder.ACTION_SAVE_TEAM);
            if(vue.confirmSave(teams.getTeam(i).GetTeamName())){
                if (teams.getServer() == null) {
                    vue.showMessage("Hors ligne : impossible de sauvegarder.");
                    return;
                }
                equipeEnvoyee = teams.getTeam(i);
                TeamDataTransfer dto = new TeamDataTransfer(equipeEnvoyee);
                String dtoJson = new Gson().toJson(dto);
                teams.getServer().envoyer("/team/save/"+dtoJson);
            }
        }
    }

    public void reponseServeur(String ligne) {
        if (ligne.startsWith("/team/list/")) {
            String json = ligne.substring("/team/list/".length());
            TeamDataTransfer[] listeEquipe = new Gson().fromJson(json, TeamDataTransfer[].class);
            if (listeEquipe != null) {
                for (TeamDataTransfer dto : listeEquipe) {
                    teams.addTeam(dto.toWatchBuilder());
                }
                refreshList();
            }
            return;
        }
        if (ligne.startsWith("/success/team/")) {
            int id = Integer.parseInt(ligne.substring("/success/team/".length()));
            if (equipeEnvoyee != null) {
                equipeEnvoyee.setTeamId(id);
                vue.showMessage("Equipe \"" + equipeEnvoyee.GetTeamName() + "\" sauvegardee.");
            }
            equipeEnvoyee = null;
        } else if (ligne.startsWith("/error/team/")) {
            String raison = ligne.substring("/error/team/".length());
            if (raison.equals("invalid")) {
                vue.showMessage("Equipe refusee par le serveur : elle n'est pas valide.");
            } else {
                vue.showMessage("Erreur du serveur pendant la sauvegarde.");
            }
            equipeEnvoyee = null;
        }
    }

    private static int index(String cmd, String prefix) {
        return Integer.parseInt(cmd.substring(prefix.length()));
    }
}
