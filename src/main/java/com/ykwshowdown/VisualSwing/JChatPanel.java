package com.ykwshowdown.VisualSwing;

import java.awt.*;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import com.ykwshowdown.chat.Message;
import com.ykwshowdown.chat.ModeleChat;
import com.ykwshowdown.chat.ObservateurChat;

public class JChatPanel extends JPanel implements ObservateurChat {

    public static final String ACTION_ENVOYER = "CHAT_ENVOYER";

    private static final DateTimeFormatter HEURE = DateTimeFormatter.ofPattern("HH:mm");

    private final ModeleChat modele;
    private JTextArea zoneMessages;
    private JTextField champSaisie;
    private JButton boutonEnvoyer;

    public JChatPanel(ModeleChat modele) {
        this.modele = modele;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(340, 0));
        setBackground(new Color(0xF4F2F8));
        setBorder(new MatteBorder(0, 1, 0, 0, new Color(0xC9C2D6)));

        JLabel titre = new JLabel("Chat");
        titre.setFont(new Font("Verdana", Font.BOLD, 16));
        titre.setForeground(Color.WHITE);
        titre.setBorder(new EmptyBorder(10, 14, 10, 14));
        JPanel entete = new JPanel(new BorderLayout());
        entete.setBackground(new Color(0x4D7FB8));
        entete.add(titre, BorderLayout.WEST);

        zoneMessages = new JTextArea();
        zoneMessages.setEditable(false);
        zoneMessages.setLineWrap(true);
        zoneMessages.setWrapStyleWord(true);
        zoneMessages.setFont(new Font("Verdana", Font.PLAIN, 13));
        zoneMessages.setBackground(Color.WHITE);
        zoneMessages.setBorder(new EmptyBorder(8, 10, 8, 10));
        JScrollPane defilement = new JScrollPane(zoneMessages);
        defilement.setBorder(BorderFactory.createEmptyBorder());

        champSaisie = new JTextField();
        champSaisie.setFont(new Font("Verdana", Font.PLAIN, 13));
        champSaisie.setActionCommand(ACTION_ENVOYER);
        boutonEnvoyer = new JButton("Envoyer");
        boutonEnvoyer.setActionCommand(ACTION_ENVOYER);
        JPanel saisie = new JPanel(new BorderLayout(6, 0));
        saisie.setOpaque(false);
        saisie.setBorder(new EmptyBorder(8, 8, 8, 8));
        saisie.add(champSaisie, BorderLayout.CENTER);
        saisie.add(boutonEnvoyer, BorderLayout.EAST);

        add(entete, BorderLayout.NORTH);
        add(defilement, BorderLayout.CENTER);
        add(saisie, BorderLayout.SOUTH);

        modele.addObservateur(this);
    }

    @Override
    public void chatModifie() {
        StringBuilder sb = new StringBuilder();
        for (Message m : modele.getMessages()) {
            sb.append("[").append(m.heure().format(HEURE)).append("] ")
              .append(m.auteur()).append(" : ").append(m.texte()).append("\n");
        }
        zoneMessages.setText(sb.toString());
        zoneMessages.setCaretPosition(zoneMessages.getDocument().getLength());
    }

    public String prendreSaisie() {
        String texte = champSaisie.getText().trim();
        champSaisie.setText("");
        champSaisie.requestFocusInWindow();
        return texte;
    }

    public void setEcouteur(ActionListener ecouteur) {
        champSaisie.addActionListener(ecouteur);
        boutonEnvoyer.addActionListener(ecouteur);
    }
}
