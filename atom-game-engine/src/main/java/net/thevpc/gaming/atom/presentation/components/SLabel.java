/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.presentation.components;

import net.thevpc.gaming.atom.model.Point;
import net.thevpc.gaming.atom.model.RatioBox;
import net.thevpc.gaming.atom.model.RatioDimension;
import net.thevpc.gaming.atom.model.ViewBox;
import net.thevpc.gaming.atom.presentation.*;
import net.thevpc.gaming.atom.presentation.layers.LayerDrawingContext;
import net.thevpc.gaming.atom.util.AtomUtils;

import java.awt.*;

/**
 * @author Taha Ben Salah
 */
public class SLabel extends DefaultSceneComponent {

    private String text;
    private TextStyleMap styleMap = new TextStyleMap();

    public SLabel(String value) {
        this(null, value);
    }

    public SLabel(String name, String value) {
        super(name);
        this.text = value;
        getTextStyle(SceneComponentState.DEFAULT).setForeColor(Color.BLACK);
        setController(new DefaultSceneController() {
            @Override
            public void keyPressed(SceneKeyEvent e) {
                keyPressedImpl(e);
            }
        });
    }

    public String getText() {
        return text;
    }

    public SLabel setText(String value) {
        this.text = value;
        return this;
    }

    @Override
    public void draw(LayerDrawingContext context) {
        if (!isVisible()) {
            return;
        }
        SceneComponentState status = SceneComponentState.DEFAULT;
        if (hasFocus()) {
            status=status.add(SceneComponentState.FOCUSED);
        }
        if (!isEnabled()) {
            status=status.add(SceneComponentState.DISABLED);
        }
        TextStyle style = styleMap.getTextStyle(status);
        AtomUtils.drawText(context, text, getX(), getY(), getWidth(), getHeight(), style, -1);
    }

    protected void keyPressedImpl(SceneKeyEvent e) {
        switch (e.getKeyCode()) {
            case TAB: {
                propagateFocus(!e.isShiftPressed());
                e.setConsumed(true);
                break;
            }
        }
    }

    public SLabel setTextStyle(SceneComponentState status, TextStyle style) {
        styleMap.setTextStyle(status, style);
        return this;
    }

    public TextStyle getTextStyle(SceneComponentState status) {
        return styleMap.getTextStyle(status);
    }


    @Override
    public SLabel setFocusable(boolean focusable) {
        return (SLabel) super.setFocusable(focusable);
    }

    @Override
    public SLabel setBounds(ViewBox r) {
        return (SLabel) super.setBounds(r);
    }

    @Override
    public SLabel setScene(Scene scene) {
        return (SLabel) super.setScene(scene);
    }

    @Override
    public SLabel setSize(net.thevpc.gaming.atom.model.Dimension size) {
        return (SLabel) super.setSize(size);
    }

    @Override
    public SLabel setLocation(Point position) {
        return (SLabel) super.setLocation(position);
    }

    @Override
    public SLabel setSize(RatioDimension size) {
        return (SLabel) super.setSize(size);
    }

    @Override
    public SLabel setBounds(RatioBox bounds) {
        return (SLabel) super.setBounds(bounds);
    }

    @Override
    public SLabel setVisible(boolean visible) {
        return (SLabel) super.setVisible(visible);
    }

    @Override
    public SLabel setFocused(boolean focused) {
        return (SLabel) super.setFocused(focused);
    }

    @Override
    public SLabel setController(SceneController controller) {
        return (SLabel) super.setController(controller);
    }

    @Override
    public SLabel setEnabled(boolean enabled) {
        return (SLabel) super.setEnabled(enabled);
    }

    @Override
    public SLabel setAlignment(Alignment alignment) {
        return (SLabel) super.setAlignment(alignment);
    }
}
