/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.soussecraft.main.business;

import net.thevpc.gaming.atom.annotations.AtomScene;
import net.thevpc.gaming.atom.annotations.AtomSceneEngine;
import net.thevpc.gaming.atom.engine.SpriteMainTask;
import net.thevpc.gaming.atom.engine.maintasks.AttackSpriteMainTask;
import net.thevpc.gaming.atom.engine.maintasks.FindPathToPointSpriteMainTask;
import net.thevpc.gaming.atom.examples.soussecraft.main.business.tasks.BuildMainTask;
import net.thevpc.gaming.atom.extension.strategy.StrategySceneExtension;
import net.thevpc.gaming.atom.extension.strategy.players.StrategyGamePlayer;
import net.thevpc.gaming.atom.extension.strategy.resources.Resource;
import net.thevpc.gaming.atom.extension.strategy.tasks.GatherResourceSpriteMainTask;
import net.thevpc.gaming.atom.model.ModelPoint;
import net.thevpc.gaming.atom.model.Player;
import net.thevpc.gaming.atom.model.Sprite;
import net.thevpc.gaming.atom.model.SpriteSelection;
import net.thevpc.gaming.atom.examples.soussecraft.main.ModelTracker;
import net.thevpc.gaming.atom.examples.soussecraft.main.dal.DALServer;
import net.thevpc.gaming.atom.examples.soussecraft.main.dal.DALServerListener;
import net.thevpc.gaming.atom.examples.soussecraft.main.model.GamePhase;
import net.thevpc.gaming.atom.examples.soussecraft.main.model.etc.AttackLocationIndicator;
import net.thevpc.gaming.atom.examples.soussecraft.main.model.etc.GatherLocationIndicator;
import net.thevpc.gaming.atom.examples.soussecraft.main.model.etc.GotoLocationIndicator;
import net.thevpc.gaming.atom.examples.soussecraft.main.model.resources.Minerals;
import net.thevpc.gaming.atom.examples.soussecraft.main.model.resources.Woods;
import net.thevpc.gaming.atom.examples.soussecraft.main.model.structures.CommandCenter;
import net.thevpc.gaming.atom.examples.soussecraft.main.model.units.Worker;

/**
 *
 * @author Taha Ben Salah
 */
@AtomSceneEngine(id = "mainServer")
@AtomScene(id = "mainServer")
public class MainEngineServer extends MainEngine {

    private DALServer dal;
    private ModelTracker modelTracker=new ModelTracker();

    public MainEngineServer() {
        dal = new DALServer(new DALServerListener() {

            public int clientConnected() {
                int playersCount = getPlayers().size();
                if (playersCount < 4) {
                    StrategyGamePlayer player = new StrategyGamePlayer("Player " + (playersCount + 1));
                    int c = addPlayer(player);
                    setGamePhase(GamePhase.STARING);
                    return c;
                } else {
                    throw new IllegalArgumentException("Too many players");
                }
            }

            public void selectTile(int playerId, ModelPoint point, int idTile) {
                MainEngineServer.this.selectTile(playerId, point, idTile);
            }

            public void selectSprite(int playerId, ModelPoint point, Integer spriteId, boolean appendSelection) {
                MainEngineServer.this.selectSprite(playerId, point, spriteId, appendSelection);
            }

            public void moveSelectionToSprite(int playerId, ModelPoint point, Integer spriteId) {
                MainEngineServer.this.moveSelectionToSprite(playerId, point, spriteId);
            }

            public void moveSelectionToTile(int playerId, ModelPoint point, Integer tileId) {
                MainEngineServer.this.moveSelectionToTile(playerId, point, tileId);
            }
        });
    }


    public void startGame() {
        modelTracker.install(this);
        if (getPlayers().size() < 2) {
            throw new IllegalArgumentException("Too few players");
        }
        prepareGameModel();
        setGamePhase(GamePhase.STARTED);
        dal.gameStarted(this,modelTracker);
    }

    @Override
    protected void modelUpdating() {
        modelTracker.reset();
    }

    @Override
    protected void modelUpdated() {
        if (getGamePhase() == GamePhase.STARTED) {
            dal.modelChanged(this,modelTracker);
        }
    }

    private void prepareGameModel() {
        addSprite(new Minerals(2000, 12, 20));
        addSprite(new Woods(2000, 16, 20));
        for (Player player : getPlayers()) {
            switch (player.getId()) {
                case 1: {
                    prepareModelPlayer(1, new Placer(0, 0, 1, 1));
                    break;
                }
                case 2: {
                    prepareModelPlayer(2, new Placer(80, 0, -1, 1));
                    break;
                }
                case 3: {
                    prepareModelPlayer(1, new Placer(0, 0, 1, -1));
                    break;
                }
                case 4: {
                    prepareModelPlayer(2, new Placer(80, 0, -1, -1));
                    break;
                }
            }
        }

    }

    private void prepareModelPlayer(int playerId, Placer placer) {
        StrategyGamePlayer player = (StrategyGamePlayer) getPlayer(playerId);
        StrategySceneExtension extension = getExtension(StrategySceneExtension.class);
        player.getResources().setMaxResource(1000, extension.getResourceTypes());

        addSprite(placer, playerId, new CommandCenter(), 0, 14);
        //addSprite(placer, player, new Atomimum(),10,0);
//        addSprite(placer, player, new Ship(),9,8);
//        addSprite(placer, player, new Ship(),15,8);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                addSprite(placer, playerId, new Worker(), 20 + i * 2, 10 + (int) (j * 1.5));
            }

        }
