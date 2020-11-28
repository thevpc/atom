/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.extension;

import net.thevpc.gaming.atom.presentation.Scene;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public interface SceneExtension {
    void install(Scene scene);

    void uninstall(Scene scene);
}
