/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.engine.pathfinder;

import net.vpc.gaming.atom.model.ModelPoint;

import java.util.*;

/**
 * A* (A star) Path Finder Algorithm  implementation
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class AStarTilePathFinder implements TilePathFinder {

    /**
     * {@inheritDoc}
     */
    public ModelPoint[] findPath(ModelPoint start, ModelPoint end, TransitionStrategy transitionStrategy) {
        Map<ModelPoint, DecoratedTile> decoMap = new HashMap<ModelPoint, DecoratedTile>();
        SimpleHeuristic heuristic = new SimpleHeuristic();
        DistanceComparator comparator = new DistanceComparator();
        DecoratedTile startDeco = new DecoratedTile(start);
        startDeco.cost = 0;
        startDeco.heuristics = heuristic.getMoveHeuristic(start, end);
        startDeco.score = startDeco.heuristics + startDeco.cost;

        DecoratedTile best = startDeco;

        decoMap.put(start, startDeco);

        PriorityQueue<DecoratedTile> openSetDeco = new PriorityQueue<DecoratedTile>(100, comparator);
        Set<ModelPoint> openSet = new HashSet<ModelPoint>();
        Set<ModelPoint> closedSet = new HashSet<ModelPoint>();

        openSetDeco.add(startDeco);
        openSet.add(start);
        DecoratedTile xx = null;
        while ((xx = openSetDeco.poll()) != null) {
            ModelPoint x = xx.tile;
//            System.out.println("\tPath Finder : Check Tile " + xx);
            if (x.equals(end)) {
//                System.out.println("\tPath Finder : Found " + x);
                LinkedList<ModelPoint> path = new LinkedList<ModelPoint>();
                while (xx != null) {
                    path.addFirst(xx.tile);
                    xx = xx.from;
                }

                return path.toArray(new ModelPoint[path.size()]);
            } else if (xx.heuristics < best.heuristics) {
                best = xx;
            }
            openSet.remove(x);
            closedSet.add(x);
            for (Transition move : transitionStrategy.getTransitions(x)) {
                ModelPoint y = move.getTo();
                if (!closedSet.contains(y)) {
                    boolean tentative_is_better = true;
                    double tentative_cost = xx.cost + move.getCost();
                    DecoratedTile yy = decoMap.get(y);
                    if (yy == null) {
                        yy = new DecoratedTile(y);
                        decoMap.put(y, yy);
                    }
                    boolean toAdd = false;
                    if (!openSet.contains(y)) {
                        toAdd = true;
                        tentative_is_better = true;
                    } else if (tentative_cost < yy.cost) {
                        tentative_is_better = true;
                    }
                    if (tentative_is_better) {
                        if (toAdd) {
                            yy.from = xx;
                            yy.cost = tentative_cost;
                            yy.heuristics = heuristic.getMoveHeuristic(y, end);
                            yy.score = yy.cost + yy.heuristics;
                            openSetDeco.add(yy);
                            openSet.add(y);
//                                    System.out.println("\t\tPath Finder : Add Neighbour " + yy);
                        } else {
                            //remove update than add again to enable update position
                            openSetDeco.remove(yy);
                            yy.from = xx;
                            yy.cost = tentative_cost;
                            yy.heuristics = heuristic.getMoveHeuristic(y, end);
                            yy.score = yy.cost + yy.heuristics;
                            openSetDeco.add(yy);
//                                    System.out.println("\t\tPath Finder : Updated Neighbour " + yy);
                        }
                    }
                }
            }
        }
//        System.out.println("Path Finder End : Not found");
        LinkedList<ModelPoint> path = new LinkedList<ModelPoint>();
        xx = best;
        while (xx != null) {
            path.addFirst(xx.tile);
            xx = xx.from;
        }
        return path.toArray(new ModelPoint[path.size()]);
    }

    //    private int getTilesCount() {
//        int x = map.length;
//        if (x > 0) {
//            return x * map[0].length;
//        }
//        return x;
//    }
    private static class DistanceComparator implements Comparator<DecoratedTile> {

        public DistanceComparator() {
        }

        public int compare(DecoratedTile o1, DecoratedTile o2) {
            double d1 = o1.score;
            double d2 = o2.score;
            return d1 > d2 ? 1 : (d1 < d2) ? -1 : 0;
        }
    }

    private static class DecoratedTile {

        ModelPoint tile;
        DecoratedTile from;
        double cost = Double.MAX_VALUE;
        double heuristics = Double.MAX_VALUE;
        double score = Double.MAX_VALUE;

        public DecoratedTile(ModelPoint tile) {
            this.tile = tile;
        }

        @Override
        public String toString() {
            return "DecoratedTile{" + "tile=" + tile + ", cost=" + cost + ", heuristics=" + heuristics + ", score=" + score + ", from=" + from + '}';
        }
    }

    public static class SimpleHeuristic implements Heuristic {

        public double getMoveHeuristic(ModelPoint start, ModelPoint end) {
            return start.distance(end);
        }
    }

    public static interface Heuristic {

        double getMoveHeuristic(ModelPoint start, ModelPoint end);
    }
}
