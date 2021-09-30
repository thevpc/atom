package net.thevpc.gaming.atom.test;

import net.thevpc.gaming.atom.engine.collisiontasks.*;
import net.thevpc.gaming.atom.model.ModelBox;
import net.thevpc.gaming.atom.model.ModelDimension;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class TestDiscreteSceneCollisionManagerHelper {
    @Test
    public void test1(){
        DiscreteSceneCollisionManagerHelper t=new DiscreteSceneCollisionManagerHelper(10,1E-3,new ModelDimension(10,10));
        ArrayList<Obstacle> motionObstacles = new ArrayList<>();
        ArrayList<Obstacle> motionlessObstacles = new ArrayList<>();
        motionObstacles.add(new Obstacle(
                ObstacleType.SPRITE,
                "A",1,new ModelBox(0,10,1,1),new ModelBox(10,10,1,1),true,false,false
        ));
        motionObstacles.add(new Obstacle(
                ObstacleType.SPRITE,
                "B",2,new ModelBox(10,0,1,1),new ModelBox(10,10,1,1),true,false,false
        ));
        List<CollisionInfo> c = t.evalCollisions(null, motionObstacles, motionlessObstacles);
        System.out.println(c);
    }
}
