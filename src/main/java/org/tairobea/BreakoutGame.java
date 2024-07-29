package org.tairobea;

import javax.swing.*;
import java.awt.*;
import static org.tairobea.BreakoutPanel.PANEL_HEIGHT;
import static org.tairobea.BreakoutPanel.PANEL_WIDTH;


public class BreakoutGame extends JFrame {
    private BreakoutPanel panel;

    public BreakoutGame(){
        panel = new BreakoutPanel();
        setSize(PANEL_WIDTH, PANEL_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Hola Mijo");
        setResizable(false);
        setLocationRelativeTo(null);
        add(panel);
        pack();
        panel.requestFocus();
        setVisible(true);
    }

}