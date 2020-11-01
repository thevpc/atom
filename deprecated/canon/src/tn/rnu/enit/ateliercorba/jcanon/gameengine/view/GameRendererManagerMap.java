package tn.rnu.enit.ateliercorba.jcanon.gameengine.view;

import tn.rnu.enit.ateliercorba.jcanon.gameengine.model.Sprite;

import java.util.Hashtable;

/**
 * @author Taha Ben Salah (thevpc@walla.com)
 * @creationtime 11 dec. 2006 00:21:55
 */
public class GameRendererManagerMap implements GameRendererManager{

    public GameRendererManagerMap() {
    }

    private Hashtable<Class,SpriteView> viewForModel=new Hashtable<Class, SpriteView>();

    public void register(Class model, SpriteView view){
        viewForModel.put(model,view);
    }


    public SpriteView getSpriteView(Sprite sprite) {
        return getSpriteView(sprite.getClass());
    }

    public SpriteView getSpriteView(Class model){
        return viewForModel.get(model);
    }

}
