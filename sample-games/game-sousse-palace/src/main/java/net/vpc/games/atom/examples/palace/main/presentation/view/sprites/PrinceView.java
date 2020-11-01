/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.games.atom.examples.palace.main.presentation.view.sprites;

import net.vpc.gaming.atom.annotations.AtomSpriteView;
import net.vpc.gaming.atom.model.MovementStyles;
import net.vpc.gaming.atom.model.Orientation;
import net.vpc.gaming.atom.model.OrientationType;
import net.vpc.gaming.atom.model.Sprite;
import net.vpc.gaming.atom.presentation.ImageSpriteView;
import net.vpc.gaming.atom.presentation.OrientationImageSelector;
import net.vpc.gaming.atom.presentation.Scene;

/**
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
@AtomSpriteView(kind = "Prince")
public class PrinceView extends ImageSpriteView {

    public PrinceView() {
//        super(1, 1, "person.png");
        super("/net/vpc/games/atom/examples/palace/person.png", 13, 16, new OrientationImageSelector(OrientationType.PLUS_ORIENTATION) {
            @Override
            protected int getImageIndex(Sprite sprite, Scene view, long frame, Orientation dir, int player, double speed) {
                boolean move = sprite.getMovementStyle()!= MovementStyles.STOPPED
                        || Math.abs((sprite.getVelocity().getX()))>1e-2;
                int max_players = 4;
                int anime_per_dir = 13;
                int anime_per_player = anime_per_dir * 4;
                int ndx_frame = move ? ((int) (frame % anime_per_dir)) : 0;//(anime_per_dir - 1);
                int ndx_dir = !move? 3:(dir == Orientation.EAST ? 0 : dir == Orientation.WEST ? 1 : dir == Orientation.NORTH ? 2 : dir == Orientation.SOUTH ? 3 : -1);
                int ndx_player = player<=0?0:(player - 1 % max_players);
                int index = ndx_player * anime_per_player + ndx_dir * anime_per_dir + ndx_frame;
//                System.out.println("dir="+dir+" speed="+speed+" move="+move+" index="+index);
                return index;
            }
        });
    }
}
