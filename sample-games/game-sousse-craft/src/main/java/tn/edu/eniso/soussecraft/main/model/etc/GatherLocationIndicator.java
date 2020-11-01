/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.edu.eniso.soussecraft.main.model.etc;

import net.vpc.gaming.atom.model.ModelPoint;
import net.vpc.gaming.atom.model.animations.LocationIndicator;

/**
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class GatherLocationIndicator extends LocationIndicator {

    public GatherLocationIndicator(int playerId, ModelPoint position) {
        super(playerId, position, 20);
    }
    
}
