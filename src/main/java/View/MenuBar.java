package View;

import Controller.SystemState;
import Model.Canvas;
import Model.CanvasIcon;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

public class MenuBar extends JMenuBar implements ActionListener,ItemListener{
    public MenuBar() {
        JMenu mFile = new JMenu("File");
        JMenuItem oNew = new JMenuItem("New");

        JMenuItem omSave = new JMenu("Save");
        JMenuItem oExport = new JMenuItem("Export as .png");
        JMenuItem oSave = new JMenuItem("Save");
        JMenuItem oSaveAs = new JMenuItem("Save As...");

        JMenuItem oOpen = new JMenuItem("Open");
        JMenuItem oExit = new JMenuItem("Exit");

        mFile.add(oNew);
        oNew.addActionListener(this);
        mFile.add(omSave);
        omSave.add(oSave);
        oSave.addActionListener(this);
        omSave.add(oSaveAs);
        oSaveAs.addActionListener(this);
        omSave.add(oExport);
        oExport.addActionListener(this);
        mFile.add(oOpen);
        oOpen.addActionListener(this);
        mFile.add(oExit);
        oExit.addActionListener(this);

        JMenu mEdit = new JMenu("Edit");
        JMenuItem oUndo = new JMenuItem("Undo");
        JMenuItem oRedo = new JMenuItem("Redo");
        mEdit.add(oUndo);
        oUndo.addActionListener(this);
        mEdit.add(oRedo);
        oRedo.addActionListener(this);

        JMenu mHelp = new JMenu("Help");
        JMenuItem about = new JMenuItem("About");
        JMenuItem version = new JMenuItem("Version");
        mHelp.add(about);
        about.addActionListener(this);
        mHelp.add(version);
        version.addActionListener(this);

        add(mFile);
        add(mEdit);
        add(mHelp);
        setVisible(true);
    }

    @Override
    public void itemStateChanged(ItemEvent arg0) {
        System.out.println(arg0.getStateChange());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //TODO Add file loading/saving functionality
        //TODO Add ability to export canvas as .png
        String action = e.getActionCommand();
        if(action == "Exit") {
            int n = 0;
            if(SystemState.unsaved) {
                n = JOptionPane.showConfirmDialog(
                        JOptionPane.getRootFrame(),
                        "You have unsaved work!\nAre you sure you want to quit?",
                        "Wait!",
                        JOptionPane.YES_NO_OPTION
                );
            }
            if(n == 0) { System.exit(0); }
        } else if(action == "Undo") {
            System.out.println("Doing undo");
            SystemState.canvasPointer.setCanvas(
                    SystemState.history.undo()
            );
        } else if(action == "Redo") {
            SystemState.canvasPointer.setCanvas(
                    SystemState.history.redo()
            );
        } else if(action == "New") {
            SystemState.canvasPointer.getCanvas().clear();
            SystemState.history.clear(SystemState.canvasPointer.getCanvas());
            SystemState.unsaved = true;
        } else if(action == "Save") {
            File f;
            if(SystemState.currentFile != null) { f = SystemState.currentFile; }
            else {
                JFileChooser fc = new JFileChooser();
                fc.showSaveDialog(SystemState.rootPane);
                f = fc.getSelectedFile();
                SystemState.currentFile = f;
            }
            SystemState.canvasPointer.getCanvas().saveCanvasAs(f.getAbsolutePath());
            SystemState.unsaved = false;
        } else if(action == "Save As") {
            JFileChooser fc = new JFileChooser();
            fc.showSaveDialog(SystemState.rootPane);
            File f = fc.getSelectedFile();
            SystemState.currentFile = f;
            SystemState.canvasPointer.getCanvas().saveCanvasAs(f.getAbsolutePath());
            SystemState.unsaved = false;
        } else if(action == "Export as .png") {
            JFileChooser fc = new JFileChooser();
            fc.showSaveDialog(SystemState.rootPane);
            File f = fc.getSelectedFile();
            try {
                SystemState.canvasPointer.getCanvas().exportAsPNG(f);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        } else if(action == "Open") {
            JFileChooser fc = new JFileChooser();
            fc.showOpenDialog(SystemState.rootPane);
            try {
                FileInputStream f = new FileInputStream(fc.getSelectedFile().getAbsolutePath());
                ObjectInputStream in = new ObjectInputStream(f);
                Canvas canvas = (Canvas)in.readObject();
                for(CanvasIcon x : canvas.items) {
                    x.setupImage();
                }
                SystemState.canvasPointer.setCanvas(canvas);
                SystemState.history.clear(canvas);
                in.close();
            } catch (FileNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (ClassNotFoundException e2) {
                e2.printStackTrace();
            } catch (IOException e3) {
                // TODO Auto-generated catch block
                e3.printStackTrace();
            }
            SystemState.unsaved = true;
        } else if(action == "About") {
            JOptionPane.showMessageDialog(getParent(), "With Comic lab you will be able to create comic strips fast and efficiently. \n You can select different types of backgrounds or change its color, and add avatars, \nas well as speech bubbles to narrate the story.");
        } else if(action == "Version") {
            JOptionPane.showMessageDialog(getParent(), "Version: 1.1");
        }
    }
}
