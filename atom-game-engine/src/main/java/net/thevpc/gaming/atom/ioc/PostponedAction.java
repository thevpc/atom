package net.thevpc.gaming.atom.ioc;

/**
 * Created by vpc on 9/25/16.
 */
interface PostponedAction {

    default boolean isRunnable(){return true;}

    int getOrder();

    void run();
}
