package com.ykwshowdown.VisualSwing;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.ykwshowdown.VisualSwing.StylizedButton.BackgroundPanel;
import com.ykwshowdown.VisualSwing.StylizedButton.StyledButton;
import com.ykwshowdown.chat.ModeleChat;
import com.ykwshowdown.menu.HomeMenu;

/**
 * Menu principal : boutons a gauche, chat a droite.
 */
public class JHomeMenu extends JPanel {

    public static final String ACTION_COMBAT  = "MENU_COMBAT";
    public static final String ACTION_EQUIPES = "MENU_EQUIPES";

    static final Font VERDANA = new Font("Verdana", Font.PLAIN, 14);

    private HomeMenu leMenu;
    private StyledButton battleButton;
    private StyledButton teamButton;
    private JChatPanel chat;

    public JHomeMenu(HomeMenu menu, ModeleChat modeleChat) {
        this.leMenu = menu;
        setLayout(new BorderLayout());
        setBackground(new Color(240, 235, 245));

        battleButton = new StyledButton("Combat !", "Trouver un adversaire", 0xC987B8, 0xA84D96, 0x823B74);
        battleButton.setFont(VERDANA);
        battleButton.setPreferredSize(new Dimension(240, 70));
        battleButton.setActionCommand(ACTION_COMBAT);

        teamButton = new StyledButton("Modifier les equipes", 0x89AAD4, 0x4D7FB8, 0x3B628E);
        teamButton.setFont(VERDANA);
        teamButton.setPreferredSize(new Dimension(240, 55));
        teamButton.setActionCommand(ACTION_EQUIPES);

        // --- Colonne de boutons ---
        JLabel titre = new JLabel("YKW Showdown", SwingConstants.CENTER);
        titre.setFont(new Font("Verdana", Font.BOLD, 28));
        titre.setForeground(new Color(0x823B74));

        JPanel fightPanel = new BackgroundPanel(Color.BLACK, 10, 10, 10, 10, 0.5f, 25);
        fightPanel.setLayout(new BorderLayout());
        fightPanel.add(battleButton, BorderLayout.CENTER);

        JPanel teamPanel = new BackgroundPanel(Color.BLACK, 10, 10, 10, 10, 0.5f, 25);
        teamPanel.setLayout(new BorderLayout());
        teamPanel.add(teamButton, BorderLayout.CENTER);

        JPanel colonne = new JPanel(new GridLayout(3, 1, 0, 16));
        colonne.setOpaque(false);
        colonne.add(titre);
        colonne.add(fightPanel);
        colonne.add(teamPanel);

        // Centre la colonne dans l'espace a gauche du chat
        JPanel centre = new JPanel(new GridBagLayout());
        centre.setOpaque(false);
        centre.add(colonne);

        chat = new JChatPanel(modeleChat);

        add(centre, BorderLayout.CENTER);
        add(chat, BorderLayout.EAST);
    }

    /** Branche le controleur sur tous les composants interactifs du menu. */
    public void setEcouteur(ActionListener ecouteur) {
        battleButton.addActionListener(ecouteur);
        teamButton.addActionListener(ecouteur);
        chat.setEcouteur(ecouteur);
    }

    public JChatPanel getChat() {
        return chat;
    }

    public StyledButton getBattleButton() {
        return battleButton;
    }

    public StyledButton getTeamButton() {
        return teamButton;
    }
}
