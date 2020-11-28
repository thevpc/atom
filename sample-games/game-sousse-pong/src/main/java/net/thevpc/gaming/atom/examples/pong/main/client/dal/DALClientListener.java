/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.pong.main.client.dal;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public interface DALClientListener {

    public void connected();

    public void modelChanged(ModelUpdater modelUpdater);
}
