/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.presentation.layers;

import net.thevpc.gaming.atom.engine.GameEngine;
import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.presentation.Game;
import net.thevpc.gaming.atom.presentation.Scene;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class LayerDrawingContext {

    private Graphics2D graphics;
    private Scene scene;
    private AffineTransform screenTransform;
    private AffineTransform boardTransform;
    private Map<String, Object> userObjects;

    public LayerDrawingContext(Graphics2D graphics, Scene scene, AffineTransform screenTransform, AffineTransform boardTransform) {
        this.graphics = graphics;
        this.scene = scene;
        this.screenTransform = screenTransform;
        this.boardTransform = boardTransform;
    }

    public Graphics2D getGraphics() {
        return graphics;
    }

    public AffineTransform getBoardTransform() {
        return boardTransform;
    }

    public AffineTransform getScreenTransform() {
        return screenTransform;
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

    public void setUserObject(String name, Object value) {
        if (value == null) {
            if (userObjects != null) {
                userObjects.remove(name);
            }
        } else {
            if (userObjects == null) {
                userObjects = new HashMap<>();
            }
            userObjects.put(name, value);
        }
    }

    public <T> T getUserObject(String name) {
        if (userObjects != null) {
            T v = (T) userObjects.get(name);
            return v;
        }
        return null;
    }

    public <T> T getUserObject(String name, T defaultObject) {
        if (userObjects != null) {
            T v = (T) userObjects.get(name);
            return (v == null ? defaultObject : v);
        }
        return defaultObject;
    }
}
