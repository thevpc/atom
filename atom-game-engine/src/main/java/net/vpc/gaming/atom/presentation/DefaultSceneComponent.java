/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.presentation;
import net.vpc.gaming.atom.model.*;
import net.vpc.gaming.atom.model.RatioViewBox;
import net.vpc.gaming.atom.presentation.components.SceneComponent;
import net.vpc.gaming.atom.presentation.layers.LayerDrawingContext;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class DefaultSceneComponent implements SceneComponent {

    private ViewPoint location = new ViewPoint(0, 0, 0);
    private ViewDimension size = new ViewDimension(0, 0, 0);
    private Scene scene;
    private boolean visible = true;
    private boolean focusable = false;
    private boolean focused = false;
    private boolean enabled = true;
    private SceneController controller;
    private String name;

    public DefaultSceneComponent() {

    }

    public DefaultSceneComponent(String name) {
        this.name = name;
    }

    public boolean isFocusable() {
        return focusable;
    }

    public void setFocusable(boolean focusable) {
        this.focusable = focusable;
    }

    public boolean hasFocus() {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public ViewDimension getSize() {
        return size;
    }

    @Override
    public void setSize(RatioDimension size) {
        ViewDimension screen = scene.getAbsoluteCameraSize();
        setSize(new ViewDimension(
                (int) (screen.getWidth() * size.getWidth()),
                (int) (screen.getHeight() * size.getHeight())
        ));
    }

    @Override
    public ViewBox getBounds() {
        return new ViewBox(getLocation(), getSize());
    }

    @Override
    public void setBounds(RatioViewBox bounds) {
        ViewDimension screen = scene.getAbsoluteCameraSize();
        int w = screen.getWidth();
        int h = screen.getHeight();
        ViewBox b = new ViewBox(
                (int) (w * bounds.getX()),
                (int) (h * bounds.getY()),
                (int) (w * bounds.getWidth()),
                (int) (h * bounds.getHeight())

        );
        setBounds(b);
    }

    public void setBounds(ViewBox r) {
        setLocation(new ViewPoint(r.getX(), r.getY()));
        setSize(new ViewDimension(r.getWidth(), r.getHeight(), r.getAltitude()));
    }

    public void setSize(ViewDimension size) {
        this.size = size;
    }

    @Override
    public ViewDimension getSize(ViewDimension size) {
        return size;
    }

    public void addEventListener(SceneController listener) {
        //
    }

    public ViewPoint getLocation() {
        return location;
    }

    public void setLocation(ViewPoint position) {
        this.location = position;
    }

    public void setLocation(RatioPoint position) {
        ViewDimension screen = scene.getAbsoluteCameraSize();
        setLocation(new ViewPoint(
                (int) (screen.getWidth() * position.getX()),
                (int) (screen.getHeight() * position.getY())
        ));
    }

    public void draw(LayerDrawingContext par1) {
        //do nothing
    }

    public void nextFrame() {
        //do nothing
    }

    public void removeEventListener(SceneController listener) {
        //do nothing
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void start() {
        //
    }

    public int getHeight() {
        return size.getHeight();
    }

    public int getWidth() {
        return size.getWidth();
    }

    public int getX() {
        return getLocation().getX();
    }

    public int getY() {
        return getLocation().getY();
    }

    public SceneController getController() {
        return controller;
    }

    public void setController(SceneController controller) {
        this.controller = controller;
    }

    public String getName() {
        return name;
    }

    protected void propagateFocus(boolean toNext) {
        if (toNext) {
            String next = getScene().getNextFocusComponent(getName());
            if (next != null) {
                setFocused(false);
                getScene().getComponent(next).setFocused(true);
            }
        } else {
            String next = getScene().getPreviousFocusComponent(getName());
            if (next != null) {
                setFocused(false);
                getScene().getComponent(next).setFocused(true);
            }
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{name=" + name + ", visible=" + visible + ", focusable=" + focusable + ", focused=" + focused + '}';
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
