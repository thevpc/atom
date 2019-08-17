/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.edu.eniso.pong.main.server.engine.collision;

import net.vpc.gaming.atom.engine.collision.BorderCollision;
import net.vpc.gaming.atom.engine.collision.DefaultSpriteCollisionManager;
import net.vpc.gaming.atom.engine.collision.SpriteCollision;
import net.vpc.gaming.atom.model.DirectionTransform;
import net.vpc.gaming.atom.model.ModelPoint;
import net.vpc.gaming.atom.model.MovementStyles;
import net.vpc.gaming.atom.model.Sprite;
import tn.edu.eniso.pong.main.server.engine.tasks.BallFollowPaddleSpriteTask;
import tn.edu.eniso.pong.main.shared.model.Paddle;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class BallCollisionManager extends DefaultSpriteCollisionManager {

    @Override
    public void collideWithBorder(BorderCollision borderCollision) {
        boolean north = borderCollision.isBorderCollisionNorth();
        boolean south = borderCollision.isBorderCollisionSouth();
        boolean east = borderCollision.isBorderCollisionEast();
        boolean west = borderCollision.isBorderCollisionWest();
        Sprite ball = borderCollision.getSprite();
        ModelPoint lastValidPosition = borderCollision.getLastValidPosition();
        if (north) {
            Paddle paddle = borderCollision.getSceneEngine().findSpriteByPlayer(Paddle.class, 2);
            paddle.addLife(-1);
            if (!paddle.isDead()) {
                borderCollision.getSceneEngine().setSpriteTask(ball, new BallFollowPaddleSpriteTask(2));
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
                    borderCollision.getSceneEngine().setSpriteTask(ball, new BallFollowPaddleSpriteTask(1));
                    ball.setMovementStyle(MovementStyles.MOVING_SLOW);
                }
            }
        }
    }

    @Override
    public void collideWithSprite(SpriteCollision spriteCollision) {
        Sprite sprite = spriteCollision.getSprite();
        boolean north = spriteCollision.isOtherCollisionNorth();
        boolean south = spriteCollision.isOtherCollisionSouth();
        boolean east = spriteCollision.isOtherCollisionEast();
        boolean west = spriteCollision.isOtherCollisionWest();
//        System.out.println("collideWithSprite "+(north ? "N" : "-") + ";" + (east ? "E" : "-") + ";" + (south ? "S" : "-") + ";" + (west ? "W" : "-") + " :: " + velocity + " " + lastValidPosition + " -> " + sprite.getLocation());
        if ((north && west) || (north && east) || (south && west) || (south && east)) {
            sprite.setDirection(DirectionTransform.BACKWARD);
        } else if (north || south) {
            sprite.setDirection(DirectionTransform.HORIZONTAL_MIRROR);
        } else if (east || west) {
            sprite.setDirection(DirectionTransform.VERTICAL_MIRROR);
        }
        sprite.setLocation(spriteCollision.getLastValidPosition());
    }
}
