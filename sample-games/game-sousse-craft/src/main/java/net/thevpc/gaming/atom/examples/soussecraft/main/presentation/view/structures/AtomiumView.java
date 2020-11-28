package net.thevpc.gaming.atom.examples.soussecraft.main.presentation.view.structures;

import net.thevpc.gaming.atom.model.Sprite;
import net.thevpc.gaming.atom.presentation.Scene;
import net.thevpc.gaming.atom.presentation.SpriteViewImageSelector;
import net.thevpc.gaming.atom.examples.soussecraft.main.presentation.view.etc.DefaultDashboardSpriteView;

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
