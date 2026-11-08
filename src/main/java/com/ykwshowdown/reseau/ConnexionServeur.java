package com.ykwshowdown.reseau;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.SwingUtilities;

/**
 * Modele reseau : la connexion au serveur YKW Showdown (port 5001).
 *
 * Protocole (une ligne par requete, une ligne par reponse) :
 *   /signin/user/<pseudo>/passwd/<mdp>  -> /success/signin   ou /error/signin
 *   /login/user/<pseudo>/passwd/<mdp>   -> /success/login    ou /error/login
 *   requete mal formee                  -> /error/invalid
 *   commande inconnue                   -> /error/unknown
 *
 * Les methodes d'envoi ne lisent JAMAIS la reponse : un seul thread lit la socket
 * (demarrerEcoute) et transmet chaque ligne a l'ecouteur actuel, sur le thread Swing.
 */
public class ConnexionServeur {

    private final Socket socket;
    private final BufferedReader lecteur;
    private final PrintWriter writer;

    // Qui recoit les messages du serveur en ce moment (le login, puis le menu)
    private volatile EcouteurServeur ecouteur;

    public ConnexionServeur(String hote, int port) throws IOException {
        socket = new Socket(hote, port);
        lecteur = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true);
        demarrerEcoute();
    }

    /** Change le destinataire des messages du serveur. */
    public void setEcouteur(EcouteurServeur ecouteur) {
        this.ecouteur = ecouteur;
    }

    // Requete avec envoie seulement

    public void signin(String pseudo, String mdp) {
        envoyer("/signin/user/" + pseudo + "/passwd/" + mdp);
    }

    public void login(String pseudo, String mdp) {
        envoyer("/login/user/" + pseudo + "/passwd/" + mdp);
    }

    public void envoyer(String ligne) {
        writer.println(ligne);
    }

    public void fermer() {
        try {
            socket.close();
        } catch (IOException ignored) {
        }
    }


    /** L'endroit qui lit la socket. */
    private void demarrerEcoute() {
        Thread t = new Thread(() -> {
            try {
                String ligne;
                while ((ligne = lecteur.readLine()) != null) {
                    transmit(ligne);
                }
                transmit("/error/deconnecte");
            } catch (IOException e) {
                if (!socket.isClosed()) {
                    transmit("/error/deconnecte");
                }
            }
        }, "ecoute-serveur");
        t.setDaemon(true);
        t.start();
    }

    /** Renvoie la ligne sur le thread Swing, a l'ecouteur actuel. */
    private void transmit(String ligne) {
        SwingUtilities.invokeLater(() -> {
            EcouteurServeur e = ecouteur;
            if (e != null) {
                e.messageRecu(ligne);
            }
        });
    }
}
