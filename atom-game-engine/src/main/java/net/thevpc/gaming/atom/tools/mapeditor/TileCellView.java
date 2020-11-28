/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.tools.mapeditor;

import net.thevpc.gaming.atom.model.CellDef;
import net.thevpc.gaming.atom.util.AtomUtils;

import javax.swing.*;
import java.awt.image.BufferedImage;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class TileCellView {

    CellDef cell;
    BufferedImage image;
    Icon icon;

    public TileCellView(CellDef cell, BufferedImage image) {
        this.cell = cell;
        this.image = image;
        icon = new ImageIcon(AtomUtils.resizeImage(image, 100, 100));
    }

    public CellDef getCell() {
        return cell;
    }

    public BufferedImage getImage() {
        return image;
    }

    public Icon getIcon() {
        return icon;
    }
}
