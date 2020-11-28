/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.presentation;

import net.thevpc.gaming.atom.model.*;
import net.thevpc.gaming.atom.presentation.components.STextField;
import net.thevpc.gaming.atom.presentation.components.SceneComponent;
import net.thevpc.gaming.atom.presentation.layers.LayerDrawingContext;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class DefaultSceneComponent implements SceneComponent {

    private Point location = new ViewPoint(0, 0, 0);
    private Dimension size = new ViewDimension(0, 0, 0);
    private Scene scene;
    private boolean visible = true;
    private boolean focusable = false;
    private boolean focused = false;
    private boolean enabled = true;
    private SceneController controller;
    private String name;
    private Alignment alignment = Alignment.CENTER;

    public DefaultSceneComponent() {

    }

    public DefaultSceneComponent(String name) {
        this.name = name;
    }

    public SceneComponent setFocusable(boolean focusable) {
        this.focusable = focusable;
        return this;
    }

    public Dimension getSize() {
        return size;
    }

    public ViewDimension getViewSize() {
        if (size instanceof ViewDimension) {
            return (ViewDimension) size;
        }
        if (size instanceof ModelDimension) {
            return getValidScene().toViewDimension((ModelDimension) scene);
        }
        if (size instanceof RatioDimension) {
            return getValidScene().toViewDimension((RatioDimension) scene);
        }
        return (ViewDimension) scene;
    }

    protected Scene getValidScene() {
        if (scene == null) {
            throw new IllegalStateException("Component is not yet bound to a scene");
        }
        return scene;
    }

    public SceneComponent setBounds(ViewBox r) {
        setLocation(new ViewPoint(r.getX(), r.getY()));
        setSize(new ViewDimension(r.getWidth(), r.getHeight(), r.getAltitude()));
        return this;
    }

    public Scene getScene() {
        return scene;
    }

    public SceneComponent setScene(Scene scene) {
        this.scene = scene;
        return this;
    }

    public void start() {
        //
    }

    public void draw(LayerDrawingContext par1) {
        //do nothing
    }

    public void nextFrame() {
        //do nothing
    }

    public SceneComponent addEventListener(SceneController listener) {
        //
        return this;
    }

    public SceneComponent removeEventListener(SceneController listener) {
        //do nothing
        return this;
    }

    public ViewPoint getLocation() {
        return getValidScene().toViewPoint(location);
    }

    public SceneComponent setSize(Dimension size) {
        this.size = size;
        return this;
    }

    public SceneComponent setLocation(Point position) {
        this.location = position;
        return this;
    }

    @Override
    public SceneComponent setSize(RatioDimension size) {
        ViewDimension screen = scene.getCamera().getViewDimension();
        setSize(new ViewDimension(
                (int) (screen.getWidth() * size.getWidth()),
                (int) (screen.getHeight() * size.getHeight())
        ));
        return this;
    }



    @Override
    public ViewBox getViewBounds() {
        return new ViewBox(getLocation(), getViewSize());
    }

    @Override
    public SceneComponent setBounds(RatioBox bounds) {
        ViewDimension screen = scene.getCamera().getViewDimension();
        int w = screen.getWidth();
        int h = screen.getHeight();
        ViewBox b = new ViewBox(
                (int) (w * bounds.getX()),
                (int) (h * bounds.getY()),
                (int) (w * bounds.getWidth()),
                (int) (h * bounds.getHeight())

        );
        setBounds(b);
        return this;
    }

    public boolean isVisible() {
        return visible;
    }

    public SceneComponent setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public boolean isFocusable() {
        return focusable;
    }

    public boolean hasFocus() {
        return focused;
    }

    public SceneComponent setFocused(boolean focused) {
        this.focused = focused;
        return this;
    }

    public SceneController getController() {
        return controller;
    }

    public int getHeight() {
        return getViewSize().getHeight();
    }

    public int getWidth() {
        return getViewSize().getWidth();
    }

    public int getX() {
        return getLocation().getX();
    }

    public int getY() {
        return getLocation().getY();
    }

    public SceneComponent setController(SceneController controller) {
        this.controller = controller;
        return this;
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

    public SceneComponent setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public Alignment getAlignment() {
        return alignment;
    }

    public SceneComponent setAlignment(Alignment alignment) {
        this.alignment = alignment;
        return this;
    }
}
