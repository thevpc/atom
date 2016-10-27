/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.util;

import net.vpc.gaming.atom.debug.AtomDebug;
import net.vpc.gaming.atom.presentation.ResizableImage;
import net.vpc.gaming.atom.presentation.SequenceGenerator;
import net.vpc.gaming.atom.presentation.components.Alignment;
import net.vpc.gaming.atom.presentation.components.TextStyle;
import net.vpc.gaming.atom.presentation.layers.LayerDrawingContext;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import net.vpc.gaming.atom.model.ModelBox;
import net.vpc.gaming.atom.model.ModelPoint;
import net.vpc.gaming.atom.model.ModelSegment;
import net.vpc.gaming.atom.model.ViewBox;
import net.vpc.gaming.atom.model.ViewPoint;

/**
 * @author vpc
 */
public class AtomUtils {

    public static int randomInt(int max) {
        return (int) (Math.random() * max);
    }

    public static int randomInt(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }

    public static Image createImage(String path) {
        return createImage(path, null);
    }

    public static Image createImage(String path, Class baseType) {
        URL url = AtomUtils.createURL(path, baseType);
        try {
            return ImageIO.read(url);
            //return new ImageIcon(AGEUtils.createURL(path,baseType)).getImage();
        } catch (IOException ex) {
            throw new IllegalArgumentException("unable to load image resource " + path);
        }
    }

    public static InputStream createStreamVariant(String... paths) {
        return createStreamVariant(null, paths);
    }

    public static InputStream createStreamVariant(Class baseType, String... paths) {
        for (String path : paths) {
            checkResourcePath(path, baseType);
        }
        if (baseType == null) {
            baseType = AtomUtils.class;
        }
        for (String path : paths) {
            InputStream resource = baseType.getResourceAsStream(path);
            if (resource != null) {
                return resource;
            }
        }
        for (String path : paths) {
            throwResourceNotFound(path, baseType);
        }
        throwResourceNotFound("/NoPathFound", baseType);
        return null;
    }

    public static InputStream createStream(String path) {
        return createStream(path, null);
    }

    public static InputStream createStream(String path, Class baseType) {
        checkResourcePath(path, baseType);
        if (baseType == null) {
            baseType = AtomUtils.class;
        }
        InputStream resource = baseType.getResourceAsStream(path);
        if (resource == null) {
            throwResourceNotFound(path, baseType);
        }
        return resource;
    }

    public static URL createURL(String path) {
        return createURL(path, null);
    }

    public static URL createURL(String path, Class baseType) {
        checkResourcePath(path, baseType);
        if (baseType == null) {
            baseType = AtomUtils.class;
        }
        URL resource = baseType.getResource(path);
        if (resource == null) {
            throwResourceNotFound(path, baseType);
        }
        return resource;
    }

    private static void checkResourcePath(String path, Class baseType) {
        if (path == null) {
            throw new IllegalArgumentException("Empty Resource Path");
        }
        if (baseType == null && !path.startsWith("/")) {
            throw new IllegalArgumentException("Absolute path expected : " + path);
        }
    }

    private static void throwResourceNotFound(String path, Class baseType) {
        if (path.startsWith("/")) {
            throw new IllegalArgumentException("Resource not found : " + path);
        }

        throw new IllegalArgumentException("Resource not found : /" + baseType.getPackage().getName().replace(".", "/") + "/" + path);
    }

    public static final InputStream createStream(URL u) {
        try {
            return u.openStream();
        } catch (IOException ex) {
            throw new RuntimeIOException(ex);
        }
    }

    public static ViewPoint getCenterdPosition(int width, int height, ViewBox bounds) {
        int w = width;
        int h = height;
        int x0 = bounds.getX() + bounds.getWidth() / 2 - w / 2;
        int y0 = bounds.getY() + bounds.getHeight() / 2 - h / 2;
        return new ViewPoint(x0, y0);
    }

