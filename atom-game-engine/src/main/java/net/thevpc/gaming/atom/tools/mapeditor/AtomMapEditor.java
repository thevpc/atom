/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.tools.mapeditor;

import net.thevpc.gaming.atom.model.DefaultSceneEngineModel;
import net.thevpc.gaming.atom.util.RuntimeIOException;
import net.thevpc.gaming.atom.util.AtomUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
//import net.thevpc.gaming.boardgame.model.background.BoardBackground;
//import net.thevpc.gaming.boardgame.model.background.BoardBackground.GameBackgroundZone;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public final class AtomMapEditor extends JPanel {

    TileCellView[] cells;
    ZonesGrid grid;
    int imageWidth;
    int imageHeight;
    private JList zonesList;
    private MapProject mapProject;
    private CellWalls obstacles;
    private CellElevation elevations;
    private JScrollPane gridScrolls;

    private AtomMapEditor() throws RuntimeIOException {
        setLayout(new BorderLayout());
//        imageWidth = 30;
//        imageHeight = 30;
        JToolBar toolBar = new JToolBar();

        JButton load = new JButton("Load");
        load.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser c = new JFileChooser();
                c.setFileSelectionMode(JFileChooser.FILES_ONLY);
                //c.addChoosableFileFilter(new FileNameExtensionFilter("map file", "mpr"));
                c.setSelectedFile(mapProject == null ? null : mapProject.getMapFile());
                int r = c.showOpenDialog(null);
                if (r == JFileChooser.APPROVE_OPTION) {
                    try {
                        File ff = c.getSelectedFile();
                        mapProject = new MapProject(ff);
                        mapProject.load();
                        prepareProject();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, ex);
                    }
                }
            }
        });
        toolBar.add(load);


        JButton save = new JButton("Save");
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    mapProject.save();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, ex);
                }
            }
        });
        toolBar.add(save);

        JButton saveAs = new JButton("Save");
        saveAs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser c = new JFileChooser();
                c.setFileSelectionMode(JFileChooser.FILES_ONLY);
                c.addChoosableFileFilter(new FileNameExtensionFilter("map file", "mpr"));
                c.setSelectedFile(mapProject.getMapFile());
                int r = c.showSaveDialog(null);
                if (r == JFileChooser.APPROVE_OPTION) {
                    try {
                        File ff = c.getSelectedFile();
                        mapProject.saveAs(ff);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, ex);
                    }
                }
            }
        });
        toolBar.add(saveAs);

        JButton newButton = new JButton("New");
        newButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                NewEditor ed = new NewEditor();
                ed.clear();
                int r = JOptionPane.showConfirmDialog(AtomMapEditor.this, ed, "New Project", JOptionPane.OK_CANCEL_OPTION);
                if (r == JOptionPane.OK_OPTION) {
                    try {
                        DefaultSceneEngineModel mapInfo = new DefaultSceneEngineModel(
                                ed.getMapColumns(), ed.getMapRows(),
                                ed.getTileColumnsPerCell(),
                                ed.getTileRowsPerCell(),
                                ed.getImageColumns() * ed.getImageRows());
                        MapProject p = new MapProject(new File(ed.getProjectFile()));
                        p.setSceneEngineModel(mapInfo);
                        p.setCellColumnsPerImage(ed.getImageColumns());
                        p.setCellRowsPerImage(ed.getImageRows());
                        AtomUtils.copyFile(new File(ed.getImageFile()), p.getImageFile());
                        mapProject = p;
                        prepareProject();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, ex);
                    }

                }
            }
        });
        toolBar.add(newButton);

        JButton editButton = new JButton("Edit");
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (mapProject == null) {
                    return;
                }
                NewEditor ed = new NewEditor();
                ed.setMapColumns(mapProject.getSceneEngineModel().getMapColumns());
                ed.setMapRows(mapProject.getSceneEngineModel().getMapRows());
                ed.setTileColumnsPerCell(mapProject.getSceneEngineModel().getCellWidth());
                ed.setTileRowsPerCell(mapProject.getSceneEngineModel().getCellHeight());
                ed.setImageColumns(mapProject.getCellColumnsPerImage());
                ed.setImageRows(mapProject.getCellRowsPerImage());
                ed.setProjectFile(mapProject.getBaseFile().getPath());
                ed.setImageFile(mapProject.getImageFile().getPath());
                ed.getProjectFileField().setEditable(false);
                ed.getProjectFileButton().setEnabled(false);
                int r = JOptionPane.showConfirmDialog(AtomMapEditor.this, ed, "Edit Project", JOptionPane.OK_CANCEL_OPTION);
                if (r == JOptionPane.OK_OPTION) {
                    try {
                        mapProject.getSceneEngineModel().setMapColumns(ed.getMapColumns());
                        mapProject.getSceneEngineModel().setMapRows(ed.getMapRows());
                        mapProject.getSceneEngineModel().setCellWidth(ed.getTileColumnsPerCell());
                        mapProject.getSceneEngineModel().setCellHeight(ed.getTileRowsPerCell());
                        mapProject.setCellColumnsPerImage(ed.getImageColumns());
                        mapProject.setCellRowsPerImage(ed.getImageRows());
                        AtomUtils.copyFile(new File(ed.getImageFile()), mapProject.getImageFile());
                        prepareProject();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, ex);
                    }

                }
            }
        });
        toolBar.add(editButton);


        zonesList = new JList();
        zonesList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                TileCellView z = (TileCellView) value;
                Component r = null;
                if (z != null) {
                    r = super.getListCellRendererComponent(list, z.getIcon(), index, isSelected, cellHasFocus);
                } else {
                    r = super.getListCellRendererComponent(list, z, index, isSelected, cellHasFocus);
                }
                setText(String.valueOf(index));
                return r;
            }
        });
        zonesList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                TileCellView zz = (TileCellView) zonesList.getSelectedValue();
                if (zz != null) {
                    obstacles.setMap(zz.cell);
                    elevations.setMap(zz.cell);
                }
            }
        });
        gridScrolls = new JScrollPane();
        gridScrolls.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        gridScrolls.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        gridScrolls.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent e) {
                gridScrolls.repaint();
            }
        });
        gridScrolls.getHorizontalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent e) {
                gridScrolls.repaint();
            }
        });
        gridScrolls.setPreferredSize(new Dimension(600, 400));


        JTabbedPane p = new JTabbedPane();
        p.add("Map", gridScrolls);


        add(p, BorderLayout.CENTER);
        JPanel east = new JPanel(new GridLayout(2, 1));
        east.add(new JScrollPane(zonesList));

        JTabbedPane p2 = new JTabbedPane();
        obstacles = new CellWalls(this, 50, 50);
        p2.add("Obstacles", obstacles);
        elevations = new CellElevation(this, 50, 50);
        p2.add("Elevations", elevations);
        east.add(p2);

        add(east, BorderLayout.EAST);
        add(toolBar, BorderLayout.NORTH);
    }

    private AtomMapEditor(File filePattern) throws IOException {
        this();
        mapProject = new MapProject(filePattern);
        mapProject.load();
        prepareProject();
    }

    public static void main(String[] args) {

        new AtomMapEditor().showFrame();
//            JFileChooser c = new JFileChooser();
//            c.setFileSelectionMode(JFileChooser.FILES_ONLY);
//            c.addChoosableFileFilter(new FileNameExtensionFilter("map file", "map"));
//            int r = c.showOpenDialog(null);
//            if (r == JFileChooser.APPROVE_OPTION) {
//                String path = c.getSelectedFile().getPath();
//                new MapEditor(path.substring(0, path.length() - (".map".length()))).showWindow();
//            }
    }

    public void prepareProject() throws IOException {
        DefaultSceneEngineModel mapInfo = mapProject.getSceneEngineModel();
        BufferedImage[] images = mapProject.loadCellImages();
        cells = new TileCellView[mapInfo.getBoardCellDefinitions().length];
        for (int i = 0; i < cells.length; i++) {
            TileCellView v = new TileCellView(mapInfo.getBoardCellDefinitions()[i], images[i]);
            cells[i] = v;
        }
        BufferedImage image0 = cells.length == 0 ? null : cells[0].getImage();
        if (image0 != null) {
            imageWidth = image0.getWidth();
            imageHeight = image0.getHeight();
        } else {
            imageWidth = 0;
            imageHeight = 0;
        }
        int[][] mapInts = mapInfo.getBoardCellsMatrix();
        TileCellView[][] map = new TileCellView[mapInts.length][mapInts.length == 0 ? 0 : mapInts[0].length];
        for (int i = 0; i < map.length; i++) {
            TileCellView[] tileCellViews = map[i];
            for (int j = 0; j < tileCellViews.length; j++) {
                tileCellViews[j] = cells[mapInts[i][j]];
            }
        }
        grid = new ZonesGrid(this, map, mapInts, imageWidth, imageHeight);


        obstacles.setMap(cells.length == 0 ? null : cells[0].cell);
        elevations.setMap(cells.length == 0 ? null : cells[0].cell);
        final DefaultListModel lm = new DefaultListModel();
        for (TileCellView z : cells) {
            lm.addElement(z);
        }
        zonesList.setModel(lm);
        gridScrolls.setViewportView(grid);
        if (cells.length > 0) {
            zonesList.setSelectedIndex(0);
        }
    }

    public void showFrame() {
        JFrame f = new JFrame(getClass().getName());
        f.add(this);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setVisible(true);
    }

    public int getSelectedMapZoneIndex() {
        return zonesList.getSelectedIndex();
    }

    public TileCellView getSelectedMapZone() {
        return (TileCellView) zonesList.getSelectedValue();
    }
}
