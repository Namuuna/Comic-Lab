package Model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

abstract class ProtoBubble extends CanvasIcon {
    protected String text;
    private Font fontSmall;
    private Font fontBig;
    private Font fontHuge;
    private Font font;
    protected int w = 120; //text width and height
    protected int h = 120;
    protected int tW = 0;
    protected int tH = 0;
    protected Color fgColor = new Color(0,0,0);
    transient private Graphics g;
    protected transient Graphics2D g2;

    /*
     * ProtoBubble - this class is for Speech Bubbles, Thought Bubbles
     * and Captions to extend. It handles basic drawing calculations -
     * how big the font can be depending on the size of the object.
     */
    ProtoBubble(String text, int w, int h) {
        super();
        this.w = w; this.h = h;
        this.text = text;
        fontSmall = new Font("Dialog", Font.PLAIN, 12);
        fontBig = new Font("Dialog", Font.PLAIN, 18);
        fontHuge = new Font("Dialog", Font.PLAIN, 32);
        font = fontSmall;
    }

    ProtoBubble(String text) {
        super();
        w = 120; h = 120;
        this.text = text;
        fontSmall = new Font("Dialog", Font.PLAIN, 12);
        fontBig = new Font("Dialog", Font.PLAIN, 20);
        fontHuge = new Font("Dialog", Font.PLAIN, 28);
    }

    public void paint(Graphics g) {
        this.g = g;
        g2 = (Graphics2D) this.g;
        g2.setColor(Color.WHITE);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.fillOval(0, 0, w-1, h-1);
        g2.setColor(super.fgColor);
        calculateFontSize(g2);
        g2.drawString(text, (w/2)-(tW/2), (h/2)+(tH/4));
    }

    /*
     * calculateFontSize: check for
     */
    public void calculateFontSize(Graphics2D tG2) {
        tG2.setFont(fontHuge);
        FontRenderContext frc = tG2.getFontRenderContext();
        Rectangle2D bounds = tG2.getFont().getStringBounds(text,frc);
        tW = (int)bounds.getWidth(); tH = (int)bounds.getHeight();

        if(tW > w-20 || tH > h-20) tG2.setFont(fontBig);
        bounds = tG2.getFont().getStringBounds(text,frc);
        tW = (int)bounds.getWidth(); tH = (int)bounds.getHeight();

        if(tW > w-20 || tH > h-20) tG2.setFont(fontSmall);
        bounds = tG2.getFont().getStringBounds(text,frc);
        tW = (int)bounds.getWidth(); tH = (int)bounds.getHeight();
    }

    /*
     * (non-Javadoc)
     * @see CanvasIcon#resize(int, int)
     * speech bubbles should be at least 10px wide and high, so we can
     * select them and resize them. bug workaround. call super.resize()
     * so CanvasIcon.resize()'s logic is invoked too.
     */
    @Override
    public void resize(int w, int h) {
        super.resize(w,h);
        if(w > 10 && h > 10) {
            this.w = w; this.h = h;
        }
        else{ this.w = 10; this.h = 10; }
    }

    /*
     * (non-Javadoc)
     * @see CanvasIcon#getImage()
     * Canvas draws Images passed to it by the CanvasIcons.
     * Since anything inheriting from ProtoBubble is drawing simple shapes and
     * text procedurally, it has no image to pass. Convert the graphics object
     * into a BufferedImage in this overriding method.
     */
    public BufferedImage getImage() {
        BufferedImage bufImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        this.paint(bufImage.getGraphics());
        return bufImage;
    }

    /*
     * change the text of this item, but only if the user enters some text in the dialogue
     * which pops up.
     */
    public void popupChangeText() {
        String input = JOptionPane.showInputDialog("What text should this speech bubble have?");
        if(input != null && input != "") text = input;
        repaint();

    }
}
