package com.ykwshowdown;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;

import javax.swing.*;

import com.ykwshowdown.VisualSwing.JEcranRetour;
import com.ykwshowdown.VisualSwing.JHomeMenu;
import com.ykwshowdown.VisualSwing.JLogMenu;
import com.ykwshowdown.VisualSwing.JTeamBuilder;
import com.ykwshowdown.chat.Message;
import com.ykwshowdown.chat.ModeleChat;
import com.ykwshowdown.controleurs.ControleurLogin;
import com.ykwshowdown.controleurs.ControleurMenu;
import com.ykwshowdown.menu.HomeMenu;
import com.ykwshowdown.reseau.ConnexionServeur;

public class Main {

    private static final String HOTE = "185.157.247.30";
    private static final int PORT = 5001;

    private static java.util.List<Image> iconesCarrees(Image source, double zoom) {
        java.util.List<Image> icones = new java.util.ArrayList<>();
        int w = source.getWidth(null), h = source.getHeight(null);

        for (int taille : new int[]{16, 24, 32, 48, 64, 128, 256}) {
            java.awt.image.BufferedImage carre =
                    new java.awt.image.BufferedImage(taille, taille, java.awt.image.BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = carre.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            double echelle = Math.min((double) taille / w, (double) taille / h) * zoom;
            int nw = (int) (w * echelle), nh = (int) (h * echelle);
            g.drawImage(source, (taille - nw) / 2, (taille - nh) / 2, nw, nh, null);
            g.dispose();
            icones.add(carre);
        }
        return icones;
    }

    public static void main(String[] args) {

        ConnexionServeur serveur;
        try {
            serveur = new ConnexionServeur(HOTE, PORT);
        } catch (IOException e) {
            System.out.println("Serveur injoignable (" + e.getMessage() + ") : mode hors ligne");
            serveur = null;
        }

        final ConnexionServeur s = serveur;
        SwingUtilities.invokeLater(() -> lancer(s));
    }

    private static void lancer(ConnexionServeur serveur) {
        // --- Modeles ---
        ModeleChat modeleChat = new ModeleChat(serveur);
        modeleChat.ajouter(Message.maintenant("Systeme", "Bienvenue sur YKW Showdown !"));

        // --- Vues ---
        JLogMenu login = new JLogMenu();
        JHomeMenu menu = new JHomeMenu(new HomeMenu(), modeleChat);
        JEcranRetour ecranEquipes = new JEcranRetour("Modifier les equipes", new JTeamBuilder());

        JLabel bientot = new JLabel("Ecran de combat : a venir", SwingConstants.CENTER);
        bientot.setFont(new Font("Verdana", Font.BOLD, 20));
        JEcranRetour ecranCombat = new JEcranRetour("Combat", bientot);

        // --- Navigation par cartes ---
        CardLayout cartes = new CardLayout();
        JPanel conteneur = new JPanel(cartes);
        conteneur.add(login, ControleurMenu.CARTE_LOGSIGN);
        conteneur.add(menu, ControleurMenu.CARTE_MENU);
        conteneur.add(ecranEquipes, ControleurMenu.CARTE_EQUIPES);
        conteneur.add(ecranCombat, ControleurMenu.CARTE_COMBAT);

        // --- Controleurs ---
        ControleurMenu ctrlMenu = new ControleurMenu(cartes, conteneur, menu, modeleChat, ecranEquipes, ecranCombat, "Invite");
        new ControleurLogin(login, ctrlMenu, serveur);
        if (serveur != null) {
            serveur.envoyer("/data/all");
        }


        ImageIcon icon = new ImageIcon("src/asset/gameIcon/icon.png");
        // --- Fenetre ---
        JFrame frame = new JFrame("YKW Showdown");
        frame.setContentPane(conteneur);
        frame.setSize(1200, 750);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (serveur != null) {
                    serveur.fermer();
                }
            }
        });
        frame.setIconImages(iconesCarrees(icon.getImage(), 1.08));
        frame.setVisible(true);
    }
}
