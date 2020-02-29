package Controller;

import Model.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.Objects;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ToolBox extends InternalBox implements ChangeListener, ActionListener {
    private JColorChooser colours;
    private JCheckBox bSwitchResize;
    private JToggleButton bPaintMode;

    public ToolBox() {
        super();
        setIconifiable(true);
        setResizable(false);
        setTitle("Tools");

        FlowLayout fl = new FlowLayout();

        JPanel bC = new JPanel(); //container for the buttons
        JPanel cC = new JPanel(); //container for check boxes

        bC.setLayout(fl);
        JButton bAddFrame = new JButton("Frame");
        bC.add(bAddFrame); bAddFrame.addActionListener(this);
        JButton bAddBubble = new JButton("Speech Bubble");
        bC.add(bAddBubble); bAddBubble.addActionListener(this);
        JButton bAddThought = new JButton("Thought Bubble");
        bC.add(bAddThought); bAddThought.addActionListener(this);
        JButton bAddCaption = new JButton("Caption");
        bC.add(bAddCaption); bAddCaption.addActionListener(this);

        cC.setLayout(fl);
        bPaintMode = new JToggleButton("Paint Mode");
        cC.add(bPaintMode); bPaintMode.addActionListener(this);
        bSwitchResize = new JCheckBox("Keep aspect ratio");
        cC.add(bSwitchResize); bSwitchResize.addActionListener(this);

        add(bC,BorderLayout.NORTH);
        add(cC,BorderLayout.NORTH);

        colours = new JColorChooser(SystemState.canvasPointer.getCanvas().getBgColor());
        colours.setPreviewPanel(new JPanel());
        colours.setPreferredSize(new Dimension(420,140));
        add(colours,BorderLayout.SOUTH);
        colours.getSelectionModel().addChangeListener(this);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        // TODO Auto-generated method stub
        CanvasIcon selected = SystemState.canvasPointer.getCanvas().getSelectedElement();
        if(selected != null) {
            selected.setFgColor(colours.getColor());
        } else {
            SystemState.canvasPointer.getCanvas().setBgColor(colours.getColor());
        }
        SystemState.canvasPointer.getCanvas().repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(Objects.equals(e.getActionCommand(), "Frame")) SystemState.canvasPointer.getCanvas().addToCanvas(new ComicFrame(100,100), -1, -1);
        if(Objects.equals(e.getActionCommand(), "Speech Bubble")) SystemState.canvasPointer.getCanvas().addToCanvas(new SpeechBubble("Text"), -1, -1);
        if(Objects.equals(e.getActionCommand(), "Thought Bubble")) SystemState.canvasPointer.getCanvas().addToCanvas(new ThoughtBubble("Text"), -1, -1);
        if(Objects.equals(e.getActionCommand(), "Caption")) SystemState.canvasPointer.getCanvas().addToCanvas(new Caption("Text"), -1, -1);
        if(e.getSource() == bSwitchResize) SystemState.retainAspect = bSwitchResize.isSelected();
        if(e.getSource() == bPaintMode) SystemState.paintMode = bPaintMode.isSelected();
    }
}
