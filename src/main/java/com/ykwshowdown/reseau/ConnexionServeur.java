package com.ykwshowdown.reseau;

import com.ykwshowdown.menu.WatchBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.SwingUtilities;

public class ConnexionServeur {

    private final Socket socket;
    private final BufferedReader lecteur;
    private final PrintWriter writer;

    private volatile EcouteurServeur ecouteur;

    public ConnexionServeur(String hote, int port) throws IOException {
        socket = new Socket(hote, port);
        lecteur = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true);
        demarrerEcoute();
    }



    public void setEcouteur(EcouteurServeur ecouteur) {
        this.ecouteur = ecouteur;
    }

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

    private void transmit(String ligne) {
        SwingUtilities.invokeLater(() -> {
            EcouteurServeur e = ecouteur;
            if (e != null) {
                e.messageRecu(ligne);
            }
        });
    }
}
