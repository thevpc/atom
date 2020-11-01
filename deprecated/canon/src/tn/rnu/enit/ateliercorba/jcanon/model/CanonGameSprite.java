package tn.rnu.enit.ateliercorba.jcanon.model;

import tn.rnu.enit.ateliercorba.jcanon.gameengine.model.AbstractSprite;

/**
 * @author Taha Ben Salah (thevpc@walla.com)
 * @creationtime 13 oct. 2007 19:56:31
 */
public class CanonGameSprite extends AbstractSprite {
    public CanonGameSprite() {
    }

    public CanonGameModel getGame() {
        return (CanonGameModel) super.getGame();
    }
}
