package org.vpc.mbreakout.screens;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

import org.vpc.mbreakout.MBScreen;
import org.vpc.mbreakout.MBScreenIds;
import org.vpc.mbreakout.MBScreenSupport;
import org.vpc.mbreakout.MBreakoutMIDlet;

public class MBMenuScreen extends List implements CommandListener,MBScreen{
	private MBScreenSupport support;
	private Command exitCommand;
	public MBMenuScreen(MBreakoutMIDlet midlet) {
		super("Mini-Breakout", List.IMPLICIT);
		support=new MBScreenSupport(midlet,MBScreenIds.MENU);
		append(midlet.getMessage("newgame"), null);
		append(midlet.getMessage("instructions"), null);

		exitCommand = new Command("Exit", Command.EXIT, 1);
        addCommand(exitCommand);
        
        setCommandListener(this);
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

    public void gameActiveChanged(boolean newValue)
    {
        if (newValue)
        {
            insert(0, getMidlet().getMessage("continue"), null);
        }
        else
        {
            delete(0);
        }
    }

    public void commandAction(Command c, Displayable arg1) {
        if (c == List.SELECT_COMMAND)
        {
            int index = getSelectedIndex();
            if (index != -1)  // should never be -1
            {
                if (!getMidlet().isGameActive())
                {
                    index++;
                }
                switch (index)
                {
                case 0:   // Continue
                    //support.getMidlet().menuListContinue();
                    break;
                case 1:   // New game
                    getMidlet().showScreen(MBScreenIds.GAME);
                    MBGameScreen s=(MBGameScreen) getMidlet().getScreen(MBScreenIds.GAME);
                    s.start();
                    break;
                case 2:   // Instructions
                    getMidlet().showScreen(MBScreenIds.INSTRUCTIONS);
                    break;
                default:
                    // can't happen
                    break;
                }
            }
        }
        else if (c == exitCommand)
        {
            getMidlet().quit();
        }
		
	}
	
}
