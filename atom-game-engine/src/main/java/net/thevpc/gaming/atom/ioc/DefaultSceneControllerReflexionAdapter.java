package net.thevpc.gaming.atom.ioc;

import net.thevpc.gaming.atom.presentation.DefaultSceneController;

/**
 * Created by vpc on 9/25/16.
 */
public class DefaultSceneControllerReflexionAdapter extends DefaultSceneController {
    private Object instance;
    private Class clazz;

    public DefaultSceneControllerReflexionAdapter(Object instance) {
        this.instance = instance;
        this.clazz = instance.getClass();
    }
}
