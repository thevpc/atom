/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.presentation;

import net.vpc.gaming.atom.model.ModelPoint;
import net.vpc.gaming.atom.model.Player;
import net.vpc.gaming.atom.model.ViewPoint;
import net.vpc.gaming.atom.presentation.layers.Layer;

import java.awt.event.InputEvent;

/**
 * Event fired on mouse events
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class SceneMouseEvent {

    private int playerId;
    private long when;
    private int modifiers;
    private double x;
    private double y;
    private int clickCount;
    boolean popupTrigger;
    private int button;
    private ModelPoint point;
    private int viewX;
    private int viewY;
    private ViewPoint viewPoint;
    private Layer layer;
    private Scene scene;
    private Object object;
    private Player controlPlayer;
    private boolean consumed;
    private boolean disabled;

    public SceneMouseEvent(Scene scene, int playerId, long when, int modifiers, double x, double y, int viewX, int viewY, int clickCount, boolean popupTrigger, int button, Layer layer, Object sprite) {
        this.scene = scene;
        this.when = when;
        this.modifiers = modifiers;
        this.x = x;
        this.y = y;
        this.viewX = viewX;
        this.viewY = viewY;
        this.clickCount = clickCount;
        this.popupTrigger = popupTrigger;
        this.button = button;
        this.point = new ModelPoint(x, y, 0);
        this.viewPoint = new ViewPoint(viewX, viewY);
        this.playerId = playerId;
        this.layer = layer;
        this.object = sprite;
        this.controlPlayer = playerId < 0 ? null : scene.getSceneEngine().getPlayer(playerId);
    }

    public Scene getScene() {
        return scene;
    }

    public Player getControlPlayer() {
        return controlPlayer;
    }

    //    public GameMouseEvent(int playerId, long when, int modifiers, double x, double y, int clickCount, boolean popupTrigger, int button) {
//        this.playerId = playerId;
//        this.when = when;
//        this.modifiers = modifiers;
//        this.x = x;
//        this.y = y;
//        this.clickCount = clickCount;
//        this.popupTrigger = popupTrigger;
//        this.button = button;
//        this.point = new DPoint(x, y);
//    }
    public int getPlayerId() {
        return playerId;
    }

    public boolean isLeftMouseButton() {
        return ((getModifiers() & InputEvent.BUTTON1_MASK) != 0);
    }

    public boolean isMiddleMouseButton() {
        return ((getModifiers() & InputEvent.BUTTON2_MASK) == InputEvent.BUTTON2_MASK);
    }

    public boolean isRightMouseButton() {
        return ((getModifiers() & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK);
    }

    /**
     * Returns whether or not the Shift modifier is down on this event.
     */
    public boolean isShiftDown() {
        return (modifiers & InputEvent.SHIFT_MASK) != 0;
    }

    /**
     * Returns whether or not the Control modifier is down on this event.
     */
    public boolean isControlDown() {
        return (modifiers & InputEvent.CTRL_MASK) != 0;
    }

    /**
     * Returns whether or not the Meta modifier is down on this event.
     */
    public boolean isMetaDown() {
        return (modifiers & InputEvent.META_MASK) != 0;
    }

    /**
     * Returns whether or not the Alt modifier is down on this event.
     */
    public boolean isAltDown() {
        return (modifiers & InputEvent.ALT_MASK) != 0;
    }

    /**
     * Returns whether or not the AltGraph modifier is down on this event.
     */
    public boolean isAltGraphDown() {
        return (modifiers & InputEvent.ALT_GRAPH_MASK) != 0;
    }

    public int getButton() {
        return button;
    }

    public int getClickCount() {
        return clickCount;
    }

    public int getModifiers() {
        return modifiers;
    }

    public boolean isPopupTrigger() {
        return popupTrigger;
    }

    public long getWhen() {
        return when;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public ModelPoint getPoint() {
        return point;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public ViewPoint getViewPoint() {
        return viewPoint;
    }

    public int getViewX() {
        return viewX;
    }

    public int getViewY() {
        return viewY;
    }

    public boolean isConsumed() {
        return consumed;
    }

    public void setConsumed(boolean consumed) {
        this.consumed = consumed;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public Layer getLayer() {
        return layer;
    }

    public Object getObject() {
        return object;
    }

}
