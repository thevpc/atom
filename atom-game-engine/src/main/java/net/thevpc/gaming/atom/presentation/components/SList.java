/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.presentation.components;

import net.thevpc.gaming.atom.presentation.DefaultSceneComponent;
import net.thevpc.gaming.atom.presentation.DefaultSceneController;
import net.thevpc.gaming.atom.presentation.layers.LayerDrawingContext;
import net.thevpc.gaming.atom.presentation.SceneKeyEvent;
import net.thevpc.gaming.atom.util.AtomUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author vpc
 */
public class SList extends DefaultSceneComponent {

    private Integer selectedIndex = -1;
    private List<SListItem> values = new ArrayList<SListItem>();
    private TextStyleMap styleMap = new TextStyleMap();
    private ListComponentType listType = ListComponentType.VERTICAL;
    private int spacing = 0;
    private List<SceneComponentSelectionListener> listeners = new ArrayList<>();


    public SList() {
        super();
    }

    public SList(String name) {
        super(name);
        getTextStyle(SceneComponentState.DEFAULT).setForeColor(Color.BLACK);
        setFocusable(true);
        setController(new DefaultSceneController() {
            @Override
            public void keyPressed(SceneKeyEvent e) {
                keyPressedImpl(e);
            }
        });
    }

    public void add(Object object) {
        if (object instanceof SListItem) {
            add((SListItem) object);
        } else {
            add(new SListItem(object, null));
        }
    }

    public SList add(SListItem item) {
        values.add(item == null ? new SListItem(null, null) : item);
        return this;
    }

    public SList add(Object value, String label) {
       return add(new SListItem(value, label));
    }

    public int size() {
        return values.size();
    }

    public List<SListItem> getValues() {
        return Collections.unmodifiableList(values);
    }

    public void removeAt(int i) {
        values.remove(i);
    }

    public SList clear() {
        values.clear();
        return this;
    }

    public SListItem getSelectedItem() {
        if (getSelectedIndex() != null) {
            int i = getSelectedIndex();
            if (i >= 0 && i < values.size()) {
                return values.get(i);
            }
        }
        return null;
    }

    public Object getSelectedValue() {
        SListItem i = getSelectedItem();
        if (i != null) {
            return i.getValue();
        }
        return null;
    }

    public Integer getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(Integer selectedIndex) {
        int old = this.selectedIndex;
        this.selectedIndex = selectedIndex;
        if (old != selectedIndex) {
            for (SceneComponentSelectionListener listener : listeners) {
                listener.onSelectionChanged(new SceneComponentSelectionChangeEvent(
                        this, old, selectedIndex
                ));
            }
        }
    }

    public void addSelectionListener(SceneComponentSelectionListener li) {
        if (li != null && !listeners.contains(li)) {
            listeners.add(li);
        }
    }

    public void removeSelectionListener(SceneComponentSelectionListener li) {
        if (li != null && !listeners.contains(li)) {
            listeners.remove(li);
        }
    }

