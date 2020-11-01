package org.vpc.mbreakout;

public class MBScreenSupport {
	private MBreakoutMIDlet midlet;
	private int id;
	public MBScreenSupport(MBreakoutMIDlet midlet, int id) {
		super();
		this.midlet = midlet;
		this.id = id;
	}
	public MBreakoutMIDlet getMidlet() {
		return midlet;
	}
	public int getId() {
		return id;
	}
}
