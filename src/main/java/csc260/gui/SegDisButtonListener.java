package csc260.gui;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.lang.*;

import csc260.map_model.*;

public class SegDisButtonListener implements ActionListener{
  private SegmentBank theBank;
  private Route theRoute;
  private RouteState theState;
  private HashMap<String, Segment> theStorage;
  private JList bankList;
  private DefaultListModel theModel;
  private SegmentDisplay theDisplay;

  private static final int INITIAL_X = 150;
  private static final int INITIAL_Y = 250;
  private static final int INITIAL_X2 = 300;
  private static final int INITIAL_Y2 = 250;

  private static final int LOWEST_LIMIT = 50;
  private static final int HIGHEST_LIMIT = 100;

  public SegDisButtonListener(SegmentDisplay thsDisplay, SegmentBank theBank, Route theRoute, RouteState theState, HashMap<String, Segment> theStorage, JList bankList, DefaultListModel theModel){
    this.theDisplay = theDisplay;
    this.theBank = theBank;
    this.theRoute = theRoute;
    this.theState = theState;
    this.theStorage = theStorage;
    this.bankList = bankList;
    this.theModel = theModel;
  }

  public void actionPerformed(ActionEvent click){
    //If the import button is pressed
    if (click.getActionCommand().equals("Import Segment")){
      ArrayList<String> filePaths = addMultipleFilePaths();
      for (String eachPath : filePaths){
        Segment newSeg = new Segment();
        // CSVFileHandler newCSV = new CSVFileHandler();
        FileHandler newCSV = new CSVHandler();
        File newFile = new File(eachPath);
        newSeg = newCSV.fileToSegment(newFile);
        String newSegmentName = newSeg.getStart()+"->"+newSeg.getIntermediatePath()+"->"+newSeg.getDestination();
        if (!theStorage.containsValue(newSeg) && !newSegmentName.equals("->->")){
          theModel.addElement(newSegmentName);
          theStorage.put(newSegmentName, newSeg);
          this.theBank.addSegmentToBank(newSeg);
          this.theRoute.getSegmentBank().addSegmentToBank(newSeg);
          System.out.println(newSegmentName+" is added a segment into the bank");
        }
      }
    }
    //If the add button is pressed
    else if (click.getActionCommand().equals("Add Segment")){
      Object[] added = this.bankList.getSelectedValues();
      for (Object add : added){
        String temp_string = add.toString();
        Segment tempSeg = this.theStorage.get(temp_string);
        //Determine the x & y positions of the start and the end points
        if (this.theStorage.containsKey(temp_string)){
          if(this.theRoute.numSegmentsInRoute() == 0){
            tempSeg.setStartX(INITIAL_X);
            tempSeg.setStartY(INITIAL_Y);
            tempSeg.setEndX(INITIAL_X2);
            tempSeg.setEndY(INITIAL_Y2);
            System.out.println("added the first segment");
            tempSeg.setIsSelected(true);
            this.theRoute.addSegmentToRoute(tempSeg);
          } else if (this.theRoute.getPositionToAdd(tempSeg) == -1){
              Segment first = this.theRoute.getFirstSegment();
              int endX = first.getStartX();
              int endY = first.getStartY();
              int startX = first.getStartX()+randomNumber(LOWEST_LIMIT, HIGHEST_LIMIT);
              int startY = first.getStartY()+randomNumber(LOWEST_LIMIT, HIGHEST_LIMIT);
              tempSeg.setStartX(startX);
              tempSeg.setStartY(startY);
              tempSeg.setEndX(endX);
              tempSeg.setEndY(endY);
              System.out.println("added front");
              tempSeg.setIsSelected(true);
              this.theRoute.addSegmentToRoute(tempSeg);
            } else if (this.theRoute.getPositionToAdd(tempSeg) == (this.theRoute.numSegmentsInRoute())){
              Segment last = this.theRoute.getLastSegment();
              int startX = last.getEndX();
              int startY = last.getEndY();
              int endX = last.getEndX()+randomNumber(LOWEST_LIMIT, HIGHEST_LIMIT);
              int endY = last.getEndY()+randomNumber(LOWEST_LIMIT, HIGHEST_LIMIT);
              tempSeg.setStartX(startX);
              tempSeg.setStartY(startY);
              tempSeg.setEndX(endX);
              tempSeg.setEndY(endY);
              System.out.println("added end");
              tempSeg.setIsSelected(true);
              this.theRoute.addSegmentToRoute(tempSeg);
            } else if (this.theRoute.getPositionToAdd(tempSeg) < this.theRoute.numSegmentsInRoute()
            && this.theRoute.getPositionToAdd(tempSeg) >= 0){
              Segment toReplace = this.theRoute.getRouteContents().get(this.theRoute.getPositionToAdd(tempSeg));
              int startX = toReplace.getStartX();
              int startY = toReplace.getStartY();
              int endX = toReplace.getEndX();
              int endY = toReplace.getEndY();
              tempSeg.setStartX(startX);
              tempSeg.setStartY(startY);
              tempSeg.setEndX(endX);
              tempSeg.setEndY(endY);
              System.out.println("replace an segment");
              tempSeg.setIsSelected(true);
              this.theRoute.addSegmentToRoute(tempSeg);
            }
            System.out.println("The segment of " + add.toString() + " is added to the Route");
            Route newRoute = this.theRoute.clone();
            this.theState.addRoute(newRoute);
            System.out.println("The overall route has: " + this.theRoute.printStartEnd());
            System.out.println("The size of the route is " + this.theRoute.numSegmentsInRoute());
          }
        }
        // this.theState.addRoute(this.theRoute);
        // System.out.println("The overall route has: " + this.theRoute.printStartEnd());
        // System.out.println("The size of the route is " + this.theRoute.numSegmentsInRoute());
    }

    // If the label button is pressed
    else if (click.getActionCommand().equals("Label Segment")){
      System.out.println("Selected segment for editing labels: ");
      Object[] selected = bankList.getSelectedValues();
      for (Object select : selected){
        String newLabel = "";
        String start = "";
        String end = "";
        String middle = "";
        String select_String = select.toString();
        Segment selectSeg = new Segment();

        if (this.theStorage.containsKey(select_String)){
          selectSeg = this.theStorage.get(select_String);
          this.theStorage.remove(select_String);
        }
        JFrame tempFrame = new JFrame();
        String tempStart = JOptionPane.showInputDialog(tempFrame,"Label the start");
        String tempMiddle = JOptionPane.showInputDialog(tempFrame,"Label the intermediate Path");
        String tempEnd = JOptionPane.showInputDialog(tempFrame,"Label the destination");

        if (tempStart.equals("")){
          start = selectSeg.getStart();
        } else {
          start = tempStart;
        }
        if (tempMiddle.equals("")){
          middle = selectSeg.getIntermediatePath();
        } else {
          middle = tempMiddle;
        }
        if (tempEnd.equals("")){
          end = selectSeg.getDestination();
        } else {
          end = tempEnd;
        }

        newLabel = start+"->"+middle+"->"+end;
        this.theModel.removeElement(select);
        this.theModel.addElement(newLabel);
        this.theStorage.put(newLabel, selectSeg);

        this.theBank.renameSegment(selectSeg, start, middle, end);
        this.theRoute.getSegmentBank().renameSegment(selectSeg, start, middle, end);
        this.theRoute.renameSegment(selectSeg, start, middle, end);
      }
      Route newRoute = this.theRoute.clone();
      this.theState.addRoute(newRoute);
      System.out.println("The overall route has: " + this.theRoute.printStartEnd());
    }
    else if (click.getActionCommand().equals("Remove Segment")){
      Object[] selected = this.bankList.getSelectedValues();
      for (Object select : selected){
        String select_Str = select.toString();
        Segment temp_Seg = new Segment();
        System.out.println(this.theStorage.containsKey(select_Str));
        if (this.theStorage.containsKey(select_Str)){
          temp_Seg = this.theStorage.get(select_Str);
          // this.theStorage.remove(select_Str);
          // System.out.println("Removed from storage");
          //
          // System.out.println(this.theRoute.getSegmentBank().containsSegment(temp_Seg));
          // if (this.theRoute.getSegmentBank().containsSegment(temp_Seg)){
          //   this.theRoute.getSegmentBank().removeSegmentFromBank(temp_Seg);
          //   System.out.println("Removed from the bank");
          // }

          System.out.println(this.theRoute.containsSegment(temp_Seg));
          if (this.theRoute.containsSegment(temp_Seg)){
            this.theRoute.removeSegment(temp_Seg);
            System.out.println("Removed from the route");
          }
          Route newRoute = this.theRoute.clone();
          this.theState.addRoute(newRoute);
          System.out.println(this.theRoute.printStartEnd());
        }
        //this.theModel.removeElement(select);
      }
    }
  }

  /**
  * @return the selected file path
  */
  private String addFilePath(){
    String filePath = "";
    String fileName = "";
    JFileChooser fileChooser = new JFileChooser();
    if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
      java.io.File file = fileChooser.getSelectedFile();
      filePath = file.getPath();
      fileName = file.getName();
      //System.out.println(filePath);
    }
    return filePath;
  }

  /**
  * @return multiple selected file paths
  */
  private ArrayList<String> addMultipleFilePaths(){
    ArrayList<String> listOfFileName = new ArrayList<String>();
    JFileChooser filesChooser = new JFileChooser();
    filesChooser.setMultiSelectionEnabled(true);
    if (filesChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
      File[] files = filesChooser.getSelectedFiles();
      for (File selectedFile : files){
        String filePath = selectedFile.getPath();
        listOfFileName.add(filePath);
      }
    }
    return listOfFileName;
  }

  /**
  * @param min the lowest limit
  * @param max the highest possible number
  *
  * @return a random integer ranging from min and max
  */
  private int randomNumber (int min, int max){
    Random randomNum = new Random();
		return randomNum.nextInt((max - min) + 1) + min;
  }

}
