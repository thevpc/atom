/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.kombla.main.server.engine;

import net.thevpc.gaming.atom.examples.kombla.main.shared.engine.BaseMainEngine;
import net.thevpc.gaming.atom.examples.kombla.main.server.dal.MainServerDAOListener;
import net.thevpc.gaming.atom.examples.kombla.main.shared.model.DynamicGameModel;
import net.thevpc.gaming.atom.examples.kombla.main.shared.model.StartGameInfo;
import net.thevpc.gaming.atom.annotations.AtomSceneEngine;
import net.thevpc.gaming.atom.model.*;
import net.thevpc.gaming.atom.examples.kombla.main.server.dal.MainServerDAO;

import java.util.stream.Collectors;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
@AtomSceneEngine(id = "mainServer", columns = 12, rows = 12)
public class MainServerEngine extends BaseMainEngine {

    private MainServerDAO dal;


    @Override
    protected void sceneActivating() {
        super.sceneActivating();
        //put here your MainClientDAO instance
//        dal = new TCPMainServerDAO();
//        dal = new UDPMainServerDAO();
        dal.start(new MainServerDAOListener() {
            @Override
            public StartGameInfo onReceivePlayerJoined(String name) {
                Sprite sprite = addBomberPlayer(name);
                int playerId = sprite.getPlayerId();
                return new StartGameInfo(playerId, maze);
            }

            @Override
            public void onReceiveMoveLeft(int playerId) {
                move(playerId, Orientation.WEST);
            }

            @Override
            public void onReceiveMoveRight(int playerId) {
                move(playerId, Orientation.EAST);
            }

            @Override
            public void onReceiveMoveUp(int playerId) {
                move(playerId, Orientation.NORTH);

            }

            @Override
            public void onReceiveMoveDown(int playerId) {
                move(playerId, Orientation.SOUTH);
            }

            @Override
            public void onReceiveReleaseBomb(int playerId) {
                releaseBomb(playerId);
            }
        }, getAppConfig(getGameEngine()));
    }




    /**
     * each frame broadcast shared data to players. This method is called by
     * ATOM to READ model (R/O mode)
     */
    @Override
    protected void modelUpdated() {
        switch ((String) getModel().getProperty("Phase")) {
            case "WAITING": {
                //do nothing
                break;
            }
            case "GAMING":
            case "GAMEOVER": {
                //do nothing
                dal.sendModelChanged(new DynamicGameModel(getFrame(),
                        //copy to fix ObjectOutputStream issue!
                        getSprites().stream().map(Sprite::copy).collect(Collectors.toList())
                        , getPlayers().stream().map(Player::copy).collect(Collectors.toList())
                ));
                break;
            }
        }
    }
}
