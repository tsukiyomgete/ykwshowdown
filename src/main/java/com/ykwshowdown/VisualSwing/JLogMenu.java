package com.ykwshowdown.VisualSwing;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

public class JLogMenu extends JPanel {
    public static final String ACTION_LOG  = "LOGIN_BUTTON";
    public static final String ACTION_SIGN = "logIn_BUTTON";

    private JTextField usernameInputField = new JTextField(20);
    private JPasswordField passwordInputField = new JPasswordField(20);
    private JLabel message = new JLabel(" ", SwingConstants.CENTER);
    private JButton logIn = new JButton("Log In");
    private JButton signIn = new JButton("Sign In");

    public JLogMenu()
    {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 235, 245));

        JLabel title = new JLabel("Connexion", SwingConstants.LEFT);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);

        c.gridx = 0; c.gridy = 0; c.anchor = GridBagConstraints.LINE_END;
        form.add(new JLabel("Username:"), c);
        c.gridx = 1; c.anchor = GridBagConstraints.LINE_START;
        form.add(usernameInputField, c);
        c.gridx = 0; c.gridy = 1; c.anchor = GridBagConstraints.LINE_END;
        form.add(new JLabel("Password:"), c);
        c.gridx = 1; c.anchor = GridBagConstraints.LINE_START;
        form.add(passwordInputField, c);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(form, BorderLayout.WEST);
        centerPanel.add(message, BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);

        logIn.setActionCommand(ACTION_LOG);
        signIn.setActionCommand(ACTION_SIGN);
        passwordInputField.setActionCommand(ACTION_LOG);

        JPanel actionsContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        actionsContainer.add(signIn);
        actionsContainer.add(logIn);
        add(actionsContainer, BorderLayout.SOUTH);
    }
    public void setEcouteur(ActionListener ecouteur) {
        logIn.addActionListener(ecouteur);
        signIn.addActionListener(ecouteur);
        passwordInputField.addActionListener(ecouteur);
    }
    public void setMessage(String texte) {
        message.setText(texte);
    }
    public String getPseudo(){
        return usernameInputField.getText();
    }
    public String getMdp(){
        return new String(passwordInputField.getPassword());
    }
}
