/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.presentation.components;

import java.util.*;

/**
 * @author vpc
 */
public class TextStyleMap {

    private Map<Integer, TextStyle> textStyles = new HashMap<Integer, TextStyle>();

    public void setTextStyle(TextStyle style, int status) {
        textStyles.put(status, new TextStyle(style.getName(), style));
        rebuildStyles();
    }

    public TextStyle getTextStyle(int status) {
        TextStyle defaultStyle = textStyles.get(status);
        if (defaultStyle == null) {
            defaultStyle = new TextStyle(createName(status));
            textStyles.put(status, defaultStyle);
            rebuildStyles();
        }
        return defaultStyle;
    }

    private String createName(int status) {
        StringBuilder s = new StringBuilder();
        if ((status & TextStyle.DISABLED) != 0) {
            status &= ~TextStyle.DISABLED;
            if (s.length() > 0) {
                s.append("_");
            }
            s.append("DISABLED");
        }
        if ((status & TextStyle.FOCUSED) != 0) {
            status &= ~TextStyle.FOCUSED;
            if (s.length() > 0) {
                s.append("_");
            }
            s.append("FOCUSED");
        }
        if ((status & TextStyle.SELECTED) != 0) {
            status &= ~TextStyle.SELECTED;
            if (s.length() > 0) {
                s.append("_");
            }
            s.append("SELECTED");
        }
        if ((status & TextStyle.HOVER) != 0) {
            status &= ~TextStyle.HOVER;
            if (s.length() > 0) {
                s.append("_");
            }
            s.append("HOVER");
        }
        if (status == 0) {
            if (s.length() == 0) {
                return "DEFAULT";
            } else {
                return s.toString();
            }
        } else {
            if (s.length() > 0) {
                s.append(",");
            }
            s.append(String.valueOf(status));
            return s.toString();
        }
    }

    private void rebuildStyles() {
        TextStyle defaultStyle = getTextStyle(0);
        for (Map.Entry<Integer, TextStyle> entry : textStyles.entrySet()) {
            entry.getValue().setBaseStyles(new ArrayList<TextStyle>());
        }
        for (Map.Entry<Integer, TextStyle> entry : textStyles.entrySet()) {
            int status = entry.getKey();
            TextStyle style = entry.getValue();
            if (status == 0) {
                //do nothing
            } else {
                TreeMap<Integer, TextStyle> bases = new TreeMap<Integer, TextStyle>(new Comparator<Integer>() {
                    public int compare(Integer o1, Integer o2) {
                        return o2.compareTo(o1);
                    }
                });
                for (Map.Entry<Integer, TextStyle> entry2 : textStyles.entrySet()) {
                    int status2 = entry2.getKey();
                    if (status2 == 0 || (status2 < status && ((status & status2) != 0))) {
                        bases.put(status2, entry2.getValue());
                    }
                }
                style.setBaseStyles(new ArrayList<TextStyle>(bases.values()));
            }
        }
    }
//    private boolean isPowerOfTwo(int x) {
//        return (x != 0) && ((x & (x - 1)) == 0);
//    }
}
