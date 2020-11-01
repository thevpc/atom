/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.games.atom.examples.kombla.main.local.engine;

import net.vpc.gaming.atom.annotations.AtomSceneEngine;
import net.vpc.gaming.atom.engine.maze.Maze;
import net.vpc.gaming.atom.engine.maze.MazeDecorator;
import net.vpc.gaming.atom.engine.tasks.HoldPositionSpriteTask;
import net.vpc.gaming.atom.engine.tasks.MoveSpriteTask;
import net.vpc.gaming.atom.model.*;
import net.vpc.games.atom.examples.kombla.main.shared.engine.AbstractMainEngine;


/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
@AtomSceneEngine(id = "mainLocal", columns = 12, rows = 12)
public class MainLocalEngine extends AbstractMainEngine {

    public MainLocalEngine() {
        setModel(new DefaultSceneEngineModel(generateMaze(12, 12)));
    }

    private static CellDef[][] generateMaze(int columns, int rows) {
        return Maze.generateBoxedMaze(columns, rows, Maze.Algo.DEPTH_FIRST, new MazeDecorator() {

            @Override
            public void decorate(CellDef cell, boolean obstacle) {
                if (!obstacle) {
                    cell.setType(0);
                } else {
                    cell.setType(randomType(1, 5));
                }
            }

            private int randomType(int min, int max) {
                return (int) (min + Math.random() * (max - min));
            }
        });

    }

    @Override
    protected void sceneActivating() {
        Sprite boss = addBomberPlayer("Boss");
        setCurrentPlayerId(boss.getPlayerId());
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
        Sprite bomb = createSprite("Bomb");
        bomb.setSize(new ModelDimension(1, 1));
        bomb.setSpeed(0);
        bomb.setCrossable(true);
        bomb.setPlayerId(playerId);
        return bomb;
    }

    public Sprite createPerson(int playerId) {
        Sprite person = createSprite("Person");
        person.setSize(new ModelDimension(1, 1));
        person.setSpeed(0.1);
        person.setLife(3);
//        person.setLayer(Sprite.SPRITES_LAYER + 3);
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

}
