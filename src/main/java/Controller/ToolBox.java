package Controller;

import Model.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.io.*;
import java.util.Objects;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;


public class ToolBox extends InternalBox implements ChangeListener, ActionListener {
    private JColorChooser colours;
    private JCheckBox bSwitchResize;
    private JToggleButton bPaintMode;

    public ToolBox() {
        super();
        setIconifiable(true);
        setResizable(false);
        setTitle("TOOLS");

        FlowLayout fl = new FlowLayout();

        JPanel bC = new JPanel(); //container for the buttons
        JPanel cC = new JPanel(); //container for check boxes

        bC.setLayout(fl);
        JButton bAddFrame = new JButton("Frame");
        bC.add(bAddFrame); bAddFrame.addActionListener(this);
//        JButton bAddBubble = new JButton("Speech Bubble");
//        bC.add(bAddBubble); bAddBubble.addActionListener(this);
//        JButton bAddThought = new JButton("Thought Bubble");
//        bC.add(bAddThought); bAddThought.addActionListener(this);
        JButton bAddCaption = new JButton("Caption");
        bC.add(bAddCaption); bAddCaption.addActionListener(this);

        JButton buttonAdd = new JButton("Upload File");
        bC.add(buttonAdd);
        buttonAdd.addActionListener(this);

        cC.setLayout(fl);
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
        if(Objects.equals(e.getActionCommand(), "Caption")) SystemState.canvasPointer.getCanvas().addToCanvas(new Caption("Text"), -1, -1);
        if(e.getSource() == bSwitchResize) SystemState.retainAspect = bSwitchResize.isSelected();
        if(e.getSource() == bPaintMode) SystemState.paintMode = bPaintMode.isSelected();
        if(e.getActionCommand() == "Upload File"){
            JFileChooser fc = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("*.Images","jpg", "gif");
            fc.addChoosableFileFilter(filter);
            int result = fc.showSaveDialog(null);
            if(result == JFileChooser.APPROVE_OPTION) {
                File selected = fc.getSelectedFile();
                String fileName = selected.getName();
                JOptionPane.showMessageDialog(getParent(), fileName);
                try {
                    saveFile(selected, fileName);
                } catch(IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public void saveFile(File source, String fileName) throws IOException{
        File destination = new File("assets/Uploaded Files/" + fileName);
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(destination);

            byte[] buf = new byte[1024];
            int bytes;
            while((bytes = is.read(buf)) > 0) {
                os.write(buf, 0, bytes);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
