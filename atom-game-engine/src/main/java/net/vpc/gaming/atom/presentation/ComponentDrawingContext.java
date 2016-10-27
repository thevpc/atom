/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.presentation;

import net.vpc.gaming.atom.model.ViewPoint;
import net.vpc.gaming.atom.presentation.components.SceneComponent;

import java.awt.*;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class ComponentDrawingContext {

    private SceneComponent component;
    private Scene sceneView;
    private ViewPoint position;
    private Graphics2D graphics;

    public ComponentDrawingContext(SceneComponent component, Scene scene, ViewPoint location, Graphics2D graphics) {
        this.component = component;
        this.sceneView = scene;
        this.position = location;
        this.graphics = graphics;
    }

    public Graphics2D getGraphics() {
        return graphics;
    }

    public ViewPoint getLocation() {
        return position;
    }

    public SceneComponent getSprite() {
        return component;
    }

    public Scene getScene() {
        return sceneView;
    }
}
