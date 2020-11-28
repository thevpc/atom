/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.model;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class DefaultSceneModelWriter {

    private PrintWriter writer;

    public DefaultSceneModelWriter(File file) throws IOException {
        this(new PrintWriter(file));
    }

    public DefaultSceneModelWriter(PrintWriter writer) {
        this.writer = writer;
    }

    public void write(DefaultSceneEngineModel b) {
        writer.println("dim:");
        writer.println(b.getCellWidth() + "x" + b.getCellHeight());
        for (CellDef c : b.getBoardCellDefinitions()) {
            writer.println("cell:");
            writer.println(c.getType());
            for (TileDef[] tileInfos : c.getTiles()) {
                for (TileDef tileInfo : tileInfos) {
                    writer.print("(");
                    int[] z = tileInfo.getZ();
                    for (int aZ : z) {
                        writer.print(aZ);
                        writer.print(";");
                    }
                    writer.print(tileInfo.getWalls());
                    writer.print(")");
                }
                writer.println();
            }
        }

        writer.println("map:");
        for (int[] tileInfos : b.getBoardCellsMatrix()) {
            for (int tileInfo : tileInfos) {
                writer.print("(");
                writer.print(tileInfo);
                writer.print(")");
            }
            writer.println();
        }
    }

    public void close() {
        writer.close();
    }
}