    public static ViewBox drawCenteredString(Graphics2D g2d, String text, ViewBox bounds) {
        Rectangle2D stringBounds = g2d.getFont().getStringBounds(text, g2d.getFontRenderContext());
        int w = (int) Math.round(stringBounds.getWidth());
        int h = (int) Math.round(stringBounds.getHeight());
        int x0 = bounds.getX() + bounds.getWidth() / 2 - w / 2;
        int y0 = bounds.getY() + bounds.getHeight() / 2 - h / 2;
        FontMetrics fontMetrics = g2d.getFontMetrics();
//        g2d.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
        g2d.drawString(text, x0, y0 + fontMetrics.getAscent());
        return new ViewBox(x0, y0 + fontMetrics.getAscent(), fontMetrics.stringWidth(text), fontMetrics.getHeight());
//        g2d.drawRect(x0, y0, w, h);
    }

    public static AffineTransform createAntiIsometricTransform(ViewBox r) {
        AffineTransform t1 = AffineTransform.getRotateInstance(Math.PI / 4, r.getWidth() / 2, r.getHeight() / 2);
        AffineTransform t2 = AffineTransform.getScaleInstance(1, 0.5);
        t2.concatenate(t1);
        return t2;
    }

    public static AffineTransform createIsometricTransformInverse(ViewBox r) {
        AffineTransform mapTransform = createIsometricTransform(r);
        try {
            mapTransform = mapTransform.createInverse();
        } catch (NoninvertibleTransformException ex) {
            throw new IllegalArgumentException();
        }
        return mapTransform;
    }

    public static AffineTransform createIsometricTransformInverse(ModelBox r) {
        AffineTransform mapTransform = createIsometricTransform(r);
        try {
            mapTransform = mapTransform.createInverse();
        } catch (NoninvertibleTransformException ex) {
            throw new IllegalArgumentException();
        }
        return mapTransform;
    }

    public static AffineTransform createIsometricTransform(ViewBox r) {
        AffineTransform t1 = AffineTransform.getRotateInstance(-Math.PI / 4, r.getWidth() / 2, r.getHeight() / 2);
        AffineTransform t2 = AffineTransform.getScaleInstance(1, 0.5);
        t2.concatenate(t1);
        return t2;
    }

    public static AffineTransform createIsometricTransform(ModelBox r) {
        AffineTransform t1 = AffineTransform.getRotateInstance(-Math.PI / 4, r.getWidth() / 2, r.getHeight() / 2);
        AffineTransform t2 = AffineTransform.getScaleInstance(1, 0.5);
        t2.concatenate(t1);
        return t2;
    }

    public static BufferedImage[] splitImage(String imageMap, Class baseType, int cols, int rows) {
        return AtomUtils.splitImage(createStream(imageMap, baseType), cols, rows);
    }

    public static BufferedImage[] splitImage(String imageMap, int cols, int rows) {
        return splitImage(imageMap, null, cols, rows);
    }

    public static BufferedImage[] splitImage(InputStream fis, int cols, int rows) throws RuntimeIOException {
        return splitImage(loadBufferedImage(fis), cols, rows);
    }

    //    public static Image[] splitImage(Image image, int rows, int cols) throws IOException {
//        if(image instanceof BufferedImage){
//            return splitImage((BufferedImage)image, rows, cols);
//        }
//        imgs[count] = new BufferedImage(chunkWidth, chunkHeight, image.getType());
//
//    }
//
    public static BufferedImage[] splitImage(BufferedImage image, int cols, int rows) throws RuntimeIOException {

        //BufferedImage image = ImageIO.read(fis); //reading the image file
        int chunks = rows * cols;

        int chunkWidth = image.getWidth() / cols; // determines the chunk width and height
        int chunkHeight = image.getHeight() / rows;
        int count = 0;
        BufferedImage imgs[] = new BufferedImage[chunks]; //Image array to hold image chunks
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                //Initialize the image array with image chunks
                imgs[count] = new BufferedImage(chunkWidth, chunkHeight, BufferedImage.TYPE_INT_ARGB);

                // draws the image chunk
                Graphics2D gr = imgs[count++].createGraphics();
                gr.drawImage(image, 0, 0, chunkWidth, chunkHeight,
                        chunkWidth * x,
                        chunkHeight * y,
                        chunkWidth * x + chunkWidth,
                        chunkHeight * y + chunkHeight,
                        null);
                gr.dispose();
            }
        }
