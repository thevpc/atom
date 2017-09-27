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
import java.util.ArrayList;

/**
 * @author Taha Ben Salah
 */
public class SButton extends DefaultSceneComponent {

    private String text;
    private TextStyleMap styleMap = new TextStyleMap();
    private java.util.List<SceneComponentActionListener> listeners = new ArrayList<>();

    public SButton(String name, String value) {
        super(name);
        getTextStyle(TextStyle.DEFAULT).setForeColor(Color.BLACK);
        getTextStyle(TextStyle.DEFAULT).setBorderArc(3);
        getTextStyle(TextStyle.DEFAULT).setFillBackground(true);
        this.text = value;
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
        if (hasFocus()) {
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
            case SceneKeyEvent.VK_ENTER: {
                if (hasFocus()) {
                    for (SceneComponentActionListener listener : listeners) {
                        listener.onAction(new SceneComponentActionEvent(this));
                    }
                }
                break;
            }
        }
    }

    public void addSelectionListener(SceneComponentActionListener li) {
        if (li != null && !listeners.contains(li)) {
            listeners.add(li);
        }
    }

    public void removeSelectionListener(SceneComponentActionListener li) {
        if (li != null && !listeners.contains(li)) {
            listeners.remove(li);
        }
    }

    public void setTextStyle(TextStyle style, int status) {
        styleMap.setTextStyle(style, status);
    }

    public TextStyle getTextStyle(int status) {
        return styleMap.getTextStyle(status);
    }
}
