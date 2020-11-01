package tn.rnu.enit.ateliercorba.jcanon.gameengine.view;

import tn.rnu.enit.ateliercorba.jcanon.gameengine.model.Sprite;

/**
 * @author Taha Ben Salah (thevpc@walla.com)
 * @creationtime 5 dec. 2006 11:22:55
 */
public interface GameRendererManager {
    public SpriteView getSpriteView(Sprite sprite);
}
