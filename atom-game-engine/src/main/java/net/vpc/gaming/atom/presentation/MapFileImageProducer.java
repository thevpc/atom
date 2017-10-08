package net.vpc.gaming.atom.presentation;

import net.vpc.gaming.atom.util.AtomUtils;
import net.vpc.gaming.atom.util.RuntimeIOException;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: vpc
 * Date: 8/24/13
 * Time: 10:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class MapFileImageProducer implements ImageProducer {
    private Image[] fullImages;

    public MapFileImageProducer(String imageUrl) {
        InputStream figStream = AtomUtils.createStream(AtomUtils.changeFileExtension(imageUrl, "fig"));
        InputStream imageStream = AtomUtils.createStreamVariant(AtomUtils.changeFileExtension(imageUrl, "png"));
        Properties p = new Properties();
        try {
            try {
                p.load(figStream);
            } finally {
                figStream.close();
            }
        } catch (IOException e) {
            throw new RuntimeIOException(e);
        }
//            imageTileWidth = Integer.parseInt(p.getProperty("tileWidth"));
//            imageTileHeight = Integer.parseInt(p.getProperty("tileHeight"));
        int imageMapColumns = Integer.parseInt(p.getProperty("columns"));
        int imageMapRows = Integer.parseInt(p.getProperty("rows"));
        this.fullImages = AtomUtils.splitImage(imageStream, imageMapColumns, imageMapRows,0,0);
    }

    @Override
    public int getImagesCount(int type) {
        return fullImages.length;
    }

    @Override
    public Image getImage(int type, int index) {
        return fullImages[index % fullImages.length];
    }
}
