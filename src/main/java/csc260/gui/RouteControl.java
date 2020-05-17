package csc260.gui;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.*;
import java.lang.*;
import javax.swing.*;

import csc260.gui.CanvasDisplay;
import csc260.map_model.*;

public class RouteControl implements MouseListener, MouseMotionListener{

  private Route overallPath;
  private RouteState overallState;
  private CanvasDisplay overallDisplay;
  private SegmentVertex segmentVertexToMove;
  private boolean dragging;
  private int xPosition, yPosition;
  private int oldX, oldY;


  public RouteControl(Route overallPath, RouteState overallState, CanvasDisplay overallDisplay){
    this.overallPath = overallPath;
    this.overallState = overallState;
    this.overallDisplay = overallDisplay;
    this.segmentVertexToMove = new SegmentVertex();
    this.overallDisplay.addMouseListener(this);
    this.overallDisplay.addMouseMotionListener(this);
    this.dragging = false;
  }

  public void mouseClicked(MouseEvent click){

  }

  public void mouseEntered(MouseEvent click){

  }

  public void mouseExited(MouseEvent click){

  }

  public void mouseDragged(MouseEvent drag){
    xPosition = drag.getX();
    yPosition = drag.getY();
    this.dragging = true;
    this.segmentVertexToMove = this.overallPath.segmentToMove(xPosition, yPosition);
    if (this.segmentVertexToMove != null){
      this.overallPath.updateSegment(this.segmentVertexToMove, xPosition, yPosition, Color.blue);
    }
  }

  public void mouseMoved(MouseEvent moved){

  }

  public void mousePressed(MouseEvent press){
    // xPosition = press.getX();
    // yPosition = press.getY();
    //
    // this.segmentVertexToMove = this.overallPath.segmentToMove(xPosition, yPosition);
    // //System.out.println(this.segmentVertexToMove.getName());
    // this.dragging = true;
    // oldX = this.segmentVertexToMove.getX();
    // oldY = this.segmentVertexToMove.getY();
    // //System.out.println("Press detected!");
  }

  public void mouseReleased(MouseEvent press){
    if(this.dragging == true){
      this.dragging = false;
      xPosition = press.getX();
      yPosition = press.getY();
      this.segmentVertexToMove = this.overallPath.segmentToMove(xPosition, yPosition);
      this.overallPath.updateSegment(this.segmentVertexToMove, xPosition, yPosition, Color.black);
      Route cloneRoute = this.overallPath.clone();
      this.overallState.addRoute(cloneRoute);
    }
    // if(this.segmentVertexToMove != null){
    //   xPosition = press.getX();
    //   yPosition = press.getY();
    //   // System.out.print("click x:");
    //   // System.out.println(xPosition);
    //   // System.out.print("click y:");
    //   // System.out.println(yPosition);
    //   if (Math.abs(xPosition-oldX) > 0 || Math.abs(yPosition-oldY) > 0 && this.dragging){
    //     this.overallPath.updateSegment(this.segmentVertexToMove, xPosition, yPosition);
    //   }
    //   this.dragging = false;
    //   this.segmentVertexToMove = new SegmentVertex();
    //   //System.out.println("Mouse released, reset the segmentToMove");
    // }
  }
}
