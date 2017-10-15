package net.vpc.gaming.atom.presentation;

import net.vpc.gaming.atom.model.ViewBox;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ImageGrid implements ImageMap{
    private int rows;
    private int columns;
    private int xseparator;
    private int yseparator;
    private boolean startsWithXseparator;
    private boolean endsWithXseparator;
    private boolean startsWithYseparator;
    private boolean endsWithYseparator;

    public ImageGrid(int columns,int rows) {
        this.rows = rows;
        this.columns = columns;
    }

    public int getRows() {
        return rows;
    }

    public ImageGrid setRows(int rows) {
        this.rows = rows;
        return this;
    }

    public int getColumns() {
        return columns;
    }

    public ImageGrid setColumns(int columns) {
        this.columns = columns;
        return this;
    }

    public int getXseparator() {
        return xseparator;
    }

    public ImageGrid setSeparator(int separator) {
        setXseparator(separator);
        setYseparator(separator);
        return this;
    }

    public ImageGrid setXseparator(int xseparator) {
        this.xseparator = xseparator;
        return this;
    }

    public int getYseparator() {
        return yseparator;
    }

    public ImageGrid setYseparator(int yseparator) {
        this.yseparator = yseparator;
        return this;
    }

    public boolean isStartsWithXseparator() {
        return startsWithXseparator;
    }

    public ImageGrid setStartsWithSeparator(boolean startsWithSeparator) {
        setStartsWithXseparator(startsWithSeparator);
        setStartsWithYseparator(startsWithSeparator);
        return this;
    }
    public ImageGrid setEndsWithSeparator(boolean startsWithSeparator) {
        setEndsWithXseparator(startsWithSeparator);
        setEndsWithYseparator(startsWithSeparator);
        return this;
    }

    public ImageGrid setStartsWithXseparator(boolean startsWithXseparator) {
        this.startsWithXseparator = startsWithXseparator;
        return this;
    }

    public boolean isEndsWithXseparator() {
        return endsWithXseparator;
    }

    public ImageGrid setEndsWithXseparator(boolean endsWithXseparator) {
        this.endsWithXseparator = endsWithXseparator;
        return this;
    }

    public boolean isStartsWithYseparator() {
        return startsWithYseparator;
    }

    public ImageGrid setStartsWithYseparator(boolean startsWithYseparator) {
        this.startsWithYseparator = startsWithYseparator;
        return this;
    }

    public boolean isEndsWithYseparator() {
        return endsWithYseparator;
    }

    public ImageGrid setEndsWithYseparator(boolean endsWithYseparator) {
        this.endsWithYseparator = endsWithYseparator;
        return this;
    }

    @Override
    public List<ViewBox> extract(int width, int height) {
        int xseps=(startsWithXseparator?1:0)+(endsWithXseparator?1:0)+(columns-1);
        int yseps=(startsWithYseparator?1:0)+(endsWithYseparator?1:0)+(rows-1);
        Dimension chunkDim= new Dimension(
                (width-xseps*xseparator)/columns,
                (height-yseps*yseparator)/rows
        );
        List<ViewBox> all=new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                int startx=startsWithXseparator?xseparator:0;
                int starty=startsWithYseparator?yseparator:0;
                int x0 = startx + (c > 0 ? (xseparator * (c - 1)) : 0) + chunkDim.width * c;
                int y0 = starty + (r > 0 ? (yseparator * (r - 1)) : 0) + chunkDim.height * r;
                all.add( new ViewBox(
                        x0,
                        y0,
                        chunkDim.width,
                        chunkDim.height
                ));
            }
        }
        return all;
    }
}
