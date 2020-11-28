package net.thevpc.gaming.atom.examples.soussecraft.main.presentation.view.units;

import java.awt.Graphics2D;

import net.thevpc.gaming.atom.model.*;
import net.thevpc.gaming.atom.presentation.ImageSpriteView;
import net.thevpc.gaming.atom.presentation.OrientationImageSelector;
import net.thevpc.gaming.atom.presentation.Scene;
import net.thevpc.gaming.atom.examples.soussecraft.main.presentation.view.etc.DashboardSupport;
import net.thevpc.gaming.atom.examples.soussecraft.main.presentation.view.etc.DashboardSpriteView;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 10 dec. 2006 23:24:32
 */
public class WorkerView extends ImageSpriteView implements DashboardSpriteView {

    private DashboardSupport dashboardSupport;

    public WorkerView() {
        super("Worker.png", 16, 6);
        setImageSelector(new OrientationImageSelector(OrientationType.PLUS_ORIENTATION) {
            @Override
            protected int getImageIndex(Sprite sprite, Scene view, long frame, Orientation dir, int player, double speed) {
                int animation = (speed == 0)? 0 : (int) (frame / 6);
                boolean fighting = false;
                int dirIndex = 0;
                switch (dir) {
                    case SOUTH: {
                        dirIndex = 0;
                        break;
                    }
                    case NORTH: {
                        dirIndex = 1;
                        break;
                    }
                    case EAST: {
                        dirIndex = 2;
                        break;
                    }
                    case WEST: {
                        dirIndex = 3;
                        break;
                    }
                }
                if (fighting) {
                    return (player - 1) * 24 + (dirIndex * 6) + 4 + animation % 2;
                } else {
                    return (player - 1) * 24 + (dirIndex * 6) + animation % 4;
                }
            }
        });
        setMargin(new ViewPoint(7, 15));
        dashboardSupport = new DashboardSupport(this);
    }

    public void drawDetails(Sprite sprite, Graphics2D graphics, Scene view, ViewPoint point, ViewDimension dimension) {
        dashboardSupport.drawDetails(sprite, graphics, view, point, dimension);
    }
}
