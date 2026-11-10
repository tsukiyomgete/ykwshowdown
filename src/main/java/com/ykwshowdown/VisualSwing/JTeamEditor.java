package com.ykwshowdown.VisualSwing;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.ykwshowdown.VisualSwing.StylizedButton.StyledButton;
import com.ykwshowdown.database.DataBase;
import com.ykwshowdown.menu.WatchBuilder;
import com.ykwshowdown.yokai.Attitude;
import com.ykwshowdown.yokai.Equipement;
import com.ykwshowdown.yokai.IV;
import com.ykwshowdown.yokai.Stat;
import com.ykwshowdown.yokai.Tier;
import com.ykwshowdown.yokai.Yokai;
import com.ykwshowdown.yokai.YokaiGeneral;
import com.ykwshowdown.yokai.face_icon;

/** TODO rename */
public class JTeamEditor extends JPanel {

    public static final String ACTION_BACK_TO_LIST = "EDITOR_BACK";
    public static final String ACTION_IMPORT_EXPORT = "EDITOR_IMPORT_EXPORT";
    public static final String ACTION_VALIDATE = "EDITOR_VALIDATE";
    public static final String ACTION_SELECT_SLOT = "EDITOR_SLOT_";     // TODO rename
    public static final String ACTION_SELECT_YOKAI = "EDITOR_YOKAI";    // TODO rename
    public static final String ACTION_SELECT_EQUIPMENT = "EDITOR_EQUIPMENT";
    public static final String ACTION_SELECT_ATTITUDE = "EDITOR_ATTITUDE";
    public static final String ACTION_NICKNAME = "EDITOR_NICKNAME";
    public static final String ACTION_IV_CHANGED = "EDITOR_IV_CHANGED";
    public static final String ACTION_IV_RANDOM = "EDITOR_IV_RANDOM";
    public static final String ACTION_IV_RESET = "EDITOR_IV_RESET";
    public static final String ACTION_REMOVE_YOKAI = "EDITOR_REMOVE";
    public static final String ACTION_CLOSE_SHEET = "EDITOR_CLOSE";

    public static final int MAX_YOKAI = 6;
    private static final Tier[] FORMATS = {Tier.OU, Tier.UU, Tier.NU, Tier.Ubers};

    static final Color BACKGROUND = new Color(240, 235, 245);
    static final Color CARD_BACKGROUND = new Color(245, 245, 245);
    static final Color BORDER_COLOR = new Color(0xC9C2D6);
    static final Color MUTED_TEXT = new Color(0x777777);
    static final Color GOOD = new Color(0x2E8B57);
    static final Color BAD = new Color(0xC0392B);
    static final Font VERDANA = new Font("Verdana", Font.PLAIN, 12);
    static final Font VERDANA_BOLD = new Font("Verdana", Font.BOLD, 11);

    private final JTextField teamNameField = new JTextField("Untitled 1", 24);
    private final JComboBox<String> formatChoice = new JComboBox<>();
    private final StyledButton backButton = blueButton("← List", 90);
    private final StyledButton importExportButton = blueButton("Import/Export", 130);
    private final StyledButton validateButton = blueButton("✔ Validate", 110);
    private final JLabel validationLabel = new JLabel(" ");
    private final TeamWheel wheel = new TeamWheel();
    private final YokaiSheet sheet = new YokaiSheet();

    private WatchBuilder team;   // TODO rename

    // ===== TODO rename =====

