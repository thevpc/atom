package net.thevpc.gaming.atom.examples.soussecraft.main.model.structures;

import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.engine.SpriteMainTask;
import net.thevpc.gaming.atom.extension.strategy.Structure;
import net.thevpc.gaming.atom.extension.strategy.resources.DefaultResourceRepository;
import net.thevpc.gaming.atom.extension.strategy.resources.ResourceCarrier;
import net.thevpc.gaming.atom.extension.strategy.resources.ResourceRepository;
import net.thevpc.gaming.atom.model.ModelDimension;
import net.thevpc.gaming.atom.model.ModelPoint;
import net.thevpc.gaming.atom.model.Sprite;
import net.thevpc.gaming.atom.model.armors.LevelSpriteArmor;
import net.thevpc.gaming.atom.examples.soussecraft.main.model.resources.Minerals;
import net.thevpc.gaming.atom.examples.soussecraft.main.model.resources.Woods;
import net.thevpc.gaming.atom.examples.soussecraft.main.model.units.Worker;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 10 dec. 2006 23:19:21
 */
public class CommandCenter extends Structure implements ResourceCarrier {

    private ResourceRepository resources;
    int workersToProduct = 0;
    int productionCounter = 100;

    public CommandCenter() {
        setName("Command Center");
        setSize(new ModelDimension(8, 4));
        setMaxLife(2000);
        setLife(2000);
        setSight(20);
        setArmors(new LevelSpriteArmor(0));
        resources = new DefaultResourceRepository();
        //pour enregistrer les ressource
        resources.setMaxResource(-1, Minerals.class, Woods.class);
        super.setMainTask(new CommandCenterMainTask());
    }

    public ResourceRepository getResources() {
        return resources;
    }

    public int getWorkersToProduct() {
        return workersToProduct;
    }

    public boolean productionStep(SceneEngine sceneEngine) {
        if (workersToProduct > 0) {
            productionCounter--;
            if (productionCounter <= 0) {
                Worker w = new Worker();
                w.setPlayerId(getPlayerId());
                w.setLocation(new ModelPoint(100, 100));
                sceneEngine.addSprite(w);
                productionCounter = 100;
                workersToProduct--;
                return true;
            }
        }
        return false;
    }

    public static class CommandCenterMainTask implements SpriteMainTask {

        private CommandCenter sprite;

        public CommandCenterMainTask() {
        }

        public boolean nextFrame(SceneEngine scene, Sprite sprite) {
            this.sprite=((CommandCenter)sprite);
            this.sprite.productionStep(scene);
            return true;
        }

//        public ModelPoint[] getTaskPath() {
//            return null;
//        }

        @Override
        public String toString() {
            if(sprite==null) {
                return "-";
            }
            switch (sprite.getWorkersToProduct()) {
                case 0: {
                    return "-";
                }
                case 1: {
                    return "Produce Worker";
                }
                default: {
                    return "Produce " + sprite.getWorkersToProduct() + " Workers";
                }
            }
        }
    }
}
