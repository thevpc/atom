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

    private Map<SceneComponentState, TextStyle> textStyles = new HashMap<SceneComponentState, TextStyle>();

    public void setTextStyle(SceneComponentState status, TextStyle style) {
        textStyles.put(status, new TextStyle(style.getName(), style));
        rebuildStyles();
    }

    public TextStyle getTextStyle(SceneComponentState status) {
        TextStyle defaultStyle = textStyles.get(status);
        if (defaultStyle == null) {
            defaultStyle = new TextStyle(createName(status));
            textStyles.put(status, defaultStyle);
            rebuildStyles();
        }
        return defaultStyle;
    }

    private String createName(SceneComponentState status) {
        return status.toString();
    }

    private void rebuildStyles() {
        TextStyle defaultStyle = getTextStyle(SceneComponentState.DEFAULT);
        for (Map.Entry<SceneComponentState, TextStyle> entry : textStyles.entrySet()) {
            entry.getValue().setBaseStyles(new ArrayList<TextStyle>());
        }
        for (Map.Entry<SceneComponentState, TextStyle> entry : textStyles.entrySet()) {
            SceneComponentState status = entry.getKey();
            TextStyle style = entry.getValue();
            if (status == SceneComponentState.DEFAULT) {
                //do nothing
            } else {
                TreeMap<SceneComponentState, TextStyle> bases = new TreeMap<SceneComponentState, TextStyle>();
                for (Map.Entry<SceneComponentState, TextStyle> entry2 : textStyles.entrySet()) {
                    SceneComponentState status2 = entry2.getKey();
                    bases.put(status2, entry2.getValue());
                }
                style.setBaseStyles(new ArrayList<TextStyle>(bases.values()));
            }
        }
    }
//    private boolean isPowerOfTwo(int x) {
//        return (x != 0) && ((x & (x - 1)) == 0);
//    }
}
