package org.vpc.mbreakout.screens;

// Copyright 2003 Nokia Corporation. 
// 
// THIS SOURCE CODE IS PROVIDED 'AS IS', WITH NO WARRANTIES WHATSOEVER, 
// EXPRESS OR IMPLIED, INCLUDING ANY WARRANTY OF MERCHANTABILITY, FITNESS 
// FOR ANY PARTICULAR PURPOSE, OR ARISING FROM A COURSE OF DEALING, USAGE 
// OR TRADE PRACTICE, RELATING TO THE SOURCE CODE OR ANY WARRANTY OTHERWISE 
// ARISING OUT OF ANY PROPOSAL, SPECIFICATION, OR SAMPLE AND WITH NO 
// OBLIGATION OF NOKIA TO PROVIDE THE LICENSEE WITH ANY MAINTENANCE OR 
// SUPPORT. FURTHERMORE, NOKIA MAKES NO WARRANTY THAT EXERCISE OF THE 
// RIGHTS GRANTED HEREUNDER DOES NOT INFRINGE OR MAY NOT CAUSE INFRINGEMENT 
// OF ANY PATENT OR OTHER INTELLECTUAL PROPERTY RIGHTS OWNED OR CONTROLLED 
// BY THIRD PARTIES 
// 
// Furthermore, information provided in this source code is preliminary, 
// and may be changed substantially prior to final release. Nokia Corporation 
// retains the right to make changes to this source code at 
// any time, without notice. This source code is provided for informational 
// purposes only. 
// 
// Nokia and Nokia Connecting People are registered trademarks of Nokia
// Corporation.
// Java and all Java-based marks are trademarks or registered trademarks of
// Sun Microsystems, Inc.
// Other product and company names mentioned herein may be trademarks or
// trade names of their respective owners.
// 
// A non-exclusive, non-transferable, worldwide, limited license is hereby 
// granted to the Licensee to download, print, reproduce and modify the 
// source code. The licensee has the right to market, sell, distribute and 
// make available the source code in original or modified form only when 
// incorporated into the programs developed by the Licensee. No other 
// license, express or implied, by estoppel or otherwise, to any other 
// intellectual property rights is granted herein.

// unnamed package

import javax.microedition.lcdui.*;

import org.vpc.mbreakout.MBScreen;
import org.vpc.mbreakout.MBScreenIds;
import org.vpc.mbreakout.MBScreenSupport;
import org.vpc.mbreakout.MBreakoutMIDlet;
import org.vpc.mbreakout.util.MBUtils;


public class MBSplashScreen extends Canvas implements Runnable, MBScreen {
	private Image splashImage;

	private volatile boolean dismissed = false;

	private MBScreenSupport support;

	public MBSplashScreen(MBreakoutMIDlet midlet) {
		this.support = new MBScreenSupport(midlet, MBScreenIds.SPLASH);
		setFullScreenMode(true);
		splashImage = MBUtils.createImage("/org/vpc/mbreakout/images/splash.png");
		new Thread(this).start();
	}

	public void run() {
		synchronized (this) {
			try {
				wait(3000L); // 3 seconds
			} catch (InterruptedException e) {
				// can't happen in MIDP: no Thread.interrupt method
			}
		}
		dismiss();
	}

	public void paint(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		g.setColor(0x00FFFFFF); // white
		g.fillRect(0, 0, width, height);

		g.setColor(0x00FF0000); // red
		g.drawRect(1, 1, width - 2, height - 2); // red border one pixel from
		// edge

		if (splashImage != null) {
			g.drawImage(splashImage, width / 2, height / 2, Graphics.VCENTER
					| Graphics.HCENTER);
			splashImage = null;
			// midlet.splashScreenPainted();
		}
	}

	public void keyPressed(int keyCode) {
		dismiss();
	}

	private synchronized void dismiss() {
		if (!dismissed) {
			dismissed = true;
			getMidlet().showScreen(MBScreenIds.MENU);
		}
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
