package net.thevpc.gaming.atom.presentation;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: vpc
 * Date: 8/18/13
 * Time: 4:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class DefaultSceneKeyEventFilter implements SceneKeyEventFilter {
    private final Set<KeyCode> keyCodes;
    private final Boolean shiftKey;
    private final Boolean controlKey;
    private final Boolean altKey;

    public DefaultSceneKeyEventFilter(KeyCode... keyCodes) {
        this(keyCodes, null, null, null);
    }

    public DefaultSceneKeyEventFilter(KeyCode[] keyCodes, Boolean shiftKey, Boolean controlKey, Boolean altKey) {
        this.keyCodes = KeyCode.setOf(keyCodes);
        this.shiftKey = shiftKey;
        this.controlKey = controlKey;
        this.altKey = altKey;
    }

    @Override
    public boolean accept(SceneKeyEvent event) {
        if (keyCodes != null) {
            if (!event.getKeyCodes().equals(keyCodes)) {
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
            return event.isAltPressed() == altKey;
        }
        return true;
    }
}
