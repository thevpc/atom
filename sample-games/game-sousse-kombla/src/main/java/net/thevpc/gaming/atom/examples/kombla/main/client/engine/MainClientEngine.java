/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.kombla.main.client.engine;

import net.thevpc.gaming.atom.examples.kombla.main.client.dal.MainClientDAO;
import net.thevpc.gaming.atom.examples.kombla.main.client.dal.MainClientDAOListener;
import net.thevpc.gaming.atom.examples.kombla.main.shared.engine.AbstractMainEngine;
import net.thevpc.gaming.atom.examples.kombla.main.shared.model.DynamicGameModel;
import net.thevpc.gaming.atom.examples.kombla.main.shared.model.StartGameInfo;
import net.thevpc.gaming.atom.annotations.AtomSceneEngine;
import net.thevpc.gaming.atom.model.*;
import net.thevpc.gaming.atom.model.*;

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
                            Player p = createPlayer().copyFrom(player);
                            addPlayer(p);
                        }
                        for (Sprite sprite : model.getSprites()) {
                            Sprite s = createSprite(sprite.getKind()).copyFrom(sprite);
                            if("Person".equals(sprite.getKind()) || "Bomb".equals(sprite.getKind())){
                                s.setSize(new ModelDimension(0.5,0.5));
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
