/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.presentation;

import net.thevpc.gaming.atom.engine.SceneEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class DefaultSceneFactory implements SceneFactory {

    private Map<String, Scene> viewForScene = new HashMap<String, Scene>();

    public void bindScene(Scene scene, String sceneID) {
        Scene v = viewForScene.get(sceneID);
        if (v != null) {
            throw new IllegalArgumentException("Scene alread bound : " + sceneID);
        }
        viewForScene.put(sceneID, scene);
    }

    public void bindScene(Scene scene, Class<? extends SceneEngine> sceneEngineType) {
        bindScene(scene, sceneEngineType.getName());
    }

    public Scene getScene(String sceneID) {
        Scene v = viewForScene.get(sceneID);
        if (v == null) {
            throw new NoSuchElementException("Scene not found : " + sceneID);
        }
        return v;
    }

    public boolean isBound(String sceneID) {
        return viewForScene.containsKey(sceneID);
    }

    @Override
    public Scene createScene(String sceneID) {
        return getScene(sceneID);
    }


}
