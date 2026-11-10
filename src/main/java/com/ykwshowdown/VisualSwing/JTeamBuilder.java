package com.ykwshowdown.VisualSwing;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.ykwshowdown.VisualSwing.StylizedButton.StyledButton;
import com.ykwshowdown.menu.WatchBuilder;
import com.ykwshowdown.menu.WatchList;
import com.ykwshowdown.yokai.YokaiGeneral;

/** TODO rename */
public class JTeamBuilder extends JPanel {

    public static final String ACTION_CREE_EQUIPE = "CREATE_TEAM";
    public static final String ACTION_EDIT_TEAM = "TEAM_EDIT_";       // TODO rename
    public static final String ACTION_COPY_TEAM = "TEAM_COPY_";
    public static final String ACTION_DELETE_TEAM = "TEAM_DELETE_";

    private static final Color BACKGROUND = new Color(240, 235, 245);
    private static final Color ROW_BACKGROUND = new Color(245, 245, 245);
    private static final Color BORDER_COLOR = new Color(0xC9C2D6);
    private static final Color MUTED_TEXT = new Color(0x777777);
    private static final Font VERDANA = new Font("Verdana", Font.PLAIN, 12);
    private static final Font VERDANA_BOLD = new Font("Verdana", Font.BOLD, 12);
    private static final int MEDAL = 44;   // TODO rename

    private final JLabel lblAllTeams = new JLabel("All teams (0)");
    private final StyledButton btnNewTeam = new StyledButton("+ New Team", 0xC987B8, 0xA84D96, 0x823B74);
    private final JTextField txtSearch = new JTextField(15);
    private final JPanel teamsListPanel = new JPanel();

    private ActionListener ecouteur;
    private WatchList teams;   // TODO rename

    // ===== TODO rename =====

