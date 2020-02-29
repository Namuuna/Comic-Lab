package Controller;

import Model.CanvasContainer;
import Model.History;
import Model.RootWindow;
import comicApplet.Applet;

import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JToolTip;

public class SystemState {

    public static boolean unsaved = false;
    public static CanvasContainer canvasPointer;
    public static Component glassPane;
    public static RootWindow rootPane;
    public static History history;
    public static File currentFile;
    public static boolean retainAspect = false;
    public static Vector<String> errors = new Vector<String>();
    protected static boolean paintMode = false;
    public static boolean isApplet = false;
}

