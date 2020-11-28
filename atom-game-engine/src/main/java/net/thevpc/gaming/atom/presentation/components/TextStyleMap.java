/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.presentation.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private List<TextStyle> findStyles(SceneComponentState s) {
        List<TextStyle> a = new ArrayList<>();
        for (Map.Entry<SceneComponentState, TextStyle> kv : textStyles.entrySet()) {
            if (kv.getKey().equals(SceneComponentState.DEFAULT)||s.contains(kv.getKey())){
                a.add(kv.getValue());
            }
        }
        return a;
    }

    private void rebuildStyles() {
        for (Map.Entry<SceneComponentState, TextStyle> entry : textStyles.entrySet()) {
            entry.getValue().setBaseStyles(new ArrayList<TextStyle>());
        }
        for (Map.Entry<SceneComponentState, TextStyle> entry : textStyles.entrySet()) {
            SceneComponentState status = entry.getKey();
            List<TextStyle> base = findStyles(status);
            base.remove(entry.getValue());
            entry.getValue().setBaseStyles(base);
        }
    }

//    private boolean isPowerOfTwo(int x) {
//        return (x != 0) && ((x & (x - 1)) == 0);
//    }
}
