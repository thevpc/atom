/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.edu.eniso.soussecraft.main.dal;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import net.vpc.gaming.atom.engine.SpriteTask;
import net.vpc.gaming.atom.engine.tasks.AttackSpriteTask;
import net.vpc.gaming.atom.engine.tasks.FindPathToPointSpriteTask;
import net.vpc.gaming.atom.extension.strategy.resources.ResourceRepository;
import net.vpc.gaming.atom.extension.strategy.tasks.GatherResourceSpriteTask;
import net.vpc.gaming.atom.model.ModelPoint;
import net.vpc.gaming.atom.model.Sprite;
import tn.edu.eniso.soussecraft.main.business.tasks.BuildNewAtomiumTask;
import tn.edu.eniso.soussecraft.main.model.etc.GatherLocationIndicator;
import tn.edu.eniso.soussecraft.main.model.etc.GotoLocationIndicator;
import tn.edu.eniso.soussecraft.main.model.resources.Minerals;
import tn.edu.eniso.soussecraft.main.model.resources.Woods;
import tn.edu.eniso.soussecraft.main.model.structures.Atomium;
import tn.edu.eniso.soussecraft.main.model.structures.CommandCenter;
import tn.edu.eniso.soussecraft.main.model.units.Worker;

/**
 *
 * @author Taha Ben Salah
 */
public class DalModel implements Serializable {

    private Set<DALSprite> added = new HashSet<DALSprite>();
    private Set<DALSprite> updated = new HashSet<DALSprite>();
    private Set<Integer> removed = new HashSet<Integer>();
    private int playersCount;

    public DALSprite convert(Sprite s) {
        return createSprite(s);
    }

    public int getPlayersCount() {
        return playersCount;
    }

    public void setPlayersCount(int playersCount) {
        this.playersCount = playersCount;
    }
    

    public void add(Sprite sprite) {
        DALSprite s = convert(sprite);
        added.remove(s);
        updated.remove(s);
        removed.remove(s.getId());
        added.add(s);
    }

    public void remove(Sprite sprite) {
        DALSprite s = convert(sprite);
        added.remove(s);
        updated.remove(s);
        removed.add(s.getId());
    }

    public void update(Sprite sprite) {
        DALSprite s = convert(sprite);
        added.remove(s);
        updated.remove(s);
        removed.remove(s.getId());
        updated.add(s);
    }

    public Set<DALSprite> getAdded() {
        return added;
    }

    public Set<Integer> getRemoved() {
        return removed;
    }

    public Set<DALSprite> getUpdated() {
        return updated;
    }

    public static DALSprite createSprite(Sprite t) {
        DALSprite s=null;
        if(t instanceof Worker){
            s=new DALWorker(t);
        }else if(t instanceof Atomium){
            s=new DALAtomium(t);
        }else if(t instanceof CommandCenter){
            s=new DALCommandCenter(t);
        }else if(t instanceof Minerals){
            s=new DALMinerals(t);
        }else if(t instanceof Woods){
            s=new DALWoods(t);
        }else if(t instanceof GotoLocationIndicator){
            s=new DALGotoLocationIndicator(t);
        }else if(t instanceof GatherLocationIndicator){
            s=new DALGatherLocationIndicator(t);
        }else{
            throw new IllegalArgumentException("Unsupported DALSprite for "+t);
        }
        return s;
    }
    
    public abstract static class DALSprite<T extends Sprite> implements Serializable {

        protected int id;
        protected int player;
        protected SpriteTask task;
        protected ModelPoint location;

        public int getId() {
            return id;
        }

        public DALSprite(Sprite sprite) {
            updateFrom((T)sprite);
        }
        

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final DALSprite other = (DALSprite) obj;
            if (this.id != other.id) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 29 * hash + this.id;
            return hash;
        }

        public abstract T create();

        public void update(T sprite) {
            sprite.setId(getId());
            sprite.setTask(task);
            sprite.setLocation(location);
            sprite.setPlayerId(player);
        }

        public void updateFrom(T sprite) {
            task = cloneTask(sprite.getTask());
            location=sprite.getLocation();
            id=sprite.getId();
            player=sprite.getPlayerId();
        }

