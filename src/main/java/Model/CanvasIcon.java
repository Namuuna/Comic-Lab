package Model;

import Controller.SystemState;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JOptionPane;


public class CanvasIcon extends JComponent implements Serializable {
    transient private BufferedImage image;
    private String file;
    private int[] serializableImage;
    protected int w = 0, h = 0, x = 0, y = 0, dW = 0, dH = 0;
    private boolean selected, fH, fV;
    protected Color fgColor = new Color(0,0,0);

    CanvasIcon() { }

    CanvasIcon(String file) {
        this.file = file;
        try {
            image = ImageIO.read(new File(file));
            dW = image.getWidth(); dH = image.getHeight();
            w = dW; h = dH;
        } catch (IOException e) {
            SystemState.errors.add(file+" could not be loaded.");
        }
    }

    /*
     * (non-Javadoc)
     * @see java.awt.Component#resize(int, int)
     *	resize, check width and height args are over
     *	one, so that when the item is deselected it
     *	is big enough to be selected again.
     */
    @Override
    public void resize(int width, int height) {

        if(width > 1 && height > 1) {
            w = width;
            h = height;
        }
    }

    public BufferedImage getImage() { return image; }
    public void setImage(BufferedImage i) { image = i; }
    public String getFile() { return file; }

    /*
     * setupImage: if this object is being de-serialized, we need
     * to load the image again (BufferedImages aren't serializable).
     */
    public void setupImage() {
        try {
            if(file != null) image = ImageIO.read(new File(file));
            //dW = image.getWidth(); dH = image.getHeight();
            //w = dW; h = dH;
        } catch (IOException e) {
            SystemState.errors.add(file+" could not be loaded.");
        }
    }

    public int getWidth() { return Math.abs(w); }
    public int getDefaultWidth() { return dW; }
    public int getHeight() { return Math.abs(h); }
    public int getDefaultHeight() { return dH; }
    public boolean isSelected() { return selected; }
    public void setSelected(boolean s) { selected = s; }
    public void setFgColor(Color c) { fgColor = c; }
    public boolean isFlippedH() { return fH; }
    public boolean isFlippedV() { return fV; }
    public void setFlippedH(boolean i) { fH = i; }
    public void setFlippedV(boolean i) { fV = i; }

    public int getcX() { return x; }
    public void setcX(int x) { this.x = x; }
    public int getcY() { return y; }
    public void setcY(int y) { this.y = y; }
}
