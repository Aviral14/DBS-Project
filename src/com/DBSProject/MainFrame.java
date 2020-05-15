package com.DBSProject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static com.DBSProject.CommonConstants.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        super("DBS Project");
        setSize(frameWidth, frameHeight);
        setUndecorated(true);
        Shape shape = new RoundRectangle2D.Float(0, 0, frameWidth, frameHeight, 25, 25);
        setShape(shape);

        // Fix size and center on screen
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        mainFrame = this;

        RoundButton minimize = new RoundButton("_", frameWidth - 105, 5, 50, 20, 10, Color.white, blueColor, false);
        RoundButton close = new RoundButton("X", frameWidth - 55, 5, 50, 20, 10, Color.white, Color.red, false);
        close.setFont(new Font("ARIAL", Font.BOLD, 13));
        minimize.setFont(new Font("ARIAL", Font.BOLD, 15));
        // Fix cross platform rendering issues. Displays on Windows only.
        minimize.setMargin(new Insets(0, 0, 7, 0));
        close.addActionListener(e -> System.exit(0));
        minimize.addActionListener(e -> setState(JFrame.ICONIFIED));

        add(minimize);
        add(close);
        add(new MainPanel(), BorderLayout.CENTER);
    }
}

class MainPanel extends BackgroundPanel {

    public MainPanel() {
        framePanel = this;
        RoundButton[] modules = new RoundButton[2];
        JLabel jLabel = new JLabel("<html>DBS<br/>Project</html>");

        int temp = (frameHeight - 250) / 2;
        for (int i = 0; i < 2; i++) {
            modules[i] = new RoundButton("Module " + (i + 1), frameWidth - 300, temp + 100 * (i) + 40, 200, 50, 25,
                    Color.WHITE, blueColor, true);
            add(modules[i]);

            int finalI = i;
            modules[i].addActionListener(e -> {
                mainFrame.remove(framePanel);
                addModule(finalI + 1);
                mainFrame.repaint();
            });
        }
        temp = (frameHeight - 200) / 2;
        jLabel.setBounds(300, temp, 600, 200);
        jLabel.setForeground(Color.WHITE);
        jLabel.setFont(new Font(jLabel.getName(), Font.BOLD, 60));
        add(jLabel);

        try {
            BufferedImage image = ImageIO.read(new File("src/resources/Database.png"));
            Image logo = image.getScaledInstance(180, 150, Image.SCALE_SMOOTH);
            final JLabel imageLabel = new JLabel(new ImageIcon(logo));
            imageLabel.setBounds(100, temp, 150, 200);
            add(imageLabel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addModule(int x) {
        switch (x) {
            case 1:
                mainFrame.add(new Module1());
                break;
            case 2:
                mainFrame.add(new Module2());
        }
    }
}
