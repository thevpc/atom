package net.thevpc.gaming.atom.examples.kombla.main.shared.engine.collision;

import net.thevpc.gaming.atom.annotations.AtomSpriteCollisionTask;
import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.engine.collisiontasks.*;
import net.thevpc.gaming.atom.model.MovementStyles;
import net.thevpc.gaming.atom.model.Sprite;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created with IntelliJ IDEA.
 * User: vpc
 * Date: 9/7/13
 * Time: 12:41 AM
 * To change this template use File | Settings | File Templates.
 */
@AtomSpriteCollisionTask(
        sceneEngine = "mainLocal,mainServer",
        kind = "Person"
)
public class PersonCollisionTask implements SpriteCollisionTask, PropertyChangeListener {
    double lastDirection = Double.NaN;
    double currentDirection = Double.NaN;
    boolean updatingDirection = false;

    public PersonCollisionTask() {
        System.out.println("rr");
    }

    @Override
    public void install(SceneEngine sceneEngine, Sprite sprite) {
        sprite.addPropertyChangeListener("direction", this);
    }

    @Override
    public void uninstall(SceneEngine sceneEngine, Sprite sprite) {
        sprite.removePropertyChangeListener("direction", this);
    }

    @Override
    public void collideWithTile(TileCollision collision) {
        //System.out.println("Collision "+collisiontasks);
        collision.adjustSpritePosition();
        //change direction
        fallbackDirection(collision.getSprite(), collision.getSceneEngine());
    }

    @Override
    public void collideWithSprite(SpriteCollision collision) {
        if(collision.getOther().getKind().equals("Explosion") || collision.getOther().getKind().equals("Bomb")){
            //do nothing
            //collisiontasks mechanism is implemented in
            return;
        }else{
            collision.adjustSpritePosition();
        }
        //change direction
        fallbackDirection(collision.getSprite(), collision.getSceneEngine());
    }

    @Override
    public void collideWithBorder(BorderCollision collision) {
        //System.out.println("Collision "+collisiontasks);
        collision.adjustSpritePosition();
        fallbackDirection(collision.getSprite(), collision.getSceneEngine());
    }

    public void fallbackDirection(Sprite sprite, SceneEngine sceneEngine) {
        sceneEngine.setSpriteMainTask(sprite,null);
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
