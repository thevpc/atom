package net.thevpc.gaming.atom.presentation;

import javax.swing.Timer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.*;

public class TemporalScoreBoard extends JComponent {
    private ModelIterator iter;
    private SnapshotDiff snapshotDiff;
    private Timer timer;
    private Model model;
    private int maxAnimation = 12;
    private int maxRowHeight = 100;
    private int animation = 0;
    private int fps = 100;
    private boolean ended = false;
    private boolean timerStarted = false;
    private boolean timerPaused = false;
    private ColPalette colorPalette = new ColPalette2();
    private NumberFormat valueFormat;
    private NumberFormat percentFormat;
    private Font valueFont;
    private Font nameFont;
    private float barArc = 0.3f;
    private float barStroke = 3;
    private int vgap = 10;

    public TemporalScoreBoard(Model model) {
        this.model = model;
    }

    public TemporalScoreBoard(Val[] vals, double step) {
        this.model = new ValModel(vals, step);
    }

    public static void main(String[] args) {
//        ArrayList<Integer> ar=new ArrayList<>(Stream.iterate(1,x->x+1).limit(10).collect(Collectors.toList()));
//        Collections.shuffle(ar);
//        System.out.println(ar);
//        if(true){
//            return;
//        }
        JFrame frame = new JFrame();
        Val[] vals = {
                new Val("Esprit-IA", 13.70),
                new Val("Alpha Team", 17.32),
                new Val("DeepTeam", 14.23),
                new Val("Going Data Way", 11.25),
                new Val("The lieutenants", 13.88),

//                new Val("Packman Bytes", 0),
//                new Val("NWA", 0),
//                new Val("Smart process", 0),
//                new Val("CardioPatch", 0),
//                new Val("G2foss", 0),
        };
        TemporalScoreBoard b = new TemporalScoreBoard(vals, 0.2);
        b.setColorPalette(new ColPalette3(vals));
        b.setValueFormat(new DecimalFormat("#00.00"));
        b.setNameFont(new Font("Fetamont Script", Font.BOLD, 24));
        b.setValueFont(new Font("Fetamont Script", Font.BOLD, 16));
        b.setPreferredSize(new Dimension(600, 400));

        JPanel panel = new JPanel(new BorderLayout());
        GradientLabel title = new GradientLabel("IA Health Hackathon 2021");
        title.setMinimumSize(new Dimension(400, 100));
        title.setPreferredSize(new Dimension(400, 100));
        title.setForeground(new Color(0x3363a2ff));
        title.setBackground(Color.BLACK);
        title.setOpaque(true);
        title.setBorder(BorderFactory.createEtchedBorder());
        title.setFont(new Font("Montague", Font.PLAIN, 32));
//        title.setHorizontalTextPosition(SwingConstants.CENTER);
//        title.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(title, BorderLayout.NORTH);
        panel.add(b, BorderLayout.CENTER);

        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);
        b.addMouseSupport();
    }

    public void addMouseSupport() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (!timerStarted) {
                        start();
                    } else {
                        if (e.getClickCount() == 1) {
                            timerPaused = !timerPaused;
                        } else if (e.getClickCount() == 2) {
                            if (timerPaused) {
                                tic();
                            }
                        }
                    }
                }
            }
        });
    }

    public ColPalette getColorPalette() {
        return colorPalette;
    }

    public TemporalScoreBoard setColorPalette(ColPalette colorPalette) {
        this.colorPalette = colorPalette;
        return this;
    }

    public Font getNameFont() {
        return nameFont;
    }

    public TemporalScoreBoard setNameFont(Font nameFont) {
        this.nameFont = nameFont;
        return this;
    }

    public NumberFormat getValueFormat() {
        return valueFormat;
    }

    public TemporalScoreBoard setValueFormat(NumberFormat valueFormat) {
        this.valueFormat = valueFormat;
        return this;
    }

    public NumberFormat getPercentFormat() {
        return percentFormat;
    }

    public TemporalScoreBoard setPercentFormat(NumberFormat percentFormat) {
        this.percentFormat = percentFormat;
        return this;
    }

    public Font getValueFont() {
        return valueFont;
    }

    public TemporalScoreBoard setValueFont(Font valueFont) {
        this.valueFont = valueFont;
        return this;
    }

    protected String createValueString(double vv){
        //vv+=10;
        return valueFormat == null ? String.valueOf(vv) : valueFormat.format(vv);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D gg = (Graphics2D) g;
        Dimension s = getSize();

        gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gp = new GradientPaint(0, 0,
                Color.WHITE, s.width, s.height,
                new Color(254, 240, 230));
        gg.setPaint(gp);
        gg.fillRect(0, 0, s.width, s.height);


        if (snapshotDiff != null) {
            List<SnapshotValueDiff> items = snapshotDiff.items();
            int itemSize = items.size();
            if (itemSize > 0) {
                float anim = animation / (float) maxAnimation;
                int w = s.width;
                int h = s.height / itemSize;
                if (w > 0 && h > 0) {
                    if (h > maxRowHeight) {
                        h = maxRowHeight;
                    }
                    FontMetrics fontMetrics = getFontMetrics(getFont());
                    FontMetrics itemFontMetrics = nameFont == null ? fontMetrics : getFontMetrics(nameFont);
                    FontMetrics valueFontMetrics = valueFont == null ? fontMetrics : getFontMetrics(valueFont);
                    List<String> itemStrings = new ArrayList<>();
                    List<String> valueStrings = new ArrayList<>();
                    int wSuffix = 0;
                    for (SnapshotValueDiff item : items) {
                        String iString = String.valueOf(item.item());
                        itemStrings.add(iString);
                        double vv = item.newValue == null ? 0 : item.newValue.value();
                        String vString = createValueString(vv);
                        valueStrings.add(vString);
                        Rectangle2D b = itemFontMetrics.getStringBounds(iString, g);
                        if (b.getWidth() > wSuffix) {
                            wSuffix = (int) b.getWidth();
                        }
                    }
                    wSuffix += 5;
                    for (int i = 0; i < items.size(); i++) {
                        SnapshotValueDiff item = items.get(i);
                        paintSnapshotValueDiff(item, gg, anim, w - wSuffix, h, fontMetrics, itemFontMetrics, valueFontMetrics
                                , itemStrings.get(i)
                                , valueStrings.get(i)
                        );
                    }
                }
            }
        }
    }


    protected void paintSnapshotValueDiff(SnapshotValueDiff d, Graphics2D g, float animation, int width, int height,
                                          FontMetrics fontMetrics,
                                          FontMetrics titleFontMetrics,
                                          FontMetrics valueFontMetrics,
                                          String titleString,
                                          String valueString
    ) {
        Stroke oldStroke = g.getStroke();
        g.setFont(getFont());
        int oldIndex = d.oldValue == null ? -1 : d.oldValue.index();
        float oldPercent = d.oldValue == null ? 0 : d.oldValue.percent();
        int newIndex = d.newValue == null ? -1 : d.newValue.index();
        float newPercent = d.newValue == null ? 0 : d.newValue.percent();

        int y0 = oldIndex < 0 ? -100 : oldIndex * height;
        int y1 = newIndex < 0 ? -100 : newIndex * height;
        int y = (int) SqrtDoubleInterpolator.INSTANCE.eval(y0, y1, animation);

        int w0 = (int) ((width) * oldPercent);
        if (w0 < 10) {
            w0 = 10;
        }
        int w1 = (int) ((width) * newPercent);
        if (w1 < 10) {
            w1 = 10;
        }
        int w = (int) LinearDoubleInterpolator.INSTANCE.eval(w0, w1, animation);

//        float hue = 0.9f; //hue
//        float saturation = 1.0f; //saturation
//        float brightness = 0.8f; //brightness
//
        Object item = d.item();
        Color myRGBColor = colorPalette.getColor(item);
        float[] hsb = Color.RGBtoHSB(myRGBColor.getRed(), myRGBColor.getGreen(), myRGBColor.getBlue(), null);
        long glowPeriod = 5;
        hsb[2] = (float) (0.8f + Math.sin((System.currentTimeMillis() % (glowPeriod * 1000)) / ((glowPeriod * 1000.0)) * 2 * Math.PI) * 0.2f);
        myRGBColor = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gp = new GradientPaint(0, y,
                myRGBColor.brighter().brighter(), 0, y + height,
                myRGBColor.darker().darker());
        g.setPaint(gp);
        int arc = (int) (height * barArc);
        if (arc < 1) {
            arc = 1;
        }
        int vgapBefore = (int) Math.ceil(vgap / 2.0);
        int vgapAfter = vgap - vgapBefore;

        g.fillRoundRect(2, y + vgapBefore, w, height - vgap, arc, arc);

        g.setStroke(new BasicStroke(barStroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(myRGBColor.darker().darker().darker());
        g.drawRoundRect(2, y + vgapBefore, w, height - vgap, arc, arc);


        g.setColor(detectedForeground(myRGBColor.darker().darker()));
        g.setFont(valueFont == null ? getFont() : valueFont);
        Rectangle2D valueStringBounds = valueFontMetrics.getStringBounds(valueString, g);
        g.drawString(valueString, w - (int) valueStringBounds.getWidth() - 3,
                (int) (y + height / 2 - valueStringBounds.getHeight() / 2) + valueFontMetrics.getAscent()
        );

//        g.drawRect( w - (int) valueStringBounds.getWidth() - 3,
//                (int)(h + height/2 - valueStringBounds.getHeight()/2),
//                (int) valueStringBounds.getWidth(),
//                (int) valueStringBounds.getHeight()
//        );

        g.setColor(Color.BLACK);
        g.setFont(nameFont == null ? getFont() : nameFont);
        Rectangle2D titleStringBounds = titleFontMetrics.getStringBounds(titleString, g);
        g.drawString(titleString, w + 5,
                (int) (y + height / 2 - titleStringBounds.getHeight() / 2) + titleFontMetrics.getAscent()
        );
    }

    private Color detectedForeground(Color background) {
        int r = background.getRed();
        int g = background.getGreen();
        int b = background.getBlue();
        double lum = (0.2126 * r) + (0.7151 * g) + (0.0721 * b);
        return lum < 50 ? Color.WHITE : Color.BLACK;
    }

    public void tic() {
        if (!ended) {
            if (iter == null) {
                iter = model.iter();
            }
            if (snapshotDiff == null) {
                animation = 0;
                Snapshot q = iter.next();
                if (q == null) {
                    ended = true;
                    iter = null;
                } else {
                    snapshotDiff = new SnapshotDiff(null, q);
                }
            } else {
                if (animation < maxAnimation) {
                    animation++;
                } else {
                    Snapshot q = iter.next();
                    if (q == null) {
                        ended = true;
                        iter = null;
                    } else {
                        animation = 0;
                        Snapshot oldSnapshot = snapshotDiff.newSnapshot;
                        snapshotDiff = new SnapshotDiff(oldSnapshot, q);
                    }
                }
            }
        }
        debug();
        invalidate();
        repaint();
    }

    public void start() {
        if (timerStarted) {
            return;
        }
        timerStarted = true;
        ended = false;
        int delay = 1000 / fps;
        if (delay < 10) {
            delay = 10;
        }
        timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!timerPaused) {
                    tic();
                }
            }
        });
        timer.start();
    }

    private void debug() {
//        if (snapshotDiff == null) {
//            System.out.println("snapshotDiff=null");
//        } else {
//            System.out.println(snapshotDiff.items());
//        }
    }


    public enum SnapshotValueAction {
        ADDED, REMOVED, UPDATED, UNCHANGED
    }

    public interface SnapshotValue {
        int index();

        Object item();

        double value();

        float percent();

        boolean frozen();
    }

    public interface Snapshot {
        Object time();

        int indexOf(Object value);

        List<SnapshotValue> items();

        double min();

        double max();
    }

    public interface Model {
        ModelIterator iter();
    }

    public interface ModelIterator {
        Snapshot next();
    }

    public interface ColPalette {
        Color getColor(Object item);
    }

    public interface DoubleInterpolator {
        double eval(double from, double to, float time);
    }

    public static class SnapshotValueImpl implements SnapshotValue {
        int index;

        Object item;

        double value;
        float percent;
        boolean frozen;

        public SnapshotValueImpl(int index, Object item, double value, float percent, boolean frozen) {
            this.index = index;
            this.item = item;
            this.value = value;
            this.percent = percent;
            this.frozen = frozen;
        }

        @Override
        public int index() {
            return index;
        }

        @Override
        public Object item() {
            return item;
        }

        @Override
        public double value() {
            return value;
        }

        @Override
        public float percent() {
            return percent;
        }

        @Override
        public boolean frozen() {
            return frozen;
        }
    }

    public static class SnapshotDiff {
        Snapshot oldSnapshot;
        Snapshot newSnapshot;
        List<SnapshotValueDiff> items = new ArrayList<>();

        public SnapshotDiff(Snapshot oldSnapshot, Snapshot newSnapshot) {
            this.oldSnapshot = oldSnapshot;
            this.newSnapshot = newSnapshot;
            List<SnapshotValue> oitems = new ArrayList<>(oldSnapshot == null ? new ArrayList<>() : oldSnapshot.items());
            List<SnapshotValue> nitems = new ArrayList<>(newSnapshot == null ? new ArrayList<>() : newSnapshot.items());
            Map<Object, SnapshotValue> oitemsMap = new LinkedHashMap<>();
            Map<Object, SnapshotValue> nitemsMap = new LinkedHashMap<>();
            for (SnapshotValue oitem : oitems) {
                oitemsMap.put(oitem.item(), oitem);
            }
            if (oitemsMap.size() != oitems.size()) {
                throw new IllegalArgumentException("invalid objects");
            }
            for (SnapshotValue nitem : nitems) {
                nitemsMap.put(nitem.item(), nitem);
            }
            if (nitemsMap.size() != nitems.size()) {
                throw new IllegalArgumentException("invalid objects");
            }
            LinkedHashSet<Object> allKeys = new LinkedHashSet<>(nitemsMap.keySet());
            allKeys.addAll(oitemsMap.keySet());
            for (Object k : allKeys) {
                SnapshotValue o = oitemsMap.get(k);
                SnapshotValue n = nitemsMap.get(k);
                items.add(new SnapshotValueDiff(o, n));
            }
        }

        List<SnapshotValueDiff> items() {
            return items;
        }
    }

    public static class SnapshotImpl implements Snapshot {
        List<SnapshotValue> items;
        Map<Object, Integer> objToIndex = new HashMap<>();
        private Object time;
        private double min;
        private double max;

        public SnapshotImpl(Object time, Map<Object, Val2> value, final double min, final double max) {
            this.time = time;
            this.min = min;
            this.max = max;
//            double min = Double.NaN;
//            double max = Double.NaN;
            this.items = new ArrayList<>();
            for (Map.Entry<Object, Val2> objectDoubleEntry : value.entrySet()) {
                Val2 vd = objectDoubleEntry.getValue();
                Double v = vd == null ? null : vd.value;
                double v0 = v == null ? Double.NaN : v;
                items.add(new SnapshotValueImpl(
                        0, objectDoubleEntry.getKey(),
                        v0, 0, vd == null ? false : vd.frozen
                ));
                if (!Double.isNaN(v0) && !Double.isInfinite(v0)) {
                    if (Double.isNaN(this.max) || this.max < v0) {
                        this.max = v0;
                    }
                    if (Double.isNaN(this.min) || this.min > v0) {
                        this.min = v0;
                    }
                }
            }
            for (int i = 0; i < items.size(); i++) {
                SnapshotValueImpl ii = (SnapshotValueImpl) items.get(i);
                if (Double.isNaN(this.min) || this.max <= this.min) {
                    ii.percent = 0;
                } else {
                    if (Double.isNaN(ii.value)) {
                        ii.percent = 0;
                    } else if (Double.isFinite(ii.value)) {
                        ii.percent = (float) ((ii.value - this.min) / (this.max - this.min));
                    } else if (ii.value > 0) {
                        ii.percent = 1;
                    } else {
                        ii.percent = 0;
                    }
                }
            }
            items.sort(new Comparator<SnapshotValue>() {
                @Override
                public int compare(SnapshotValue o1, SnapshotValue o2) {
                    float v1 = o1.percent();
                    float v2 = o2.percent();
                    return Float.compare(v2, v1);
                }
            });
            for (int i = 0; i < items.size(); i++) {
                SnapshotValueImpl ii = (SnapshotValueImpl) items.get(i);
                ii.index = i;
                objToIndex.put(ii.item, ii.index);
            }
        }

        @Override
        public Object time() {
            return time;
        }

        @Override
        public int indexOf(Object value) {
            Integer i = objToIndex.get(value);
            return i == null ? -1 : i;
        }

        @Override
        public List<SnapshotValue> items() {
            return items;
        }

        public double min() {
            return min;
        }

        public double max() {
            return max;
        }
    }

    public static class SnapshotValueDiff {
        SnapshotValue oldValue;
        SnapshotValue newValue;
        SnapshotValueAction action;

        public SnapshotValueDiff(SnapshotValue oldValue, SnapshotValue newValue) {
            this.oldValue = oldValue;
            this.newValue = newValue;
            if (oldValue == null && newValue == null) {
                action = SnapshotValueAction.UNCHANGED;
            } else if (oldValue == null) {
                action = SnapshotValueAction.ADDED;
            } else if (newValue == null) {
                action = SnapshotValueAction.REMOVED;
            } else if (oldValue.index() == newValue.index()) {
                action = SnapshotValueAction.UNCHANGED;
            } else {
                action = SnapshotValueAction.UPDATED;
            }
        }

        public Object item() {
            if (oldValue != null) {
                return oldValue.item();
            }
            if (newValue != null) {
                return newValue.item();
            }
            return null;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(action.toString().toLowerCase()).append("{");
            sb.append(item());
            sb.append(":");
            switch (action) {
                case ADDED: {
                    sb.append(newValue.value()).append("(").append(newValue.percent()).append("%)");
                    break;
                }
                case REMOVED: {
                    sb.append(oldValue.value()).append("(").append(oldValue.percent()).append("%)");
                    break;
                }
                case UNCHANGED: {
                    if (newValue != null) {
                        sb.append(oldValue.value()).append("(").append(oldValue.percent()).append("%)");
                    } else {
                        sb.append("null");
                    }
                    sb.append("->");
                    if (newValue != null) {
                        sb.append(newValue.value()).append("(").append(newValue.percent()).append("%)");
                        sb.append(newValue.value()).append("(").append(newValue.percent()).append("%)");
                    } else {
                        sb.append("null");
                    }
                    break;
                }
                case UPDATED: {
                    sb.append(oldValue.value()).append("(").append(oldValue.percent()).append("%)");
                    sb.append("->");
                    sb.append(newValue.value()).append("(").append(newValue.percent()).append("%)");
                    break;
                }
            }
            sb.append("}");
            return sb.toString();
        }
    }

    public static class Val2 {
        double value;
        boolean frozen;

        public Val2(double value, boolean frozen) {
            this.value = value;
            this.frozen = frozen;
        }

        public double getValue() {
            return value;
        }

        public boolean isFrozen() {
            return frozen;
        }
    }

    public static class Val {
        Object object;
        double value;

        public Val(Object object, double value) {
            this.object = object;
            this.value = value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(object, value);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Val val = (Val) o;
            return Double.compare(val.value, value) == 0 && Objects.equals(object, val.object);
        }
    }

    private static class ValModel implements Model {
        private final Val[] vals0;
        private final Val[] vals;
        private final double step;
        private final double min;
        private final double max;

        public ValModel(Val[] vals, double step) {
            double min = Double.NaN;
            double max = Double.NaN;
            this.vals = vals;
            this.step = step;
            this.vals0 = new Val[vals.length];
            for (int i = 0; i < vals.length; i++) {
                vals0[i] = new Val(vals[i].object, 0);
                double v = vals[i].value;
                if (!Double.isNaN(v) && !Double.isInfinite(v)) {
                    if (Double.isNaN(min) || v < min) {
                        min = v;
                    }
                    if (Double.isNaN(max) || v > max) {
                        max = v;
                    }
                }
            }
            this.min = min;
            this.max = max;
        }

        public Val nextVal(Val from, Val to) {
            if (from.value < to.value) {
                double d = from.value + step;
                if (d >= to.value) {
                    d = to.value;
                }
                return new Val(to.object, d);
            }
            if (from.value > to.value) {
                double d = from.value - step;
                if (d >= to.value) {
                    d = to.value;
                }
                return new Val(to.object, d);
            }
            return to;
        }

        public ModelIterator iter() {
            return new ModelIterator() {
                Val[] v0;
                long time = 0;
                SnapshotImpl last;

                @Override
                public Snapshot next() {
                    Val[] v = new Val[vals.length];
                    boolean[] frozen = new boolean[vals.length];
                    boolean someChange = false;
                    for (int i = 0; i < vals.length; i++) {
                        if (v0 == null) {
                            v[i] = vals0[i];
                            someChange = true;
                        } else {
                            v[i] = nextVal(v0[i], vals[i]);
                            frozen[i] = (v[i].equals(v0[i]) && v[i].equals(vals[i]) && v0[i].equals(vals[i]));
                            if (!someChange && !frozen[i]) {
                                someChange = true;
                            }
                        }
                    }
                    v0 = v;
                    if (!someChange) {
                        return null;
                    }
                    Map<Object, Val2> m = new LinkedHashMap<>();
                    for (int i = 0; i < v.length; i++) {
                        Val val = v[i];
                        m.put(val.object, new Val2(val.value, frozen[i]));
                    }
                    double min = last == null ? ValModel.this.min : last.min();
                    double max = last == null ? ValModel.this.max : last.max();
                    return (last = new SnapshotImpl(time++, m, min, max));
                }
            };
        }
    }

    public static class ColPalette3 implements ColPalette {
        Val[] vals;
        private List<Color> palette = new ArrayList<>();

        public ColPalette3(Val[] vals) {
            this.vals = vals;
            float window = 0.99f;
            int maxCol = vals.length;
            for (int i = 0; i < maxCol; i++) {
                float cc = (float) ((1 - window / 2) + window * (i / (maxCol * 1.0)));
                palette.add(Color.getHSBColor(cc, 1.0f, 0.8f));
            }
        }

        @Override
        public Color getColor(Object item) {
            for (int i = 0; i < vals.length; i++) {
                if (Objects.equals(vals[i].object, item)) {
                    return palette.get(i);
                }
            }
            return palette.get(0);
        }
    }

    public static class LinearDoubleInterpolator implements DoubleInterpolator {
        public static DoubleInterpolator INSTANCE = new LinearDoubleInterpolator();

        public double eval(double from, double to, float time) {
            return ((to - from) * time + from);
        }
    }

    public static class SinDoubleInterpolator implements DoubleInterpolator {
        public static DoubleInterpolator INSTANCE = new SinDoubleInterpolator();

        public double eval(double from, double to, float time) {
            time = (float) Math.sin(time * Math.PI / 2);
            return ((to - from) * time + from);
        }
    }

    public static class SqrtDoubleInterpolator implements DoubleInterpolator {
        public static DoubleInterpolator INSTANCE = new SqrtDoubleInterpolator();

        public double eval(double from, double to, float time) {
            time = (float) Math.sqrt(time);
            return ((to - from) * time + from);
        }
    }

    public static class GradientLabel extends JComponent {
        private static final int COLOR_COUNT = 30;
        private String text;
        private float[] fractions;
        private Color[] colors;

        public GradientLabel(String text) {
            this.text = text;
            fractions = new float[COLOR_COUNT];
            colors = new Color[COLOR_COUNT];
            for (int i = 0; i < colors.length; i++) {
                fractions[i] = ((float) i) / COLOR_COUNT;
                float hue = fractions[i];
                colors[i] = Color.getHSBColor(hue, 1f, 1f);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Dimension s = getSize();
            Graphics2D g2 = (Graphics2D) g;
            if (isOpaque()) {
                Color bc = getBackground();
                if (bc != null) {
                    g2.setColor(bc);
                    g2.fillRect(0, 0, s.width, s.height);
                }
            }
            String safeText = text == null ? "" : text;
            FontMetrics fm = g2.getFontMetrics(getFont());
            Rectangle2D bb = fm.getStringBounds(safeText, g2);
            int xx = (int) (s.width / 2 - bb.getWidth() / 2);
            int yy = (int) (s.height / 2 - bb.getHeight() / 2);
            g2.setColor(Color.BLUE);
//            g2.drawRect(xx, yy, (int) bb.getWidth(),(int) bb.getHeight());
            Paint myPaint = new LinearGradientPaint(xx, yy, s.width, s.height, fractions, colors);

            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setPaint(myPaint);
            g2.drawString(safeText, xx, yy + fm.getAscent());
        }
    }

    public static class ColPalette2 implements ColPalette {
        private List<Color> palette = new ArrayList<>();

        public ColPalette2() {
            float window = 0.99f;
            int maxCol = 128;
            for (int i = 0; i < maxCol; i++) {
                float cc = (float) ((1 - window / 2) + window * (i / (maxCol * 1.0)));
                palette.add(Color.getHSBColor(cc, 1.0f, 0.8f));
            }
        }

        public Color getColor(Object any) {
            int i = any == null ? 0 : any.hashCode();
            i = Math.abs(i);
            i = i % palette.size();
            return palette.get(i);
        }

    }
}
