package org.vpc.mbreakout.screens;

import javax.microedition.lcdui.*;

import org.vpc.mbreakout.MBScreen;
import org.vpc.mbreakout.MBScreenIds;
import org.vpc.mbreakout.MBScreenSupport;
import org.vpc.mbreakout.MBreakoutMIDlet;

public class MBInstructionsScreen extends Form implements CommandListener,MBScreen {
	private final Command backCommand;
	private final MBScreenSupport support;

	public MBInstructionsScreen(MBreakoutMIDlet midlet) {
		super(midlet.getMessage("instructions"));
		support=new MBScreenSupport(midlet,MBScreenIds.INSTRUCTIONS);
		append(new StringItem(null, midlet.getMessage("instructions.text")));

		backCommand = new Command(midlet.getMessage("back"), Command.BACK, 1);
		addCommand(backCommand);
		setCommandListener(this);
	}

	public void commandAction(Command c, Displayable d) {
		getMidlet().showScreen(MBScreenIds.MENU);
	}
	
	public Displayable getDisplayable() {
		return this;
	}
	public int getId() {
		return support.getId();
	}
	public MBreakoutMIDlet getMidlet() {
		return support.getMidlet();
	}
}
