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

    public void addEventListener(SceneController listener);

    public void removeEventListener(SceneController listener);

    public ViewPoint getLocation();

    public void setSize(ViewDimension size);

    public void setLocation(ViewPoint position);

    public void setLocation(RatioPoint position);

    public void setSize(RatioDimension size);

    public ViewDimension getSize(ViewDimension size);

    public ViewBox getBounds();

    public void setBounds(RatioViewBox bounds);

    public boolean isVisible();

    public void setVisible(boolean visible);

    public boolean isFocusable();

    public boolean isFocused();

    public void setFocused(boolean focused);

    public SceneController getController();
}
