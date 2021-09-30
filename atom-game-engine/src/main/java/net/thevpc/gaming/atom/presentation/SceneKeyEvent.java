/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.presentation;

import net.thevpc.gaming.atom.engine.GameEngine;
import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.model.Player;
import net.thevpc.gaming.atom.presentation.layers.Layer;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Set;

/**
 * Event fired on key events
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class SceneKeyEvent {



    private int playerId;
    private Player controlPlayer;
    private long when;
    private int modifiers;
    private KeyCode keyCode;
    private char keyChar;
    private int keyLocation;
    private boolean consumed;
    private Scene scene;
    private Layer layer;
    private boolean disabled;
    private KeyCodeSet keyCodes;

    public SceneKeyEvent(Scene scene, Layer layer, long when, int modifiers, KeyCode keyCode, char keyChar, int keyLocation, KeyCode[] keyCodes) {
        this.scene = scene;
        this.layer = layer;
        this.when = when;
        this.modifiers = modifiers;
        this.keyCode = keyCode;
        this.keyChar = keyChar;
        this.keyCodes = KeyCodeSet.of(keyCodes);
        this.playerId = scene.getEventPlayer(0, 0, 0);
        this.controlPlayer = playerId < 0 ? null : scene.getSceneEngine().getPlayer(playerId);
    }

    public SceneKeyEvent(Scene scene, Layer layer, int playerId, long when, int modifiers, KeyCode keyCode, char keyChar, int keyLocation) {
        this.scene = scene;
        this.layer = layer;
        this.playerId = playerId;
        this.when = when;
        this.modifiers = modifiers;
        this.keyCode = keyCode;
        this.keyChar = keyChar;
        this.keyLocation = keyLocation;
        this.controlPlayer = playerId < 0 ? null : scene.getSceneEngine().getPlayer(playerId);
        this.keyCodes = KeyCodeSet.of(keyCode);
    }

    public Layer getLayer() {
        return layer;
    }

    public Scene getScene() {
        return scene;
    }

    public SceneEngine getSceneEngine() {
        return getScene().getSceneEngine();
    }

    public GameEngine getGameEngine() {
        return getSceneEngine().getGameEngine();
    }

    public Game getGame() {
        return getScene().getGame();
    }

    public int getPlayerId() {
        return playerId;
    }

    public char getKeyChar() {
        return keyChar;
    }

    public KeyCode getKeyCode() {
        return keyCode;
    }

    public int getKeyLocation() {
        return keyLocation;
    }

    public int getModifiers() {
        return modifiers;
    }

    public long getWhen() {
        return when;
    }


    public static String getKeyText(int... keyCodes) {
        StringBuilder b = new StringBuilder();
        for (int keyCode : keyCodes) {
            if (b.length() > 0) {
                b.append(",");
            }
            b.append(getKeyText(keyCode));
        }
        return "[" + b + "]";
    }

    public static String getKeyText(int keyCode) {
        return KeyEvent.getKeyText(keyCode);
    }

    public String getKeyText() {
        return KeyEvent.getKeyText(modifiers);
    }

    public boolean isConsumed() {
        return consumed;
    }

    public void setConsumed(boolean consumed) {
        this.consumed = consumed;
    }

    public KeyCodeSet getKeyCodes() {
        return keyCodes;
    }

    public boolean isCtrlPressed() {
        return ((getModifiers() & InputEvent.CTRL_MASK) != 0);
    }

    public boolean isShiftPressed() {
        return ((getModifiers() & InputEvent.SHIFT_MASK) != 0);
    }

    public boolean isAltPressed() {
        return ((getModifiers() & InputEvent.ALT_MASK) != 0);
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public String toString() {
        return "SceneKeyEvent{"
                + "playerId=" + playerId
                + ", controlPlayer=" + controlPlayer
                + ", when=" + when
                + ", modifiers=" + modifiers
                + ", keyCode=" + keyCode
                + ", keyChar=" + keyChar
                + ", keyLocation=" + keyLocation
                + ", consumed=" + consumed
                + ", scene=" + scene
                + ", layer=" + layer
                + ", disabled=" + disabled
                + ", keyCodes=" + keyCodes.toString()
                + '}';
    }
}
