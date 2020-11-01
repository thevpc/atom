/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.games.atom.examples.kombla.main.client.engine;

import net.vpc.gaming.atom.annotations.AtomSceneEngine;
import net.vpc.gaming.atom.model.*;
import net.vpc.games.atom.examples.kombla.main.client.dal.MainClientDAO;
import net.vpc.games.atom.examples.kombla.main.client.dal.MainClientDAOListener;
import net.vpc.games.atom.examples.kombla.main.shared.model.DynamicGameModel;
import net.vpc.games.atom.examples.kombla.main.shared.model.StartGameInfo;
import net.vpc.games.atom.examples.kombla.main.shared.engine.AbstractMainEngine;

import java.util.Map;


/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
@AtomSceneEngine(id = "mainClient", columns = 12, rows = 12)
public class MainClientEngine extends AbstractMainEngine {
    private MainClientDAO dao;
    public MainClientEngine() {
    }

    @Override
    protected void sceneActivating() {
        //put here your MainClientDAO instance
        //dao = new TCPMainClientDAO();
//        dao = new UDPMainClientDAO();

        dao.start(new MainClientDAOListener() {
            @Override
            public void onModelChanged(final DynamicGameModel model) {
                invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        resetSprites();
                        resetPlayers();
                        getModel().setFrame(model.getFrame());
                        for (Player player : model.getPlayers()) {
                            Player p = createPlayer();
                            p.setName(player.getName());
                            p.setId(player.getId());
                            addPlayer(p);
                        }
                        for (Sprite sprite : model.getSprites()) {
                            Sprite s = createSprite(sprite.getKind());
                            if("Person".equals(sprite.getKind()) || "Bomb".equals(sprite.getKind())){
                                s.setSize(new ModelDimension(0.5,0.5));
                            }
                            s.setName(sprite.getName());
                            s.setId(sprite.getId());
                            s.setLocation(sprite.getLocation());
                            s.setDirection(sprite.getDirection());
                            s.setLife(sprite.getLife());
                            s.setMovementStyle(sprite.getMovementStyle());
                            s.setPlayerId(sprite.getPlayerId());
                            Map<String, Object> pp = sprite.getProperties();
                            if(pp!=null) {
                                for (Map.Entry<String, Object> ee : pp.entrySet()) {
                                    s.setProperty(ee.getKey(),ee.getValue());
                                }
                            }
                            addSprite(s);
                        }
                        MainClientEngine.this.getModel().setProperty("modelChanged",System.currentTimeMillis());
                    }
                });
            }
        }, getAppConfig(getGameEngine()));
        //call server to connect
        StartGameInfo startGameInfo = dao.connect();
        //configure model's maze with data retrieved.
        setModel(new DefaultSceneEngineModel(startGameInfo.getMaze()));
        //create new player
        setCurrentPlayerId(startGameInfo.getPlayerId());
    }

    public void releaseBomb() {
        dao.sendFire();
    }

    public void move(Orientation direction) {
        switch (direction){
            case EAST:{
                dao.sendMoveRight();
                break;
            }
            case WEST:{
                dao.sendMoveLeft();
                break;
            }
            case NORTH:{
                dao.sendMoveUp();
                break;
            }
            case SOUTH:{
                dao.sendMoveDown();
                break;
            }
        }
    }
}
