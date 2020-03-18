package Model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class Caption extends ProtoBubble {
    public Caption(String text) {
        super(text);
    }

    Caption(String text, int w, int h) {
        super(text,w,h);
    }

    /*
     * (non-Javadoc)
     * @see ProtoBubble#paint(java.awt.Graphics)
     * Caption extends ProtoBubble but overrides paint completely since
     * it draws a rectangle, not a circle, behind the text.
     * (this is what paint does).
     */
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setBackground(new Color(0x0000000, true));
        g2.setColor(new Color(0x0000000, true));
        g2.fillRect(0, 0, w, h);
        g2.setStroke(new BasicStroke(3));
        g2.drawRect(1, 1, super.w-2, super.h-2);
        calculateFontSize(g2);
        g2.setColor(Color.BLACK);
        g2.drawString(text, (w/2)-(tW/2), (h/2)+(tH/4));

    }
}