    public JTeamEditor() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND);
        teamNameField.setFont(VERDANA);
        for (Tier t : FORMATS) {
            formatChoice.addItem("[YKW2] " + t.getTier());
        }

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        topBar.setOpaque(false);
        topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));
        topBar.add(backButton);
        topBar.add(teamNameField);
        topBar.add(importExportButton);

        JLabel formatLabel = new JLabel("Format :");
        formatLabel.setFont(VERDANA_BOLD);
        formatChoice.setFont(VERDANA);
        validationLabel.setFont(VERDANA_BOLD);
        JPanel formatRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        formatRow.setOpaque(false);
        formatRow.add(formatLabel);
        formatRow.add(formatChoice);
        formatRow.add(validateButton);
        formatRow.add(validationLabel);
        formatRow.setBorder(new EmptyBorder(14, 12, 0, 0));

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.add(formatRow, BorderLayout.NORTH);
        center.add(wheel, BorderLayout.CENTER);

        sheet.setVisible(false);

        add(topBar, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(sheet, BorderLayout.EAST);

        backButton.setActionCommand(ACTION_BACK_TO_LIST);
        importExportButton.setActionCommand(ACTION_IMPORT_EXPORT);
        validateButton.setActionCommand(ACTION_VALIDATE);
    }

    private static StyledButton blueButton(String text, int width) {
        StyledButton b = new StyledButton(text, 0x89AAD4, 0x4D7FB8, 0x3B628E);
        b.setFont(VERDANA_BOLD);
        b.setPreferredSize(new Dimension(width, 30));
        return b;
    }

    // ===== TODO rename =====

    public void setListener(ActionListener listener) {
        backButton.addActionListener(listener);
        importExportButton.addActionListener(listener);
        validateButton.addActionListener(listener);
        wheel.setListener(listener);
        sheet.setListener(listener);
    }

    // ===== TODO rename =====

    /** TODO rename */
    public void showTeam(WatchBuilder team) {
        this.team = team;
        teamNameField.setText(team.GetTeamName());
        Tier format = team.getFormat() == null ? Tier.OU : team.getFormat();
        for (int i = 0; i < FORMATS.length; i++) {
            if (FORMATS[i] == format) {
                formatChoice.setSelectedIndex(i);
            }
        }
        validationLabel.setText(" ");
        closeSheet();
        refresh();
    }

    /** TODO rename */
    public void refresh() {
        if (team == null) {
            return;
        }
        for (int i = 0; i < MAX_YOKAI; i++) {
            YokaiGeneral yg = team.getYokaiIndex(i);
            wheel.setYokai(i, yg == null ? null : yg.GetYokai());
        }
        int selection = wheel.getSelection();
        if (selection >= 0) {
            sheet.display(selection, team.getYokaiIndex(selection));
        }
    }

    // ===== TODO rename =====

    public void openSheet(int position) {
        wheel.setSelection(position);
        sheet.display(position, team == null ? null : team.getYokaiIndex(position));
        sheet.setVisible(true);
        revalidate();
        repaint();
    }

    public void closeSheet() {
        wheel.setSelection(-1);
        sheet.setVisible(false);
        revalidate();
        repaint();
    }

    public int getSelectedSlot() {
        return wheel.getSelection();
    }

    // ===== TODO rename =====

    /** TODO rename */
    public void fillLists() {
        sheet.fillYokaiNameBox();
        sheet.fillEquipmentBox();
        sheet.fillAttitudeBox();
    }

    public String getTypedYokaiName() {
        return sheet.getYokaiBoxName();
    }

    public String getChosenEquipment() {
        return sheet.getEquipmentBoxName();
    }

    public String getChosenAttitude() {
        return sheet.getAttitudeBoxName();
    }

    public String getNickname() {
        return sheet.getNickname();
    }

    /** TODO rename */
    public int[] getIvValues() {
        return sheet.getIvValues();
    }

    // ===== TODO rename =====

    public void setSheetMessage(String message) {
        sheet.setMessage(message);
    }

    /** TODO rename */
    public void showValidation(String problems) {
        if (problems == null) {
            validationLabel.setForeground(GOOD);
            validationLabel.setText("Team valid for " + getFormat());
        } else {
            validationLabel.setForeground(BAD);
            validationLabel.setText("Team not valid");
            JOptionPane.showMessageDialog(this, problems, "Validate", JOptionPane.WARNING_MESSAGE);
        }
    }

    /** TODO rename */
    public String askImportExport(String exportText) {
        JTextArea area = new JTextArea(exportText, 22, 48);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(area);
        Object[] options = {"Import", "Close"};
        int choice = JOptionPane.showOptionDialog(this, scroll, "Import / Export (copy or paste a team)",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[1]);
        return choice == 0 ? area.getText() : null;
    }

    public void showError(String text) {
        JOptionPane.showMessageDialog(this, text, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // ===== TODO rename =====

    public String getTeamName() {
        return teamNameField.getText();
    }

    public String getFormat() {
        return (String) formatChoice.getSelectedItem();
    }

    public Tier getFormatTier() {
        return FORMATS[Math.max(0, formatChoice.getSelectedIndex())];
    }


    /** TODO rename */
    static class TeamWheel extends JComponent {

        private static final int[] ANGLES = {150, 90, 30, 210, 270, 330};
        private final Yokai[] yokais = new Yokai[MAX_YOKAI];
        private int selection = -1;
        private int hover = -1;
        private ActionListener listener;

        // ===== TODO rename =====

        TeamWheel() {
            setPreferredSize(new Dimension(460, 460));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            MouseAdapter mouse = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int slot = slotAt(e.getPoint());
                    if (slot >= 0 && listener != null) {
                        listener.actionPerformed(new ActionEvent(TeamWheel.this,
                                ActionEvent.ACTION_PERFORMED, ACTION_SELECT_SLOT + slot));
                    }
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    int slot = slotAt(e.getPoint());
                    if (slot != hover) {
                        hover = slot;
                        repaint();
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hover = -1;
                    repaint();
                }
            };
            addMouseListener(mouse);
            addMouseMotionListener(mouse);
        }

        // ===== TODO rename =====

        void setListener(ActionListener listener) {
            this.listener = listener;
        }

        // ===== TODO rename =====

        void setYokai(int position, Yokai yokai) {
            yokais[position] = yokai;
            repaint();
        }

        Yokai getYokai(int position) {
            return yokais[position];
        }

        void setSelection(int position) {
            selection = position;
            repaint();
        }

        int getSelection() {
            return selection;
        }

        // ===== TODO rename =====

        private double radius() {
            return Math.min(getWidth(), getHeight()) / 2.0 - 20;
        }

        /** TODO rename */
        private int medalSize() {
            int scale = Math.max(1, (int) Math.round(2 * radius() * 0.22 / face_icon.CELL));
            return face_icon.CELL * scale;
        }

        private Point slotCenter(int i) {
            double r = radius() * 0.66;
            double a = Math.toRadians(ANGLES[i]);
            return new Point((int) (getWidth() / 2.0 + r * Math.cos(a)), (int) (getHeight() / 2.0 - r * Math.sin(a)));
        }

        private int slotAt(Point p) {
            double medalRadius = medalSize() / 2.0;
            for (int i = 0; i < MAX_YOKAI; i++) {
                if (p.distance(slotCenter(i)) <= medalRadius) {
                    return i;
                }
            }
            return -1;
        }

        // ===== TODO rename =====

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            double R = radius();
            double cx = getWidth() / 2.0;
            double cy = getHeight() / 2.0;

            // TODO rename
            g2.setColor(Color.WHITE);
            g2.fill(new Ellipse2D.Double(cx - R - 10, cy - R - 10, 2 * R + 20, 2 * R + 20));
            g2.setColor(BORDER_COLOR);
            g2.setStroke(new BasicStroke(2f));
            g2.draw(new Ellipse2D.Double(cx - R - 10, cy - R - 10, 2 * R + 20, 2 * R + 20));

            // TODO rename
            for (int i = 0; i < MAX_YOKAI; i++) {
                boolean front = i < 3;
                Color top = new Color(front ? 0xC987B8 : 0x89AAD4);
                Color bottom = new Color(front ? 0xA84D96 : 0x4D7FB8);
                Arc2D slice = new Arc2D.Double(cx - R, cy - R, 2 * R, 2 * R, ANGLES[i] - 30, 60, Arc2D.PIE);
                g2.setPaint(new GradientPaint(0, (float) (cy - R), top, 0, (float) (cy + R), bottom));
                g2.fill(slice);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(3f));
                g2.draw(slice);
            }
            g2.setColor(new Color(0x823B74));
            g2.setStroke(new BasicStroke(2f));
            g2.draw(new Ellipse2D.Double(cx - R, cy - R, 2 * R, 2 * R));

            // TODO rename
            double rc = R * 0.27;
            g2.setColor(Color.WHITE);
            g2.fill(new Ellipse2D.Double(cx - rc - 4, cy - rc - 4, 2 * rc + 8, 2 * rc + 8));
            g2.setColor(new Color(0xA84D96));
            g2.fill(new Arc2D.Double(cx - rc, cy - rc, 2 * rc, 2 * rc, 0, 180, Arc2D.PIE));
            g2.setColor(new Color(0x4D7FB8));
            g2.fill(new Arc2D.Double(cx - rc, cy - rc, 2 * rc, 2 * rc, 180, 180, Arc2D.PIE));
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Verdana", Font.BOLD, Math.max(10, (int) (rc / 3.2))));
            drawCentered(g2, "FRONT", cx, cy - rc * 0.38);
            drawCentered(g2, "BACK", cx, cy + rc * 0.38);
            g2.fillRect((int) (cx - rc), (int) cy - 1, (int) (2 * rc), 2);

            // TODO rename
            int size = medalSize();
            double r = size / 2.0;
            for (int i = 0; i < MAX_YOKAI; i++) {
                Point c = slotCenter(i);
                int x = c.x - size / 2;
                int y = c.y - size / 2;

                Ellipse2D medal = new Ellipse2D.Double(c.x - r, c.y - r, size, size);
                g2.setColor(i == hover ? new Color(0xFFF6D6) : Color.WHITE);
                g2.fill(medal);

                // TODO rename
                face_icon face = MedalSprites.faceOf(yokais[i]);
                if (MedalSprites.isLoaded() && face != null) {
                    MedalSprites.draw(g2, face, x, y, size);
                } else if (yokais[i] == null) {
                    // TODO rename
                    g2.setColor(BORDER_COLOR);
                    g2.setFont(new Font("Verdana", Font.BOLD, (int) (r * 0.9)));
                    drawCentered(g2, "+", c.x, c.y);
                } else {
                    // TODO rename
                    g2.setColor(new Color(0x333333));
                    g2.setFont(new Font("Verdana", Font.BOLD, Math.max(9, (int) (r / 4.2))));
                    drawCentered(g2, shorten(yokais[i].GetName(), 11), c.x, c.y - r * 0.12);
                    g2.setColor(MUTED_TEXT);
                    g2.setFont(new Font("Verdana", Font.PLAIN, Math.max(8, (int) (r / 5))));
                    drawCentered(g2, "Rank " + yokais[i].GetRank(), c.x, c.y + r * 0.3);
                }

                // TODO rename
                if (i == selection) {
                    g2.setColor(new Color(0xF2B33D));
                    g2.setStroke(new BasicStroke(5f));
                } else {
                    g2.setColor(new Color(i < 3 ? 0x823B74 : 0x3B628E));
                    g2.setStroke(new BasicStroke(2.5f));
                }
                g2.draw(medal);
            }
            g2.dispose();
        }

        private static String shorten(String s, int max) {
            return s.length() <= max ? s : s.substring(0, max - 1) + ".";
        }

        private static void drawCentered(Graphics2D g2, String text, double x, double y) {
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(text, (float) (x - fm.stringWidth(text) / 2.0), (float) (y + fm.getAscent() / 2.0 - 2));
        }
    }


    /** TODO rename */
    private static class YokaiSheet extends JPanel {

        private static final String[] STAT_NAMES = {"HP", "Str", "Spr", "Def", "Spd"};
        private static final String[] ELEMENT_NAMES = {"Fire", "Ice", "Earth", "Lightning", "Water", "Wind"};

        private final JLabel title = new JLabel();
        private final JButton closeButton = new JButton("X");
        private final SearchBox yokaiNameBox = new SearchBox(ACTION_SELECT_YOKAI);
        private final SearchBox equipmentBox = new SearchBox(ACTION_SELECT_EQUIPMENT);
        private final SearchBox attitudeBox = new SearchBox(ACTION_SELECT_ATTITUDE);

        private final JTextField nicknameField = new JTextField();
        private final JLabel message = new JLabel(" ");
        private final JLabel[] details = new JLabel[4];
        private final JLabel[] baseValues = new JLabel[5];    // TODO rename
        private final JLabel[] finalValues = new JLabel[5];   // TODO rename
        private final StatBar[] bars = new StatBar[5];
        private final JSpinner[] ivSpinners = new JSpinner[5];
        private final JLabel ivPointsLabel = new JLabel(" ");
        private final JButton ivRandomButton = smallButton("Random");
        private final JButton ivResetButton = smallButton("Reset");
        private final JLabel gearInfo = new JLabel(" ");
        private final JLabel[] resistances = new JLabel[6];
        private final JLabel[] actions = new JLabel[4];
        private final JLabel[] chances = new JLabel[4];
        private final StyledButton removeButton = new StyledButton("Remove", 0xC987B8, 0xA84D96, 0x823B74);

        private ActionListener listener;
        private boolean updating = false;   // TODO rename

        // ===== TODO rename =====

        YokaiSheet() {
            setLayout(new BorderLayout());
            setBackground(CARD_BACKGROUND);
            setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, BORDER_COLOR));
            setPreferredSize(new Dimension(370, 0));

            // TODO rename
            title.setFont(new Font("Verdana", Font.BOLD, 13));
            closeButton.setFont(VERDANA_BOLD);
            closeButton.setFocusPainted(false);
            closeButton.setContentAreaFilled(false);
            closeButton.setBorderPainted(false);
            closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            JPanel header = new JPanel(new BorderLayout());
            header.setOpaque(false);
            header.setBorder(new EmptyBorder(8, 12, 4, 6));
            header.add(title, BorderLayout.WEST);
            header.add(closeButton, BorderLayout.EAST);

            // TODO rename
            JPanel content = new JPanel();
            content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
            content.setOpaque(false);
            content.setBorder(new EmptyBorder(0, 12, 12, 12));

            nicknameField.setFont(VERDANA);
            nicknameField.setToolTipText("Enter to save (" + YokaiGeneral.MAX_NICKNAME + " characters max)");
            message.setFont(VERDANA);
            message.setForeground(new Color(0xA84D96));
            gearInfo.setFont(new Font("Verdana", Font.PLAIN, 10));
            gearInfo.setForeground(MUTED_TEXT);
            JPanel inputRow = new JPanel(new GridLayout(1, 2, 8, 0));
            inputRow.setOpaque(false);
            inputRow.add(column("Yo-kai", yokaiNameBox));
            inputRow.add(column("Nickname", nicknameField));

            JPanel gearRow = new JPanel(new GridLayout(1, 2, 8, 0));
            gearRow.setOpaque(false);
            gearRow.add(column("Equipment", equipmentBox));
            gearRow.add(column("Attitude", attitudeBox));
            JPanel gear = new JPanel(new BorderLayout(0, 2));
            gear.setOpaque(false);
            gear.add(gearRow, BorderLayout.CENTER);
            gear.add(gearInfo, BorderLayout.SOUTH);

            content.add(block(inputRow));
            content.add(block(message));
            content.add(block(column("Details", detailsTable())));
            content.add(block(column("Stats (without bonus / final)", statsTable())));
            content.add(block(column("IV  (HP/2 + Str + Spr + Def + Spd ≤ " + IV.MAXIVPOINTS + ")", ivTable())));
            content.add(block(gear));
            content.add(block(column("Resistances", resistancesTable())));
            content.add(block(column("Actions", list(actions))));
            content.add(block(column("Behaviour", list(chances))));
            removeButton.setFont(VERDANA_BOLD);
            removeButton.setPreferredSize(new Dimension(110, 30));
            removeButton.setMaximumSize(new Dimension(110, 30));
            content.add(block(removeButton));
            content.add(Box.createVerticalGlue());

            JScrollPane scroll = new JScrollPane(content);
            scroll.setBorder(null);
            scroll.getViewport().setBackground(CARD_BACKGROUND);
            scroll.getVerticalScrollBar().setUnitIncrement(16);

            add(header, BorderLayout.NORTH);
            add(scroll, BorderLayout.CENTER);

            closeButton.setActionCommand(ACTION_CLOSE_SHEET);
            removeButton.setActionCommand(ACTION_REMOVE_YOKAI);
            nicknameField.setActionCommand(ACTION_NICKNAME);
            ivRandomButton.setActionCommand(ACTION_IV_RANDOM);
            ivResetButton.setActionCommand(ACTION_IV_RESET);
        }

        // ===== TODO rename =====

        void setListener(ActionListener listener) {
            this.listener = listener;
            closeButton.addActionListener(listener);
            yokaiNameBox.setListener(listener);
            equipmentBox.setListener(listener);
            attitudeBox.setListener(listener);
            nicknameField.addActionListener(listener);
            ivRandomButton.addActionListener(listener);
            ivResetButton.addActionListener(listener);
            removeButton.addActionListener(listener);
            // TODO rename
            nicknameField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    fire(nicknameField, ACTION_NICKNAME);
                }
            });
        }

        /** TODO rename */
        private void fire(Object source, String command) {
            if (!updating && listener != null) {
                listener.actionPerformed(new ActionEvent(source, ActionEvent.ACTION_PERFORMED, command));
            }
        }

        // ===== TODO rename =====

        void fillYokaiNameBox() {
            List<String> names = new ArrayList<>();
            for (Yokai y : DataBase.getAllYokai()) {
                names.add(y.GetName());
            }
            yokaiNameBox.setNames(names);
        }

        public void fillNameYokaiNameBox() {
            fillYokaiNameBox();
        }

        void fillEquipmentBox() {
            List<String> names = new ArrayList<>();
            for (DataBase.RawEquipment e : DataBase.getAllEquipment()) {
                names.add(e.getName());
            }
            equipmentBox.setNames(names);
        }

        void fillAttitudeBox() {
            List<String> names = new ArrayList<>();
            for (DataBase.RawAttitude a : DataBase.getAllAttitude()) {
                names.add(a.getName());
            }
            attitudeBox.setNames(names);
        }

        String getYokaiBoxName() {
            return yokaiNameBox.getChosenName();
        }

        String getEquipmentBoxName() {
            return equipmentBox.getChosenName();
        }

        String getAttitudeBoxName() {
            return attitudeBox.getChosenName();
        }

        String getNickname() {
            return nicknameField.getText();
        }

        int[] getIvValues() {
            int[] iv = new int[5];
            for (int i = 0; i < 5; i++) {
                iv[i] = (Integer) ivSpinners[i].getValue();
            }
            return iv;
        }

        // ===== TODO rename =====

        /** TODO rename */
        void display(int position, YokaiGeneral yg) {
            if (position < 3) {
                title.setText("Slot " + (position + 1) + " - Front");
            } else {
                title.setText("Slot " + (position - 2) + " - Back");
            }
            setMessage(null);

            updating = true;
            if (yg == null || yg.GetYokai() == null) {
                clear();
                updating = false;
                return;
            }
            Yokai y = yg.GetYokai();

            yokaiNameBox.selectWithoutEvent(y.GetName());
            equipmentBox.selectWithoutEvent(yg.GetEquipement() == null ? null : yg.GetEquipementName());
            attitudeBox.selectWithoutEvent(yg.GetAttitude() == null ? null : yg.GetAttitudeName());
            nicknameField.setText(yg.GetNickname());
            nicknameField.setEnabled(true);
            removeButton.setEnabled(true);
            gearInfo.setText(gearText(yg.GetEquipement(), yg.GetAttitude()));

            details[0].setText(String.valueOf(yg.GetLevel()));
            details[1].setText(String.valueOf(y.GetRank()));
            details[2].setText(y.GetTribe());
            details[3].setText(y.GetTierString());

            int[] base = values(yg.calculateStats(false));
            int[] fin = values(yg.GetStats());
            for (int i = 0; i < 5; i++) {
                baseValues[i].setText(String.valueOf(base[i]));
                finalValues[i].setText(String.valueOf(fin[i]));
                finalValues[i].setForeground(fin[i] > base[i] ? GOOD : fin[i] < base[i] ? BAD : Color.BLACK);
                bars[i].setValue(fin[i]);
            }

            int[] iv = {yg.GetIvHP(), yg.GetIvSTR(), yg.GetIvSPR(), yg.GetIvDEF(), yg.GetIvSPE()};
            for (int i = 0; i < 5; i++) {
                ivSpinners[i].setValue(iv[i]);
                ivSpinners[i].setEnabled(true);
            }
            int left = yg.GetIv().getPointsLeft();
            ivPointsLabel.setText("Points left : " + left + " / " + IV.MAXIVPOINTS);
            ivPointsLabel.setForeground(left == 0 ? GOOD : Color.BLACK);
            ivRandomButton.setEnabled(true);
            ivResetButton.setEnabled(true);

            for (int i = 0; i < 6; i++) {
                double r = y.getResistance(i);
                resistances[i].setText("x" + r);
                if (r < 1) {
                    resistances[i].setForeground(GOOD);   // TODO rename
                } else if (r > 1) {
                    resistances[i].setForeground(BAD);    // TODO rename
                } else {
                    resistances[i].setForeground(Color.BLACK);
                }
            }

            actions[0].setText(" Attack : " + actionName(y.getAttackId()));
            actions[1].setText(" Technique : " + actionName(y.getTechniqueId()));
            actions[2].setText(" Inspirit : " + actionName(y.getInspiritId()));
            actions[3].setText(" Soultimate : " + actionName(y.getUltimateId()));

            chances[0].setText(" Attack : " + y.getBaseAttackPerC() + " %");
            chances[1].setText(" Technique : " + y.getBaseTechniquePerC() + " %");
            chances[2].setText(" Inspirit : " + y.getBaseInspiritPerC() + " %");
            chances[3].setText(" Guard : " + y.getBaseGuardPerC() + " %");
            updating = false;
        }

        private void clear() {
            yokaiNameBox.selectWithoutEvent(null);
            equipmentBox.selectWithoutEvent(null);
            attitudeBox.selectWithoutEvent(null);
            nicknameField.setText("");
            nicknameField.setEnabled(false);
            removeButton.setEnabled(false);
            gearInfo.setText(" ");

            details[0].setText(String.valueOf(YokaiGeneral.DEFAULT_LEVEL));
            for (int i = 1; i < 4; i++) {
                details[i].setText("-");
            }
            for (int i = 0; i < 5; i++) {
                baseValues[i].setText("-");
                finalValues[i].setText("-");
                finalValues[i].setForeground(Color.BLACK);
                bars[i].setValue(0);
                ivSpinners[i].setValue(0);
                ivSpinners[i].setEnabled(false);
            }
            ivPointsLabel.setText("Choose a Yo-kai first");
            ivPointsLabel.setForeground(MUTED_TEXT);
            ivRandomButton.setEnabled(false);
            ivResetButton.setEnabled(false);
            for (int i = 0; i < 6; i++) {
                resistances[i].setText("-");
                resistances[i].setForeground(Color.BLACK);
            }
            String[] actionLabels = {"Attack", "Technique", "Inspirit", "Soultimate"};
            String[] chanceLabels = {"Attack", "Technique", "Inspirit", "Guard"};
            for (int i = 0; i < 4; i++) {
                actions[i].setText(" " + actionLabels[i] + " : -");
                chances[i].setText(" " + chanceLabels[i] + " : -");
            }
        }

        void setMessage(String text) {
            message.setText(text == null || text.isEmpty() ? " " : text);
        }

        /** TODO rename */
        private static String gearText(Equipement e, Attitude a) {
            String text = "";
            if (e != null) {
                text += "Equipment: " + bonuses(e.GetHP(), e.GetSTR(), e.GetSPR(), e.GetDEF(), e.GetSPE());
                if (e.GetEffect() != null && !e.GetEffect().isBlank()) {
                    text += " (" + e.GetEffect() + ")";
                }
            }
            if (a != null) {
                text += (text.isEmpty() ? "" : "   ") + "Attitude: "
                        + bonuses(a.GetHP(), a.GetSTR(), a.GetSPR(), a.GetDEF(), a.GetSPE());
            }
            return text.isEmpty() ? " " : text;
        }

        private static String bonuses(int... v) {
            String text = "";
            for (int i = 0; i < 5; i++) {
                if (v[i] != 0) {
                    text += (text.isEmpty() ? "" : ", ") + STAT_NAMES[i] + (v[i] > 0 ? " +" : " ") + v[i];
                }
            }
            return text.isEmpty() ? "-" : text;
        }

        private static int[] values(Stat s) {
            return new int[]{s.GetHP(), s.GetSTR(), s.GetSPR(), s.GetDEF(), s.GetSPE()};
        }

        private static String actionName(Integer id) {
            if (id == null) {
                return "-";
            }
            DataBase.RawAction a = DataBase.getAction(id);
            return a == null ? "#" + id : a.getName();
        }

        // ===== TODO rename =====

        private JPanel detailsTable() {
            JPanel table = new JPanel(new GridLayout(2, 4));
            table.setBackground(Color.WHITE);
            table.setBorder(new LineBorder(BORDER_COLOR));
            for (String t : new String[]{"Level", "Rank", "Tribe", "Tier"}) {
                table.add(smallTitle(t, SwingConstants.CENTER));
            }
            for (int i = 0; i < 4; i++) {
                details[i] = new JLabel("-", SwingConstants.CENTER);
                details[i].setFont(VERDANA);
                table.add(details[i]);
            }
            return table;
        }

        private JPanel statsTable() {
            JPanel table = new JPanel(new GridBagLayout());
            table.setBackground(Color.WHITE);
            table.setBorder(BorderFactory.createCompoundBorder(new LineBorder(BORDER_COLOR), new EmptyBorder(4, 6, 4, 6)));
            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(2, 2, 2, 6);
            c.fill = GridBagConstraints.HORIZONTAL;
            for (int i = 0; i < 5; i++) {
                c.gridy = i;
                c.gridx = 0; c.weightx = 0;
                table.add(smallTitle(STAT_NAMES[i], SwingConstants.LEFT), c);
                baseValues[i] = new JLabel("-", SwingConstants.RIGHT);
                baseValues[i].setFont(VERDANA);
                baseValues[i].setForeground(MUTED_TEXT);
                c.gridx = 1;
                table.add(baseValues[i], c);
                bars[i] = new StatBar(i == 0 ? 450 : 250);   // TODO rename
                c.gridx = 2; c.weightx = 1;
                table.add(bars[i], c);
                finalValues[i] = new JLabel("-", SwingConstants.RIGHT);
                finalValues[i].setFont(VERDANA_BOLD);
                c.gridx = 3; c.weightx = 0;
                table.add(finalValues[i], c);
            }
            return table;
        }

        /** TODO rename */
        private JPanel ivTable() {
            JPanel grid = new JPanel(new GridLayout(2, 5, 4, 2));
            grid.setOpaque(false);
            for (String name : STAT_NAMES) {
                grid.add(smallTitle(name, SwingConstants.CENTER));
            }
            for (int i = 0; i < 5; i++) {
                SpinnerNumberModel model = i == 0
                        ? new SpinnerNumberModel(0, 0, 2 * IV.MAXIVPOINTS, 2)   // TODO rename
                        : new SpinnerNumberModel(0, 0, IV.MAXIVPOINTS, 1);
                JSpinner spinner = new JSpinner(model);
                spinner.setFont(VERDANA);
                spinner.addChangeListener(e -> fire(spinner, ACTION_IV_CHANGED));
                ivSpinners[i] = spinner;
                grid.add(spinner);
            }

            ivPointsLabel.setFont(VERDANA);
            ivRandomButton.setToolTipText("40 draws like in the game: +2 HP or +1 in another stat");
            JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
            buttons.setOpaque(false);
            buttons.add(ivRandomButton);
            buttons.add(ivResetButton);
            JPanel bottom = new JPanel(new BorderLayout());
            bottom.setOpaque(false);
            bottom.add(ivPointsLabel, BorderLayout.WEST);
            bottom.add(buttons, BorderLayout.EAST);

            JPanel table = new JPanel(new BorderLayout(0, 4));
            table.setBackground(Color.WHITE);
            table.setBorder(BorderFactory.createCompoundBorder(new LineBorder(BORDER_COLOR), new EmptyBorder(4, 6, 4, 6)));
            table.add(grid, BorderLayout.CENTER);
            table.add(bottom, BorderLayout.SOUTH);
            return table;
        }

        private JPanel resistancesTable() {
            JPanel table = new JPanel(new GridLayout(2, 6));
            table.setBackground(Color.WHITE);
            table.setBorder(new LineBorder(BORDER_COLOR));
            for (String e : ELEMENT_NAMES) {
                JLabel l = smallTitle(e.length() > 5 ? e.substring(0, 5) + "." : e, SwingConstants.CENTER);
                l.setFont(new Font("Verdana", Font.BOLD, 9));
                table.add(l);
            }
            for (int i = 0; i < 6; i++) {
                resistances[i] = new JLabel("-", SwingConstants.CENTER);
                resistances[i].setFont(VERDANA);
                table.add(resistances[i]);
            }
            return table;
        }

        private static JPanel list(JLabel[] rows) {
            JPanel list = new JPanel(new GridLayout(rows.length, 1, 0, 4));
            list.setOpaque(false);
            for (int i = 0; i < rows.length; i++) {
                rows[i] = new JLabel(" -");
                rows[i].setFont(VERDANA);
                rows[i].setOpaque(true);
                rows[i].setBackground(Color.WHITE);
                rows[i].setBorder(new LineBorder(BORDER_COLOR));
                list.add(rows[i]);
            }
            return list;
        }

        // ===== TODO rename =====

        /** TODO rename */
        private static JComponent block(JComponent c) {
            JPanel p = new JPanel(new BorderLayout());
            p.setOpaque(false);
            p.setBorder(new EmptyBorder(0, 0, 10, 0));
            p.add(c, c instanceof JButton ? BorderLayout.WEST : BorderLayout.CENTER);
            p.setAlignmentX(LEFT_ALIGNMENT);
            p.setMaximumSize(new Dimension(Integer.MAX_VALUE, p.getPreferredSize().height));
            return p;
        }

        private static JPanel column(String title, JComponent content) {
            JPanel p = new JPanel(new BorderLayout(0, 2));
            p.setOpaque(false);
            p.add(smallTitle(title, SwingConstants.LEFT), BorderLayout.NORTH);
            p.add(content, BorderLayout.CENTER);
            return p;
        }

        private static JLabel smallTitle(String text, int alignment) {
            JLabel l = new JLabel(text, alignment);
            l.setFont(VERDANA_BOLD);
            l.setForeground(MUTED_TEXT);
            return l;
        }

        private static JButton smallButton(String text) {
            JButton b = new JButton(text);
            b.setFont(new Font("Verdana", Font.PLAIN, 10));
            b.setMargin(new Insets(1, 6, 1, 6));
            b.setFocusPainted(false);
            return b;
        }
    }


    /** TODO rename */
    static class SearchBox extends JComboBox<String> {

        private final JTextField searchField;
        private final List<String> allNames = new ArrayList<>();   // TODO rename
        private boolean filtering = false;                         // TODO rename
        private ActionListener listener;

        // ===== TODO rename =====

        SearchBox(String actionCommand) {
            setFont(VERDANA);
            setEditable(true);
            setMaximumRowCount(10);
            setActionCommand(actionCommand);
            searchField = (JTextField) getEditor().getEditorComponent();
            searchField.getDocument().addDocumentListener(new DocumentListener() {
                public void insertUpdate(DocumentEvent e) { filter(); }
                public void removeUpdate(DocumentEvent e) { filter(); }
                public void changedUpdate(DocumentEvent e) { }
            });
        }

        // ===== TODO rename =====

        void setListener(ActionListener listener) {
            this.listener = listener;
            addActionListener(listener);
        }

        // ===== TODO rename =====

        /** TODO rename */
        void setNames(List<String> names) {
            allNames.clear();
            allNames.addAll(names);
            allNames.sort(String.CASE_INSENSITIVE_ORDER);   // TODO rename
            filtering = true;
            refill(allNames);
            filtering = false;
        }

        /** TODO rename */
        String getChosenName() {
            Object selected = getSelectedItem();
            if (selected == null) {
                return "";
            }
            return selected.toString().trim();
        }

        /** TODO rename */
        void selectWithoutEvent(String name) {
            filtering = true;
            if (getItemCount() != allNames.size()) {
                refill(allNames);   // TODO rename
            }
            removeActionListener(listener);
            setSelectedItem(name);
            addActionListener(listener);
            filtering = false;
        }

        // ===== TODO rename =====

        /** TODO rename */
        private void refill(List<String> names) {
            removeActionListener(listener);
            removeAllItems();
            for (String name : names) {
                addItem(name);
            }
            setSelectedIndex(-1);
            addActionListener(listener);
        }

        /** TODO rename */
        private void filter() {
            if (filtering) {
                return;
            }
            // TODO rename
            SwingUtilities.invokeLater(() -> {
                String text = searchField.getText();
                Object selected = getSelectedItem();
                if (selected != null && selected.toString().equals(text)) {
                    return;   // TODO rename
                }

                String search = text.trim().toLowerCase();
                List<String> startsWith = new ArrayList<>();
                List<String> contains = new ArrayList<>();
                for (String name : allNames) {
                    String lower = name.toLowerCase();
                    if (lower.startsWith(search)) {
                        startsWith.add(name);
                    } else if (lower.contains(search)) {
                        contains.add(name);
                    }
                }
                startsWith.addAll(contains);

                filtering = true;
                refill(startsWith);
                searchField.setText(text);   // TODO rename
                filtering = false;

                hidePopup();   // TODO rename
                if (!startsWith.isEmpty() && searchField.isFocusOwner()) {
                    showPopup();
                }
            });
        }
    }


    static class StatBar extends JComponent {
        private final int statMax;
        private int value;

        StatBar(int statMax) {
            this.statMax = statMax;
            setPreferredSize(new Dimension(140, 12));
        }

        void setValue(int value) {
            this.value = value;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (value <= 0) {
                return;
            }
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int width = Math.max(2, (int) (getWidth() * Math.min(value, statMax) / (double) statMax));
            // TODO rename
            float hue = Math.min(value, statMax) / (float) statMax * 0.33f;
            g2.setColor(Color.getHSBColor(hue, 0.45f, 0.85f));
            g2.fillRoundRect(0, 1, width, getHeight() - 2, 4, 4);
            g2.dispose();
        }
    }
}
