package net.thevpc.gaming.atom.presentation.components;

public class SceneComponentSelectionChangeEvent extends SceneComponentEvent{
    private int oldSelection;
    private int newSelection;
    public SceneComponentSelectionChangeEvent(SceneComponent target, int oldSelection,int newSelection) {
        super(target, 0, new Object[]{oldSelection,newSelection});
        this.oldSelection=oldSelection;
        this.newSelection=newSelection;
    }

    public int getOldSelection() {
        return oldSelection;
    }

    public int getNewSelection() {
        return newSelection;
    }
}
