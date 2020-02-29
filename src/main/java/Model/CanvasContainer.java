package Model;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneLayout;
import javax.swing.SwingConstants;

public class CanvasContainer extends JPanel {
    private Canvas canvas;
    private JScrollPane sp;

    public CanvasContainer() {
        super();
        //setTitle("Canvas");
        canvas = new Canvas();
        BorderLayout bl = new BorderLayout();
        setLayout(bl);
        sp = new JScrollPane(canvas);
        add(sp,BorderLayout.CENTER);
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke((char) KeyEvent.VK_DELETE), "delImg");
        getActionMap().put("delImg", new AbstractAction() {
            public void actionPerformed(ActionEvent evt) {
                canvas.deleteSelectedElement();
            }
        });
    }

    public void setCanvas(Canvas canvas) {
        sp.setViewportView(canvas);
        sp.revalidate();
        this.canvas = canvas;
        canvas.repaint();
        sp.repaint();
        repaint();
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public JScrollPane getScrollPane() {
        return sp;
    }
}
