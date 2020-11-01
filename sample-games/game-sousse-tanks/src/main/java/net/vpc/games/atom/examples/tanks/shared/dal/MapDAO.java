/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.games.atom.examples.tanks.shared.dal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import net.vpc.gaming.atom.model.Tile;

/**
 *
 * @author vpc
 */
public class MapDAO {
    public void save(int[][] maze,String name){
        try {
            File file=toFile(name);
            file.getParentFile().mkdirs();
            ObjectOutputStream o=new ObjectOutputStream(new FileOutputStream(file));
            o.writeObject(maze);
            o.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public int[][] load(String name){
        try {
            File file=toFile(name);
            ObjectInputStream o=new ObjectInputStream(new FileInputStream(file));
            int[][] m=(int[][])o.readObject();
            o.close();
            return m;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    private File toFile(String name){
       return new File(System.getProperty("user.home")+"/games/tanks/"+name+".map"); 
    }
}