//        showImagesFrame(imgs);
        return imgs;
    }

    public static ResizableImage[][] splitResizableImageMatrix(BufferedImage image, int cols, int rows) throws RuntimeIOException {
        BufferedImage[][] bufferedImages = splitImageMatrix(image, cols, rows);
        ResizableImage[][] r = new ResizableImage[bufferedImages.length][];
        for (int i = 0; i < r.length; i++) {
            r[i] = new ResizableImage[bufferedImages[i].length];
            for (int j = 0; j < r[i].length; j++) {
                r[i][j] = new ResizableImage(bufferedImages[i][j]);
            }
        }
        return r;
    }

    public static ResizableImage[] splitResizableImage(BufferedImage image, int cols, int rows) throws RuntimeIOException {
        BufferedImage[] bufferedImages = splitImage(image, cols, rows);
        ResizableImage[] r = new ResizableImage[bufferedImages.length];
        for (int i = 0; i < r.length; i++) {
            r[i] = new ResizableImage(bufferedImages[i]);
        }
        return r;
    }

    public static BufferedImage[][] splitImageMatrix(BufferedImage image, int cols, int rows) throws RuntimeIOException {

        //BufferedImage image = ImageIO.read(fis); //reading the image file
//        int chunks = rows * cols;

        int chunkWidth = image.getWidth() / cols; // determines the chunk width and height
        int chunkHeight = image.getHeight() / rows;
        int count = 0;
        BufferedImage imgs[][] = new BufferedImage[rows][cols]; //Image array to hold image chunks
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                BufferedImage ii = new BufferedImage(chunkWidth, chunkHeight, BufferedImage.TYPE_INT_ARGB);
                //Initialize the image array with image chunks
                imgs[row][col] = ii;

                // draws the image chunk
                Graphics2D gr = ii.createGraphics();
                gr.drawImage(image, 0, 0, chunkWidth, chunkHeight, chunkWidth * col, chunkHeight * row, chunkWidth * (col + 1), chunkHeight * (row + 1), null);
                gr.dispose();
            }
        }
        return imgs;
    }

    public static Image resizeImage(Image image, int with, int height) {
        AtomDebug.DRAW_IMAGE_RESCALE_COUNT++;
        Image scaledInstance = image.getScaledInstance(with, height, Image.SCALE_SMOOTH);
        return scaledInstance;
//        BufferedImage buffered = new BufferedImage(with, height, BufferedImage.TYPE_INT_ARGB);
//        Graphics graphics = buffered.getGraphics();
//        graphics.drawImage(scaledInstance, 0, 0, null);
//        graphics.dispose();
//        return buffered;
//        int iw = image.getWidth(null);
//        int ih = image.getHeight(null);
//        if (iw == with && ih == height) {
//            return image;
//        }
//        int type = BufferedImage.TYPE_INT_ARGB;
//        if (image instanceof BufferedImage) {
//            BufferedImage bi = (BufferedImage) image;
//            type = bi.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : bi.getType();
//        }
//        BufferedImage resizedImage = new BufferedImage(with, height, type);
//        Graphics2D g = resizedImage.createGraphics();
//        g.setComposite(AlphaComposite.Src);
//        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g.drawImage(image, 0, 0, with, height, null);
//        g.dispose();
//        return resizedImage;
    }

    public static BufferedImage loadBufferedImage(File file) {
        FileInputStream is = null;
        try {
            try {
                is = new FileInputStream(file);
                return ImageIO.read(is);
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        } catch (IOException ex) {
            throw new RuntimeIOException(ex);
        }
    }

    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }
        MediaTracker tracker = new MediaTracker(new JLabel());
        tracker.addImage(image, 0);
        try {
            tracker.waitForID(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        BufferedImage b = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = b.createGraphics();
        AtomDebug.DRAW_IMAGE_DRAW_COUNT++;
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return b;
    }

    public static Image loadImage(String url) {
        return loadImage(createStream(url));
    }

    public static Image loadBufferedImage(String url) {
        return loadBufferedImage(createStream(url));
    }

    public static Image loadImage(InputStream in) {
        try {

//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            int next = in.read();
//            while (next > -1) {
//                bos.write(next);
//                next = in.read();
//            }
//            bos.flush();
//            byte[] result = bos.toByteArray();
//
//            return Toolkit.getDefaultToolkit().createImage(result);
            return ImageIO.read(in);
        } catch (IOException ex) {
            throw new RuntimeIOException(ex);
        }
    }

    public static BufferedImage loadBufferedImage(InputStream in) {
        try {

//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            int next = in.read();
//            while (next > -1) {
//                bos.write(next);
//                next = in.read();
//            }
//            bos.flush();
//            byte[] result = bos.toByteArray();
//
//            return toBufferedImage(Toolkit.getDefaultToolkit().createImage(result));
            return ImageIO.read(in);
        } catch (IOException ex) {
            throw new RuntimeIOException(ex);
        }
    }

    private static Alignment toAlignment(Alignment i) {
        if (i == null) {
            return Alignment.LEFT;
        }
        return i;
    }

    private static int toInt(Integer i) {
        if (i == null) {
            return 0;
        }
        return i.intValue();
    }

    private static boolean toBool(Boolean i) {
        if (i == null) {
            return false;
        }
        return i.booleanValue();
    }

    public static void drawText(LayerDrawingContext context, String text, int x, int y, int w, int h, TextStyle style, int cursorPosition) {
        Graphics2D graphics = context.getGraphics();

        int insetX = toInt(style.getInsetX());
        int insetY = toInt(style.getInsetY());
        Font font = style.getFont();
        Color foreColor = style.getForeColor();
        Color backgroundColor = style.getBackgroundColor();
        Color backgroundColor2 = style.getBackgroundColor2();
        Color borderColor = style.getBorderColor();
        Color cursorColor = style.getCursorColor();
        Alignment alignement = toAlignment(style.getAlignment());
        int borderWidth = toInt(style.getBorderWidth());
        boolean fillBackground = toBool(style.getFillBackground());
        SequenceGenerator blinkForegroundHelper = style.getBlinkForegroundHelper();
        SequenceGenerator blinkBackgroundHelper = style.getBlinkBackgroundHelper();
        SequenceGenerator blinkBorderHelper = style.getBlinkBorderHelper();
        SequenceGenerator blinkCursorHelper = style.getBlinkCursorHelper();
        long frame = context.getScene().getFrame();
        int arc = toInt(style.getBorderArc());

        Color validBorderColor = borderColor;
        if (validBorderColor == null) {
            validBorderColor = backgroundColor == null ? null : backgroundColor.darker().darker();
        }
        if (font != null) {
            graphics.setFont(font);
        }

        int x1 = x;
        int y1 = y;
        int width1 = w;
        int height1 = h;
        boolean showBackground = fillBackground && backgroundColor != null;
        if (showBackground) {
            if (blinkBackgroundHelper != null) {
                if (blinkBackgroundHelper.next(frame) == 1) {
                    //do not draw string
                    showBackground = false;
                }
            }
        }
        if (showBackground) {
            Paint oldPaint = graphics.getPaint();
            Color pc = backgroundColor;
            Color sc = backgroundColor2;
            if (sc == null) {
                sc = backgroundColor;
            }
            GradientPaint gp = new GradientPaint(x1, y1, sc, x1, y1 + height1, pc, true);
            graphics.setPaint(gp);
//            g2d.setColor(bkg);
            graphics.fillRoundRect(x1, y1, width1, height1, arc, arc);
            graphics.setPaint(oldPaint);
        }

        boolean showBorder = validBorderColor != null && borderWidth >= 0;
        if (showBorder) {
            if (blinkBorderHelper != null) {
                if (blinkBorderHelper.next(frame) == 1) {
                    //do not draw string
                    showBorder = false;
                }
            }
            if (showBorder) {
                Stroke oldStroke = graphics.getStroke();
                int sbordw = borderWidth;
                if (sbordw > 0) {
                    graphics.setStroke(new BasicStroke(borderWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    if (validBorderColor != null) {
                        graphics.setColor(validBorderColor);
//            g2d.setColor(fore);
//            g2d.draw3DRect(x, y, width, height, true);
                        graphics.drawRoundRect(x1, y1, width1, height1, arc, arc);
                        graphics.setStroke(oldStroke);
                    }
                }
            }
        }
        boolean showText = true;
        if (blinkForegroundHelper != null) {
            if (blinkForegroundHelper.next(frame) == 1) {
                //do not draw string
                showText = false;
            }
        }
        if (showText) {
            if (foreColor != null) {
                graphics.setColor(foreColor);
            }
            String t = text;

//            ViewBox allStringBox = null;
            switch (alignement) {
                case CENTER: {
//                    allStringBox =
                    drawCenteredString(graphics, t, new ViewBox(x1 + insetX, y1 + insetY, width1, height1));
                    break;
                }
                case LEFT: {
                    FontMetrics fontMetrics = graphics.getFontMetrics();
                    graphics.drawString(t, x1 + insetX, y1 + insetY + fontMetrics.getHeight());
//                    allStringBox = new ViewBox(x1 + insetX, y1 + insetY+ fontMetrics.getHeight(), fontMetrics.stringWidth(text), fontMetrics.getHeight());
                    break;
                }
                default: {
                    FontMetrics fontMetrics = graphics.getFontMetrics();
                    graphics.drawString(t, x1 + insetX, y1 + insetY + fontMetrics.getHeight());
//                    allStringBox = new ViewBox(x1 + insetX, y1 + insetY, fontMetrics.stringWidth(text), fontMetrics.getHeight());
                    break;
                }
            }
            boolean showCursor = cursorPosition >= 0 && cursorPosition <= text.length();
            if (showCursor) {
                if (blinkCursorHelper != null) {
                    if (blinkCursorHelper.next(frame) == 1) {
                        //do not draw string
                        showCursor = false;
                    }
                }
                if (showCursor) {
                    FontMetrics fontMetrics = graphics.getFontMetrics();
                    ViewBox box2 = new ViewBox(x1 + insetX, y1 + insetY, fontMetrics.stringWidth(text.substring(0, cursorPosition)), fontMetrics.getHeight());

//                        String cursorChar = cursorPosition >= text.length() ? "_" : String.valueOf(text.charAt(cursorPosition));
                    int cursorWidth = 2;//fontMetrics.stringWidth(cursorChar);
                    Color cc = cursorColor;
                    if (cc == null) {
                        cc = foreColor;
                    }
                    graphics.setColor(cc);
                    graphics.drawRect(box2.getX() + box2.getWidth(), box2.getY() + box2.getHeight() - fontMetrics.getAscent(), cursorWidth, fontMetrics.getAscent());

                }
            }
        }
//        graphics.setColor(Color.RED);
//        graphics.drawRect(x, y, w, h);

    }

    public static ViewPoint findParallellogrammeCenter(Shape parallellogramme) {
        PathIterator pathIterator = parallellogramme.getPathIterator(null);
        ArrayList<ModelPoint> points = new ArrayList<ModelPoint>();
        while (!pathIterator.isDone()) {
            double[] r = new double[10];
            int t = pathIterator.currentSegment(r);
            switch (t) {
                case PathIterator.SEG_MOVETO: {
                    points.add(new ModelPoint(r[0], r[1]));
                    break;
                }
                case PathIterator.SEG_LINETO: {
                    points.add(new ModelPoint(r[0], r[1]));
                    break;
                }
                case PathIterator.SEG_CLOSE: {
                    break;
                }
                default: {
                    throw new IllegalArgumentException("Why");
                }
            }
            pathIterator.next();
        }
        ModelPoint p1 = points.get(0);
        ModelPoint p2 = points.get(1);
        ModelPoint p3 = points.get(2);
        ModelPoint p4 = points.get(3);
//        Line2D L1=new Line2D.Double(points.get(0).getX(),points.get(0).getY(),points.get(2).getX(),points.get(2).getY());
//        Line2D L2=new Line2D.Double(points.get(1).getX(),points.get(1).getY(),points.get(3).getX(),points.get(3).getY());
//        Geometry.f.
        ModelSegment s1 = new ModelSegment(p1, p3);
        ModelPoint center = s1.getCenter();
        return new ViewPoint(center.getIntX(), center.getIntY());
//        DSegment s2 = new DSegment(p2, p4);
//        DPoint c = s1.intersect(s2);

//        return new Point(c.getIntX(), c.getIntY());
    }

    public static String getFileSimpleName(File f) {
        String name = f.getName();
        int x = name.lastIndexOf('.');
        if (x >= 0) {
            return name.substring(0, x);
        } else {
            return name;
        }
    }

    public static String getFileSimpleName(String f) {
        int i = f.lastIndexOf("/");
        String name = null;
        if (i < 0) {
            name = f;
        } else {
            name = f.substring(i);
        }
        int x = name.lastIndexOf('.');
        if (x >= 0) {
            return name.substring(0, x);
        } else {
            return name;
        }
    }

    public static String getFileExtension(File f) {
        String name = f.getName();
        int x = name.lastIndexOf('.');
        if (x >= 0) {
            return name.substring(x + 1);
        } else {
            return null;
        }
    }

    public static File changeFileExtension(File f, String newExtension) {
        File p = f.getParentFile();
        String name = getFileSimpleName(f);
        if (newExtension != null) {
            name += "." + newExtension;
        }
        return new File(p, getFileSimpleName(f) + "." + newExtension);
    }

    public static String changeFileExtension(String f, String newExtension) {
        int i = f.lastIndexOf("/");
        String parent = null;
        if (i < 0) {
            parent = null;
        } else {
            parent = f.substring(0, i);
        }
        String name = getFileSimpleName(f);
        if (newExtension != null) {
            name += "." + newExtension;
        }
        if (parent == null) {
            return name;
        } else {
            return parent + "/" + name;
        }
    }

    public static void copyFile(File from, File to) throws IOException {
        if (!from.equals(to)) {
            Files.copy(from.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public static boolean isAlphaImage(BufferedImage b) {
        Raster raster = b.getAlphaRaster();
        if (raster != null) {
            int[] alphaPixel = new int[raster.getNumBands()];
            for (int x = 0; x < raster.getWidth(); x++) {
                for (int y = 0; y < raster.getHeight(); y++) {
                    raster.getPixel(x, y, alphaPixel);
                    if (alphaPixel[0] != 0x00) {
                        return false;
                    }
                }
            }
        }


//        int[] rgb = b.getRGB(0, 0, b.getWidth(), b.getHeight(), null, 0, 1);
//        for (int i : rgb) {
//            if((i & 0xFF000000) != 0xFF000000){
//                return false;
//            }
//        }
        return true;
    }

    public static URL toURL(File f){
        try {
            return f.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeIOException(e);
        }
    }

    public static boolean safeEquals(double a,double b,double precision){
        double v = a - b;
        if(v<0){
            return -v<precision;
        }
        return v<precision;
    }
}
