/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.engine;

/**
 * A Task is the "Business" part of a scene sceneEngine that handles
 * the behavior of a sprite all over the time.
 * <p/>
 * Tasks have special method <code>nextFrame</code> that's invoked
 * every frame to update sprite model when time evolves
 * <p/>
 * The Simplest Way to implement a SceneEngineTask is to implement <code>SceneEngineTask</code>.
 * <p/>
 * The Following is a simple gravity Task that moves to the down one Unit each frame step :
 * <pre>
 * public class GravityTask implements SceneEngineTask {
 *    public public void nextFrame(SceneEngine scene){
 *       Person s=scene.findSprite(Person.class);
 *       ModelPoint p=s.getLocation();
 *       s.setLocation(new ModelPoint(p.getX(),p.getY()-1));
 *    }
 * }
 * </pre>
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public interface SceneEngineTask {

    /*
     * called each frame step. may update model
     * For instance, a move task should update sprite position according to the velocity of the sprite
     */
    public boolean nextFrame(SceneEngine scene);

}
