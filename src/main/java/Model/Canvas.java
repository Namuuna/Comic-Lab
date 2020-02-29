package Model;

import Controller.SystemState;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class Canvas extends JPanel implements Serializable, MouseMotionListener, MouseListener {
    private Color bgColor;
    public Vector<CanvasIcon> items;
    int w = 0, h = 0, lastX = 0, lastY = 0;
    private CanvasIcon draggedItem;
    private CanvasIcon resizedItem;
    private boolean beingResized = false;
    private ItemContextMenu menu;

    Canvas() {
        super();
        bgColor = new Color(255,255,255);
        items = new Vector<CanvasIcon>();
        menu = new ItemContextMenu();
        addMouseMotionListener(this);
        addMouseListener(this);
    }

    /*
     * addToCanvas: add a CanvasIcon at a certain position.
     * if x and y arguments are -1, set to center of the viewport, so that whatever you
     * create is always in view. Then add this change to the history and force the canvas
     * to repaint so the new object can be seen.
     */
    public void addToCanvas(CanvasIcon item, int x, int y) {
        if(x == -1 || y == -1) {
            x = (SystemState.canvasPointer.getScrollPane().getViewport().getX()+
                    (SystemState.canvasPointer.getScrollPane().getViewport().getWidth()/2));
            y = (SystemState.canvasPointer.getScrollPane().getViewport().getY()+
                    (SystemState.canvasPointer.getScrollPane().getViewport().getHeight()/2));
        }
        item.setcX(x); item.setcY(y);
        items.add(item);
        resizeCanvas();
        setSelectedElement(item);
        SystemState.history.addToHistory(this);
        repaint();
    }

    /*
     * resizeCanvas: Loop through the items on the canvas, checking their sizes and
     *	X,Y coordinates. If the sum of their position & size are bigger
     *	than the canvas's width and height, make the canvas that width &
     *	height, plus 50 pixels to force the scrollbar to update immediately.
     */
    public void resizeCanvas() {
        int tW = 0, tH = 0;

        for(CanvasIcon i : items) {
            if(i.getcX()+i.getWidth() > tW) tW = i.getcX()+i.getWidth()+50;
            if(i.getcY()+i.getHeight() > tH) tH = i.getcY()+i.getHeight()+50;
        }

        //bug: seem to have to set both preferred size and actual size
        //to get the scrollbars to update correctly.
        setPreferredSize(new Dimension(tW,tH));
        setSize(tW, tH);
        SystemState.canvasPointer.getScrollPane().revalidate();
    }

    public void setBgColor(Color c) {
        //set background colour but also repaint
        //so the change is seen immediately.
        bgColor = c;
        setBackground(bgColor);
        repaint();
    }

    public Color getBgColor() {
        return bgColor;
    }

    public void clear() {
        items.clear();
        w = 0; h = 0;
        repaint();
    }

    /*
     * saveCanvasAs: serialize the Canvas to a file (filename)
     */
    public void saveCanvasAs(String filename) {
        try{
            FileOutputStream fos = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(this);
            out.close();
        } catch(IOException e) {
            JOptionPane.showMessageDialog(SystemState.rootPane, "There was an error saving your file! Sorry :(");
        }
    }

    /*
     * setSelectedElement: loop through the canvas items,
     * make sure everything's deselected, then set @item to selected.
     */
    public void setSelectedElement(CanvasIcon item) {
        for(CanvasIcon i : items) {
            i.setSelected(false);
        }
        item.setSelected(true);
        draggedItem = item;
    }

    /*
     * getSelectedElement: loop through items, return first selected
     * element.
     */
    public CanvasIcon getSelectedElement() {
        if(draggedItem != null) {
            return draggedItem;
        }
        for(CanvasIcon i : items) {
            //there should only be one selected element,
            //so return it as soon as one is found.
            if(i.isSelected()) return i;
        }
        return null;
    }

    public void flipSelectedElement(String direction) {
        if(draggedItem != null) {
            if(direction == "horizontal") draggedItem.setFlippedH(true);
            if(direction == "vertical") draggedItem.setFlippedV(true);
        }
    }

    public void deleteSelectedElement() {
        //delete whatever element has been selected,
        //then add this change to the history.
        if(draggedItem != null) {
            items.remove(draggedItem);
            draggedItem = null;
            resizeCanvas();
            repaint();
            SystemState.history.addToHistory(this);
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.swing.JComponent#paint(java.awt.Graphics)
     * paint: loop through all the canvas items, painting them at their
     * custom sizes and positions on canvas. flip them if their isFlipped*() methods
     * return true. if selected, draw rectangle around them with handle user can drag
     * to resize selected image.
     */
    public void paint(Graphics g) {
        //Make resizing of images look nice; bicubic interpolation
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        //Draw the background colour
        super.paintComponent(g);

        //Paint the canvas items.
        for(CanvasIcon i : items) {
            //If item is selected, draw a box around it
            if(i.isSelected()) {
                g.setColor(new Color(255,255,255));
                //The box should always be the opposite colour of its background,
                //so it's always visible.
                g.setXORMode(new Color(0,0,0));
                g.drawRect(i.getcX()-10, i.getcY()-10, i.getWidth()+20, i.getHeight()+20);
                g.fillRect((i.getcX()+i.getWidth()), (i.getcY()+i.getHeight()), 10, 10);
            }
            //Return to 'paint mode' from XOR mode, so it won't invert the actual image.
            g.setPaintMode();
            int tmpW = i.getWidth(); int tmpH = i.getHeight();
            int tmpX = i.getcX(); int tmpY = i.getcY();
            //If the image is meant to be flipped, invert width/height so it draws
            //it backwards.
            if(i.isFlippedH()) { tmpX += tmpW; tmpW = -tmpW; }
            if(i.isFlippedV()) { tmpY += tmpH; tmpH = -tmpH; }

            //Draw image to the canvas.
            g.drawImage(i.getImage(), tmpX, tmpY, tmpW, tmpH, this);
        }
    }

    //taken straight from http://stackoverflow.com/questions/4530736/how-to-capture-a-swing-gui-element
    //(used for exporting comic as .png image - didn't need to change anything)
    public void exportAsPNG(File imageFile) throws IOException {
        BufferedImage bufImage = new BufferedImage(this.getSize().width, this.getSize().height, BufferedImage.TYPE_INT_RGB);
        for(CanvasIcon i : items) { i.setSelected(false); }
        this.paint(bufImage.createGraphics());
        imageFile.createNewFile();
        ImageIO.write(bufImage, "png", imageFile);
    }

    /*
     * (non-Javadoc)
     * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
     * mouseDragged: if an item is selected, move it around the canvas following the coordinates
     * of the mouse cursor. if an item is being dragged by the bottom-right square, user wants
     * to resize item, so resize it instead.
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        int dX = 0, dY = 0;
        CanvasIcon i = draggedItem;

        if(draggedItem != null) {
            //check if mouse is over currently selected item (if there is an
            //item selected)
            if((x >= i.getcX() && x <= i.getcX()+i.getWidth()) &&
                    (y >= i.getcY() && y <= i.getcY()+i.getHeight()) && beingResized == false) {
                //if mouse over item, make item follow mouse's X,Y coordinates
                //but from the item's center.
                i.setcX(x-(i.getWidth()/2));
                i.setcY(y-(i.getHeight()/2));
                lastX = 0; lastY = 0;
            }
            //if the mouse is inside the small black box beneath the bottom-right
            //corner of the item, the user wants to resize it. set resizedItem to
            //point to the item.
            else if((x >= (i.getcX()+i.getWidth()-10) && x <= (i.getcX()+i.getWidth())+10) &&
                    (y >= i.getcY()+i.getHeight()-10) && (y <= i.getcY()+i.getHeight()+10)) {
                resizedItem = i;
            }
        }

        if(resizedItem != null) {
            //if we've only started resizing the image, we want to reset all the
            //variables used in calculating how much the mouse has moved since the
            //method was last called, so that it's not a big number that makes
            //the item itself irrationally big/small.
            if(beingResized == false) { dX = 0; dY = 0; lastX = 0; lastY = 0; }
            else { dX = x-lastX; dY = y-lastY; }
            beingResized = true;
            if(SystemState.retainAspect == true) {
                i.resize(i.getWidth()+dX, i.getHeight()*((i.getWidth())/i.getHeight())+dX);
            } else {
                i.resize(i.getWidth()+dX, i.getHeight()+dY);
            }
            lastX = x; lastY = y;
        }

        resizeCanvas();
        repaint();
    }

    /*
     * (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     * mouseClicked: if mouse is over something, select it. otherwise, deselect
     * everything. if an item's been clicked, bring it to the front by moving it to
     * the end of the items vector.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX(); int y = e.getY();
        boolean somethingSelected = false;
        //loop through the canvas items and set them all as 'not selected'
        for(CanvasIcon i : items) {
            i.setSelected(false);
            //if the mouse is over an item, set it as selected again
            //and set draggedItem to point to the item.
            if((x >= i.getcX() && x <= i.getcX()+i.getWidth()) &&
                    (y >= i.getcY() && y <= i.getcY()+i.getHeight())) {
                draggedItem = i; i.setSelected(true);
                somethingSelected = true;
            }
        }

        //bugfix: if nothing's selected, but draggedItem still points
        //to something, we need to set it to null so nothing's moved
        //around or resized by accident.
        if(somethingSelected == false) { draggedItem = null; }

        //when we select something we want to bring it to the front,
        //drawn over the top of everything else. so if something's been
        //selected, remove it from the vector and add it to the end again,
        //so that it gets drawn last in paint().
        else { items.remove(draggedItem); items.add(draggedItem); }
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //if something has been moved or resized, add it to history.
        if(beingResized == true || resizedItem != null || draggedItem != null) SystemState.history.addToHistory(this);
        //then reset all the variables used in mouseDragged().
        lastX = 0; lastY = 0; beingResized = false; resizedItem = null;
    }

    /*
     * (non-Javadoc)
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     * mousePressed: if right click is pressed, bring up an ItemContextMenu at
     * the right position.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON3) menu.popup(getSelectedElement(), getLocationOnScreen().x+e.getX(), getLocationOnScreen().y+e.getY());
        else { menu.hide(); }
    }

    //None of the events below need to be caught, but implementing the interface
    //requires we have them defined in code, so these methods are all intentionally
    //left blank.
    @Override
    public void mouseMoved(MouseEvent e) { }
    @Override
    public void mouseEntered(MouseEvent arg0) { }
    @Override
    public void mouseExited(MouseEvent arg0) { }
}
