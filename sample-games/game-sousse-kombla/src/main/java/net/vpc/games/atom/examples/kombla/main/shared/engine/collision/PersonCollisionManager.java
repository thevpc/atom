package net.vpc.games.atom.examples.kombla.main.shared.engine.collision;

import net.vpc.gaming.atom.annotations.AtomSpriteCollisionManager;
import net.vpc.gaming.atom.engine.SceneEngine;
import net.vpc.gaming.atom.engine.collision.BorderCollision;
import net.vpc.gaming.atom.engine.collision.SimpleSpriteCollisionManager;
import net.vpc.gaming.atom.engine.collision.SpriteCollision;
import net.vpc.gaming.atom.engine.collision.TileCollision;
import net.vpc.gaming.atom.model.MovementStyles;
import net.vpc.gaming.atom.model.Sprite;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created with IntelliJ IDEA.
 * User: vpc
 * Date: 9/7/13
 * Time: 12:41 AM
 * To change this template use File | Settings | File Templates.
 */
@AtomSpriteCollisionManager(
        engine = "mainLocal,mainServer",
        kind = "Person"
)
public class PersonCollisionManager extends SimpleSpriteCollisionManager implements PropertyChangeListener {
    double lastDirection = Double.NaN;
    double currentDirection = Double.NaN;
    boolean updatingDirection = false;

    @Override
    public void install(SceneEngine sceneEngine, Sprite sprite) {
        sprite.addPropertyChangeListener("direction", this);
    }

    @Override
    public void uninstall(SceneEngine sceneEngine, Sprite sprite) {
        sprite.removePropertyChangeListener("direction", this);
    }

    @Override
    protected void collideWithTileImpl(TileCollision collision) {
        //System.out.println("Collision "+collision);
        super.collideWithTileImpl(collision);
        //change direction
        fallbackDirection(collision.getSprite(), collision.getSceneEngine());
    }

    @Override
    protected void collideWithSpriteImpl(SpriteCollision collision) {
        if(collision.getOther().getKind().equals("Explosion") || collision.getOther().getKind().equals("Bomb")){
            //do nothing
            //collision mechanism is implemented in
            return;
        }else{
            super.collideWithSpriteImpl(collision);
        }
        super.collideWithSpriteImpl(collision);
        //change direction
        fallbackDirection(collision.getSprite(), collision.getSceneEngine());
    }

    @Override
    protected void collideWithBorderImpl(BorderCollision collision) {
        //System.out.println("Collision "+collision);
        super.collideWithBorderImpl(collision);
        fallbackDirection(collision.getSprite(), collision.getSceneEngine());
    }

    protected void fallbackDirection(Sprite sprite, SceneEngine sceneEngine) {
        sceneEngine.setSpriteTask(sprite,null);
        sprite.setMovementStyle(MovementStyles.STOPPED);
        if (!Double.isNaN(lastDirection)) {
            updatingDirection = true;
            //System.out.println("Fallback : "+sprite.getDirection()+" => "+GeometryUtils.getAngleDirectionText(lastDirection));
            //sprite.setDirection(lastDirection);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        double oldValue = (Double) evt.getOldValue();
        double newValue = (Double) evt.getNewValue();
        //System.out.println("DIR : " + GeometryUtils.getAngleDirectionText(lastDirection)+"->"+GeometryUtils.getAngleDirectionText(currentDirection)+" :: "+GeometryUtils.getAngleDirectionText(oldValue)+"->"+GeometryUtils.getAngleDirectionText(newValue));
        if (updatingDirection) {
            return;
        }
        updatingDirection = true;
        if (Double.isNaN(lastDirection)) {
            lastDirection = newValue;
            currentDirection = newValue;
        } else if (newValue != currentDirection) {
            lastDirection = currentDirection;
            currentDirection = newValue;
        }
        updatingDirection = false;
    }
}
