/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.soussecraft.main.model.resources;

import net.thevpc.gaming.atom.extension.strategy.resources.Resource;
import net.thevpc.gaming.atom.model.ModelDimension;
import net.thevpc.gaming.atom.model.ModelPoint;

/**
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class Minerals extends Resource {

    public Minerals(int reserve, double x, double y) {
        super(reserve);
        setSize(new ModelDimension(2, 1));
        setLocation(new ModelPoint(x, y));
    }
}
