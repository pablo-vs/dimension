package es.ucm.fdi.business.workspace;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class DrawableAxis extends JPanel {

    private double XScale, YScale;
    private int x, y;
    private int scaleDensity;
    private Graph gr;

    public DrawableAxis(double XScale, double YScale, int x, int y, Graph g) {
        this.XScale = XScale;
        this.YScale = YScale;
        this.x = x;
        this.y = y;
        scaleDensity = 5;
        this.gr = g;
    }

    @Override
    public void paint(Graphics g) {
        drawGraph(g);
    }

    public void drawGraph(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        gr.paint(g2, x / 2, y / 2, XScale);
    }

    public void drawAxis(Graphics g) {
        g.drawLine(0, y / 2, x, y / 2);
        g.drawLine(x / 2, 0, x / 2, y);
        int i = x / 2;
        while (i < x) {
            g.drawLine(i, y / 2 - 2, i, y / 2 + 2);
            i += 5;
        }
        i = x / 2;
        while (i > 0) {
            g.drawLine(i, y / 2 - 2, i, y / 2 + 2);
            i -= 5;
        }
        i = y / 2;
        while (i < x) {
            g.drawLine(x / 2 - 2, i, x / 2 + 2, i);
            i += 5;
        }
        i = y / 2;
        while (i > 0) {
            g.drawLine(x / 2 - 2, i, x / 2 + 2, i);
            i -= 5;
        }
    }
}
