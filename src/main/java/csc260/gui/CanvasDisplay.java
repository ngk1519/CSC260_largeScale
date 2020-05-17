package csc260.gui;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Ellipse2D;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.lang.*;

import csc260.map_model.*;


/**
 * The overall display for showing the route
 * From http://www.java2s.com/Code/Java/Swing-JFC/TheSwingifiedbuttonexample.htm
 * Modified by Kevin Ng
 */
public class CanvasDisplay extends JPanel implements Listener{

  private RouteControl mouseController;
  private Route theOverallRoute;
  private RouteState overallState;
  private ArrayList<Segment> segmentList;
  private SegmentBank theOverallBank;

  private DefaultListModel model;
  private JList bankList;
  private HashMap<String, Segment> scrollBarStorage;

  private static int WIDTH = 200;
  private static int GAP = 20;
  private static int LINE_WIDTH = 5;
  private static int OVAL_HEIGHT = 30;
  private static int OVAL_WIDTH = 30;

  //screen X limit range is 0 - frame.max_x
  private static final int INITIAL_X = 1;
  //screen Y limit range is
  private static final int INITIAL_Y = 35;
  private static final int INITIAL_X2 = 200;
  private static final int INITIAL_Y2 = 250;

  private JButton exportButton, saveButton, loadButton, clearButton, undoButton;

  public CanvasDisplay(Route overallRoute, RouteState overallState, SegmentBank overallBank, DefaultListModel model, JList bankList, HashMap<String, Segment> scrollBarStorage){
    this.theOverallRoute = overallRoute;
    this.overallState = overallState;
    this.theOverallBank = overallBank;
    this.model = model;
    this.bankList = bankList;
    this.scrollBarStorage = scrollBarStorage;
    this.mouseController = new RouteControl(overallRoute, overallState, this);
    this.segmentList = this.theOverallRoute.getRouteContents();
    // setSize(new Dimension(WIDTH*3, WIDTH*3));
    // setPreferredSize(new Dimension(WIDTH*3, WIDTH*3));

    JPanel toolbar = new JPanel();
    toolbar.setLayout(new FlowLayout(FlowLayout.LEFT));

    // ActionListener printListener = new ActionListener(){
    //   public void actionPerformed(ActionEvent event) {
    //     System.out.println(event.getActionCommand() + " button be selected");
    //   }
    // };

    CanvasDisButtonListener buttonsListener = new CanvasDisButtonListener(this.theOverallRoute, this.overallState,
    this.theOverallBank, this.model, this.bankList, this.scrollBarStorage);
    // Create the save button, and add the save button to the window
    this.exportButton = new JButton("Export Route");
    this.exportButton.addActionListener(buttonsListener);
    toolbar.add(this.exportButton);

    this.saveButton = new JButton("Save Route");
    this.saveButton.addActionListener(buttonsListener);
    toolbar.add(this.saveButton);

    this.loadButton = new JButton("Load Route");
    this.loadButton.addActionListener(buttonsListener);
    toolbar.add(this.loadButton);

    this.clearButton = new JButton("Clear Route");
    this.clearButton.addActionListener(buttonsListener);
    toolbar.add(this.clearButton);

    // this.redoButton = new JButton("Redo");
    // this.redoButton.addActionListener(buttonsListener);
    // toolbar.add(this.redoButton);

    this.undoButton = new JButton("Undo");
    this.undoButton.addActionListener(buttonsListener);
    toolbar.add(this.undoButton);

    // this.themeButton = new JButton("Set Theme");
    // this.themeButton.addActionListener(new CanvasDisButtonListener(this.theOverallRoute, this.theOverallBank,
    // this.model, this.bankList, this.scrollBarStorage));
    // toolbar.add(this.themeButton);

    add(toolbar, BorderLayout.NORTH);
    //add(this);
    // setSize(new Dimension(WIDTH*3, WIDTH*3));
    // setVisible(true);
  }

