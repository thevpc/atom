/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.soussecraft.main.model.etc;

import net.thevpc.gaming.atom.model.ModelPoint;
import net.thevpc.gaming.atom.model.animations.LocationIndicator;

/**
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class GatherLocationIndicator extends LocationIndicator {

    public GatherLocationIndicator(int playerId, ModelPoint position) {
        super(playerId, position, 20);
    }
    
}
