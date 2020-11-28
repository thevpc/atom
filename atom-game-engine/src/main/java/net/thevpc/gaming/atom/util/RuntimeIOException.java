package net.thevpc.gaming.atom.util;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: vpc
 * Date: 8/18/13
 * Time: 3:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class RuntimeIOException extends RuntimeException {
    public RuntimeIOException(String message) {
        this(new IOException(message));
    }

    public RuntimeIOException(IOException cause) {
        super(cause);
    }
}
