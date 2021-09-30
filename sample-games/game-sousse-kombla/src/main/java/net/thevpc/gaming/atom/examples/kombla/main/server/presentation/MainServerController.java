package net.thevpc.gaming.atom.examples.kombla.main.server.presentation;

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package tn.edu.eniso.kombla.main.server.presentation;
//
//import AtomSceneController;
//import Orientation;
//import Sprite;
//import DefaultSceneController;
//import Scene;
//import SceneKeyEvent;
//import tn.edu.eniso.kombla.main.server.engine.MainServerEngine;
//
///**
// * @author Taha Ben Salah (taha.bensalah@gmail.com)
// */
//@AtomSceneController(
//        scene = "mainServer"
//)
//public class MainServerController extends DefaultSceneController {
//
//
//    public MainServerController() {
//    }
//
//    @Override
//    public void keyPressed(SceneKeyEvent e) {
//        keyPressedTask(e);
//    }
//
//    public void keyPressedTask(SceneKeyEvent e) {
//        MainServerEngine scene = e.getScene().getSceneEngine();
//        switch (e.getKeyCode()) {
//            case LEFT: {
//                scene.move(Orientation.WEST);
//                break;
//            }
//            case RIGHT: {
//                scene.move(Orientation.EAST);
//                break;
//            }
//            case UP: {
//                scene.move(Orientation.NORTH);
//                break;
//            }
//            case DOWN: {
//                scene.move(Orientation.SOUTH);
//                break;
//            }
//            case SPACE: {
//                scene.releaseBomb();
//                break;
//            }
//        }
//    }
//
//    @Override
//    public void sceneStarted(final Scene scene) {
//        MainServerEngine mscene = scene.getSceneEngine();
//        Sprite sprite = mscene.findBomber();
//        scene.lockCamera(sprite);
//        scene.addControlPlayer(scene.getPlayers().get(0).getId());
//    }
//}
