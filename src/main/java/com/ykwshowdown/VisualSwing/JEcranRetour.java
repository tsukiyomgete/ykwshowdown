package com.ykwshowdown.VisualSwing;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class JEcranRetour extends JPanel {

    public static final String ACTION_RETOUR = "RETOUR_MENU";

    private JButton boutonRetour;

    public JEcranRetour(String titre, JComponent contenu) {
        setLayout(new BorderLayout());

        boutonRetour = new JButton("← Retour");
        boutonRetour.setActionCommand(ACTION_RETOUR);
        boutonRetour.setFocusPainted(false);

        JLabel lblTitre = new JLabel(titre);
        lblTitre.setFont(new Font("Verdana", Font.BOLD, 16));
        lblTitre.setForeground(Color.WHITE);

        JPanel barre = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        barre.setBackground(new Color(0x3B628E));
        barre.setBorder(new EmptyBorder(8, 4, 8, 4));
        barre.add(boutonRetour);
        barre.add(lblTitre);

        add(barre, BorderLayout.NORTH);
        add(contenu, BorderLayout.CENTER);
    }

    public void setEcouteur(ActionListener ecouteur) {
        boutonRetour.addActionListener(ecouteur);
    }
}
