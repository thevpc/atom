package tn.rnu.enit.ateliercorba.jcanon.gameengine.view;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyAdapter;

/**
 * @author Taha Ben Salah (thevpc@walla.com)
 * @creationtime 5 dec. 2006 11:11:45
 */
public class FrameGameView implements GameView {
    private JFrame frame;

    public void setComponent(JComponent component){
        getFrame().add(component);
    }
    public void show(){
        getFrame().pack();
        getFrame().setVisible(true);
    }
    public JFrame getFrame(){
        if(frame==null){
            frame=new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent e) {
                    FrameGameView.this.keyPressed(e);
                }
            });
        }
        return frame;
    }

    public void addKeyListener(KeyListener l) {
        getFrame().addKeyListener(l);
    }

    public void keyPressed(KeyEvent event){

    }
}
