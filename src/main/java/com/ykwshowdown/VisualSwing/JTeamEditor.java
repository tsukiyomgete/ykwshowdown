package com.ykwshowdown.VisualSwing;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import com.ykwshowdown.VisualSwing.StylizedButton.StyledButton;

/**
 * Editeur d'une equipe (vue seulement) : barre du haut, format, cartes Yo-kai, bouton d'ajout.
 */
public class JTeamEditor extends JPanel {

    public static final String ACTION_RETOUR_LISTE = "EDITOR_BACK";
    public static final String ACTION_IMPORT_EXPORT = "EDITOR_IMPORT_EXPORT";
    public static final String ACTION_VALIDER = "EDITOR_VALIDATE";
    public static final String ACTION_AJOUTER_YOKAI = "EDITOR_ADD_YOKAI";
    public static final String ACTION_COPIER = "EDITOR_COPY_";       // + position : "EDITOR_COPY_0"
    public static final String ACTION_DEPLACER = "EDITOR_MOVE_";
    public static final String ACTION_SUPPRIMER = "EDITOR_DELETE_";

    public static final int MAX_YOKAI = 6;

    static final Color FOND = new Color(240, 235, 245);
    static final Color FOND_CARTE = new Color(245, 245, 245);
    static final Color BORDURE = new Color(0xC9C2D6);
    static final Color TEXTE_DISCRET = new Color(0x777777);
    static final Font VERDANA = new Font("Verdana", Font.PLAIN, 12);
    static final Font VERDANA_GRAS = new Font("Verdana", Font.BOLD, 11);

    private final JTextField champNomEquipe = new JTextField("Untitled 1", 24);
    private final JComboBox<String> choixFormat = new JComboBox<>(new String[]{"[YKW2] OU", "[YKW2] UU", "[YKW2] NU", "[YKW2] Ubers"});
    private final JPanel listeCartes = new JPanel();
    private final StyledButton boutonRetour = boutonBleu("← List", 90);
    private final StyledButton boutonImportExport = boutonBleu("Import/Export", 130);
    private final StyledButton boutonValider = boutonBleu("✔ Validate", 110);
    private final StyledButton boutonAjouter = new StyledButton("+ Add Yo-kai", 0xC987B8, 0xA84D96, 0x823B74);
    private final List<CarteYokai> cartes = new ArrayList<>();
    private ActionListener ecouteur;

