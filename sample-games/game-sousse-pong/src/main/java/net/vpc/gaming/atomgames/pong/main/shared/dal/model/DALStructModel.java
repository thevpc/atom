/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atomgames.pong.main.shared.dal.model;

import java.io.Serializable;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class DALStructModel implements Serializable {

    public long frame;
    public DALStructSprite paddle1;
    public DALStructSprite paddle2;
    public DALStructSprite ball;
}