//        addSprite(placer, playerId, new Worker(),0,10);
//        addSprite(placer, player, new Worker(),2,8);
//        addSprite(placer, player, new Worker(),2,10);
//        addSprite(placer, player, new Worker(),4,8);
//        addSprite(placer, player, new Worker(),4,10);
//        addSprite(placer, player, new Worker(),6,8);
//        addSprite(placer, player, new Worker(),6,10);
    }

    private void addSprite(Placer placer, int player, Sprite s, int x, int y) {
        s.setLocation(placer.locate(x, y, s));
        s.setPlayerId(player);
        addSprite(s);
    }

    private class Placer {

        private int x;
        private int y;
        private int xf;
        private int yf;

        public Placer(int x, int y, int xf, int yf) {
            this(x, y, xf, yf, 15, 15);
        }

        public Placer(int x, int y, int xf, int yf, int cellW, int cellH) {
            this.x = x;
            this.y = y;
            this.xf = xf;
            this.yf = yf;
        }

        private ModelPoint locate(int x, int y, Sprite s) {
            double x1 = xf * x + this.x;
            if (xf < 0) {
                x1 = x1 - s.getWidth();
            }
            double y1 = yf * y + this.y;
            if (yf < 0) {
                y1 = y1 - s.getHeight();
            }
            return new ModelPoint(x1, y1);
        }
    }

    @Override
    protected void sceneActivated() {
        dal.start();
        int player = getModel().addPlayer(new StrategyGamePlayer("server"));
        //gameModel.setControlPlayer(player);
    }

    public void selectTile(int playerId, ModelPoint point, int idTile) {
        getModel().getPlayer(playerId).getSelection().clear();
    }

    public void selectSprite(int playerId, ModelPoint point, Integer spriteId, boolean appendSelection) {
        Player player = getPlayer(playerId);
        SpriteSelection selection = player.getSelection();
        Sprite sprite = spriteId == null ? null : getModel().getSprite(spriteId);
        if (sprite == null) {
            for (Sprite s : selection.getSelectedSprites()) {
                if (s instanceof Worker) {
                    SpriteMainTask spriteMainTask = s.getMainTask();
                    if (spriteMainTask instanceof BuildMainTask) {
                        BuildMainTask tt = (BuildMainTask) spriteMainTask;
                        tt.setLocation(point);
                    }
                }
            }
        } else {

            if (!appendSelection) {
                selection.clear();
                if (sprite != null && sprite.isSelectable()) {
                    selection.add(sprite);
                }
            } else {
                if (sprite != null && sprite.isSelectable()) {
                    if (selection.contains(sprite)) {
                        selection.remove(sprite);
                    } else {
                        selection.add(sprite);
                    }
                }
            }
        }
    }

    private CommandCenter findCommandCenter(int playerId) {
        return findSpriteByPlayer(CommandCenter.class, playerId);
    }

    public void moveSelectionToSprite(int playerId, ModelPoint point, Integer spriteId) {

        Player player = getPlayer(playerId);
        SpriteSelection selection = player.getSelection();
        Sprite sprite = getModel().getSprite(spriteId);
        if (sprite.getPlayerId() == Player.NO_PLAYER) {
            if (sprite instanceof Resource) {
                addSprite(new GatherLocationIndicator(playerId, point));
                CommandCenter commandCenter = findCommandCenter(playerId);
                for (Sprite sprite2 : selection.getSelectedSprites()) {
                    if (sprite2.isOwnedByPlayer(playerId)) {
                        sprite2.setMainTask(new GatherResourceSpriteMainTask(commandCenter.getId(), sprite.getId()));
                    }
                }
                return;
            }
        } else if (sprite.getPlayerId() != playerId) {
            addSprite(new AttackLocationIndicator(playerId, point));
            Sprite ennemi = sprite;
            for (Sprite sprite2 : selection.getSelectedSprites()) {
                if (sprite2.isOwnedByPlayer(playerId)) {
                    sprite2.setMainTask(new AttackSpriteMainTask(ennemi, 2));
                }
            }

            //                for (Integer spriteId : player.getSelectedSprites()) {
//                    Sprite sprite2 = gameModel.getSprite(spriteId);
//                    if (sprite2.isOwnedByPlayer(playerId)) {
//                        sprite2.setSpriteTask(new AttackSpriteMainTask(point));
//                    }
//                }
            return;
        }

    }

    public void moveSelectionToTile(int playerId, ModelPoint point, Integer tileId) {

        Player player = getPlayer(playerId);
        addSprite(new GotoLocationIndicator(playerId, point));
        SpriteSelection selection = player.getSelection();
        for (Sprite sprite2 : selection.getSelectedSprites()) {
            if (sprite2.isOwnedByPlayer(playerId)) {
                sprite2.setMainTask(new FindPathToPointSpriteMainTask(point));
//                sprite2.setTask(new LinearMoveToTask(point, true));
            }
        }
    }
}
