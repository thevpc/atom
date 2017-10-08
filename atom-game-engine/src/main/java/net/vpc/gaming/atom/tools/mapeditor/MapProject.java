/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.tools.mapeditor;

import net.vpc.gaming.atom.model.DefaultSceneEngineModel;
import net.vpc.gaming.atom.model.DefaultSceneModelWriter;
import net.vpc.gaming.atom.util.AtomUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class MapProject {

    private File baseFile;
    private DefaultSceneEngineModel sceneEngineModel;
    private File mapFile;
    private File imageFile;
    private File figFile;
    private int cellColumnsPerImage;
    private int cellRowsPerImage;

    public MapProject(File baseFile) {
        this.baseFile = baseFile;
        mapFile = AtomUtils.changeFileExtension(baseFile, "map");
        imageFile = AtomUtils.changeFileExtension(baseFile, "png");
        figFile = AtomUtils.changeFileExtension(baseFile, "fig");
    }

    public DefaultSceneEngineModel getSceneEngineModel() {
        return sceneEngineModel;
    }

    public void setSceneEngineModel(DefaultSceneEngineModel sceneEngineModel) {
        this.sceneEngineModel = sceneEngineModel;
    }

    public int getCellColumnsPerImage() {
        return cellColumnsPerImage;
    }

    public void setCellColumnsPerImage(int cellColumnsPerImage) {
        this.cellColumnsPerImage = cellColumnsPerImage;
    }

    public int getCellRowsPerImage() {
        return cellRowsPerImage;
    }

    public void setCellRowsPerImage(int cellRowsPerImage) {
        this.cellRowsPerImage = cellRowsPerImage;
    }

    public File getBaseFile() {
        return baseFile;
    }

    public File getMapFile() {
        return mapFile;
    }

    public File getImageFile() {
        return imageFile;
    }

    public File getFigFile() {
        return figFile;
    }

    public void saveAs(File file) throws IOException {
        if (AtomUtils.changeFileExtension(baseFile, "map").equals(AtomUtils.changeFileExtension(file, "map"))) {
            save();
        } else {
            AtomUtils.copyFile(AtomUtils.changeFileExtension(baseFile, "png"), AtomUtils.changeFileExtension(file, "png"));
            Properties p = new Properties();
            p.setProperty("rows", String.valueOf(cellRowsPerImage));
            p.setProperty("columns", String.valueOf(cellColumnsPerImage));
            FileWriter w = null;
            try {
                w = new FileWriter(getFigFile());
                p.store(w, "");
            } finally {
                if (w != null) {
                    w.close();
                }
            }
            DefaultSceneModelWriter ww = new DefaultSceneModelWriter(getMapFile());
            ww.write(sceneEngineModel);
            ww.close();
        }
    }

    public void save() throws IOException {
        Properties p = new Properties();
        p.setProperty("rows", String.valueOf(cellRowsPerImage));
        p.setProperty("columns", String.valueOf(cellColumnsPerImage));
        FileWriter w = null;
        try {
            w = new FileWriter(getFigFile());
            p.store(w, "");
        } finally {
            if (w != null) {
                w.close();
            }
        }
        DefaultSceneModelWriter ww = new DefaultSceneModelWriter(getMapFile());
        ww.write(sceneEngineModel);
        ww.close();
    }

    public void load() throws IOException {

        Properties p = new Properties();
        FileReader w = null;
        try {
            File figFile1 = getFigFile();
            w = new FileReader(figFile1);
            p.load(w);
        } finally {
            if (w != null) {
                w.close();
            }
        }
        cellRowsPerImage = Integer.parseInt(p.getProperty("rows"));
        cellColumnsPerImage = Integer.parseInt(p.getProperty("columns"));
        sceneEngineModel = new DefaultSceneEngineModel(mapFile);
    }

    public BufferedImage[] loadTileImages() {
        BufferedImage[] images;
        images = AtomUtils.splitImage(AtomUtils.loadBufferedImage(imageFile), getCellColumnsPerImage() * getSceneEngineModel().getCellWidth(), getSceneEngineModel().getCellHeight() * getCellRowsPerImage(),0,0);
        return images;
    }

    public BufferedImage[] loadCellImages() {
        BufferedImage[] images;
        images = AtomUtils.splitImage(AtomUtils.loadBufferedImage(imageFile), getCellColumnsPerImage(), getCellRowsPerImage(),0,0);
        return images;
    }
}
