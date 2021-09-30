/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.presentation.components;

import net.thevpc.gaming.atom.model.*;
import net.thevpc.gaming.atom.model.Dimension;
import net.thevpc.gaming.atom.model.Point;
import net.thevpc.gaming.atom.presentation.*;
import net.thevpc.gaming.atom.presentation.layers.LayerDrawingContext;
import net.thevpc.gaming.atom.util.AtomUtils;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * @author Taha Ben Salah
 */
public class STextField extends DefaultSceneComponent {

    private String text;
    private TextStyleMap styleMap = new TextStyleMap();
    private int cursorPosition = 0;
    private int columns = 0;

    public STextField(String name, String value) {
        super(name);
        getTextStyle(SceneComponentState.DEFAULT).setForeColor(Color.BLACK);
        setFocusable(true);
        setVisible(true);
        this.text = value == null ? "" : value;
        cursorPosition = text.length();
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
        SceneComponentState status = SceneComponentState.DEFAULT;
        if (hasFocus()) {
            status=status.add(SceneComponentState.FOCUSED);
        }
        if (!isEnabled()) {
            status=status.add(SceneComponentState.DISABLED);
        }
        TextStyle style = styleMap.getTextStyle(status);

        AtomUtils.drawText(context, text, getX(), getY(), getWidth(), getHeight(), style, (isFocusable() && hasFocus()) ? cursorPosition : -1);
    }

    private void keyPressedImpl(SceneKeyEvent e) {
        switch (e.getKeyCode()) {
            case TAB: {
                propagateFocus(!e.isShiftPressed());
                e.setConsumed(true);
                break;
            }
            case DELETE: {
                if (text.length() > 0) {
                    if (cursorPosition >= text.length()) {
                        text = text.substring(0, text.length() - 1);
                    } else {
                        text = text.substring(0, cursorPosition) + text.substring(cursorPosition + 1);
                    }
                    e.setConsumed(true);
                }
                break;
            }
            case BACK_SPACE: {
                if (text.length() > 0) {
                    if (cursorPosition > 0) {
                        if (cursorPosition >= text.length()) {
                            text = text.substring(0, text.length() - 1);
                        } else {
                            text = text.substring(0, cursorPosition) + text.substring(cursorPosition + 1);
                        }
                        cursorPosition--;
                        e.setConsumed(true);
                    }
                }
                break;
            }
            case LEFT: {
                if (cursorPosition > 0) {
                    cursorPosition--;
                    e.setConsumed(true);
                }
                break;
            }
            case RIGHT: {
                if (cursorPosition < text.length()) {
                    cursorPosition++;
                    e.setConsumed(true);
                }
                break;
            }
            default: {
                char c = e.getKeyChar();
                Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
                boolean printable = (!Character.isISOControl(c))
                        && c != KeyEvent.CHAR_UNDEFINED
                        && block != null
                        && block != Character.UnicodeBlock.SPECIALS;
                if (printable && text.length() < columns) {
                    if (cursorPosition >= text.length()) {
                        text = text + e.getKeyChar();
                    } else {
                        text = text.substring(0, cursorPosition) + e.getKeyChar() + text.substring(cursorPosition);
                    }
                    cursorPosition++;
                    e.setConsumed(true);
                }
                break;
            }
        }
    }

    public int getColumns() {
        return columns;
    }

    public STextField setColumns(int columns) {
        this.columns = columns;
        return this;
    }

    public STextField setTextStyle(SceneComponentState status, TextStyle style) {
        styleMap.setTextStyle(status, style);
        return this;
    }

    public TextStyle getTextStyle(SceneComponentState status) {
        return styleMap.getTextStyle(status);
    }

    @Override
    public STextField setFocusable(boolean focusable) {
        return (STextField) super.setFocusable(focusable);
    }

    @Override
    public STextField setBounds(ViewBox r) {
        return (STextField) super.setBounds(r);
    }

    @Override
    public STextField setScene(Scene scene) {
        return (STextField) super.setScene(scene);
    }

    @Override
    public STextField setSize(Dimension size) {
        return (STextField) super.setSize(size);
    }

    @Override
    public STextField setLocation(Point position) {
        return (STextField) super.setLocation(position);
    }

    @Override
    public STextField setSize(RatioDimension size) {
        return (STextField) super.setSize(size);
    }

    @Override
    public STextField setBounds(RatioBox bounds) {
        return (STextField) super.setBounds(bounds);
    }

    @Override
    public STextField setVisible(boolean visible) {
        return (STextField) super.setVisible(visible);
    }

    @Override
    public STextField setFocused(boolean focused) {
        return (STextField) super.setFocused(focused);
    }

    @Override
    public STextField setController(SceneController controller) {
        return (STextField) super.setController(controller);
    }

    @Override
    public STextField setEnabled(boolean enabled) {
        return (STextField) super.setEnabled(enabled);
    }

    @Override
    public STextField setAlignment(Alignment alignment) {
        return (STextField) super.setAlignment(alignment);
    }
}
