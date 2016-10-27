package net.vpc.gaming.atom.presentation.layers;

import net.vpc.gaming.atom.presentation.SceneKeyEvent;
import net.vpc.gaming.atom.presentation.SceneMouseEvent;

import java.awt.event.MouseEvent;

/**
 * Created with IntelliJ IDEA.
 * User: vpc
 * Date: 8/25/13
 * Time: 1:20 PM
 * To change this template use File | Settings | File Templates.
 */
public interface InteractiveLayer extends Layer {
    public SceneKeyEvent keyPressed(KeyEventExt e);

    public SceneKeyEvent keyReleased(KeyEventExt e);

    public SceneKeyEvent keyTyped(KeyEventExt e);

    public SceneMouseEvent mouseClicked(MouseEvent e);

    public SceneMouseEvent mouseDragged(MouseEvent e);

    public SceneMouseEvent mouseEntered(MouseEvent e);

    public SceneMouseEvent mouseExited(MouseEvent e);

    public SceneMouseEvent mouseMoved(MouseEvent e);

    public SceneMouseEvent mousePressed(MouseEvent e);

    public SceneMouseEvent mouseReleased(MouseEvent e);

}