    public JTeamBuilder() {
        setBackground(BACKGROUND);
        setLayout(new BorderLayout());

        lblAllTeams.setFont(new Font("Verdana", Font.BOLD, 20));

        btnNewTeam.setFont(VERDANA_BOLD);
        btnNewTeam.setPreferredSize(new Dimension(140, 32));
        txtSearch.setFont(VERDANA);
        txtSearch.setToolTipText("Search a team or a Yo-kai");
        JLabel lblSearch = new JLabel("Search :");
        lblSearch.setFont(VERDANA_BOLD);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actionPanel.setOpaque(false);
        actionPanel.add(btnNewTeam);
        actionPanel.add(lblSearch);
        actionPanel.add(txtSearch);

        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setOpaque(false);
        top.setBorder(new EmptyBorder(20, 20, 10, 20));
        lblAllTeams.setAlignmentX(LEFT_ALIGNMENT);
        actionPanel.setAlignmentX(LEFT_ALIGNMENT);
        top.add(lblAllTeams);
        top.add(Box.createVerticalStrut(10));
        top.add(actionPanel);

        teamsListPanel.setLayout(new BoxLayout(teamsListPanel, BoxLayout.Y_AXIS));
        teamsListPanel.setOpaque(false);
        teamsListPanel.setBorder(new EmptyBorder(0, 20, 20, 20));
        JPanel holder = new JPanel(new BorderLayout());   // TODO rename
        holder.setOpaque(false);
        holder.add(teamsListPanel, BorderLayout.NORTH);
        JScrollPane scroll = new JScrollPane(holder);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BACKGROUND);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        add(top, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        btnNewTeam.setActionCommand(ACTION_CREE_EQUIPE);

        // TODO rename
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { redraw(); }
            public void removeUpdate(DocumentEvent e) { redraw(); }
            public void changedUpdate(DocumentEvent e) { }
        });

        redraw();
    }

    // ===== TODO rename =====

    public void setEcouteur(ActionListener ecouteur) {
        this.ecouteur = ecouteur;
        btnNewTeam.addActionListener(ecouteur);
    }

    // ===== TODO rename =====

    /** TODO rename */
    public void showTeams(WatchList teams) {
        this.teams = teams;
        redraw();
    }

    public boolean confirmDelete(String teamName) {
        return JOptionPane.showConfirmDialog(this, "Delete the team \"" + teamName + "\" ?", "Delete",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
    }

    public void showMessage(String text) {
        JOptionPane.showMessageDialog(this, text);
    }

    private void redraw() {
        teamsListPanel.removeAll();
        int count = teams == null ? 0 : teams.getTeamCount();
        lblAllTeams.setText("All teams (" + count + ")");

        String search = txtSearch.getText().trim().toLowerCase();
        int shown = 0;
        for (int i = 0; i < count; i++) {
            WatchBuilder team = teams.getTeam(i);
            if (!search.isEmpty() && !matches(team, search)) {
                continue;
            }
            teamsListPanel.add(createTeamRow(team, i));
            teamsListPanel.add(Box.createVerticalStrut(8));
            shown++;
        }
        if (shown == 0) {
            JLabel empty = new JLabel(count == 0 ? "No team yet: click \"+ New Team\"." : "No team matches the search.");
            empty.setFont(VERDANA);
            empty.setForeground(MUTED_TEXT);
            empty.setAlignmentX(LEFT_ALIGNMENT);
            teamsListPanel.add(empty);
        }
        teamsListPanel.revalidate();
        teamsListPanel.repaint();
    }

    /** TODO rename */
    private static boolean matches(WatchBuilder team, String search) {
        if (team.GetTeamName().toLowerCase().contains(search)) {
            return true;
        }
        for (int i = 0; i < 6; i++) {
            YokaiGeneral yg = team.getYokaiIndex(i);
            if (yg != null && yg.GetYokai() != null && yg.GetName().toLowerCase().contains(search)) {
                return true;
            }
        }
        return false;
    }

    private JPanel createTeamRow(WatchBuilder team, int index) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setBackground(ROW_BACKGROUND);
        row.setBorder(BorderFactory.createCompoundBorder(new LineBorder(BORDER_COLOR, 1, true), new EmptyBorder(6, 10, 6, 10)));
        row.setAlignmentX(LEFT_ALIGNMENT);

        // TODO rename
        String format = team.getFormat() == null ? "" : "[YKW2 " + team.getFormat().getTier() + "] ";
        JLabel lblName = new JLabel(format + team.GetTeamName());
        lblName.setFont(VERDANA_BOLD);

        JPanel medals = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        medals.setOpaque(false);
        for (int i = 0; i < 6; i++) {
            YokaiGeneral yg = team.getYokaiIndex(i);
            medals.add(medalLabel(yg));
        }

        JPanel infoPanel = new JPanel(new BorderLayout(0, 4));
        infoPanel.setOpaque(false);
        infoPanel.add(lblName, BorderLayout.NORTH);
        infoPanel.add(medals, BorderLayout.CENTER);

        // TODO rename
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        actionPanel.setOpaque(false);
        actionPanel.add(rowButton("Edit", ACTION_EDIT_TEAM + index, Color.BLACK));
        actionPanel.add(rowButton("Copy", ACTION_COPY_TEAM + index, Color.BLACK));
        actionPanel.add(rowButton("Delete", ACTION_DELETE_TEAM + index, new Color(0xC0392B)));
        JPanel actionHolder = new JPanel(new GridBagLayout());   // TODO rename
        actionHolder.setOpaque(false);
        actionHolder.add(actionPanel);

        row.add(infoPanel, BorderLayout.CENTER);
        row.add(actionHolder, BorderLayout.EAST);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, row.getPreferredSize().height));
        return row;
    }

    /** TODO rename */
    private static JLabel medalLabel(YokaiGeneral yg) {
        boolean empty = yg == null || yg.GetYokai() == null;
        Icon icon = MedalSprites.icon(empty ? null : yg.GetYokai(), MEDAL);
        JLabel l = icon != null ? new JLabel(icon) : new JLabel(empty ? "-" : yg.GetName());
        l.setFont(new Font("Verdana", Font.PLAIN, 10));
        l.setPreferredSize(new Dimension(Math.max(MEDAL, l.getPreferredSize().width), MEDAL));
        if (!empty) {
            String nick = yg.GetNickname().isEmpty() ? "" : " \"" + yg.GetNickname() + "\"";
            l.setToolTipText(yg.GetName() + nick + "  Lv " + yg.GetLevel());
        }
        return l;
    }

    private JButton rowButton(String text, String command, Color color) {
        JButton b = new JButton(text);
        b.setFont(new Font("Verdana", Font.PLAIN, 11));
        b.setForeground(color);
        b.setFocusPainted(false);
        b.setActionCommand(command);
        if (ecouteur != null) {
            b.addActionListener(ecouteur);
        }
        return b;
    }
}
