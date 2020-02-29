package Model;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.List;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import javax.imageio.ImageIO;

public class History {
    private Vector<byte[]> history;
    private int historyIndex;
    private ByteArrayInputStream bis;
    private ObjectInputStream in;
    private ByteArrayOutputStream bos;
    private ObjectOutputStream out;

    //Using JBoss supposedly makes serialization quicker, but doesn't work for this
    //at the moment. Investigate later.
    //private JBossObjectInputStream in;
    //private JBossObjectOutputStream out;

    History(Canvas c) {
        history = new Vector<byte[]>();
        addToHistory(c);
        historyIndex = 0;
    }

    private Canvas getCanvasAt(int i) throws ArrayIndexOutOfBoundsException {
        try{
            bis = new ByteArrayInputStream(
                    history.elementAt(i)
            );
            in = new ObjectInputStream(bis);
            Canvas tmp = (Canvas)in.readObject();
            for(CanvasIcon x : tmp.items) {
                x.setupImage();
            }
            return tmp;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addToHistory(Canvas c) {
        bos = new ByteArrayOutputStream();
        try {
            out = new ObjectOutputStream(bos);
            if(history.size() > 0) {
                for(int i=history.size()-1; i>historyIndex; i--) {
                    history.remove(i);
                }
            }
            out.writeObject(c);
            history.add(bos.toByteArray());
            historyIndex++;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Canvas undo() {
        if(historyIndex > 0) historyIndex--;
        return getCanvasAt(historyIndex);
    }

    public Canvas redo() {
        try{
            if(historyIndex < history.size()) historyIndex++;
            else{ historyIndex = history.size()-1; }
            return getCanvasAt(historyIndex);
        } catch(ArrayIndexOutOfBoundsException e) { return getCanvasAt(history.size()-1); }
    }

    public void clear(Canvas c) {
        history.clear();
        addToHistory(c);
        historyIndex = 0;
    }
}
