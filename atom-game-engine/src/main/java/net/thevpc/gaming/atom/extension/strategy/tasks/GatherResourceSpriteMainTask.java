/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.extension.strategy.tasks;
import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.engine.maintasks.FindPathToSpriteSpriteMainTask;
import net.thevpc.gaming.atom.engine.maintasks.MotionSpriteMainTask;
import net.thevpc.gaming.atom.model.ModelPoint;
import net.thevpc.gaming.atom.model.Player;
import net.thevpc.gaming.atom.extension.strategy.resources.RepositoryEmptyException;
import net.thevpc.gaming.atom.extension.strategy.resources.RepositoryFullException;
import net.thevpc.gaming.atom.extension.strategy.resources.Resource;
import net.thevpc.gaming.atom.extension.strategy.resources.ResourceCarrier;
import net.thevpc.gaming.atom.extension.strategy.resources.ResourceRepository;
import net.thevpc.gaming.atom.model.Sprite;

/**
 * Gather a resource and unload it into command center
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class GatherResourceSpriteMainTask implements MotionSpriteMainTask {

    public static enum State {

        GATHER_RESSOURCE,
        UNLOAD_RESSOURCE,
        GOTO_RESOURCES,
        RESOURCES_EXHAUSTED,
        GOTO_STOCK
    }

    private int commandCenterId;
    private int resourceId;
    private State state = State.GOTO_RESOURCES;
    private int depositSpeed = 1;
    private int gatherSpeed = 1;
    private ModelPoint[] path = null;
    private FindPathToSpriteSpriteMainTask moveToTask = null;
//    private int gatherPeriod = 10;

    public GatherResourceSpriteMainTask(int commandCenterId, int resourceId) {
        this.commandCenterId = commandCenterId;
        this.resourceId = resourceId;
//        if (!repo.getResources().isAllowedResource(resource.getClass())) {
//            throw new IllegalArgumentException("Not allowed");
//        }
    }

    public int getCommandCenterId() {
        return commandCenterId;
    }

    public int getResourceId() {
        return resourceId;
    }

    public ModelPoint[] getMovePath() {
        return path;
    }

    private Resource getResource(SceneEngine scene) {
        return (Resource) scene.getSprite(resourceId);
    }

    private Sprite getCommandCenter(SceneEngine scene) {
        return scene.getSprite(commandCenterId);
    }

    private ResourceCarrier getResourceCarrier(SceneEngine scene) {
        return (ResourceCarrier) getCommandCenter(scene);
    }

    public boolean nextFrame(SceneEngine scene, Sprite sprite) {
        if (!(sprite instanceof ResourceCarrier)) {
            return false;
        }
        if (moveToTask == null) {
            moveToTask = new FindPathToSpriteSpriteMainTask(sprite.getSceneEngine().getSprite(resourceId), 2, true);
        }
        ResourceCarrier w = (ResourceCarrier) sprite;
        ResourceRepository wDeposit = w.getResources();
        Resource resource = getResource(scene);
        if (!wDeposit.isAllowedResource(resource.getClass())) {
            return false;
        }
        switch (state) {
            case GOTO_RESOURCES: {
                moveToTask.setTargetSprite(resource.getId());
                moveToTask.setMinDistance(2);
                moveToTask.nextFrame(scene, sprite);
                path = moveToTask.getMovePath();
                if (sprite.distance(resource) <= moveToTask.getMinDistance()) {
                    state = State.GATHER_RESSOURCE;
                }
                break;
            }
            case GOTO_STOCK: {
                moveToTask.setTargetSprite(commandCenterId);
                moveToTask.setMinDistance(2);
                moveToTask.nextFrame(scene, sprite);
                path = moveToTask.getMovePath();
                if (sprite.distance(getCommandCenter(scene)) < moveToTask.getMinDistance()) {
                    state = State.UNLOAD_RESSOURCE;
                }
                break;
            }
            case GATHER_RESSOURCE: {
                path = null;
                try {
                    wDeposit.addResource(resource.getClass(), resource.gather(gatherSpeed));
                } catch (RepositoryFullException e) {
                    state = State.GOTO_STOCK;
                } catch (RepositoryEmptyException e) {
                    if (wDeposit.getResource(resource.getClass()) > 0) {
                        state = State.GOTO_STOCK;
                    } else {
                        state = State.RESOURCES_EXHAUSTED;
                    }
                }
                break;
            }
            case UNLOAD_RESSOURCE: {
                path = null;
                Player player = scene.getPlayer(getCommandCenter(scene).getPlayerId());
                try {
                    int depositResource = wDeposit.removeResource(resource.getClass(), depositSpeed);
                    getResourceCarrier(scene).getResources().addResource(resource.getClass(), depositResource);
                    if (player instanceof ResourceCarrier) {
                        ResourceCarrier p = (ResourceCarrier) player;
                        p.getResources().addResource(resource.getClass(), depositResource);
                    }
                } catch (RepositoryFullException e) {
//                    sprite.setTask(new HoldPositionSpriteMainTask());
                    state = State.GOTO_STOCK;
                } catch (RepositoryEmptyException e) {
//                    sprite.setTask(new HoldPositionSpriteMainTask());
                    state = State.GOTO_RESOURCES;
                }
                break;
            }
            case RESOURCES_EXHAUSTED: {
                path = null;
                //DO NOTHING !!!
                break;
            }
        }
        return true;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Gather " + resourceId;
    }

    public boolean isMoving() {
        switch (getState()) {
            case GATHER_RESSOURCE:
            case RESOURCES_EXHAUSTED:
            case UNLOAD_RESSOURCE: {
                return false;
            }
        }
        return true;
    }
}