    public JTeamEditor() {
        setLayout(new BorderLayout());
        setBackground(FOND);


        champNomEquipe.setFont(VERDANA);
        JPanel barre = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        barre.setOpaque(false);
        barre.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDURE));
        barre.add(boutonRetour);
        barre.add(champNomEquipe);
        barre.add(boutonImportExport);

        // --- Format ---
        JLabel lblFormat = new JLabel("Format :");
        lblFormat.setFont(VERDANA_GRAS);
        choixFormat.setFont(VERDANA);
        JPanel ligneFormat = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        ligneFormat.setOpaque(false);
        ligneFormat.add(choixFormat);
        ligneFormat.add(boutonValider);

        JPanel blocFormat = new JPanel();
        blocFormat.setLayout(new BoxLayout(blocFormat, BoxLayout.Y_AXIS));
        blocFormat.setOpaque(false);
        lblFormat.setAlignmentX(LEFT_ALIGNMENT);
        ligneFormat.setAlignmentX(LEFT_ALIGNMENT);
        blocFormat.add(lblFormat);
        blocFormat.add(Box.createVerticalStrut(4));
        blocFormat.add(ligneFormat);
        blocFormat.setMaximumSize(new Dimension(Integer.MAX_VALUE, blocFormat.getPreferredSize().height));

        // --- Cartes Yo-kai ---
        listeCartes.setLayout(new BoxLayout(listeCartes, BoxLayout.Y_AXIS));
        listeCartes.setOpaque(false);

        boutonAjouter.setFont(new Font("Verdana", Font.PLAIN, 14));
        boutonAjouter.setPreferredSize(new Dimension(170, 40));
        boutonAjouter.setMaximumSize(new Dimension(170, 40));

        // --- Colonne centrale ---
        JPanel contenu = new JPanel();
        contenu.setLayout(new BoxLayout(contenu, BoxLayout.Y_AXIS));
        contenu.setOpaque(false);
        contenu.setBorder(new EmptyBorder(16, 20, 20, 20));
        for (JComponent c : new JComponent[]{blocFormat, listeCartes, boutonAjouter}) {
            c.setAlignmentX(LEFT_ALIGNMENT);
        }
        contenu.add(blocFormat);
        contenu.add(Box.createVerticalStrut(16));
        contenu.add(listeCartes);
        contenu.add(Box.createVerticalStrut(12));
        contenu.add(boutonAjouter);
        contenu.add(Box.createVerticalGlue());

        JScrollPane defilement = new JScrollPane(contenu);
        defilement.setBorder(null);
        defilement.getViewport().setBackground(FOND);
        defilement.getVerticalScrollBar().setUnitIncrement(16);

        add(barre, BorderLayout.NORTH);
        add(defilement, BorderLayout.CENTER);

        boutonRetour.setActionCommand(ACTION_RETOUR_LISTE);
        boutonImportExport.setActionCommand(ACTION_IMPORT_EXPORT);
        boutonValider.setActionCommand(ACTION_VALIDER);
        boutonAjouter.setActionCommand(ACTION_AJOUTER_YOKAI);
    }



    private static StyledButton boutonBleu(String texte, int largeur) {
        StyledButton b = new StyledButton(texte, 0x89AAD4, 0x4D7FB8, 0x3B628E);
        b.setFont(VERDANA_GRAS);
        b.setPreferredSize(new Dimension(largeur, 30));
        return b;
    }

    /** Ajoute une carte vide (affichage seulement : c'est le modele qui decidera plus tard). */
    public void ajouterCarteYokai() {
        if (cartes.size() >= MAX_YOKAI) {
            return;
        }
        CarteYokai carte = new CarteYokai(cartes.size());
        carte.setAlignmentX(LEFT_ALIGNMENT);
        cartes.add(carte);
        listeCartes.add(carte);
        listeCartes.add(Box.createVerticalStrut(10));
        if (ecouteur != null) {
            carte.setEcouteur(ecouteur);
        }
        boutonAjouter.setEnabled(cartes.size() < MAX_YOKAI);
        listeCartes.revalidate();
        listeCartes.repaint();
    }

    public void setEcouteur(ActionListener ecouteur) {
        this.ecouteur = ecouteur;
        boutonRetour.addActionListener(ecouteur);
        boutonImportExport.addActionListener(ecouteur);
        boutonValider.addActionListener(ecouteur);
        boutonAjouter.addActionListener(ecouteur);
        for (CarteYokai c : cartes) {
            c.setEcouteur(ecouteur);
        }
    }

    public String getNomEquipe() {
        return champNomEquipe.getText();
    }

    public String getFormat() {
        return (String) choixFormat.getSelectedItem();
    }



    static class CarteYokai extends JPanel {

        private final JButton lienCopier = lien("⎘ Copy");
        private final JButton lienDeplacer = lien("✥ Move");
        private final JButton lienSupprimer = lien("🗑 Delete");

        CarteYokai(int position) {
            setLayout(new BorderLayout());
            setOpaque(false);
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 185));

            // --- Ligne du haut : surnom a gauche, actions a droite ---
            JTextField champSurnom = new JTextField(14);
            champSurnom.setFont(VERDANA);
            JPanel blocSurnom = colonne("Nickname", champSurnom);

            JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            actions.setOpaque(false);
            actions.add(lienCopier);
            actions.add(lienDeplacer);
            actions.add(lienSupprimer);

            JPanel haut = new JPanel(new BorderLayout());
            haut.setOpaque(false);
            haut.add(blocSurnom, BorderLayout.WEST);
            haut.add(actions, BorderLayout.SOUTH);

            // --- Corps de la carte ---
            JPanel corps = new JPanel(new GridBagLayout());
            corps.setBackground(FOND_CARTE);
            corps.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(BORDURE, 1, true), new EmptyBorder(6, 8, 8, 8)));

            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(2, 4, 2, 4);
            c.fill = GridBagConstraints.BOTH;
            c.anchor = GridBagConstraints.NORTHWEST;

            // Sprite (emplacement)
            JLabel sprite = new JLabel("?", SwingConstants.CENTER);
            sprite.setFont(new Font("Verdana", Font.BOLD, 28));
            sprite.setForeground(BORDURE);
            sprite.setOpaque(true);
            sprite.setBackground(Color.WHITE);
            sprite.setBorder(new LineBorder(BORDURE));
            sprite.setPreferredSize(new Dimension(96, 72));
            sprite.setMinimumSize(new Dimension(96, 72));
            c.gridx = 0; c.gridy = 0; c.gridheight = 1; c.weightx = 0;
            corps.add(sprite, c);

            // Details
            c.gridx = 1; c.gridy = 0; c.weightx = 0.5;
            c.anchor = GridBagConstraints.NORTH;
            c.fill = GridBagConstraints.HORIZONTAL;
            corps.add(details(), c);
            c.anchor = GridBagConstraints.NORTHWEST;
            c.fill = GridBagConstraints.BOTH;


            c.gridx = 2; c.gridy = 0; c.gridheight = 2; c.weightx = 0.6;
            corps.add(actionsYokai(), c);

            // Stats
            c.gridx = 3; c.gridy = 0; c.gridheight = 2; c.weightx = 0.6;
            corps.add(stats(), c);

            // Ligne du bas : Yo-kai, Equipement, Attitude
            JPanel champs = new JPanel(new GridLayout(1, 3, 6, 0));
            champs.setOpaque(false);
            champs.add(colonne("Yo-kai", new JTextField()));
            champs.add(colonne("Equipment", new JTextField()));
            champs.add(colonne("Attitude", new JTextField()));
            c.gridx = 0; c.gridy = 1; c.gridwidth = 2; c.gridheight = 1; c.weightx = 1;
            corps.add(champs, c);

            add(haut, BorderLayout.NORTH);
            add(corps, BorderLayout.CENTER);

            lienCopier.setActionCommand(ACTION_COPIER + position);
            lienDeplacer.setActionCommand(ACTION_DEPLACER + position);
            lienSupprimer.setActionCommand(ACTION_SUPPRIMER + position);
        }

        void setEcouteur(ActionListener ecouteur) {
            lienCopier.addActionListener(ecouteur);
            lienDeplacer.addActionListener(ecouteur);
            lienSupprimer.addActionListener(ecouteur);
        }

        private static JPanel details() {
            JPanel tableau = new JPanel(new GridLayout(2, 4));
            tableau.setBackground(Color.WHITE);
            tableau.setBorder(new LineBorder(BORDURE));
            String[][] cases = {{"Level", "60"}, {"Rank", "-"}, {"Tribe", "-"}, {"Tier", "-"}};
            for (String[] cc : cases) {
                tableau.add(petitTitre(cc[0], SwingConstants.CENTER));
            }
            for (String[] cc : cases) {
                JLabel v = new JLabel(cc[1], SwingConstants.CENTER);
                v.setFont(VERDANA);
                tableau.add(v);
            }
            return colonne("Details", tableau);
        }

        private static JPanel actionsYokai() {
            JPanel liste = new JPanel(new GridLayout(4, 1, 0, 4));
            liste.setOpaque(false);
            for (String type : new String[]{"Attack", "Technique", "Inspirit", "Soultimate"}) {
                JLabel l = new JLabel(" " + type + " : -");
                l.setFont(VERDANA);
                l.setOpaque(true);
                l.setBackground(Color.WHITE);
                l.setBorder(new LineBorder(BORDURE));
                liste.add(l);
            }
            return colonne("Actions", liste);
        }

        private static JPanel stats() {
            JPanel liste = new JPanel(new GridLayout(5, 1, 0, 2));
            liste.setBackground(Color.WHITE);
            liste.setBorder(BorderFactory.createCompoundBorder(new LineBorder(BORDURE), new EmptyBorder(4, 6, 4, 6)));
            String[] noms = {"HP", "Str", "Spr", "Def", "Spd"};
            int[] valeurs = {0, 0, 0, 0, 0};
            for (int i = 0; i < noms.length; i++) {
                liste.add(new BarreStat(noms[i], valeurs[i]));
            }
            return colonne("Stats", liste);
        }


        private static JPanel colonne(String titre, JComponent contenu) {
            JPanel p = new JPanel(new BorderLayout(0, 2));
            p.setOpaque(false);
            p.add(petitTitre(titre, SwingConstants.LEFT), BorderLayout.NORTH);
            p.add(contenu, BorderLayout.CENTER);
            return p;
        }

        private static JLabel petitTitre(String texte, int alignement) {
            JLabel l = new JLabel(texte, alignement);
            l.setFont(VERDANA_GRAS);
            l.setForeground(TEXTE_DISCRET);
            return l;
        }

        private static JButton lien(String texte) {
            JButton b = new JButton(texte);
            b.setFont(VERDANA);
            b.setForeground(Color.BLACK);
            b.setContentAreaFilled(false);
            b.setBorderPainted(false);
            b.setFocusPainted(false);
            b.setMargin(new Insets(0, 0, 0, 0));
            b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            return b;
        }
    }



    static class BarreStat extends JComponent {
        private static final int STAT_MAX = 400;
        private final String nom;
        private int valeur;

        BarreStat(String nom, int valeur) {
            this.nom = nom;
            this.valeur = valeur;
            setPreferredSize(new Dimension(160, 14));
        }

        void setValeur(int valeur) {
            this.valeur = valeur;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setFont(new Font("Verdana", Font.PLAIN, 10));
            g2.setColor(TEXTE_DISCRET);
            g2.drawString(nom, 0, getHeight() - 3);

            int x = 34;
            int largeurMax = getWidth() - x - 4;
            int largeur = Math.max(2, (int) (largeurMax * Math.min(valeur, STAT_MAX) / (double) STAT_MAX));
            // rouge -> jaune -> vert selon la valeur, comme sur Showdown
            float teinte = Math.min(valeur, STAT_MAX) / (float) STAT_MAX * 0.33f;
            g2.setColor(Color.getHSBColor(teinte, 0.45f, 0.85f));
            g2.fillRoundRect(x, 3, largeur, getHeight() - 6, 4, 4);
            g2.dispose();
        }
    }
}
