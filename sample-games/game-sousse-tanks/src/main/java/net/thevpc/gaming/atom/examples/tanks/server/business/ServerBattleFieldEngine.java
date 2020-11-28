/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.tanks.server.business;

import net.thevpc.gaming.atom.engine.collisiontasks.StopSpriteCollisionTask;
import net.thevpc.gaming.atom.engine.maintasks.MovePathSpriteMainTask;
import net.thevpc.gaming.atom.examples.tanks.server.dal.TCPServerBattleFieldDAO;
import net.thevpc.gaming.atom.examples.tanks.shared.dal.DALData;
import net.thevpc.gaming.atom.examples.tanks.shared.business.AbstractBattleFieldEngine;
import net.thevpc.gaming.atom.model.ModelPoint;
import net.thevpc.gaming.atom.examples.tanks.server.dal.AbstractServerBattleFieldDAO;
import net.thevpc.gaming.atom.examples.tanks.server.dal.ServerBattleFieldListener;
import net.thevpc.gaming.atom.examples.tanks.shared.business.MapManager;
import net.thevpc.gaming.atom.annotations.AtomSceneEngine;
import net.thevpc.gaming.atom.model.ModelDimension;
import net.thevpc.gaming.atom.model.ModelVector;
import net.thevpc.gaming.atom.model.Player;
import net.thevpc.gaming.atom.model.Sprite;

/**
 *
 * @author Taha Ben Salah
 */
@AtomSceneEngine(id = "battle", columns = 16, rows = 16)
public class ServerBattleFieldEngine extends AbstractBattleFieldEngine implements ServerBattleFieldListener {

    AbstractServerBattleFieldDAO dao;

    public ServerBattleFieldEngine() {
        setModel(MapManager.getBattleFieldModel("eniso"));
    }

    @Override
    protected void sceneActivating() {
        dao = new TCPServerBattleFieldDAO();
        dao.start(this);
        Player firstPlayer = addPlayer();
        Sprite tank = createSprite("Tank");
        tank.setSize(new ModelDimension(0.5, 0.5));
        tank.setLife(3);
        tank.setSpeed(0.01);
        tank.setKind("Tank");
        tank.setPlayerId(firstPlayer.getId());
        addSprite(tank);
        tank.setMainTask(new MovePathSpriteMainTask());
        tank.setCollisionTask(new StopSpriteCollisionTask());
        tank.setLocation(randomVacantLocation(tank));
    }

    @Override
    public void rotateLeft(int playerId) {
        Sprite tank = findTankByPlayer(playerId);
        tank.setDirection(tank.getDirection() - Math.PI / 16);
    }

    @Override
    public void rotateRight(int playerId) {
        Sprite tank = findTankByPlayer(playerId);
        tank.setDirection(tank.getDirection() + Math.PI / 16);
    }

    @Override
    public void fire(int playerId) {
        Sprite tank = findTankByPlayer(playerId);
        Sprite b = createSprite("Bullet");
        b.setSize(new ModelDimension(0.2, 0.2));
        b.setSpeed(0.2);

        ModelVector v = ModelVector.newAngular(tank.getWidth(), tank.getDirection());

        double cx = tank.getBounds().getCenterX();
        double cy = tank.getBounds().getCenterY();

        double ax = cx + v.getIntX() - b.getWidth() / 2;
        double ay = cy + v.getIntY() - b.getHeight() / 2;

        b.setLocation(new ModelPoint(ax, ay));
        b.setDirection(tank.getDirection());
        b.setPlayerId(tank.getPlayerId());
        getModel().addSprite(b);
        b.setMainTask(new MovePathSpriteMainTask());
        b.setCollisionTask(new BulletCollisionTask());
    }

    private Sprite findTankByPlayer(int playerId) {
        return findSpriteByPlayer("Tank", playerId);
    }

    @Override
    public int connect() {
        //add new player to the model
        return addPlayer().getId();
    }

    @Override
    protected void modelUpdated() {
        super.modelUpdated();
        //getModel()
        DALData data = null;//
        dao.modelChanged(data);
    }

}
