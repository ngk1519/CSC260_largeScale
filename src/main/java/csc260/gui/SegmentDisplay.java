package csc260.gui;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.lang.*;

import csc260.map_model.*;
import csc260.gui.*;

/**
* The window for displaying all the segments from selected csv files
* From: http://www.java2s.com/Code/Java/Swing-JFC/AnexampleofJListwithaDefaultListModel.html
* Modified by Kevin Ng
*/
public class SegmentDisplay extends JPanel implements Listener{

  private SegmentBank theOverallBank;
  private Route theOverallRoute;
  private RouteState overallState;
  private JList bankList;
  private DefaultListModel model;
  private int counter = 15;
  private HashMap<String, Segment> scrollBarStorage;

  private static int WIDTH = 200;
  private static int GAP = 20;
  private static int LINE_WIDTH = 5;

  private static final int INITIAL_X = 50;
  private static final int INITIAL_Y = 100;
  private static final int INITIAL_X2 = 200;
  private static final int INITIAL_Y2 = 250;

  private String start = "";
  private String destination = "";
  private String intermediatePath = "";

  public SegmentDisplay(SegmentBank overallBank, Route overallRoute, RouteState overallState, DefaultListModel model, JList bankList, HashMap<String, Segment> scrollBarStorage){
    this.theOverallBank = overallBank;
    this.theOverallRoute = overallRoute;
    this.overallState = overallState;
    this.scrollBarStorage = scrollBarStorage;

    setLayout(new BorderLayout());
    this.model = model;
    this.bankList = bankList;

    JScrollPane pane = new JScrollPane(bankList);
    JButton importSegment = new JButton("Import Segment");
    JButton addSegment = new JButton("Add Segment");
    JButton LabelSegment = new JButton("Label Segment");
    JButton removeSegment = new JButton("Remove Segment");


    add(pane, BorderLayout.NORTH);
    add(importSegment, BorderLayout.LINE_START);
    add(LabelSegment, BorderLayout.CENTER);
    add(addSegment, BorderLayout.LINE_END);
    add(removeSegment, BorderLayout.SOUTH);

    SegDisButtonListener buttonsListener = new SegDisButtonListener(this,this.theOverallBank,
    this.theOverallRoute, this.overallState, this.scrollBarStorage, this.bankList, this.model);

    // Program the functionality of the importSegment button
    // For loading csv file from one's device
    // importSegment.addActionListener(new SegDisButtonListener(this,this.theOverallBank,
    // this.theOverallRoute, this.overallState, this.scrollBarStorage, this.bankList, this.model));
    //
    // // For adding the selected segment to the route
    // addSegment.addActionListener(new SegDisButtonListener(this,this.theOverallBank,
    // this.theOverallRoute, this.overallState, this.scrollBarStorage, this.bankList, this.model));
    //
    // //For editing the labels of the selected segments
    // LabelSegment.addActionListener(new SegDisButtonListener(this,this.theOverallBank,
    // this.theOverallRoute, this.overallState, this.scrollBarStorage, this.bankList, this.model));
    //
    //  removeSegment.addActionListener(new SegDisButtonListener(this,this.theOverallBank,
    //  this.theOverallRoute, this.overallState, this.scrollBarStorage, this.bankList, this.model));

    importSegment.addActionListener(buttonsListener);
    addSegment.addActionListener(buttonsListener);
    LabelSegment.addActionListener(buttonsListener);
    removeSegment.addActionListener(buttonsListener);
  }

  public void update(){
    repaint();
  }
}
