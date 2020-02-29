package Model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;

public class SpeechBubble extends ProtoBubble {
    public SpeechBubble(String text) {
        super(text);
    }

    SpeechBubble(String text, int w, int h) {
        super(text,w,h);
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Polygon p = new Polygon();
        p.addPoint(w/4, h/2);
        p.addPoint(0, h);
        p.addPoint(w/2, h/2);
        g2.setColor(Color.WHITE);
        g2.fillPolygon(p);
        super.paint(g);
    }
}
