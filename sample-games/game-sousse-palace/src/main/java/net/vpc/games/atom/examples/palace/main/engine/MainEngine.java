/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.games.atom.examples.palace.main.engine;

import net.vpc.gaming.atom.annotations.AtomSceneEngine;
import net.vpc.gaming.atom.engine.DefaultSceneEngine;
import net.vpc.gaming.atom.engine.SpriteTask;
import net.vpc.gaming.atom.engine.tasks.MoveSpriteTask;
import net.vpc.gaming.atom.extension.physics.PhysicsExtension;
import net.vpc.gaming.atom.model.*;
import net.vpc.games.atom.examples.palace.main.model.Brick;
import net.vpc.games.atom.examples.palace.main.model.Prince;


/**
 *
 * @author Taha Ben Salah
 */

@AtomSceneEngine(id = "main",fps = 25)
public final class MainEngine extends DefaultSceneEngine {

    public MainEngine() {
        setModel(new DefaultSceneEngineModel(16, 16));
        setRowWall(0, 13, 6);
        setRowWall(6, 12, 6);
        Player player = addPlayer();
        addSprite(new Prince()
                .setPlayerId(player.getId())
                .setLocation(new ModelPoint(8, 8))
                .setTask(new MoveSpriteTask())
                .setCollisionManager(new PrinceCollisionManager())
        )
        ;
        addSprite(new Brick()
                .setLocation(new ModelPoint(6, 14)));
        hold();
        installExtension(new PhysicsExtension(0.02));
//        installExtension(new GravityExtension(0.02));
    }

    public void jumpUp() {
        System.out.println("jumpUp");
        Prince prince = findSprite(Prince.class);
        if (prince == null) {
            return;
        }
        prince.setVelocity(ModelVector.newAngular(Prince.JUMP_SPEED, KeyDirection.UP));
    }

    public void jumpLeft() {
        System.out.println("jumpLeft " + ModelVector.newAngular(10, -3 * Math.PI / 4).getDirection());
        Prince prince = findSprite(Prince.class);
        if (prince == null) {
            return;
        }
        prince.setVelocity(ModelVector.newAngular(Prince.JUMP_SPEED, KeyDirection.UP_LEFT));
    }

    public void jumpRight() {
        System.out.println("jumpRight");
        Prince prince = findSprite(Prince.class);
        if (prince == null) {
            return;
        }
        SpriteTask old = getSpriteTask(prince);
        prince.setVelocity(ModelVector.newAngular(Prince.JUMP_SPEED, KeyDirection.UP_RIGHT));
//        prince.setVelocity(ModelVector.newAngular(2,Direction.NORTH_EAST));
    }

    public void hold() {
        Prince prince = findSprite(Prince.class);
        if (prince == null) {
            return;
        }
        prince.setVelocity(ModelVector.newAngular(0, KeyDirection.UP));
    }

    public void moveLeft() {
        Prince prince = findSprite(Prince.class);
        if (prince == null) {
            return;
        }
        SpriteTask old = getSpriteTask(prince);
//        if (!(old instanceof DefaultMoveTask)) {
//            prince.setTask(new DefaultMoveTask());
//        }
        prince.setVelocity(ModelVector.newAngular(Prince.MOVE_SPEED, KeyDirection.LEFT));
    }

    public void moveRight() {
        Prince prince = findSprite(Prince.class);
        if (prince == null) {
            return;
        }
        SpriteTask old = getSpriteTask(prince);
        prince.setVelocity(ModelVector.newAngular(Prince.MOVE_SPEED, KeyDirection.RIGHT));
//        if (!(old instanceof DefaultMoveTask)) {
//            prince.setTask(new DefaultMoveTask());
//        }
//        prince.face(Direction.RIGHT);
    }

    private void setRowWall(int col, int row, int columnsCount) {
        final Tile[][] tiles = getModel().getTileMatrix();
        for (int i = 0; i < columnsCount; i++) {
            setWall(tiles[row][col + i]);
        }
    }

    private void setWall(Tile t) {
        t.setWalls(Tile.WALL_BOX);
        t.setKind(4);
    }
}
