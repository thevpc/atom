package org.vpc.mbreakout;

import javax.microedition.lcdui.Displayable;

public interface MBScreen {
	MBreakoutMIDlet getMidlet();
	int getId();
	Displayable getDisplayable();
}
