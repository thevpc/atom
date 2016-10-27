/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.presentation.components;

import net.vpc.gaming.atom.presentation.SequenceGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author vpc
 */
public class TextStyle {

    public static final int DEFAULT = 0;
    public static final int DISABLED = 1;
    public static final int FOCUSED = 2;
    public static final int SELECTED = 4;
    public static final int HOVER = 8;
    private List<TextStyle> baseStyles = new ArrayList<TextStyle>();
    private String name;
    private Font font;
    private Color foreColor;
    private Color backgroundColor;
    private Color backgroundColor2;
    private Color cursorColor;
    private Alignment alignment;
    private Integer borderWidth;
    private Integer borderArc;
    private Color borderColor;
    private Integer blinkForegroundPeriod;
    private Integer blinkBackgroundPeriod;
    private Integer blinkBorderPeriod;
    private Integer blinkCursorPeriod;
    private Integer insetX;
    private Integer insetY;
    private Boolean fillBackground;
    private SequenceGenerator _blinkForegroundHelper = SequenceGenerator.createUnsignedSequence(10, 10);
    private Integer _blinkForegroundHelperVal = null;
    private SequenceGenerator _blinkBackgroundHelper = SequenceGenerator.createUnsignedSequence(10, 10);
    private Integer _blinkBackgroundHelperVal = null;
    private SequenceGenerator _blinkBorderHelper = SequenceGenerator.createUnsignedSequence(10, 10);
    private Integer _blinkBorderHelperVal = null;
    private SequenceGenerator _blinkCursorHelper = SequenceGenerator.createUnsignedSequence(10, 10);
    private Integer _blinkCursorHelperVal = null;

    public TextStyle(String name) {
        this.name = name;
    }

    public TextStyle(String name, TextStyle other) {
        setName(name);
        setAll(other);
    }

    public void setAll(TextStyle other) {
        if (other != null) {
            setFont(other.font);
            setForeColor(other.foreColor);
            setBackgroundColor(other.backgroundColor);
            setBackgroundColor2(other.backgroundColor2);
            setAlignment(other.alignment);
            setBorderColor(other.borderColor);
            setBorderWidth(other.borderWidth);
            setBlinkForegroundPeriod(other.blinkForegroundPeriod);
            setBlinkBorderPeriod(other.blinkBorderPeriod);
            setBlinkBackgroundPeriod(other.blinkBackgroundPeriod);
            setBlinkCursorPeriod(other.blinkCursorPeriod);
            setInsetX(other.insetY);
            setInsetY(other.insetX);
            setFillBackground(other.fillBackground);
            setCursorColor(other.cursorColor);
            setBorderArc(other.borderArc);
        }
    }

    public List<TextStyle> getBaseStyles() {
        return baseStyles;
    }

    public void setBaseStyles(List<TextStyle> baseStyles) {
        this.baseStyles = baseStyles;
    }

    private <T> T getInheritedProperty(String name) {
        T v = getProperty(name);
        if (v == null && baseStyles != null) {
            ArrayList<TextStyle> visited = new ArrayList<>();
            LinkedList<TextStyle> next = new LinkedList<>();
            next.addAll(baseStyles);
            while (!next.isEmpty()) {
                TextStyle textStyle = next.removeFirst();
                visited.add(textStyle);
                T v2 = textStyle.getProperty(name);
                if (v2 != null) {
                    return v2;
                }
                if (textStyle.getBaseStyles() != null) {
                    for (TextStyle style2 : textStyle.getBaseStyles()) {
                        boolean alreadyVisited = false;
                        for (TextStyle other : visited) {
                            if (style2 == other) {
                                alreadyVisited = true;
                            }
                        }
                        if (!alreadyVisited) {
                            next.addLast(style2);
                        }
                    }
                }
            }
        }
        return v;
    }

