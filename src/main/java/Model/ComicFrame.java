package Model;


import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ComicFrame extends CanvasIcon {
    public ComicFrame(int w, int h) {
        super();
        this.w = w; this.h = h;
        this.dW = w; this.dH = h;
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));
        g2.setColor(fgColor);
        g2.drawRect(0, 0, w-1, h-1);
    }

    public BufferedImage getImage() {
        repaint();
        BufferedImage bufImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        this.paint(bufImage.getGraphics());
        return bufImage;
    }
}
