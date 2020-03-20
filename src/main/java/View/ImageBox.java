package View;


import Controller.InternalBox;
import Controller.SystemState;
import Model.DraggableIcon;
import java.util.ListIterator;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ImageBox extends InternalBox implements ListSelectionListener {
    private FlowLayout fl;
    private BorderLayout bl;
    private JComponent images;
    private JScrollPane sp;

    public ImageBox() throws IOException {
        super();
        setIconifiable(true);
        setMaximizable(true);
        setTitle("PROPS");
        bl = new BorderLayout();
        fl = new FlowLayout();
        setLayout(bl);

        images = new JPanel();
        images.setLayout(fl);

        Vector<String> cats = new Vector<String>();
        if(SystemState.isApplet) {
            cats.add("Background");
            cats.add("Cool People");
            cats.add("Speech Bubbles");
            cats.add("Weird People");
            cats.add("Uploaded Files");
        } else {
            File currentDir = new File("");
            String basePath = currentDir.getAbsolutePath();
            System.out.println(basePath);
            String path = basePath + "/assets";
            System.out.println(path);
            File categories = new File(path);
            File[] files = categories.listFiles();
            if (files != null) {
                for(File f : files) {
                    if(f.isDirectory()) {
                        cats.add(f.getName());
                    }
                }
            }

//            for(File f : files) {
//                if(f.isDirectory()) {
//                    cats.add(f.getName());
//                }
//            }


        }

        JList catList = new JList(cats);
        sp = new JScrollPane(images);
        ListIterator iter = cats.listIterator();
        if (iter.hasNext()) {
            showCategory(cats.firstElement());
            JScrollPane sp2 = new JScrollPane(catList);
            add(sp2,BorderLayout.EAST);
            add(sp,BorderLayout.CENTER);
            catList.addListSelectionListener(this);
        }

    }

    /*
     * showCategory: load images from category into the images JPanel (replacing old images).
     */
    public void showCategory(String category) {
        images.removeAll();
        File directory = new File("assets/"+category);
        for(File f : Objects.requireNonNull(directory.listFiles())) {
            images.add(new DraggableIcon("assets/"+category+"/"+f.getName()));
        }
        sp.revalidate();
        repaint();
    }

    /*
     * (non-Javadoc)
     * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
     * valueChanged: find out which category has been selected in the list, invoke showCategory.
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        JList list = (JList) e.getSource();
        String category = (String) list.getSelectedValue();
        showCategory(category);
    }
}
