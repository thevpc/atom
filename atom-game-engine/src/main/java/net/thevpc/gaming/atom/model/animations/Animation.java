/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.model.animations;

import net.thevpc.gaming.atom.model.ModelPoint;
import net.thevpc.gaming.atom.model.DefaultSprite;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class Animation extends DefaultSprite {

    public Animation() {
        setAttackable(false);
        setSelectable(false);
        setCrossable(true);
        setLocation(new ModelPoint(getLocation().getX(), getLocation().getY(), Integer.MAX_VALUE));
//        setCollisionManager(new DefaultSpriteCollisionTask());
    }
}
