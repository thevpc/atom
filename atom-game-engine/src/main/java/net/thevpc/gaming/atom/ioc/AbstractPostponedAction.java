package net.thevpc.gaming.atom.ioc;

public abstract class AbstractPostponedAction implements PostponedAction{
    private int order;

    public AbstractPostponedAction(int order) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public boolean isRunnable() {
        return true;
    }
}
