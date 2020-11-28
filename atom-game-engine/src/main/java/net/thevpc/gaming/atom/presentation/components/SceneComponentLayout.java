/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.presentation.components;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class SceneComponentLayout {

    private SceneComponent component;
    private Object constraints;
    private int layer;
    private boolean isometric = false;

    public SceneComponentLayout(SceneComponent component, Object constraints, int layer, boolean isometric) {
        this.component = component;
        this.constraints = constraints;
        this.layer = layer;
        this.isometric = isometric;
    }

    public boolean isIsometric() {
        return isometric;
    }

    public int getLayer() {
        return layer;
    }

    public SceneComponent getComponent() {
        return component;
    }

    public Object getConstraints() {
        return constraints;
    }

    @Override
    public String toString() {
        return "SceneComponentLayout{" + "component=" + component + ", constraints=" + constraints + ", layer=" + layer + ", isometric=" + isometric + '}';
    }
}
