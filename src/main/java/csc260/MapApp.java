package csc260;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.lang.*;

import csc260.map_model.*;
import csc260.gui.*;

public class MapApp{

  private static int MAX_STEPS = 5;

  public static void main (String[] args){
    MapApp mainProgram = new MapApp();
    mainProgram.start();
  }

  public void start(){
    SegmentBank overallBank = new SegmentBank();
    Route overallRoute = new Route(overallBank);
    RouteState overallState = new RouteState(MAX_STEPS);
    DefaultListModel model = new DefaultListModel();
    JList bankList = new JList(model);
    HashMap<String, Segment> scrollBarStorage = new HashMap<String, Segment>();

    // if (load) {
    //   overallRoute.load(File txtFileSaved);
    //   overallBank.setSegmentBankContents(overallRoute.getSegmentBankContents());
    // }

    CanvasDisplay pathDisplay = new CanvasDisplay(overallRoute, overallState,
    overallBank, model, bankList, scrollBarStorage);
    JFrame mainFrame = new JFrame("The overall Route");
    overallRoute.addListener(pathDisplay);

    mainFrame.setContentPane(pathDisplay);
    mainFrame.setSize(700, 700);
    mainFrame.setVisible(true);

    SegmentDisplay segDis = new SegmentDisplay(overallBank, overallRoute,
    overallState, model, bankList, scrollBarStorage);
    JFrame frame = new JFrame("The segment Bank");
    overallBank.addListener(segDis);
    overallRoute.addListener(segDis);

    frame.setContentPane(segDis);
    frame.setSize(400, 300);
    frame.setVisible(true);

  }
}
