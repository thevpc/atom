/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.presentation.components;
import net.thevpc.gaming.atom.model.*;
import net.thevpc.gaming.atom.presentation.SceneController;
import net.thevpc.gaming.atom.presentation.layers.LayerDrawingContext;
import net.thevpc.gaming.atom.model.RatioBox;
import net.thevpc.gaming.atom.presentation.Scene;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public interface SceneComponent {

     String getName();

     SceneComponent setScene(Scene view);

     void start();

     void draw(LayerDrawingContext context);

     void nextFrame();

     SceneComponent addEventListener(SceneController listener);

     SceneComponent removeEventListener(SceneController listener);

     ViewPoint getLocation();

     SceneComponent setSize(Dimension size);

     SceneComponent setLocation(Point position);

     SceneComponent setSize(RatioDimension size);

     Dimension getSize();

     ViewDimension getViewSize();

     ViewBox getViewBounds();

     SceneComponent setBounds(RatioBox bounds);

     boolean isVisible();

     SceneComponent setVisible(boolean visible);

     boolean isFocusable();

     boolean hasFocus();

     SceneComponent setFocused(boolean focused);

     SceneController getController();
}
