package com.ykwshowdown.controleurs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.ykwshowdown.VisualSwing.JLogMenu;
import com.ykwshowdown.database.DataBase;
import com.ykwshowdown.reseau.ConnexionServeur;
import com.ykwshowdown.reseau.EcouteurServeur;

public class ControleurLogin implements ActionListener, EcouteurServeur {

    private JLogMenu vue;
    private ControleurMenu navigation;
    private TeamEditorController teamEditor;
    private ConnexionServeur serveur;
    private String pseudoEnCours;
    private boolean requeteEnCours;

    public ControleurLogin(JLogMenu vue, ControleurMenu navigation, TeamEditorController teamEditor, ConnexionServeur serveur) {
        this.vue = vue;
        this.navigation = navigation;
        this.teamEditor = teamEditor;
        this.serveur = serveur;
        vue.setEcouteur(this);
        if (serveur != null) {
            serveur.setEcouteur(this);
        } else {
            vue.setMessage("Serveur injoignable : mode hors ligne");
        }
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        boolean inscription = cmd.equals(JLogMenu.ACTION_SIGN);
        if (!inscription && !cmd.equals(JLogMenu.ACTION_LOG)) {
            return;
        }
        if (requeteEnCours) {
            return;
        }

        String pseudo = vue.getPseudo().trim();
        String mdp = vue.getMdp();
        String erreur = verifierSaisie(pseudo, mdp);
        if (erreur != null) {
            vue.setMessage(erreur);
            return;
        }

        if (serveur == null) {
            navigation.connexionReussie(pseudo);
            return;
        }

        pseudoEnCours = pseudo;
        requeteEnCours = true;
        if (inscription) {
            vue.setMessage("Inscription en cours...");
            serveur.signin(pseudo, mdp);
        } else {
            vue.setMessage("Connexion en cours...");
            serveur.login(pseudo, mdp);
        }

    }


    private String verifierSaisie(String pseudo, String mdp) {
        if (pseudo.isEmpty() || mdp.isEmpty()) {
            return "Remplis le pseudo et le mot de passe";
        }
        if (pseudo.contains("/") || mdp.contains("/")) {
            return "Le caractere / est interdit";
        }
        if (pseudo.length() > 32) {
            return "Pseudo trop long (32 caracteres max)";
        }
        return null;
    }



    public void messageRecu(String ligne) {
        if (ligne.startsWith("/data/")) {
            DataBase.loadServerData(ligne.substring("/data/".length()));
            teamEditor.dataLoaded();
            vue.setMessage("Donnees chargees");
            return;
        }
        if (ligne.equals("/error/data")) {
            vue.setMessage("Le serveur n'a pas pu charger les donnees");
            return;
        }

        requeteEnCours = false;
        switch (ligne) {
            case "/success/login":
            case "/success/signin":
                serveur.setEcouteur(navigation);
                navigation.connexionReussie(pseudoEnCours);
                break;
            case "/error/login":
                vue.setMessage("Pseudo ou mot de passe incorrect");
                break;
            case "/error/signin":
                vue.setMessage("Inscription impossible : pseudo deja pris ?");
                break;
            case "/error/alreadyconnected":
                vue.setMessage("Ce pseudo est deja connecte");
                break;
            case "/error/deconnecte":
                vue.setMessage("Connexion au serveur perdue");
                break;
            case "/error/ipbanil":
                vue.setMessage("Connexion refusé (Localisation en Israël)");
            default:
                vue.setMessage("Reponse inattendue du serveur : " + ligne);
        }
    }
}
