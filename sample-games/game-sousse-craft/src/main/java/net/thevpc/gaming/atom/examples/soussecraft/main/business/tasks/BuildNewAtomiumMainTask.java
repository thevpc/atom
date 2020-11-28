/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.soussecraft.main.business.tasks;

import net.thevpc.gaming.atom.model.Sprite;
import net.thevpc.gaming.atom.examples.soussecraft.main.model.structures.Atomium;

/**
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class BuildNewAtomiumMainTask extends BuildMainTask {

    public BuildNewAtomiumMainTask(double  speed) {
        super();
    }

    public Sprite createBuilding() {
        return new Atomium();
    }

    @Override
    public String toString() {
        return "Build Atomium";
    }

    

}
