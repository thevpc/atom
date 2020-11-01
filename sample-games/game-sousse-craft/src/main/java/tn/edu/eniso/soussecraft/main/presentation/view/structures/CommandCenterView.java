package tn.edu.eniso.soussecraft.main.presentation.view.structures;

import net.vpc.gaming.atom.model.ViewPoint;
import tn.edu.eniso.soussecraft.main.presentation.view.etc.DefaultDashboardSpriteView;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 10 dec. 2006 23:24:32
 */
public class CommandCenterView extends DefaultDashboardSpriteView {

    public CommandCenterView() {
        super(1,4,"CommandCenter.png");
        setMargin(new ViewPoint(0,60));
        setImageSelector((sprite, scene, frame, imagesCount) -> sprite.getPlayerId()-1);
    }
}