        private SpriteTask cloneTask(SpriteTask task) {
            if (task == null) {
                return null;
            }
            if (task instanceof CommandCenter.CommandCenterTask) {
                return new CommandCenter.CommandCenterTask();
            }
            if (task instanceof BuildNewAtomiumTask) {
                return new BuildNewAtomiumTask(15);
            }
            if (task instanceof AttackSpriteTask) {
                AttackSpriteTask r = (AttackSpriteTask) task;
                return new AttackSpriteTask(r.getVictimId(), r.getSpeed());
            }
            if (task instanceof FindPathToPointSpriteTask) {
                FindPathToPointSpriteTask r = (FindPathToPointSpriteTask) task;
                return new FindPathToPointSpriteTask(r.getTarget(), r.getMinDistance(), r.isCenter());
            }
            if (task instanceof GatherResourceSpriteTask) {
                GatherResourceSpriteTask r = (GatherResourceSpriteTask) task;
                return new GatherResourceSpriteTask(r.getCommandCenterId(), r.getResourceId());
            }
//            if (task instanceof SoftKillTask) {
//                SoftKillTask r = (SoftKillTask) task;
//                return new SoftKillTask();
//            }
            throw new IllegalArgumentException("Not Supported Clone for Task "+task);
        }
    }

    public static class DALWorker extends DALSprite<Worker> {

        private ResourceRepository resources;

        public DALWorker(Sprite sprite) {
            super(sprite);
        }

        
        @Override
        public Worker create() {
            Worker w = new Worker();
            update(w);
            return w;
        }

        @Override
        public void update(Worker w) {
            super.update(w);
            w.setResources(resources);
        }

        @Override
        public void updateFrom(Worker sprite) {
            super.updateFrom(sprite);
            resources = sprite.getResources();
        }
    }

    public static class DALAtomium extends DALSprite<Atomium> {

        public DALAtomium(Sprite sprite) {
            super(sprite);
        }

        @Override
        public Atomium create() {
            Atomium atomium = new Atomium();
            update(atomium);
            return atomium;
        }
        
    }
    public static class DALCommandCenter extends DALSprite<CommandCenter> {

        public DALCommandCenter(Sprite sprite) {
            super(sprite);
        }

        
        @Override
        public CommandCenter create() {
            CommandCenter s = new CommandCenter();
            update(s);
            return s;
        }
        
    }
    public static class DALMinerals extends DALSprite<Minerals> {

        int quantity;

        public DALMinerals(Sprite sprite) {
            super(sprite);
        }
        
        @Override
        public Minerals create() {
            Minerals s = new Minerals(quantity,location.getIntX(),location.getIntY());
            update(s);
            return s;
        }

        @Override
        public void update(Minerals sprite) {
            super.update(sprite);
            sprite.getResources().setResource(Minerals.class, quantity);
        }

        @Override
        public void updateFrom(Minerals sprite) {
            super.updateFrom(sprite);
            quantity=sprite.getResources().getResource(Woods.class);
        }
        
    }
    public static class DALWoods extends DALSprite<Woods> {
        int quantity;

        public DALWoods(Sprite sprite) {
            super(sprite);
        }
        
        @Override
        public Woods create() {
            Woods s = new Woods(quantity,location.getIntX(),location.getIntY());
            update(s);
            return s;
        }

        @Override
        public void update(Woods sprite) {
            super.update(sprite);
            sprite.getResources().setResource(Woods.class, quantity);
        }

        @Override
        public void updateFrom(Woods sprite) {
            super.updateFrom(sprite);
            quantity=sprite.getResources().getResource(Woods.class);
        }
        
    }
    public static class DALGotoLocationIndicator extends DALSprite<GotoLocationIndicator> {
        public DALGotoLocationIndicator(Sprite sprite) {
            super(sprite);
        }
        
        @Override
        public GotoLocationIndicator create() {
            GotoLocationIndicator s = new GotoLocationIndicator(player, location);
            update(s);
            return s;
        }

        @Override
        public void update(GotoLocationIndicator sprite) {
            super.update(sprite);
        }

        @Override
        public void updateFrom(GotoLocationIndicator sprite) {
            super.updateFrom(sprite);
        }
        
    }
    
    public static class DALGatherLocationIndicator extends DALSprite<GatherLocationIndicator> {
        public DALGatherLocationIndicator(Sprite sprite) {
            super(sprite);
        }
        
        @Override
        public GatherLocationIndicator create() {
            GatherLocationIndicator s = new GatherLocationIndicator(player, location);
            update(s);
            return s;
        }

        @Override
        public void update(GatherLocationIndicator sprite) {
            super.update(sprite);
        }

        @Override
        public void updateFrom(GatherLocationIndicator sprite) {
            super.updateFrom(sprite);
        }
        
    }
}
