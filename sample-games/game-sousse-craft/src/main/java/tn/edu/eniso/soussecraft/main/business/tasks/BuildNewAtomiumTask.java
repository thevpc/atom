/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.edu.eniso.soussecraft.main.business.tasks;

import net.vpc.gaming.atom.model.Sprite;
import tn.edu.eniso.soussecraft.main.model.structures.Atomium;

/**
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class BuildNewAtomiumTask extends BuildTask {

    public BuildNewAtomiumTask(double  speed) {
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
