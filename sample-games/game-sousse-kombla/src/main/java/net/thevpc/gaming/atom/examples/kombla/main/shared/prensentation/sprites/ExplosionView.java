/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.kombla.main.shared.prensentation.sprites;

import net.thevpc.gaming.atom.annotations.AtomSpriteView;
import net.thevpc.gaming.atom.model.Sprite;
import net.thevpc.gaming.atom.presentation.ImageSpriteView;
import net.thevpc.gaming.atom.presentation.Scene;
import net.thevpc.gaming.atom.presentation.SpriteViewImageSelector;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
@AtomSpriteView(
        scene = "mainLocal,mainServer,mainClient",
        kind = "Explosion"
)
public class ExplosionView extends ImageSpriteView {

    public ExplosionView() {
        super("/explosion.png", 5, 5);
        //setResizeImages(true);
        setImageSelector(new SpriteViewImageSelector() {
            @Override
            public int getImageIndex(Sprite sprite, Scene scene, long frame, int imagesCount) {
                Object explosionTime = sprite.getProperty("explosionTime");
                if(explosionTime!=null){
                    if(explosionTime instanceof String){
                        explosionTime=Integer.parseInt((String)explosionTime);
                    }
                    int explosionTimeInt=(int)explosionTime;
                    return explosionTimeInt;
                }
                return 0;
            }
        });
    }
}
