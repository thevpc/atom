package net.thevpc.gaming.atom.ioc;

public class IndexedPostponedAction implements Comparable<IndexedPostponedAction> {
    private final int index;
    private final PostponedAction action;

    public IndexedPostponedAction(int index, PostponedAction action) {
        this.index = index;
        this.action = action;
    }

    public boolean isRunnable() {
        return action.isRunnable();
    }

    public void run() {
        action.run();
    }

    @Override
    public int compareTo(IndexedPostponedAction o) {

        int x = Integer.compare(action.getOrder(), o.action.getOrder());
        if (x != 0) {
            return x;
        }
        x = Integer.compare(index, o.index);
        return x;
    }

    public PostponedAction getAction() {
        return action;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return "[" + index +
                "] " + action;
    }
}
