package net.vpc.gaming.atom.ioc;

/**
 * Created by vpc on 9/25/16.
 */
interface PostponedAction {

    boolean isRunnable();

    int getOrder();

    void run();
}