    @Override
    public void draw(LayerDrawingContext context) {
        if (!isVisible()) {
            return;
        }
        switch (listType) {
            case VERTICAL: {
                for (int i = 0; i < values.size(); i++) {
                    SListItem yy = values.get(i);
                    String text = yy.getLabel() == null ? String.valueOf(yy.getValue()) : yy.getLabel();
                    boolean selected = selectedIndex != null && selectedIndex == i;
                    int width1 = getWidth();
                    int height1 = getHeight();
                    int x1 = getX();
                    int y1 = getY();
                    y1 += +i * (height1 + spacing);
                    SceneComponentState status = SceneComponentState.DEFAULT;
                    if (hasFocus()) {
                        status=status.add(SceneComponentState.FOCUSED);
                    }
                    if (!isEnabled()) {
                        status=status.add(SceneComponentState.DISABLED);
                    }
                    if (selected) {
                        status=status.add(SceneComponentState.SELECTED);
                    }
                    TextStyle style = styleMap.getTextStyle(status);
                    AtomUtils.drawText(context, text, x1, y1, width1, height1, style, -1);
                }
                break;
            }
            case HORIZONTAL: {
                for (int i = 0; i < values.size(); i++) {
                    SListItem yy = values.get(i);
                    String text = yy.getLabel() == null ? String.valueOf(yy.getValue()) : yy.getLabel();
                    boolean selected = selectedIndex != null && selectedIndex == i;
                    int width1 = getWidth();
                    int height1 = getHeight();
                    int x1 = getX();
                    int y1 = getY();
                    x1 += +i * (width1 + spacing);
                    SceneComponentState status = SceneComponentState.DEFAULT;
                    if (hasFocus()) {
                        status=status.add(SceneComponentState.FOCUSED);
                    }
                    if (!isEnabled()) {
                        status=status.add(SceneComponentState.DISABLED);
                    }
                    if (selected) {
                        status=status.add(SceneComponentState.SELECTED);
                    }
                    TextStyle style = styleMap.getTextStyle(status);
                    AtomUtils.drawText(context, text, x1, y1, width1, height1, style, -1);
                }
                break;
            }
            case SINGLE: {
                SListItem yy = getSelectedItem();
                String text = yy == null ? null : yy.getLabel() == null ? String.valueOf(yy.getValue()) : yy.getLabel();
                boolean selected = true;
                int width1 = getWidth();
                int height1 = getHeight();
                int x1 = getX();
                int y1 = getY();
                SceneComponentState status = SceneComponentState.DEFAULT;
                if (hasFocus()) {
                    status=status.add(SceneComponentState.FOCUSED);
                }
                if (!isEnabled()) {
                    status=status.add(SceneComponentState.DISABLED);
                }
                if (selected) {
                    status=status.add(SceneComponentState.SELECTED);
                }
                TextStyle style = styleMap.getTextStyle(status);
                AtomUtils.drawText(context, text, x1, y1, width1, height1, style, -1);
                break;
            }
        }
    }

    public int getSpacing() {
        return spacing;
    }

    public void setSpacing(int spacing) {
        this.spacing = spacing;
    }

    protected void keyPressedImpl(SceneKeyEvent e) {
        switch (e.getKeyCode()) {
            case TAB: {
                propagateFocus(!e.isShiftPressed());
                e.setConsumed(true);
                break;
            }
            case DOWN: {
                int size = size();
                if (listType != ListComponentType.HORIZONTAL && size > 0) {
                    Integer i = getSelectedIndex();
                    if (i == null) {
                        i = 0;
                    } else {
                        i++;
                    }
                    if (i >= size) {
                        i = size - 1;
                    }
                    setSelectedIndex(i);
                    e.setConsumed(true);
                }
                break;
            }
            case UP: {
                int size = size();
                if (listType != ListComponentType.HORIZONTAL && size > 0) {
                    Integer i = getSelectedIndex();
                    if (i == null) {
                        i = 0;
                    } else {
                        i--;
                    }
                    if (i < 0) {
                        i = 0;
                    }
                    setSelectedIndex(i);
                    e.setConsumed(true);
                }
                break;
            }

            case LEFT: {
                int size = size();
                if (listType != ListComponentType.VERTICAL && size > 0) {
                    Integer i = getSelectedIndex();
                    if (i == null) {
                        i = 0;
                    } else {
                        i++;
                    }
                    if (i >= size) {
                        i = size - 1;
                    }
                    setSelectedIndex(i);
                    e.setConsumed(true);
                }
                break;
            }
            case RIGHT: {
                int size = size();
                if (listType != ListComponentType.VERTICAL && size > 0) {
                    Integer i = getSelectedIndex();
                    if (i == null) {
                        i = 0;
                    } else {
                        i--;
                    }
                    if (i < 0) {
                        i = 0;
                    }
                    setSelectedIndex(i);
                    e.setConsumed(true);
                }
                break;
            }

        }
    }

    public SList setTextStyle(SceneComponentState status, TextStyle style) {
        styleMap.setTextStyle(status, style);
        return this;
    }

    public TextStyle getTextStyle(SceneComponentState status) {
        return styleMap.getTextStyle(status);
    }
}
