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
public class STextField extends DefaultSceneComponent {

    private String text;
    private TextStyleMap styleMap = new TextStyleMap();
    private int cursorPosition = 0;
    private int columns = 0;

    public STextField(String name, String value) {
        super(name);
        getTextStyle(TextStyle.DEFAULT).setForeColor(Color.BLACK);
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
        int status = 0;
        if (hasFocus()) {
            status |= TextStyle.FOCUSED;
        }
        if (!isEnabled()) {
            status |= TextStyle.DISABLED;
        }
        TextStyle style = styleMap.getTextStyle(status);

        AtomUtils.drawText(context, text, getX(), getY(), getWidth(), getHeight(), style, (isFocusable() && hasFocus()) ? cursorPosition : -1);
    }

    private void keyPressedImpl(SceneKeyEvent e) {
        switch (e.getKeyCode()) {
            case SceneKeyEvent.VK_TAB: {
                propagateFocus(!e.isShiftPressed());
                e.setConsumed(true);
                break;
            }
            case SceneKeyEvent.VK_DELETE: {
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
            case SceneKeyEvent.VK_BACK_SPACE: {
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
            case SceneKeyEvent.VK_LEFT: {
                if (cursorPosition > 0) {
                    cursorPosition--;
                    e.setConsumed(true);
                }
                break;
            }
            case SceneKeyEvent.VK_RIGHT: {
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
                        && c != SceneKeyEvent.CHAR_UNDEFINED
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

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public void setTextStyle(TextStyle style, int status) {
        styleMap.setTextStyle(style, status);
    }

    public TextStyle getTextStyle(int status) {
        return styleMap.getTextStyle(status);
    }
}