    private <T> T getProperty(String name) {
        switch (name) {
            case "name": {
                return (T) name;
            }
            case "alignment": {
                return (T) alignment;
            }
            case "backgroundColor": {
                return (T) backgroundColor;
            }
            case "backgroundColor2": {
                return (T) backgroundColor2;
            }
            case "blinkBackgroundPeriod": {
                return (T) blinkBackgroundPeriod;
            }
            case "blinkBorderPeriod": {
                return (T) blinkBorderPeriod;
            }
            case "blinkCursorPeriod": {
                return (T) blinkCursorPeriod;
            }
            case "blinkForegroundPeriod": {
                return (T) blinkForegroundPeriod;
            }
            case "borderColor": {
                return (T) borderColor;
            }
            case "borderWidth": {
                return (T) borderWidth;
            }
            case "cursorColor": {
                return (T) cursorColor;
            }
            case "fillBackground": {
                return (T) fillBackground;
            }
            case "font": {
                return (T) font;
            }
            case "foreColor": {
                return (T) foreColor;
            }
            case "insetX": {
                return (T) insetX;
            }
            case "insetY": {
                return (T) insetY;
            }
            case "borderArc": {
                return (T) borderArc;
            }
        }
        throw new IllegalArgumentException("Unknwon property " + name);
    }

