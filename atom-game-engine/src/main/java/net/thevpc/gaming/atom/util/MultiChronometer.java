/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.util;

/**
 *
 * @author Taha BEN SALAH <taha.bensalah@gmail.com>
 */
public final class MultiChronometer {

    private final long startTime;
    private final String[] labels;
    private final long[] times;
    private int index;

    public MultiChronometer(int max) {
        times = new long[max];
        labels = new String[max];
        startTime = System.currentTimeMillis();
    }

    public void snapshot(String label) {
        times[index] = System.currentTimeMillis();
        labels[index] = label;
        index++;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Chrono(").append(getPeriodString(times[index-1]-startTime)).append("){");
        for (int i = 0; i < index; i++) {
            s.append("\n");
            if (i == 0) {
                s.append(labels[i]).append(":").append(getPeriodString(times[i] - startTime));
            } else {
                s.append(labels[i]).append(":").append(getPeriodString(times[i] - times[i - 1]));
            }
        }
        s.append("\n}");
        return s.toString();
    }

    public String getPeriodString(long value) {
        return String.valueOf(value);
    }
    
    public long getTime(){
        return times[index-1]-startTime;
    }
    
    public long getTime(int index){
        if(index==0){
            return times[0]-startTime;
        }else{
            return times[index]-times[index-1];
        }
    }
    
    public int size(){
        return index;
    }
}
