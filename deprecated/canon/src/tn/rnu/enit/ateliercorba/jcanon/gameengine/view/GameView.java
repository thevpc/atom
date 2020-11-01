package tn.rnu.enit.ateliercorba.jcanon.gameengine.view;

import javax.swing.*;
import java.awt.event.KeyEvent;

/**
 * @author Taha Ben Salah (thevpc@walla.com)
 * @creationtime 5 dec. 2006 11:13:50
 */
public interface GameView {
    void setComponent(JComponent component);
    public void keyPressed(KeyEvent event);
}
