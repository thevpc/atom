package net.thevpc.gaming.atom.ioc;

import net.thevpc.gaming.atom.annotations.AtomSpriteView;
import net.thevpc.gaming.atom.engine.SpriteFilter;
import net.thevpc.gaming.atom.presentation.Game;
import net.thevpc.gaming.atom.presentation.LocationIndicatorView;
import net.thevpc.gaming.atom.presentation.Scene;
import net.thevpc.gaming.atom.presentation.SpriteView;
import net.thevpc.gaming.atom.util.AtomUtils;

/**
 * Created by vpc on 10/7/16.
 */
class AtomSpriteViewCreationAction implements PostponedAction {
    private final IoCContext context;
    private final AtomAnnotationsProcessor atomAnnotationsProcessor;

    public AtomSpriteViewCreationAction(AtomAnnotationsProcessor atomAnnotationsProcessor, IoCContext context) {
        this.atomAnnotationsProcessor = atomAnnotationsProcessor;
        this.context = context;
    }
    @Override
    public String toString() {
        return "@AtomSpriteView{" +
                "" + context.findCurrent().getClass().getSimpleName() +
                ", sceneEngineId=" + context.getSceneEngineId() +
                ", sceneEngine=" + context.getSceneId() +
                '}';
    }
    @Override
    public boolean isRunnable() {
        AtomSpriteView s = context.get(AtomSpriteView.class,null);
        if (s.scene().isEmpty()) {
            return true;
        }
        for (String n : AtomAnnotationsProcessor.split(s.scene())) {
            if (!atomAnnotationsProcessor.container.contains(n, "Scene", n)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int getOrder() {
        return AtomAnnotationsProcessor.ORDER_SPRITE_VIEW;
    }

    @Override
    public void run() {
        AtomSpriteView s = context.get(AtomSpriteView.class,null);
        Game game = context.get(Game.class,null);
        Scene scene = game.getScene(context.getSceneId());
        SpriteView instance = context.findCurrentAs(SpriteView.class);
        if (instance == null) {
            instance = new LocationIndicatorView();
        }
        //instance.setCompationObject(instance);
        scene.setSpriteView(SpriteFilter.byKind(s.kind()), instance);
        String name = AtomUtils.trimToNull(s.name());
        context.register(SpriteView.class,instance, name);
        atomAnnotationsProcessor.container.register(
                scene.getId() + "/" + instance.getClass().getName()
                , "SpriteView", name, instance);
    }
}