  public void paintComponent(Graphics g){
    super.paintComponent(g);

    for (Segment eachSeg : this.segmentList){
      int startX = eachSeg.getStartX();
      int startY = eachSeg.getStartY();
      int endX = eachSeg.getEndX();
      int endY = eachSeg.getEndY();
      String startLabel = eachSeg.getStart();
      String endLabel = eachSeg.getDestination();
      String lineLabel = eachSeg.getIntermediatePath();

      if(!eachSeg.getNickName().equals("")){
        lineLabel = eachSeg.getNickName();
      }
      if(!eachSeg.getStartNickname().equals("")){
        startLabel = eachSeg.getStartNickname();
      }
      if(!eachSeg.getDestinationNickname().equals("")){
        endLabel = eachSeg.getDestinationNickname();
      }

      if(this.theOverallRoute.isFirstSegment(eachSeg)){
        drawSegment(g,startX,startY,startLabel,endX,
        endY,endLabel,lineLabel);
      }
      else{
        drawExtendedStartSegment(g,startX,startY,endX,
        endY,endLabel,lineLabel);
      }

      if(eachSeg.getStartVer().getColor().equals(Color.blue)){
        drawPoint(g, startX, startY, Color.blue);
      }
      if(eachSeg.getDestinationVer().getColor().equals(Color.blue)){
        drawPoint(g, endX, endY, Color.blue);
      }


      //System.out.println(eachSeg.getStart() + " "+  eachSeg.getIntermediatePath() + " "+ eachSeg.getDestination());
      // System.out.println(eachSeg.getStartX());
      // System.out.println(eachSeg.getStartY());
      // System.out.println(eachSeg.getEndX());
      // System.out.println(eachSeg.getEndY());
    }
    //System.out.println(eachSeg.getStart() + " "+  eachSeg.getIntermediatePath() + " "+ eachSeg.getDestination());
  }

  /**
  * Draw the segment with the start point, end point and a connection line
  */
  private void drawSegment(Graphics g, int x1, int y1, String s1, int x2, int y2, String s2, String lineLabel){
    drawPoint(g, x1, y1, Color.black);
    drawLabel(g, x1, y1, s1);
    drawPoint(g, x2, y2, Color.black);
    drawLabel(g, x2, y2, s2);
    segmentLine(g, x1, y1, x2, y2, OVAL_HEIGHT, OVAL_WIDTH, lineLabel);
  }

  /**
  * Draw the extention segment (when there is duplicate start/end point)
  */
  private void drawExtendedStartSegment(Graphics g, int x1, int y1, int x2, int y2, String s1, String lineLabel){
    drawPoint(g, x2, y2, Color.black);
    drawLabel(g, x2, y2, s1);
    segmentLine(g, x1, y1, x2, y2, OVAL_HEIGHT, OVAL_WIDTH, lineLabel);
  }

  /**
  * The necessary calculation for draw line connection between two points.
  */
  private void segmentLine(Graphics g, int x1, int y1, int x2, int y2, int height, int width, String lineLabel){
    int center_x1 = x1 + (width/2);
    int center_y1 = y1 + (height/2);
    int center_x2 = x2 + (width/2);
    int center_y2 = y2 + (height/2);
    //Line2D line = new Line2D.Double(center_x1,center_y1,center_x2,center_y2);
    drawLabel(g, (int)((center_x2+center_x1)/2.0), (int)((center_y2+center_y1)/2.0), lineLabel);
    g.drawLine(center_x1,center_y1,center_x2,center_y2);
  }

  /**
  * Draw circle for displaying points on the route
  */
  private void drawPoint(Graphics g, int xPosition, int yPosition, Color color){
    if(!color.equals(Color.black)){
      g.setColor(color);
      g.fillOval(xPosition,yPosition,OVAL_WIDTH,OVAL_HEIGHT);
    }
    g.drawOval(xPosition,yPosition,OVAL_WIDTH,OVAL_HEIGHT);
    g.setColor(Color.black);
  }


  private void drawLabel(Graphics g, int xPos, int yPos, String desiredString){
    g.drawString(desiredString, xPos, yPos);
  }

  public int getRow(int screenX){
    return screenX/WIDTH;
  }

  public int getColumn(int screenY){
    return screenY/WIDTH;
  }

	public void update(){
		repaint();
	}
}
