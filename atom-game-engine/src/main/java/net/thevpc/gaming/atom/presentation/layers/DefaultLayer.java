/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.presentation.layers;

import net.thevpc.gaming.atom.model.ModelPoint;
import net.thevpc.gaming.atom.model.SceneModel;
import net.thevpc.gaming.atom.model.ViewBox;
import net.thevpc.gaming.atom.model.ViewPoint;
import net.thevpc.gaming.atom.presentation.KeyCode;
import net.thevpc.gaming.atom.presentation.Scene;
import net.thevpc.gaming.atom.presentation.SceneKeyEvent;
import net.thevpc.gaming.atom.presentation.SceneMouseEvent;

import java.awt.event.MouseEvent;
import java.util.Arrays;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public abstract class DefaultLayer implements Layer {

    private int layer = SCREEN_BACKGROUND_LAYER;
    private Scene scene;
    private boolean enabled = true;

    public DefaultLayer() {
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void nextFrame() {
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Layer other = (Layer) obj;
        if (this.layer != other.getLayer()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + this.layer;
        return hash;
    }

    public SceneKeyEvent keyPressed(KeyEventExt e) {
        return createSceneKeyEvent(e);
    }

    public SceneKeyEvent keyReleased(KeyEventExt e) {
        return createSceneKeyEvent(e);
    }

    public SceneKeyEvent keyTyped(KeyEventExt e) {
        return createSceneKeyEvent(e);
    }

    public SceneMouseEvent mouseClicked(MouseEvent e) {
        return createSceneMouseEvent(e);
    }

    public SceneMouseEvent mouseDragged(MouseEvent e) {
        return createSceneMouseEvent(e);
    }

    public SceneMouseEvent mouseEntered(MouseEvent e) {
        return createSceneMouseEvent(e);
    }

    public SceneMouseEvent mouseExited(MouseEvent e) {
        return createSceneMouseEvent(e);
    }

    public SceneMouseEvent mouseMoved(MouseEvent e) {
        return createSceneMouseEvent(e);
    }

    public SceneMouseEvent mousePressed(MouseEvent e) {
        return createSceneMouseEvent(e);
    }

    public SceneMouseEvent mouseReleased(MouseEvent e) {
        return createSceneMouseEvent(e);
    }

    protected SceneKeyEvent createSceneKeyEvent(KeyEventExt e) {
        return null;
    }

    protected SceneMouseEvent createSceneMouseEvent(MouseEvent e) {
        return null;
    }

    protected SceneKeyEvent toSceneKeyEvent(KeyEventExt e, Layer layer) {
        return new SceneKeyEvent(
                getScene(),
                this,
                e.keyEvent.getWhen(),
                e.keyEvent.getModifiers(),
                KeyCode.of(e.keyEvent.getKeyCode()),
                e.keyEvent.getKeyChar(),
                e.keyEvent.getKeyLocation(),
                e.pressedKeys
                );
    }

    protected SceneMouseEvent toSceneMouseEvent(MouseEvent e, Object object) {
        Scene s = getScene();
        ViewBox rect = s.getCamera().getViewBounds();
        int viewX = e.getX();
        int viewY = e.getY();
        SceneModel sceneModel = s.getModel();
        double modelX = (viewX + rect.getX()) / sceneModel.getTileSize().getWidth();
        double modelY = (viewY + rect.getY()) / sceneModel.getTileSize().getHeight();
        double modelZ = 0;
        int playerId = s.getEventPlayer(new ModelPoint(modelX, modelY, modelZ), new ViewPoint(viewX, viewY), e.getClickCount(), e.getButton(), e.getModifiers());
        return new SceneMouseEvent(
                s,
                playerId,
                e.getWhen(),
                e.getModifiers(),
                modelX, modelY,
                viewX, viewY,
                e.getClickCount(),
                e.isPopupTrigger(),
                e.getButton(),
                this, object);
    }

}
