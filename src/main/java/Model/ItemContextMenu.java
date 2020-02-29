package Model;

import Controller.SystemState;
import Model.CanvasIcon;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class ItemContextMenu extends JPopupMenu implements ActionListener {
    private CanvasIcon object;
    private boolean isVisible = false;
    private JMenuItem text;


    ItemContextMenu() {
        super();
        JMenuItem delete = new JMenuItem("Delete");
        text = new JMenuItem("Change Text");
        add(delete); delete.addActionListener(this);
        add(text); text.addActionListener(this);
        text.setVisible(false);
        JMenuItem flipH = new JMenuItem("Flip Horizontal");
        add(flipH); flipH.addActionListener(this);
        JMenuItem flipV = new JMenuItem("Flip Vertical");
        add(flipV); flipV.addActionListener(this);
        JMenuItem reset = new JMenuItem("Make original size");
        add(reset); reset.addActionListener(this);
    }

    public void popup(CanvasIcon object, int x, int y) {
        if(object != null) {
            if(object instanceof ProtoBubble) text.setVisible(true);
            else { text.setVisible(false); }
            this.object = object;
            this.setLocation(x,y);
            isVisible = true;
            setVisible(true);
        }
    }

    public void hide() {
        isVisible = false;
        setVisible(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(object != null) {
            if(e.getActionCommand() == "Delete") {
                SystemState.canvasPointer.getCanvas().deleteSelectedElement();
            }
            if(e.getActionCommand() == "Change Text") {
                ProtoBubble tmp = (ProtoBubble) object;
                tmp.popupChangeText();
            }
            if(e.getActionCommand() == "Flip Horizontal") object.setFlippedH(!object.isFlippedH());
            if(e.getActionCommand() == "Flip Vertical") object.setFlippedV(!object.isFlippedV());
            if(e.getActionCommand() == "Make original size") object.resize(object.getDefaultWidth(), object.getDefaultHeight());
            hide();
            SystemState.canvasPointer.getCanvas().repaint();
        }
    }

}
