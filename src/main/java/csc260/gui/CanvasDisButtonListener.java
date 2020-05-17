package csc260.gui;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.lang.*;

import csc260.map_model.*;

public class CanvasDisButtonListener implements ActionListener {

  private Route theRoute;
  private RouteState theState;
  private SegmentBank theBank;
  private FileHandler theFile;
  private DefaultListModel model;
  private JList bankList;
  private HashMap<String, Segment> scrollBarStorage;

  public CanvasDisButtonListener(Route theRoute, RouteState theState, SegmentBank theBank, DefaultListModel model, JList bankList, HashMap<String, Segment> scrollBarStorage){
    this.theRoute = theRoute;
    this.theState = theState;
    this.theBank = theBank;
    this.model = model;
    this.bankList = bankList;
    this.scrollBarStorage = scrollBarStorage;
  }

  public void actionPerformed(ActionEvent press){
    //The actions after pressing the export button
    if(press.getActionCommand().equals("Export Route")){
      // The window for selecting the content formatting
      JFrame contentOptionFrame = new JFrame();
      String[] contentOption = {"Completed Version", "Abbreviated Version"};
      int contentFormatChoice = JOptionPane.showOptionDialog(contentOptionFrame, "Please select the content format for exporting",
      "File content formatting", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
      null, contentOption, null);


      // The window for selecting the file formatting
      JFrame optionFrame = new JFrame();
      String[] formatOption = {".csv", ".txt"};
      int formatChoice = JOptionPane.showOptionDialog(optionFrame, "Please select a file format for exporting",
      "Export file format", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
      null, formatOption, null);

      //If the user decides to export .csv file
      if (formatChoice == 0 && contentFormatChoice == 0){
        FileHandler csvFile = new CSVHandler();
        String directoryPath = getDirectory();
        System.out.println(directoryPath);
        String routeContents = this.theRoute.buildRouteString();
        System.out.println(routeContents);
        JFrame pathNameFrame = new JFrame();
        String csvTitle = JOptionPane.showInputDialog(pathNameFrame,"Please enter the title of the file export");
        try {
          csvFile.stringToExportFile(routeContents, directoryPath, csvTitle, ".csv");
        } catch (Exception e) {
            System.out.println("Invalid output. hahah");
        }
      }
      //If the user chooses to export .txt file
      else if (formatChoice == 1 && contentFormatChoice == 0){
        String directory = getDirectory();
        JFrame pathNameFrame = new JFrame();
        String routeContents = this.theRoute.buildRouteString();
        String txtTitle = JOptionPane.showInputDialog(pathNameFrame,"Please enter the title of the save file");
        FileHandler txtHandler = new TXTHandler();
        try {
          txtHandler.stringToExportFile(routeContents, directory, txtTitle, ".txt");
        } catch (Exception e) {
            System.out.println("Invalid output");
        }
      }

      else if (formatChoice == 0 && contentFormatChoice == 1){
        String directory = getDirectory();
        JFrame pathNameFrame = new JFrame();
        String routeContents = this.theRoute.buildAbbreviatedRouteString();
        String txtTitle = JOptionPane.showInputDialog(pathNameFrame,"Please enter the title of the save file");
        FileHandler txtHandler = new CSVHandler();
        try {
          txtHandler.stringToExportFile(routeContents, directory, txtTitle, ".csv");
        } catch (Exception e) {
            System.out.println("Invalid output");
        }
      }

      else if (formatChoice == 1 && contentFormatChoice == 1){
        String directory = getDirectory();
        JFrame pathNameFrame = new JFrame();
        String routeContents = this.theRoute.buildAbbreviatedRouteString();
        String txtTitle = JOptionPane.showInputDialog(pathNameFrame,"Please enter the title of the save file");
        FileHandler txtHandler = new TXTHandler();
        try {
          txtHandler.stringToExportFile(routeContents, directory, txtTitle, ".txt");
        } catch (Exception e) {
            System.out.println("Invalid output");
        }
      }


    }
    else if (press.getActionCommand().equals("Save Route")){
      String directory = getDirectory();
      JFrame pathNameFrame = new JFrame();
      String fileTitle = JOptionPane.showInputDialog(pathNameFrame,"Please enter the title of the save file");
      // CSVHandler csvHandler = new CSVHandler();
      // csvHandler.save(this.theRoute.getSegmentBankContents(),this.theRoute.getRouteContents(),directory,fileTitle);

      this.theRoute.save(directory, fileTitle);

    }
    else if (press.getActionCommand().equals("Load Route")){
      String loadFile = addFilePath();
      File newFile = new File(loadFile);
      this.theRoute.clearRoute();
      this.theBank.clearBank();
      this.model.clear();
      this.scrollBarStorage.clear();

      // CSVHandler csvHandler = new CSVHandler();
      // csvHandler.load(newFile,this.theRoute.getSegmentBankContents(),this.theRoute.getRouteContents());

      this.theRoute.load(newFile);
      System.out.println(this.theRoute.getRouteContents().size());
      this.theBank = this.theRoute.getSegmentBank();
      for (Segment eachSeg : this.theRoute.getRouteContents()){
        String newSegmentName = "";
        if (eachSeg.getStartNickname().equals("")){
          newSegmentName += eachSeg.getStart()+"->";
        } else {
          newSegmentName += eachSeg.getStartNickname()+"->";
        }
        if (eachSeg.getNickName().equals("")){
          newSegmentName += eachSeg.getIntermediatePath()+"->";
        } else {
          newSegmentName += eachSeg.getNickName()+"->";
        }
        if (eachSeg.getDestinationNickname().equals("")){
          newSegmentName += eachSeg.getDestination();
        } else {
          newSegmentName += eachSeg.getDestinationNickname();
        }
        //newSegmentName = eachSeg.getStart()+"->"+eachSeg.getIntermediatePath()+"->"+eachSeg.getDestination();
        if (!this.scrollBarStorage.containsKey(newSegmentName) && !this.model.contains(newSegmentName)){
          this.model.addElement(newSegmentName);
          this.scrollBarStorage.put(newSegmentName, eachSeg);
        }
      }
    }
    else if (press.getActionCommand().equals("Clear Route")){
      this.theRoute.clearRoute();
      Route newRoute = this.theRoute.clone();
      this.theState.addRoute(newRoute);
    }
    else if (press.getActionCommand().equals("Undo")){
      Route temp = this.theState.getPreviousRoute();
      System.out.println("The route from the routeState returns: " + temp.printStartEnd());
      this.theRoute.updateRoute(temp.getSegmentBankContents(),temp.getRouteContents(),temp.getSegmentBank());
    }
    // else if (press.getActionCommand().equals("Redo")){
    //   Route temp = this.theState.getNewerRoute();
    //   System.out.println("The route from the routeState returns: " + temp.printStartEnd());
    //   this.theRoute.updateRoute(temp.getSegmentBankContents(),temp.getRouteContents(),temp.getSegmentBank());
    // }
    // else if (press.getActionCommand().equals("Set Theme")){
    //   JFrame optionFrame = new JFrame();
    //   String[] themeOption = {"Original", "Red", "Green", "Blue"};
    //   String theme = (String) JOptionPane.showInputDialog(optionFrame, "Please set the theme",
    //   "Theme selector", JOptionPane.QUESTION_MESSAGE, null, themeOption, themeOption[0]);
    //   System.out.println(theme);
    // }
  }


  /**
  * Creates a pop-window for choosing the desired directory
  * From: https://www.rgagnon.com/javadetails/java-0370.html
  *
  * @return the selected directory path as a string
  */

  private String getDirectory(){
    String directoryPath = "";
    JFileChooser fileChoice = new JFileChooser();
    fileChoice.setCurrentDirectory(new java.io.File("."));
    fileChoice.setDialogTitle("Please choose a directory");
    fileChoice.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    fileChoice.setAcceptAllFileFilterUsed(false);

    if (fileChoice.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
      System.out.println("The Current directory: "+fileChoice.getCurrentDirectory());
      File chosenDirectory = fileChoice.getCurrentDirectory();
      directoryPath = fileChoice.getSelectedFile().getPath();
      //System.out.println("getSelectedFile() : "+fileChoice.getSelectedFile());
    }
    else {
      System.out.println("No Selection ");
    }
    return directoryPath;
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
}
