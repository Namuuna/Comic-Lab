package Controller;

import java.awt.FlowLayout;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;

public class InternalBox extends JInternalFrame {
    private FlowLayout bl;
    private JComponent inner;

    InternalBox() {
        super("",true,false,false,false);
        bl = new FlowLayout();
        setLayout(bl);
    }
}
