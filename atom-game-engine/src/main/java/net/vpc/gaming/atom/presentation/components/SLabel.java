/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.presentation.components;

import net.vpc.gaming.atom.presentation.DefaultSceneComponent;
import net.vpc.gaming.atom.presentation.DefaultSceneController;
import net.vpc.gaming.atom.presentation.SceneKeyEvent;
import net.vpc.gaming.atom.presentation.layers.LayerDrawingContext;
import net.vpc.gaming.atom.util.AtomUtils;

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
        getTextStyle(TextStyle.DEFAULT).setForeColor(Color.BLACK);
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

    public void setText(String value) {
        this.text = value;
    }

    @Override
    public void draw(LayerDrawingContext context) {
        if (!isVisible()) {
            return;
        }
        int status = 0;
        if (isFocused()) {
            status |= TextStyle.FOCUSED;
        }
        if (!isEnabled()) {
            status |= TextStyle.DISABLED;
        }
        TextStyle style = styleMap.getTextStyle(status);
        AtomUtils.drawText(context, text, getX(), getY(), getWidth(), getHeight(), style, -1);
    }

    protected void keyPressedImpl(SceneKeyEvent e) {
        switch (e.getKeyCode()) {
            case SceneKeyEvent.VK_TAB: {
                propagateFocus(!e.isShiftPressed());
                e.setConsumed(true);
                break;
            }
        }
    }

    public void setTextStyle(TextStyle style, int status) {
        styleMap.setTextStyle(style, status);
    }

    public TextStyle getTextStyle(int status) {
        return styleMap.getTextStyle(status);
    }
}
