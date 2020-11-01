/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.games.atom.examples.kombla.main.server.engine;

import net.vpc.gaming.atom.annotations.AtomSceneEngine;
import net.vpc.gaming.atom.engine.maze.Maze;
import net.vpc.gaming.atom.engine.maze.MazeTypeDecorator;
import net.vpc.gaming.atom.engine.tasks.HoldPositionSpriteTask;
import net.vpc.gaming.atom.engine.tasks.MoveSpriteTask;
import net.vpc.gaming.atom.model.*;
import net.vpc.games.atom.examples.kombla.main.server.dal.MainServerDAO;
import net.vpc.games.atom.examples.kombla.main.server.dal.MainServerDAOListener;
import net.vpc.games.atom.examples.kombla.main.shared.model.DynamicGameModel;
import net.vpc.games.atom.examples.kombla.main.shared.model.StartGameInfo;
import net.vpc.games.atom.examples.kombla.main.shared.engine.AbstractMainEngine;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
@AtomSceneEngine(id = "mainServer", columns = 12, rows = 12)
public class MainServerEngine extends AbstractMainEngine {

    private MainServerDAO dal;
    int[][] maze;

    public MainServerEngine() {
        int size = 12;
        maze = Maze.generateBoxedMaze(size, size, Maze.Algo.DEPTH_FIRST, new MazeTypeDecorator() {
            @Override
            public int decorate(int tileX, int tileY, boolean obstacle) {
                if (!obstacle) {
                    return (0);
                } else {
                    return (randomType(1, 5));
                }
            }

            private int randomType(int min, int max) {
                return (int) (min + Math.random() * (max - min));
            }
        });

        setModel(new DefaultSceneEngineModel(maze));
    }

    @Override
    protected void sceneActivating() {
        getModel().setProperty("Phase", "WAITING");
        Sprite boss = addBomberPlayer("Boss");
        setCurrentPlayerId(boss.getPlayerId());
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

    public Sprite addBomberPlayer(String name) {
        Player player = addPlayer();
        player.setName(name);
        addSprite(createPerson(player.getId()));
        Sprite person = findBomber(player.getId());
        setSpriteTask(person, new HoldPositionSpriteTask());
        person.setMovementStyle(MovementStyles.STOPPED);
        person.setLocation(randomVacantLocation(person));
        return person;
    }

    public Sprite createBomb(int playerId) {
        Sprite bomb = createSprite("Bomb")
                .setSize(new ModelDimension(0.5, 0.5))
                .setSpeed(0)
                .setCrossable(true)
                .setPlayerId(playerId);
        return bomb;
    }

    public Sprite createPerson(int playerId) {
        Sprite person = createSprite("Person");
        person.setSize(new ModelDimension(0.5, 0.5));
        person.setSpeed(0.05);
        person.setLife(30);
        person.setLayer(Sprite.SPRITES_LAYER + 3);
        person.setPlayerId(playerId);
        return person;
    }

    public Sprite findBomber(int playerId) {
        return findSpriteByKind("Person", playerId, null);
    }

    public void releaseBomb() {
        releaseBomb(getCurrentPlayerId());
    }

    public void releaseBomb(int playerId) {
        Sprite person = findBomber(playerId);
        if (person != null) {
            Sprite bomb = createBomb(person.getPlayerId());
            Tile tile = findTile(person.getLocation());
            bomb.setLocation(tile.getLocation());
            addSprite(bomb);
        }
    }

    @Override
    public void move(Orientation direction) {
        move(getCurrentPlayerId(), direction);
    }

    private void move(int playerId, Orientation direction) {
        Sprite person = findBomber(playerId);
        if (person != null) {
            if (!(getSpriteTask(person) instanceof MoveSpriteTask)) {
                setSpriteTask(person, new MoveSpriteTask());
                person.setMovementStyle(MovementStyles.MOVING_SLOW);
            }
            person.setDirection(direction);
        }
    }

    /**
     * each frame reevaluate Phase. This method is called by ATOM to UPDATE
     * model (R/W mode)
     */
    @Override
    public void updateModel() {
        if (findSpritesByKind("Person").size() < 2) {
            getModel().setProperty("Phase", "GAMEOVER");
        }
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
                dal.sendModelChanged(new DynamicGameModel(getFrame(), getSprites(), getPlayers()));
                break;
            }
        }
    }
}
