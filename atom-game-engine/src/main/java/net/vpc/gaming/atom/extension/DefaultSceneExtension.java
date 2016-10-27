/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.extension;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public abstract class DefaultSceneExtension implements SceneExtension {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }
}
