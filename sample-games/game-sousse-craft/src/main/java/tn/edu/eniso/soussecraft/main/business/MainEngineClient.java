/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.edu.eniso.soussecraft.main.business;

import net.vpc.gaming.atom.annotations.AtomSceneEngine;
import net.vpc.gaming.atom.model.ModelPoint;
import net.vpc.gaming.atom.model.Tile;
import tn.edu.eniso.soussecraft.main.dal.DALClient;
import tn.edu.eniso.soussecraft.main.dal.DALClientListener;
import tn.edu.eniso.soussecraft.main.dal.ModelUpdater;
import tn.edu.eniso.soussecraft.main.model.GamePhase;

/**
 *
 * @author Taha Ben Salah
 */
@AtomSceneEngine(id = "mainClient")
public class MainEngineClient extends MainEngine {

    private DALClient dal;
    public MainEngineClient() {
        dal = new DALClient(new DALClientListener() {

            public void playerConnected(int playerId) {
//                model.setControlPlayer(playerId);
            }

            public void gameStarted(final ModelUpdater updater) {
                invokeLater(new Runnable() {

                    public void run() {
                        updater.updateModel(MainEngineClient.this);
                        setGamePhase(GamePhase.STARTED);
                    }
                });
            }

            public void modelChanged(final ModelUpdater updater) {
                invokeLater(new Runnable() {

                    public void run() {
                        updater.updateModel(MainEngineClient.this);
                    }
                });
            }
        });
    }

    @Override
    protected void sceneActivated() {
        dal.start();
    }

    @Override
    public void selectSprite(int playerId, ModelPoint point, Integer spriteId, boolean appendSelection) {
        dal.selectSprite(playerId, point, spriteId, appendSelection);
    }

    public void selectTile(int playerId, ModelPoint point, Tile sprite) {
        dal.selectTile(playerId, point, playerId);
    }

    public void selectTile(int playerId, ModelPoint point, int idTile) {
        dal.selectTile(playerId, point, idTile);
    }

    public void moveSelectionToSprite(int playerId, ModelPoint point, Integer spriteId) {
        dal.moveSelectionToSprite(playerId, point, spriteId);
    }

    public void moveSelectionToTile(int playerId, ModelPoint point, Integer tileId) {
        dal.moveSelectionToTile(playerId, point, tileId);
    }
}
