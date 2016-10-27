/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.extension;

import net.vpc.gaming.atom.presentation.Scene;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public interface SceneExtension {
    void install(Scene scene);

    void uninstall(Scene scene);
}
