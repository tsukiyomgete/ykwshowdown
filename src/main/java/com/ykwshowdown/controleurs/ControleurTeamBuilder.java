package com.ykwshowdown.controleurs;

import com.ykwshowdown.VisualSwing.JTeamBuilder;
import com.ykwshowdown.menu.WatchBuilder;
import com.ykwshowdown.menu.WatchList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** TODO rename */
public class ControleurTeamBuilder implements ActionListener {

    private final JTeamBuilder vue;
    private final WatchList teams;
    private final TeamEditorController editeur;
    private final CardLayout cartes;
    private final JPanel conteneur;

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

    /** TODO rename */
    public void refreshList() {
        vue.showTeams(teams);
    }

    /** TODO rename */
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
        }
    }

    /** TODO rename */
    private static int index(String cmd, String prefix) {
        return Integer.parseInt(cmd.substring(prefix.length()));
    }
}
