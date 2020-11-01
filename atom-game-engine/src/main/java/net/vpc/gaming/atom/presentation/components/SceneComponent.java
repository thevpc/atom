/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.presentation.components;
import net.vpc.gaming.atom.model.*;
import net.vpc.gaming.atom.model.RatioViewBox;
import net.vpc.gaming.atom.presentation.Scene;
import net.vpc.gaming.atom.presentation.SceneController;
import net.vpc.gaming.atom.presentation.layers.LayerDrawingContext;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public interface SceneComponent {

    public String getName();

    public void setScene(Scene view);

    public void start();

    public void draw(LayerDrawingContext context);

    public void nextFrame();

    public SceneComponent addEventListener(SceneController listener);

    public SceneComponent removeEventListener(SceneController listener);

    public ViewPoint getLocation();

    public SceneComponent setSize(ViewDimension size);

    public SceneComponent setLocation(ViewPoint position);

    public SceneComponent setLocation(RatioPoint position);

    public SceneComponent setSize(RatioDimension size);

    public ViewDimension getSize(ViewDimension size);

    public ViewBox getBounds();

    public SceneComponent setBounds(RatioViewBox bounds);

    public boolean isVisible();

    public SceneComponent setVisible(boolean visible);

    public boolean isFocusable();

    public boolean hasFocus();

    public SceneComponent setFocused(boolean focused);

    public SceneController getController();
}
