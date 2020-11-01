/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.presentation;

/**
 * Adapter for SceneEventListener
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class DefaultSceneController implements SceneController {
    private int acceptedEvents = -1;//all events

    @Override
    public boolean acceptEvent(int event) {
        return (acceptedEvents & event) != 0;
    }

    public void addAcceptedEvent(int event) {
        acceptedEvents |= event;
    }

    public void removeAcceptedEvent(int event) {
        acceptedEvents &= ~event;
    }

    public void resetAcceptedEvents() {
        acceptedEvents = 0;
    }

    public void addAllAcceptedEvents() {
        acceptedEvents = -1;
    }

    /**
     * {@inheritDoc} Does nothing
     */
    public void keyTyped(SceneKeyEvent e) {
    }


    /**
     * {@inheritDoc} Does nothing
     */
    public void keyPressed(SceneKeyEvent e) {
    }

    /**
     * {@inheritDoc} Does nothing
     */
    public void keyReleased(SceneKeyEvent e) {
    }

    /**
     * {@inheritDoc} Does nothing
     */
    public void mouseClicked(SceneMouseEvent e) {
    }

    /**
     * {@inheritDoc} Does nothing
     */
    public void mousePressed(SceneMouseEvent e) {
    }

    /**
     * {@inheritDoc} Does nothing
     */
    @Override
    public void mouseReleased(SceneMouseEvent e) {
    }

    /**
     * {@inheritDoc} Does nothing
     */
    @Override
    public void mouseEntered(SceneMouseEvent e) {
    }

    /**
     * {@inheritDoc} Does nothing
     */
    @Override
    public void mouseExited(SceneMouseEvent e) {
    }

    /**
     * {@inheritDoc} Does nothing
     */
    @Override
    public void mouseDragged(SceneMouseEvent e) {
    }

    /**
     * {@inheritDoc} Does nothing
     */
    @Override
    public void mouseMoved(SceneMouseEvent e) {
    }

}
