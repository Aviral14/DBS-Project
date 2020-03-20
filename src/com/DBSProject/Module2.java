package com.DBSProject;

import javax.swing.*;
import java.awt.*;

import static com.DBSProject.CommonConstants.*;

public class Module2 extends BackgroundPanel {
    public Module2() {
        framePanel = this;

        JLabel heading = new JLabel("<html><u>Module 2</u></html>");
        heading.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
        heading.setBounds(100, 20, 1100, 80);
        heading.setForeground(Color.WHITE);
        RoundButton back = new RoundButton("←", 10, 10, 50, 25, 10, Color.white, blueColor, true);
        back.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        back.addActionListener(e -> {
            mainFrame.remove(framePanel);
            mainFrame.add(new MainPanel());
            mainFrame.repaint();
        });
        add(back);
        add(heading);
    }
}
