package com.ykwshowdown.VisualSwing.StylizedButton;

import java.awt.*;

import javax.swing.*;

public class TeamStyledButton extends JButton{
	private int bgHex;
    private int borderHex;
    
    public TeamStyledButton(String teamName, String format, int bgHex, int borderHex) {
        super("<html><div style='text-align: center; font-family: sans-serif; font-size: 8pt; white-space: nowrap; color: #333333;'>"
                + "<span style='color: #777777;'>" + format + "</span> <strong>" + teamName + "</strong>"
                + "</div></html>");
        
        this.bgHex = bgHex;
        this.borderHex = borderHex;

        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);
        
        setBorder(BorderFactory.createEmptyBorder(1, 6, 1, 6)); 
        
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        
        Dimension size = new Dimension(350, 49);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int arc = 0; 

        g2.setColor(new Color(bgHex));
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.dispose();
        
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(borderHex));
        g2.setStroke(new BasicStroke(1));
        
        g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

        g2.dispose();
    }
}
