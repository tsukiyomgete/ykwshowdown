package com.ykwshowdown.controleurs;

import com.ykwshowdown.VisualSwing.JTeamEditor;
import com.ykwshowdown.database.DataBase;
import com.ykwshowdown.menu.WatchBuilder;
import com.ykwshowdown.yokai.Yokai;
import com.ykwshowdown.yokai.YokaiGeneral;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** TODO rename */
public class TeamEditorController implements ActionListener {

    private final JTeamEditor editor;
    private final CardLayout cardLayout;
    private final JPanel container;
    private WatchBuilder team;
    private ControleurTeamBuilder teamList;

    public TeamEditorController(JTeamEditor editor, CardLayout cardLayout, JPanel container) {
        this.editor = editor;
        this.cardLayout = cardLayout;
        this.container = container;
        editor.setListener(this);
    }

    /** TODO rename */
    public void setTeamList(ControleurTeamBuilder teamList) {
        this.teamList = teamList;
    }

    /** TODO rename */
    public void dataLoaded() {
        editor.fillLists();
    }

    /** TODO rename */
    public void editTeam(WatchBuilder team) {
        this.team = team;
        editor.showTeam(team);
        cardLayout.show(container, ControleurMenu.CARTE_EDITEUR);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (team == null) {
            return;
        }
        String command = e.getActionCommand();

        if (command.equals(JTeamEditor.ACTION_BACK_TO_LIST)) {
            saveNickname();
            saveHeader();
            editor.closeSheet();
            if (teamList != null) {
                teamList.showList();
            } else {
                cardLayout.show(container, ControleurMenu.CARTE_EQUIPES);
            }

        } else if (command.startsWith(JTeamEditor.ACTION_SELECT_SLOT)) {
            saveNickname();
            int position = Integer.parseInt(command.substring(JTeamEditor.ACTION_SELECT_SLOT.length()));
            editor.openSheet(position);

        } else if (command.equals(JTeamEditor.ACTION_CLOSE_SHEET)) {
            saveNickname();
            editor.closeSheet();

        } else if (command.equals(JTeamEditor.ACTION_SELECT_YOKAI)) {
            chooseYokai();

        } else if (command.equals(JTeamEditor.ACTION_REMOVE_YOKAI)) {
            team.removeYokai(rang());
            editor.refresh();

        } else if (command.equals(JTeamEditor.ACTION_SELECT_EQUIPMENT)) {
            YokaiGeneral yg = selected();
            if (yg != null) {
                String name = editor.getChosenEquipment();
                boolean ok = yg.setEquipment(name);
                editor.refresh();
                if (!ok) {
                    editor.setSheetMessage("Unknown equipment: " + name);
                }
            }

        } else if (command.equals(JTeamEditor.ACTION_SELECT_ATTITUDE)) {
            YokaiGeneral yg = selected();
            if (yg != null) {
                String name = editor.getChosenAttitude();
                boolean ok = yg.setAttitude(name);
                editor.refresh();
                if (!ok) {
                    editor.setSheetMessage("Unknown attitude: " + name);
                }
            }

        } else if (command.equals(JTeamEditor.ACTION_NICKNAME)) {
            saveNickname();
            editor.refresh();

        } else if (command.equals(JTeamEditor.ACTION_IV_CHANGED)) {
            YokaiGeneral yg = selected();
            if (yg != null) {
                int[] iv = editor.getIvValues();
                String error = null;
                try {
                    yg.setIv(iv[0], iv[1], iv[2], iv[3], iv[4]);
                } catch (IllegalArgumentException ex) {
                    error = ex.getMessage();   // TODO rename
                }
                editor.refresh();
                if (error != null) {
                    editor.setSheetMessage(error);
                }
            }

        } else if (command.equals(JTeamEditor.ACTION_IV_RANDOM)) {
            YokaiGeneral yg = selected();
            if (yg != null) {
                yg.generateIV();
                editor.refresh();
            }

        } else if (command.equals(JTeamEditor.ACTION_IV_RESET)) {
            YokaiGeneral yg = selected();
            if (yg != null) {
                yg.setIv(0, 0, 0, 0, 0);
                editor.refresh();
            }

        } else if (command.equals(JTeamEditor.ACTION_VALIDATE)) {
            saveNickname();
            saveHeader();
            editor.showValidation(team.getValidationMessage());

        } else if (command.equals(JTeamEditor.ACTION_IMPORT_EXPORT)) {
            importExport();
        }
    }

    // ===== TODO rename =====

    private void chooseYokai() {
        if (editor.getSelectedSlot() < 0) {
            return;
        }
        String name = editor.getTypedYokaiName();
        if (name.isEmpty()) {
            return;
        }
        Yokai species = DataBase.getYokai(name);
        if (species == null) {
            editor.setSheetMessage("Unknown Yo-kai: " + name);
            return;
        }
        team.setYokai(rang(), species);   // TODO rename
        editor.refresh();
    }

    private void importExport() {
        saveNickname();
        saveHeader();
        String text = editor.askImportExport(team.toString());
        if (text == null || text.trim().equals(team.toString().trim())) {
            return;   // TODO rename
        }
        try {
            WatchBuilder imported = new WatchBuilder();
            imported.importTeam(text);
            team.copyFrom(imported);
            editor.showTeam(team);
        } catch (RuntimeException ex) {
            editor.showError("This text is not a valid team.\n(" + ex.getMessage() + ")");
        }
    }

    // ===== TODO rename =====

    /** TODO rename */
    private int rang() {
        return editor.getSelectedSlot() + 1;
    }

    /** TODO rename */
    private YokaiGeneral selected() {
        int slot = editor.getSelectedSlot();
        YokaiGeneral yg = slot < 0 ? null : team.getYokaiIndex(slot);
        if (yg == null || yg.GetYokai() == null) {
            editor.setSheetMessage("Choose a Yo-kai first");
            return null;
        }
        return yg;
    }

    /** TODO rename */
    private void saveNickname() {
        int slot = editor.getSelectedSlot();
        if (slot < 0) {
            return;
        }
        YokaiGeneral yg = team.getYokaiIndex(slot);
        if (yg != null && yg.GetYokai() != null) {
            yg.setNickname(editor.getNickname());
        }
    }

    /** TODO rename */
    private void saveHeader() {
        String name = editor.getTeamName().trim();
        team.setTeamName(name.isEmpty() ? "Untitled" : name);
        team.setFormat(editor.getFormatTier());
    }
}
