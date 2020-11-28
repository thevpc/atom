/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.presentation;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public interface SceneFactory {

    public Scene createScene(String sceneID);
//    public SceneView createSceneView(Class<? extends Scene> sceneType);
}
