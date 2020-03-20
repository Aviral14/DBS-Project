package com.DBSProject;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RectangleShape extends Component {
    private Color color;
    private Float alpha;
    public RectangleShape(int x, int y, int w, int h, Color c, Float alpha) {
        setBounds(x, y, w, h);
        color = c;
        this.alpha = alpha;
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        int w = getSize().width-1;
        int h = getSize().height-1;
        Shape shape = new RoundRectangle2D.Float(0, 0, w, h, 25, 25);
        g2.setColor(color);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.fill(shape);
        g2.dispose();
    }
}
