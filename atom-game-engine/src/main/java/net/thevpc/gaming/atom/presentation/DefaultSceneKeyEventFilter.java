package net.thevpc.gaming.atom.presentation;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: vpc
 * Date: 8/18/13
 * Time: 4:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class DefaultSceneKeyEventFilter implements SceneKeyEventFilter {
    private Set<Integer> keyCodes;
    private Boolean shiftKey;
    private Boolean controlKey;
    private Boolean altKey;

    public DefaultSceneKeyEventFilter(int[] keyCodes) {
        this(keyCodes,null,null,null);
    }

    public DefaultSceneKeyEventFilter(int[] keyCodes, Boolean shiftKey, Boolean controlKey, Boolean altKey) {
        this.keyCodes = new HashSet<Integer>();
        if (keyCodes != null) {
            for (int keyCode : keyCodes) {
                this.keyCodes.add(keyCode);
            }
        }
        this.shiftKey = shiftKey;
        this.controlKey = controlKey;
        this.altKey = altKey;
    }

    @Override
    public boolean accept(SceneKeyEvent event) {
        if (keyCodes != null) {

            Set<Integer> foundKeyCodes = new HashSet<Integer>();
            for (int keyCode : event.getKeyCodes()) {
                foundKeyCodes.add(keyCode);
            }
            if (!foundKeyCodes.equals(keyCodes)) {
                return false;
            }
        }
        if (shiftKey != null) {
            if (event.isShiftPressed() != shiftKey) {
                return false;
            }
        }

        if (controlKey != null) {
            if (event.isCtrlPressed() != controlKey) {
                return false;
            }
        }

        if (altKey != null) {
            if (event.isAltPressed() != altKey) {
                return false;
            }
        }
        return true;
    }
}
