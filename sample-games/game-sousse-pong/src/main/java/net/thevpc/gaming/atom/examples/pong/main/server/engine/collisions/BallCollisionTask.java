/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.pong.main.server.engine.collisions;

import net.thevpc.gaming.atom.engine.collisiontasks.SpriteCollisionTask;
import net.thevpc.gaming.atom.engine.collisiontasks.TileCollision;
import net.thevpc.gaming.atom.examples.pong.main.server.engine.tasks.BallFollowPaddleSpriteMainTask;
import net.thevpc.gaming.atom.engine.collisiontasks.BorderCollision;
import net.thevpc.gaming.atom.engine.collisiontasks.SpriteCollision;
import net.thevpc.gaming.atom.model.*;
import net.thevpc.gaming.atom.examples.pong.main.shared.model.Paddle;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class BallCollisionTask implements SpriteCollisionTask {

    @Override
    public void collideWithBorder(BorderCollision borderCollision) {
        CollisionSides cs = borderCollision.getBorderCollisionSides();
        boolean north = cs.isNorth();
        boolean south = cs.isSouth();
        boolean east = cs.isEast();
        boolean west = cs.isWest();
        Sprite ball = borderCollision.getSprite();
        ModelPoint lastValidPosition = borderCollision.getLastValidPosition();
        if (north) {
            Paddle paddle = borderCollision.getSceneEngine().findSpriteByPlayer(Paddle.class, 2);
            paddle.addLife(-1);
            if (!paddle.isDead()) {
                borderCollision.getSceneEngine().setSpriteMainTask(ball, new BallFollowPaddleSpriteMainTask(2));
                ball.setMovementStyle(MovementStyles.MOVING_SLOW);
            }
        } else if (east || west) {
            ball.setDirection(DirectionTransform.VERTICAL_MIRROR);
            ball.setLocation(lastValidPosition);
        } else if (south) {
            Paddle paddle = borderCollision.getSceneEngine().findSpriteByPlayer(Paddle.class, 1);
            if (paddle != null) {
                paddle.addLife(-1);
                if (!paddle.isDead()) {
                    borderCollision.getSceneEngine().setSpriteMainTask(ball, new BallFollowPaddleSpriteMainTask(1));
                    ball.setMovementStyle(MovementStyles.MOVING_SLOW);
                }
            }
        }
        borderCollision.adjustSpritePosition();
    }

    @Override
    public void collideWithSprite(SpriteCollision spriteCollision) {
        Sprite sprite = spriteCollision.getSprite();
        CollisionSides os = spriteCollision.getOtherCollisionSides();
        boolean north = os.isNorth();
        boolean south = os.isSouth();
        boolean east = os.isEast();
        boolean west = os.isWest();
//        System.out.println("collideWithSprite "+(north ? "N" : "-") + ";" + (east ? "E" : "-") + ";" + (south ? "S" : "-") + ";" + (west ? "W" : "-") + " :: " + velocity + " " + lastValidPosition + " -> " + sprite.getLocation());
        if ((north && west) || (north && east) || (south && west) || (south && east)) {
            sprite.setDirection(DirectionTransform.BACKWARD);
        } else if (north || south) {
            sprite.setDirection(DirectionTransform.HORIZONTAL_MIRROR);
        } else if (east || west) {
            sprite.setDirection(DirectionTransform.VERTICAL_MIRROR);
        }
        spriteCollision.adjustSpritePosition();
    }

    @Override
    public void collideWithTile(TileCollision tileCollision) {
        Sprite sprite = tileCollision.getSprite();
        CollisionSides ts = tileCollision.getTileCollisionSides();
        boolean north = ts.isNorth();
        boolean south = ts.isSouth();
        boolean east = ts.isEast();
        boolean west = ts.isWest();
//        System.out.println("collideWithSprite "+(north ? "N" : "-") + ";" + (east ? "E" : "-") + ";" + (south ? "S" : "-") + ";" + (west ? "W" : "-") + " :: " + velocity + " " + lastValidPosition + " -> " + sprite.getLocation());
        if ((north && west) || (north && east) || (south && west) || (south && east)) {
            sprite.setDirection(DirectionTransform.BACKWARD);
        } else if (north || south) {
            sprite.setDirection(DirectionTransform.HORIZONTAL_MIRROR);
        } else if (east || west) {
            sprite.setDirection(DirectionTransform.VERTICAL_MIRROR);
        }
        tileCollision.adjustSpritePosition();
    }
}
