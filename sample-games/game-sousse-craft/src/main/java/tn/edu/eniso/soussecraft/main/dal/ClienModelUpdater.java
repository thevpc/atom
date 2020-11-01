/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.edu.eniso.soussecraft.main.dal;

import net.vpc.gaming.atom.engine.SceneEngine;
import net.vpc.gaming.atom.model.Sprite;

/**
 *
 * @author Taha Ben Salah
 */
public class ClienModelUpdater implements ModelUpdater {

    private DalModel dalModel;

    public ClienModelUpdater(DalModel dalModel) {
        this.dalModel = dalModel;
    }

    public void updateModel(SceneEngine engine) {
        engine.getModel().resetSprites();
        for (DalModel.DALSprite d : dalModel.getAdded()) {
//            if (engine.getSprite(d.getId()) == null) {
                Sprite c = d.create();
                engine.addSprite(c);
//            } else {
//                d.update(engine.getSprite(d.getId()));
//            }
        }
//        for (DalModel.DALSprite d : dalModel.getAdded()) {
//            Sprite c = d.create();
//            Sprite old = model.getSprite(c.getId());
//            if (old == null) {
//                model.addSprite(c, c.getId());
//            } else {
//                d.update(old);
//            }
//        }
//        for (DalModel.DALSprite d : dalModel.getUpdated()) {
//            d.update(model.getSprite(d.getId()));
//        }
//        for (Integer d : dalModel.getRemoved()) {
//            model.removeSprite(d);
//        }
    }

    public DalModel getDalModel() {
        return dalModel;
    }
}
