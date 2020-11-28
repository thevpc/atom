/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.kombla.main.shared.engine;

import net.thevpc.gaming.atom.annotations.AtomSceneEngine;
import net.thevpc.gaming.atom.engine.maintasks.HoldPositionSpriteMainTask;
import net.thevpc.gaming.atom.engine.maintasks.MoveSpriteMainTask;
import net.thevpc.gaming.atom.engine.maze.Maze;
import net.thevpc.gaming.atom.engine.maze.MazeDecorator;
import net.thevpc.gaming.atom.engine.maze.MazeTypeDecorator;
import net.thevpc.gaming.atom.examples.kombla.main.shared.engine.AbstractMainEngine;
import net.thevpc.gaming.atom.model.*;


/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class BaseMainEngine extends AbstractMainEngine {

    protected int[][] maze;

    public BaseMainEngine() {
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
    }

    public Sprite addBomberPlayer(String name) {
        Player player = addPlayer().setName(name);
        addSprite(createPerson(player.getId()));
        Sprite person = findBomber(player.getId());
        setSpriteMainTask(person, new HoldPositionSpriteMainTask());
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

    public void move(Orientation direction) {
        move(getCurrentPlayerId(), direction);
    }

    protected void move(int playerId, Orientation direction) {
        Sprite person = findBomber(playerId);
        if (person != null) {
            if (!(getSpriteMainTask(person) instanceof MoveSpriteMainTask)) {
                setSpriteMainTask(person, new MoveSpriteMainTask());
                person.setMovementStyle(MovementStyles.MOVING_SLOW);
            }
            person.setDirection(direction);
        }
    }

}
