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
    private boolean selected;
    private TextStyleMap styleMap = new TextStyleMap();
    private java.util.List<SceneComponentActionListener> listeners = new ArrayList<>();

    public SButton(String name) {
        this(name,name);
    }

    public SButton(String name, String value) {
        super(name);
        getTextStyle(SceneComponentState.DEFAULT).setForeColor(Color.BLACK);
        getTextStyle(SceneComponentState.DEFAULT).setBorderArc(3);
        getTextStyle(SceneComponentState.DEFAULT).setFillBackground(true);
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

    public SButton setText(String value) {
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
        if (!isSelected()) {
            status=status.add(SceneComponentState.SELECTED);
        }
        TextStyle style = styleMap.getTextStyle(status);
        AtomUtils.drawText(context, text, getX(), getY(), getWidth(), getHeight(), style, -1);
    }

    public boolean isSelected() {
        return selected;
    }

    public SButton setSelected(boolean selected) {
        this.selected = selected;
        return this;
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

    public SButton setTextStyle(SceneComponentState status, TextStyle style) {
        styleMap.setTextStyle(status, style);
        return this;
    }

    public TextStyle getTextStyle(SceneComponentState status) {
        return styleMap.getTextStyle(status);
    }
}
