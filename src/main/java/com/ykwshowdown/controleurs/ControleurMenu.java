package com.ykwshowdown.controleurs;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import com.ykwshowdown.VisualSwing.JChatPanel;
import com.ykwshowdown.VisualSwing.JEcranRetour;
import com.ykwshowdown.VisualSwing.JHomeMenu;
import com.ykwshowdown.chat.Message;
import com.ykwshowdown.chat.ModeleChat;
import com.ykwshowdown.reseau.EcouteurServeur;

public class ControleurMenu implements ActionListener, EcouteurServeur {

    public static final String CARTE_LOGSIGN = "loginSignIn";
    public static final String CARTE_MENU    = "menu";
    public static final String CARTE_EQUIPES = "equipes";
    public static final String CARTE_COMBAT  = "combat";
    public static final String CARTE_EDITEUR = "editeur";

    private CardLayout cartes;
    private JPanel conteneur;
    private JHomeMenu menu;
    private ModeleChat modeleChat;
    private String pseudo;
    private ControleurTeamBuilder teamBuilder;

    public ControleurMenu(CardLayout cartes, JPanel conteneur, JHomeMenu menu, ModeleChat modeleChat,
                          JEcranRetour ecranEquipes, JEcranRetour ecranCombat, String pseudo) {
        this.cartes = cartes;
        this.conteneur = conteneur;
        this.menu = menu;
        this.modeleChat = modeleChat;
        this.pseudo = pseudo;

        menu.setEcouteur(this);
        ecranEquipes.setEcouteur(this);
        ecranCombat.setEcouteur(this);
    }

    public void setTeamBuilder(ControleurTeamBuilder teamBuilder) {
        this.teamBuilder = teamBuilder;
    }

    public void connexionReussie(String pseudoConnecte) {
        this.pseudo = pseudoConnecte;
        modeleChat.setPseudo(pseudoConnecte);
        modeleChat.ajouter(Message.maintenant("Systeme", "Connecte en tant que " + pseudoConnecte));
        cartes.show(conteneur, CARTE_MENU);
    }

    
    @Override
    public void messageRecu(String ligne) {
        if (modeleChat.recevoir(ligne)) {
            return;
        }
        if (ligne.startsWith("/success/team/") || ligne.startsWith("/error/team/") || ligne.startsWith("/team/list/")) {
            if (teamBuilder != null) {
                teamBuilder.reponseServeur(ligne);
            }
            return;
        }
        if (ligne.startsWith("/error/")) {
            modeleChat.ajouter(Message.maintenant("Systeme", "Erreur : " + ligne.substring("/error/".length())));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals(JHomeMenu.ACTION_COMBAT)) {
            if(!menu.getBattleStatus()){
                System.out.println("Envoi de requete matchmaking");
                menu.changeBattleButtonSearching();
            } else if(menu.getBattleStatus()){
                System.out.println("Annulation de requete matchmaking");
                menu.changeBattleButtonClassic();
            }
        } else if (cmd.equals(JHomeMenu.ACTION_EQUIPES)) {
            cartes.show(conteneur, CARTE_EQUIPES);
        } else if (cmd.equals(JEcranRetour.ACTION_RETOUR)) {
            cartes.show(conteneur, CARTE_MENU);
        } else if (cmd.equals(JChatPanel.ACTION_ENVOYER)) {
            String texte = menu.getChat().prendreSaisie();
            modeleChat.envoyer(texte);
        }
    }
}
