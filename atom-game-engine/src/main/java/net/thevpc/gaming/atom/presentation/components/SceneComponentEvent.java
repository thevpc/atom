package net.thevpc.gaming.atom.presentation.components;

import java.awt.*;

public class SceneComponentEvent extends Event{
    public SceneComponentEvent(SceneComponent target, long when, int id, int x, int y, int key, int modifiers, Object arg) {
        super(target, when, id, x, y, key, modifiers, arg);
    }

    public SceneComponentEvent(SceneComponent target, long when, int id, int x, int y, int key, int modifiers) {
        super(target, when, id, x, y, key, modifiers);
    }

    public SceneComponentEvent(SceneComponent target, int id, Object arg) {
        super(target, id, arg);
    }
}
