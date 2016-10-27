/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.edu.eniso.pong.main.shared.dal;

import net.vpc.gaming.atom.engine.SceneEngine;
import net.vpc.gaming.atom.model.ModelPoint;
import net.vpc.gaming.atom.model.Sprite;
import tn.edu.eniso.pong.main.client.dal.ModelUpdater;
import tn.edu.eniso.pong.main.shared.dal.model.DALStructModel;
import tn.edu.eniso.pong.main.shared.dal.model.DALStructSprite;
import tn.edu.eniso.pong.main.shared.model.Ball;
import tn.edu.eniso.pong.main.shared.model.Paddle;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class DALDataModelUpdater implements ModelUpdater {

    private DALStructModel data;

    public DALDataModelUpdater(DALStructModel data) {
        this.data = data;
    }

    @Override
    public void updateModel(SceneEngine sceneEngine) {
        sceneEngine.getModel().setFrame(data.frame);
        updateSprite(Paddle.class, sceneEngine, 1, data.paddle1);
        updateSprite(Paddle.class, sceneEngine, 2, data.paddle2);
        updateSprite(Ball.class, sceneEngine, null, data.ball);
    }

    private static void updateSprite(Class<? extends Sprite> t, SceneEngine sceneEngine, Integer player, DALStructSprite dalStructSprite) {
        Sprite sprite = player == null ? sceneEngine.findSprite(t) : sceneEngine.findSpriteByPlayer(t, player);
        if (sprite != null && dalStructSprite != null) {
            sprite.setLocation(new ModelPoint(dalStructSprite.x, dalStructSprite.y));
            sprite.setSpeed(dalStructSprite.speed);
            sprite.setDirection(dalStructSprite.direction);
            sprite.setLife(dalStructSprite.life);
        } else if (sprite != null && dalStructSprite == null) {
            sprite.die();
        } else if (sprite == null && dalStructSprite != null) {
            Sprite b = null;
            try {
                b = t.newInstance();
            } catch (Exception ex) {
                //should not happen
                ex.printStackTrace();
            }
            if (player != null) {
                b.setPlayerId(player);
            }
            sprite.setLocation(new ModelPoint(dalStructSprite.x, dalStructSprite.y));
            sprite.setSpeed(dalStructSprite.speed);
            sprite.setDirection(dalStructSprite.direction);
            sprite.setLife(dalStructSprite.life);
            sceneEngine.addSprite(b);
        }
    }
}
