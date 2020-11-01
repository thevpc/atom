package tn.edu.eniso.soussecraft.main.presentation.view.structures;

import net.vpc.gaming.atom.model.Sprite;
import net.vpc.gaming.atom.presentation.Scene;
import net.vpc.gaming.atom.presentation.SpriteViewImageSelector;
import tn.edu.eniso.soussecraft.main.presentation.view.etc.DefaultDashboardSpriteView;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 10 dec. 2006 23:24:32
 */
public class AtomiumView extends DefaultDashboardSpriteView {

    public AtomiumView() {
        super(1, 4, "Atomium.png");
        setImageSelector(
                new SpriteViewImageSelector() {
                    @Override
                    public int getImageIndex(Sprite sprite, Scene scene, long frame, int imagesCount) {
                        return sprite.getPlayerId() - 1;
                    }
                }
        );
    }
}