    public Font getFont() {
        return getInheritedProperty("font");
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Color getForeColor() {
        return getInheritedProperty("foreColor");
    }

    public void setForeColor(Color foreColor) {
        this.foreColor = foreColor;
    }

    public Color getBackgroundColor() {
        return getInheritedProperty("backgroundColor");
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Color getBackgroundColor2() {
        return getInheritedProperty("backgroundColor2");
    }

    public void setBackgroundColor2(Color backgroundColor2) {
        this.backgroundColor2 = backgroundColor2;
    }

    public Integer getBlinkForegroundPeriod() {
        return getInheritedProperty("blinkForegroundPeriod");
    }

    public void setBlinkForegroundPeriod(Integer blinkPeriod) {
        this.blinkForegroundPeriod = blinkPeriod;
    }

    public SequenceGenerator getBlinkForegroundHelper() {
        Integer p = getBlinkForegroundPeriod();
        if (p == null) {
            _blinkForegroundHelperVal = null;
            _blinkForegroundHelper = null;
            return null;
        } else {
            if (_blinkForegroundHelperVal == null || !_blinkForegroundHelperVal.equals(p)) {
                _blinkForegroundHelperVal = p;
                _blinkForegroundHelper = SequenceGenerator.createUnsignedSequence(p, p);
            }
            return _blinkForegroundHelper;
        }
    }

    public Integer getBlinkBackgroundPeriod() {
        if (blinkBackgroundPeriod == null && baseStyles != null) {
            for (TextStyle textStyle : baseStyles) {
                if (textStyle.getBlinkBackgroundPeriod() != null) {
                    return textStyle.getBlinkBackgroundPeriod();
                }
            }
        }
        return blinkBackgroundPeriod;
    }

    public void setBlinkBackgroundPeriod(Integer blinkPeriod) {
        this.blinkBackgroundPeriod = blinkPeriod;
    }

    public SequenceGenerator getBlinkBackgroundHelper() {
        Integer p = getBlinkBackgroundPeriod();
        if (p == null) {
            _blinkBackgroundHelperVal = null;
            _blinkBackgroundHelper = null;
            return null;
        } else {
            if (_blinkBackgroundHelperVal == null || !_blinkBackgroundHelperVal.equals(p)) {
                _blinkBackgroundHelperVal = p;
                _blinkBackgroundHelper = SequenceGenerator.createUnsignedSequence(p, p);
            }
            return _blinkBackgroundHelper;
        }
    }

    public Integer getBlinkBorderPeriod() {
        return getInheritedProperty("blinkBorderPeriod");
    }

    public void setBlinkPeriod(Integer blinkPeriod) {
        setBlinkBackgroundPeriod(blinkPeriod);
        setBlinkBorderPeriod(blinkPeriod);
        setBlinkForegroundPeriod(blinkPeriod);
        setBlinkCursorPeriod(blinkPeriod);
    }

    public void setBlinkBorderPeriod(Integer blinkPeriod) {
        this.blinkBorderPeriod = blinkPeriod;
    }

    public SequenceGenerator getBlinkBorderHelper() {
        Integer p = getBlinkBorderPeriod();
        if (p == null) {
            _blinkBorderHelperVal = null;
            _blinkBorderHelper = null;
            return null;
        } else {
            if (_blinkBorderHelperVal == null || !_blinkBorderHelperVal.equals(p)) {
                _blinkBorderHelperVal = p;
                _blinkBorderHelper = SequenceGenerator.createUnsignedSequence(p, p);
            }
            return _blinkBorderHelper;
        }
    }

    public Alignment getAlignment() {
        return getInheritedProperty("alignment");
    }

    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
    }

    public Integer getBorderWidth() {
        return getInheritedProperty("borderWidth");
    }

    public void setBorderWidth(Integer borderWidth) {
        this.borderWidth = borderWidth;
    }

    public Color getBorderColor() {
        return getInheritedProperty("borderColor");
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public Integer getBorderArc() {
        return getInheritedProperty("borderArc");
    }

    public void setBorderArc(Integer borderArc) {
        this.borderArc = borderArc;
    }

    public Integer getInsetX() {
        return getInheritedProperty("insetX");
    }

    public void setInsetX(Integer insetX) {
        this.insetX = insetX;
    }

    public Integer getInsetY() {
        return getInheritedProperty("insetY");
    }

    public void setInsetY(Integer insetY) {
        this.insetY = insetY;
    }

    public Boolean getFillBackground() {
        return getInheritedProperty("fillBackground");
    }

    public void setFillBackground(Boolean fillBackground) {
        this.fillBackground = fillBackground;
    }

    public Color getCursorColor() {
        return getInheritedProperty("cursorColor");
    }

    public void setCursorColor(Color cursorColor) {
        this.cursorColor = cursorColor;
    }

    public Integer getBlinkCursorPeriod() {
        return getInheritedProperty("blinkCursorPeriod");
    }

    public void setBlinkCursorPeriod(Integer blinkCursorPeriod) {
        this.blinkCursorPeriod = blinkCursorPeriod;
    }

    public SequenceGenerator getBlinkCursorHelper() {
        Integer p = getBlinkCursorPeriod();
        if (p == null) {
            _blinkCursorHelperVal = null;
            _blinkCursorHelper = null;
            return null;
        } else {
            if (_blinkCursorHelperVal == null || !_blinkCursorHelperVal.equals(p)) {
                _blinkCursorHelperVal = p;
                _blinkCursorHelper = SequenceGenerator.createUnsignedSequence(p, p);
            }
            return _blinkCursorHelper;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("TextStyle{");
        s.append("name=").append(name);
        if (font != null) {
            s.append(", font=").append(font);
        }
        if (foreColor != null) {
            s.append(", color=").append(foreColor);
        }
        if (fillBackground != null) {
            s.append(", fillbg=").append(fillBackground);
        }
        if (backgroundColor != null) {
            s.append(", bg=").append(backgroundColor);
        }
        if (backgroundColor2 != null) {
            s.append(", bg2=").append(backgroundColor2);
        }
        if (cursorColor != null) {
            s.append(", cursorColor=").append(cursorColor);
        }
        if (alignment != null) {
            s.append(", alignment=").append(alignment);
        }
        if (borderWidth != null) {
            s.append(", borderWidth=").append(borderWidth);
        }
        if (borderArc != null) {
            s.append(", borderArc=").append(borderArc);
        }
        if (borderColor != null) {
            s.append(", borderColor=").append(borderColor);
        }
        if (blinkForegroundPeriod != null) {
            s.append(", blinkForegroundPeriod=").append(blinkForegroundPeriod);
        }
        if (blinkBackgroundPeriod != null) {
            s.append(", blinkBackgroundPeriod=").append(blinkBackgroundPeriod);
        }
        if (blinkBorderPeriod != null) {
            s.append(", blinkBorderPeriod=").append(blinkBorderPeriod);
        }
        if (blinkCursorPeriod != null) {
            s.append(", blinkCursorPeriod=").append(blinkCursorPeriod);
        }
        if (insetX != null) {
            s.append(", insetX=").append(insetX);
        }
        if (insetY != null) {
            s.append(", insetY=").append(insetY);
        }
        if (baseStyles != null && baseStyles.size() > 0) {
            s.append(", inherits=").append(baseStyles);
        }
        s.append("}");
        return s.toString();
    }
}
