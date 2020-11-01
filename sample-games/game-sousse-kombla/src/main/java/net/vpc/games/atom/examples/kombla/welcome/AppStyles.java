/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.games.atom.examples.kombla.welcome;

import net.vpc.gaming.atom.presentation.Alignment;
import net.vpc.gaming.atom.presentation.components.*;

import java.awt.*;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class AppStyles {
    private static final Font verdana56B = new Font("verdana", Font.BOLD, 56);
    private static final Font verdana32B = new Font("verdana", Font.BOLD, 24);
    private static final Font verdana24B = new Font("verdana", Font.BOLD, 24);
    private static final Font verdana24P = new Font("verdana", Font.PLAIN, 24);
    private static final Font verdana18B = new Font("verdana", Font.BOLD, 18);
    private static final Font verdana18P = new Font("verdana", Font.PLAIN, 18);

    public static final TextStyle listDefault = new TextStyle("listDefault")
            .setFont(verdana24P)
            .setForeColor(Color.WHITE)
            .setAlignment(Alignment.CENTER)
            .setBorderWidth(1)
            .setBorderArc(10)
            .setFillBackground(true);
    public static final TextStyle listSelected = new TextStyle("listSelected")
            .setBackgroundColor(Color.BLUE.darker())
            .setBackgroundColor2(Color.CYAN)
            .setBorderWidth(3);

    public static final TextStyle listFocused = new TextStyle("listFocused");
    public static final TextStyle listSelectedFocused = new TextStyle("listSelectedFocused")
            .setBlinkForegroundPeriod(10);

    //        label1Default.setInsetX(40);
//        label1Default.setInsetY(20);
    public static final TextStyle label1Default = new TextStyle("label1Default")
            .setFont(verdana18P)
            .setForeColor(Color.WHITE)
            .setAlignment(Alignment.LEFT);
    public static final TextStyle label1Selected = new TextStyle("label1Selected");
    public static final TextStyle label1Focused = new TextStyle("label1Focused");
    public static final TextStyle title1Default = new TextStyle("title1Default");
    public static final TextStyle title1Selected = new TextStyle("title1Selected");
    public static final TextStyle title1Focused = new TextStyle("title1Focused");
    public static final TextStyle textFieldDefault = new TextStyle("textFieldDefault");
    public static final TextStyle textFieldSelected = new TextStyle("textFieldSelected");
    public static final TextStyle textFieldFocused = new TextStyle("textFieldFocused");
    public static final TextStyle buttonDefault = new TextStyle("buttonDefault");
    public static final TextStyle buttonSelected = new TextStyle("buttonSelected");
    public static final TextStyle buttonFocused = new TextStyle("buttonFocused");
    public static final TextStyle waitingDefault = new TextStyle("waitingDefault")
            .setFont(verdana32B)
            .setForeColor(Color.WHITE)
            .setAlignment(Alignment.CENTER)
            .setBorderWidth(1)
            .setBorderArc(10)
            .setFillBackground(true)
            .setBlinkPeriod(10);

    static {
//        listDefault.setInsetX(40);
//        listDefault.setInsetY(20);




        label1Focused.setBlinkForegroundPeriod(10);
        label1Selected.setBackgroundColor(Color.BLUE.darker());
        label1Selected.setBackgroundColor2(Color.CYAN);

//        title1Default.setInsetX(40);
//        title1Default.setInsetY(20);
        title1Default.setFont(verdana18P);
        title1Default.setForeColor(Color.WHITE);
        title1Default.setAlignment(Alignment.CENTER);
        title1Default.setFont(verdana56B);
        title1Focused.setBlinkForegroundPeriod(10);
        title1Selected.setBackgroundColor(Color.BLUE.darker());
        title1Selected.setBackgroundColor2(Color.CYAN);


        textFieldDefault.setFont(verdana18P);
        textFieldDefault.setForeColor(new Color(20, 20, 80));
        textFieldDefault.setAlignment(Alignment.LEFT);
        textFieldDefault.setBorderColor(Color.WHITE);
        textFieldDefault.setBackgroundColor(new Color(255, 228, 176));
        textFieldDefault.setFillBackground(true);
//        textFieldSelected.setBackgroundColor(Color.BLUE.darker());
//        textFieldSelected.setBackgroundColor2(Color.CYAN);
        textFieldFocused.setBorderWidth(1);
        textFieldDefault.setBorderColor(new Color(20, 20, 80));
//        textFieldFocused.setBlinkBorderPeriod(10);
        textFieldFocused.setBlinkCursorPeriod(10);

//        buttonDefault.setInsetX(40);
//        buttonDefault.setInsetY(20);
        buttonDefault.setFont(verdana18P);
        buttonDefault.setForeColor(Color.WHITE);
        buttonDefault.setAlignment(Alignment.CENTER);
        buttonSelected.setBackgroundColor(Color.BLUE.darker());
        buttonSelected.setBackgroundColor2(Color.CYAN);
        buttonFocused.setFont(verdana18B);
        buttonFocused.setBlinkForegroundPeriod(10);

//        title1LabelDefault.setInsetX(40);
//        title1LabelDefault.setInsetY(20);
//        title1LabelDefault.setFont(verdana18P);
//        title1LabelDefault.setForeColor(Color.WHITE);
//        title1LabelDefault.setAlignment(SwingConstants.LEFT);
    }

    public static void prepareButton1(SButton c) {
        c.setFocusable(true);
        c.setTextStyle(SceneComponentState.DEFAULT, buttonDefault);
        c.setTextStyle(SceneComponentState.FOCUSED, buttonFocused);
        c.setTextStyle(SceneComponentState.SELECTED, buttonSelected);
    }

    public static void prepareList1(SList c) {
        c.setTextStyle(SceneComponentState.DEFAULT, listDefault);
        c.setTextStyle(SceneComponentState.FOCUSED, listFocused);
        c.setTextStyle(SceneComponentState.SELECTED, listSelected);
        c.setTextStyle(SceneComponentState.SELECTED.add(SceneComponentState.FOCUSED), listSelectedFocused);
    }

    public static void prepareLabel1(SLabel c) {
        c.setTextStyle(SceneComponentState.DEFAULT, label1Default);
        c.setTextStyle(SceneComponentState.FOCUSED, label1Focused);
        c.setTextStyle(SceneComponentState.SELECTED, label1Selected);
    }

    public static void prepareTitle1(SLabel c) {
        c.setTextStyle(SceneComponentState.DEFAULT, title1Default);
        c.setTextStyle(SceneComponentState.FOCUSED, title1Focused);
        c.setTextStyle(SceneComponentState.SELECTED, title1Selected);
    }

    public static void prepareTextField1(STextField c) {
        c.setTextStyle(SceneComponentState.DEFAULT, textFieldDefault);
        c.setTextStyle(SceneComponentState.FOCUSED, textFieldFocused);
        c.setTextStyle(SceneComponentState.SELECTED, textFieldSelected);
    }

    public static void prepareWaitingMessage(SLabel c) {
        c.setTextStyle(SceneComponentState.DEFAULT, waitingDefault);
    }
}
