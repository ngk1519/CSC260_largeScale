package csc260.map_model;

import java.util.*;
import java.io.*;
import java.awt.*;

/**
* @author Patrick, Woody
* @version 02/17/2019
*/

public class Route {

    private ArrayList<Segment> segmentBankContents;
    private  ArrayList<Segment> routeContents;
    private Vector<Listener> listeners;
    private SegmentBank theBank;

    private static int OVAL_HEIGHT = 30;
    private static int OVAL_WIDTH = 30;


    // public Route () {
    //     this.segmentBankContents = new ArrayList<>();
    //     this.routeContents = new ArrayList<>();
    // }

    public Route(SegmentBank segmentBank) {
      this.segmentBankContents = segmentBank.getSegmentBankContents();
      this.theBank = segmentBank;
      this.routeContents = new ArrayList<>();
      listeners = new Vector<Listener>();
    }

    // Getter methods
    public ArrayList<Segment> getSegmentBankContents() { return this.segmentBankContents; }
    public ArrayList<Segment> getRouteContents() { return this.routeContents; }
    public SegmentBank getSegmentBank() { return this.theBank; }

    // Setter methods
    public void setSegmentBankContents (ArrayList<Segment> segmentBankContents) { this.segmentBankContents = segmentBankContents; }
    public void setRouteContents (ArrayList<Segment> routeContents) { this.routeContents = routeContents; }
    public void setSegmentBank (SegmentBank theBank) { this.theBank = theBank; }

        public Route clone(){
          ArrayList<Segment> SegBankContent = new ArrayList<>();
          ArrayList<Segment> theRouteContent = new ArrayList<>();
          SegmentBank segBank = new SegmentBank();
          Route cloneRoute = new Route(segBank);

          for (Segment eachSeg : this.segmentBankContents){
            SegBankContent.add(eachSeg);
          }
          for (Segment eachSeg : this.routeContents){
            theRouteContent.add(eachSeg);
          }
          segBank.setSegmentBankContents(this.theBank.getSegmentBankContents());

          cloneRoute.setSegmentBankContents(SegBankContent);
          cloneRoute.setRouteContents(theRouteContent);
          cloneRoute.setSegmentBank(segBank);
          return cloneRoute;
        }

        public String printStartEnd(){
          String output = "";
          String start = "";
          String end = "";
          for (Segment eachSeg : this.routeContents){
            start = eachSeg.getStart();
            end = eachSeg.getDestination();
            output += start + "->" + end + "\n";
          }
          return output;
        }

        /**
        * Look for the selected segment vertex within a segment in route
        * @param x the x position of the screen
        * @param y the y position of the screen
        *
        * @return clickedVer the selected vertex
        */
        public SegmentVertex segmentToMove(int x, int y){
          SegmentVertex toReturn = new SegmentVertex();
          for (Segment eachSegment : this.routeContents){
            if ((eachSegment.getStartX()-OVAL_WIDTH) < x && x < (eachSegment.getStartX()+OVAL_WIDTH) &&
            (eachSegment.getStartY()-OVAL_HEIGHT) < y && y < (eachSegment.getStartY()+OVAL_HEIGHT)){
              toReturn = eachSegment.getStartVer();
              toReturn.setColor(Color.blue);
              notifyListeners();
              return toReturn;
            }
            else if ((eachSegment.getEndX()-OVAL_WIDTH) < x && x < (eachSegment.getEndX()+OVAL_WIDTH) &&
            (eachSegment.getEndY()-OVAL_HEIGHT) < y && y < (eachSegment.getEndY()+OVAL_HEIGHT)){
              toReturn = eachSegment.getDestinationVer();
              toReturn.setColor(Color.blue);
              notifyListeners();
              return toReturn;
            }
          }
          return null;
        }

        public void updateSegment(SegmentVertex vertex, int newX, int newY, Color color){
          vertex.setColor(color);
          Segment currentSegment;
          Segment previousSegment;
          for (int i = 0; i < numSegmentsInRoute(); i++){
            currentSegment = this.routeContents.get(i);
            if(currentSegment.getStartVer().vertEquals(vertex)){
              if(i == 0){
                currentSegment.setStartX(newX);
                currentSegment.setStartY(newY);
              }
              else{
                previousSegment = this.routeContents.get(i-1);
                currentSegment.setStartX(newX);
                currentSegment.setStartY(newY);
                previousSegment.setEndX(newX);
                previousSegment.setEndY(newY);
              }
            }
            else if(i == numSegmentsInRoute()-1 && currentSegment.getDestinationVer().vertEquals(vertex)){
              currentSegment.setEndX(newX);
              currentSegment.setEndY(newY);
            }
          }
          notifyListeners();
        }

        public void updateRoute(ArrayList<Segment> bankContent, ArrayList<Segment> routeContent, SegmentBank theBank){
          this.clearRoute();
          //this.segmentBankContents = bankContent;
          for (Segment eachSeg : bankContent){
            eachSeg.setIsSelected(true);
            this.segmentBankContents.add(eachSeg);
          }
          this.theBank = theBank;
          //this.routeContents = routeContent;
          for (Segment eachSeg : routeContent){
            if (!eachSeg.getIsAdded()){
              eachSeg.setIsSelected(true);
              this.addSegmentToRoute(eachSeg);
            }
          }
          System.out.println("Updated");
          notifyListeners();
        }

    public boolean containsSegment(Segment inputSeg){
      return this.routeContents.contains(inputSeg);
    }

    public void renameSegment(Segment inputSeg, String startNickname, String pathNickname, String endNickname){
      SegmentVertex start = inputSeg.getStartVer();
      SegmentVertex end = inputSeg.getDestinationVer();
      Segment currentSegment = new Segment();

      if (this.containsSegment(inputSeg)){
        boolean foundSeg = false;
        int counter = 0;
        while (!foundSeg && counter < this.routeContents.size()){
          currentSegment = this.routeContents.get(counter);
          if (currentSegment.isSegEqual(inputSeg)){
            foundSeg = true;
            currentSegment.setNickName(pathNickname);
            if(currentSegment.getStartVer().isSameName(start)){
              currentSegment.renameStart(startNickname);
            }
            if(currentSegment.getStartVer().isSameName(end)){
              currentSegment.renameStart(endNickname);
            }
            if(currentSegment.getDestinationVer().isSameName(start)){
              currentSegment.renameDestination(startNickname);
            }
            if(currentSegment.getDestinationVer().isSameName(end)){
              currentSegment.renameDestination(endNickname);
            }
            this.addSegmentToRoute(currentSegment);
          }
          counter++;
        }
      }
      notifyListeners();
    }

    /**
    * @param segment the segment that is going to add and displahy on the route
    * @return false if:
    * 1. the segment cannot make a successful route
    * 2. the segment is not selected
    * 3. the segment already exists in the route, or doesn't exist in the bank
    *
    * else true
    */
    public boolean isValidAdd(Segment segment) {
      if(segment.getIsSelected() && segmentBankContents.contains(segment) && !segment.getIsAdded()){
        if(routeContents.isEmpty()){
          return true;
        }
        else{
          String segmentStart = segment.getStart();
          String segmentEnd = segment.getDestination();
          String routeStart = getFirstSegment().getStart();
          String routeEnd = getLastSegment().getDestination();
          boolean segStartEqualsRouteEnd = segmentStart.equals(routeEnd);
          boolean segEndEqualsRouteStart = segmentEnd.equals(routeStart);
          boolean sameStartAndEnd = false;
          for(Segment currentSegment: routeContents){
            if(currentSegment.sameButDifferentPath(segment)){
              sameStartAndEnd = true;
            }
          }
          return segStartEqualsRouteEnd || segEndEqualsRouteStart || sameStartAndEnd;
        }
      }
      else{
        return false;
      }
    }

    /**
    * @param segment the segment that is going to be deleted from route
    * @return false if:
    * 1. the segment is in the middle of the route
    * 2. the segment is not selected
    * 3. the segment doesn't exist in the route (also checks if the route is empty)
    *
    * else true
    */
    private boolean isValidRemove(Segment segment) {
      if(!segment.getIsSelected()) return false;
      return segment.equals(getFirstSegment()) || segment.equals(getLastSegment());
    }

    /**
    * @return the first segment of the route, null if it's an empty route
    */
    public Segment getFirstSegment() throws IllegalStateException {
      if(!this.routeContents.isEmpty()){
        return this.routeContents.get(0);
      }
      else{
        throw new IllegalStateException("No segment in route, therefore cannot call getFirstSegment.");
      }
    }

    /**
    * @return the last segment of the route, null if it's an empty route
    */
    public Segment getLastSegment() throws IllegalStateException {
      if(!this.routeContents.isEmpty()){ return this.routeContents.get(this.routeContents.size()-1);}
      else{ throw new IllegalStateException("No segment in route, therefore cannot call getLastSegment."); }
    }

    /**
    * @param segment the segment user want to add to the route
    */
    public void addSegmentToRoute(Segment segment) {
      if(isValidAdd(segment)){
        segment.setIsAdded(true);
        if(routeContents.isEmpty()){
          routeContents.add(segment);
        }
        else{
          int indexToAdd = getPositionToAdd(segment);
          if(indexToAdd != -2){
            if(0 <= indexToAdd && indexToAdd < numSegmentsInRoute()){
              routeContents.get(indexToAdd).setIsAdded(false);
              routeContents.set(indexToAdd, segment);
            }
            else if(indexToAdd == -1){
              routeContents.add(0, segment);
            }
            else if(indexToAdd == numSegmentsInRoute()){
              routeContents.add(segment);
            }
          }
        }
      }
      notifyListeners();
    }

    /**
    * @param segment the segment user want to delete from the route
    */
    public void removeSegment(Segment segment) {
      if(isValidRemove(segment)){
        segment.setIsAdded(false);
        routeContents.remove(segment);
      }
      notifyListeners();
    }

    /**
    * reset the segment bank
    */
    public void clearRoute() {
        for (Segment segment: this.segmentBankContents) {
            segment.setIsAdded(false);
            segment.setIsSelected(false);
        }
        this.routeContents.clear();
        notifyListeners();
    }

    /**
    * @return a string represents the route:
    * it includes the start point, the intermediate path, and the end point
    */
    public String buildRouteString(){
        String route = "";
        ArrayList<Segment> currentRouteContents = this.routeContents;
        int numSegments = numSegmentsInRoute();
        if(numSegments < 1){
          return "";
        }
        else if(numSegments == 1){
          return currentRouteContents.get(0).getOriginalContent();
        }
        else{
          double currentWeight = 0;
          for(int i = 0; i < numSegments; i++){
            Segment currentSegment = currentRouteContents.get(i);
            double segmentWeight = currentSegment.getWeight();
            route += editStringSegment(currentSegment, currentWeight);
            System.out.println(route);
            currentWeight += segmentWeight;
          }
          return route;
        }
    }

    public String buildAbbreviatedRouteString(){
      CharSequence[] splitter = {"onto ","toward ","on "};
      String toEdit = buildRouteString();
      String[] routeStringArray = toEdit.split("\n");
      int lines = routeStringArray.length;
      String currentLine;
      String toAbbreviate;
      String[] currentLineArray;
      for(int i = 0; i < lines; i++){
        if(1 < i && i < lines - 1){
          currentLine = routeStringArray[i];
          currentLineArray = currentLine.split(",");
          toAbbreviate = currentLineArray[1];
          for(int j = 0; j < splitter.length; j++){
            String currentSplitter = String.valueOf(splitter[j]);
            if(toAbbreviate.contains(currentSplitter)){
              currentLineArray[1] = String.valueOf(currentLineArray[0].charAt(0)) + ":" + toAbbreviate.split(currentSplitter)[1];
              j = 10000;
            }
          }
          currentLine = "";
          for(int j = 0; j < currentLineArray.length; j++){
            if(j > 0){
              currentLine += ",";
            }
            currentLine += currentLineArray[j];
          }
          routeStringArray[i] = currentLine;
        }
      }
      String toReturn = "";
      for(int i = 0; i < routeStringArray.length; i++){
        toReturn += routeStringArray[i] + "\n";
      }
      return toReturn;
    }

    /**
    * @param s a segment in the route
    * @param startDistance the beginning distance of this segment
    * @return the string of a segment that is part of the route string
    */
    private String editStringSegment(Segment s, double currentWeight){
      if(s.isSegEqual(getFirstSegment())){
        //System.out.println(editStringOfFirstSegment(s, currentWeight));
        return editStringOfFirstSegment(s, currentWeight);
      }
      else if(s.isSegEqual(getLastSegment())){
        //System.out.println(editStringOfLastSegment(s, currentWeight));
        return editStringOfLastSegment(s, currentWeight);
      }
      else{
        // System.out.println("hahaha");
        //System.out.println(editStringOfIntermediateSegment(s, currentWeight));
        return editStringOfIntermediateSegment(s, currentWeight);
      }
    }

    /**
    * @param s the first segment of the route
    * @param startDistance the beginning distance of this segment
    * @return the string of first segment that is going to used in buildRouteString
    */
    private String editStringOfFirstSegment(Segment s, double startDistance){
      String toReturn = addDistanceBy(s, startDistance);
      int numLines = numLines(toReturn);
      return removeIthLine(toReturn, numLines);
    }

    /**
    * @param s the intermediate segment of the route
    * @param startDistance the beginning distance of this segment
    * @return the string of intermediate segment that is going to used in buildRouteString
    */
    public String editStringOfIntermediateSegment(Segment s, double startDistance){
      String toReturn = addDistanceBy(s, startDistance);
      toReturn = removeIthLine(toReturn, 1);
      toReturn = removeIthLine(toReturn, 1);
      int numLines = numLines(toReturn);
      toReturn = removeIthLine(toReturn, numLines);
      return toReturn;
    }

    /**
    * @param s the last segment of the route
    * @param startDistance the beginning distance of this segment
    * @return the string of last segment that is going to used in buildRouteString
    */
    private String editStringOfLastSegment(Segment s, double startDistance){
      String toReturn = addDistanceBy(s, startDistance);
      toReturn = removeIthLine(toReturn, 1);
      return removeIthLine(toReturn, 1);
    }

    /**
    * @param String the contents of the segment
    * @return the number of lines in a segment string
    */
    private int numLines(String segmentContents){
      String[] contents = segmentContents.split("\n");
      return contents.length;
    }

    /**
    * @param s a segment
    * @param toAdd the distance to add
    * @return an updated string represents a segment in the route
    */
    private String addDistanceBy(Segment s, double toAdd){
      String toReturn = "";
      String segmentString = s.getOriginalContent();
      Scanner scanner = new Scanner(segmentString);
      ArrayList<String[]> segmentContents = new ArrayList<>();
      while (scanner.hasNextLine()) {
        String[] contents = scanner.nextLine().split(",");
        segmentContents.add(contents);
      }
      for(int i = 0; i < segmentContents.size(); i++){
        String[] line = segmentContents.get(i);
        int numPhrases = line.length;
        if(i == 0){
          for(int j = 0; j < numPhrases; j++){
            //System.out.println(line[j]);
            toReturn += line[j];
            if(j != numPhrases - 1){
              toReturn += ",";
            }
          }
        }
        else{
          for(int j = 0; j < numPhrases; j++){
            if(j == numPhrases - 1){
              double oldDistance = Double.valueOf(line[numPhrases - 1]);
              double newDistance = oldDistance + toAdd;
              String result;
              if(newDistance == 0){
                result = String.valueOf(0);
              }
              else{
                result = String.format("%.2f", oldDistance + toAdd);
              }
              toReturn += result;
              //System.out.println(result);
            }
            else{
              toReturn += line[j] + ",";
            }
          }
        }
        toReturn += "\n";
        //System.out.println(toReturn);
      }
      return toReturn;
    }

    /**
    * @param segmentString a string that represents a segment
    * @param ith the ith line to remove
    *
    * @return the updated string after removing the specified line
    */
    private String removeIthLine(String segmentString, int ith){
      String toReturn = "";
      String[] segmentContents = segmentString.split("\n");
      for(int i = 0; i < segmentContents.length; i++){
        if(i != ith - 1){
          toReturn += segmentContents[i] + "\n";
        }
      }
      return toReturn;
    }

    /**
    * @return the number of segments in the route
    */
    public int numSegmentsInRoute(){
      return routeContents.size();
    }

    /**
    * @param toAdd the segment to add
    * @return -1 if the segment should be added to the front of the route,
    * current SIZE of the route if it should be added to the end of the route,
    * the INDEX of the segment that will be repalced by the toAdd if toAdd is going to replace a segment in the route,
    * otherwise -2, meaning that the add is invalid
    */
    public int getPositionToAdd(Segment toAdd){
      if(routeContents.isEmpty()){return 0;}
      String segmentStart = toAdd.getStart();
      String segmentEnd = toAdd.getDestination();
      String routeStart = getFirstSegment().getStart();
      String routeEnd = getLastSegment().getDestination();
      if(segmentStart.equals(routeEnd)){
        return numSegmentsInRoute();
      }
      else if(segmentEnd.equals(routeStart)){
        return -1;
      }
      for(int i = 0; i < numSegmentsInRoute(); i++){
        Segment currentSegment = routeContents.get(i);
        if(currentSegment.sameButDifferentPath(toAdd)){
          return i;
        }
      }
      return -2;
    }

    /**
    * @param segment the segment to check
    @ @return true if the segment is the last segment of the route, else false
    */
    public boolean isLastSegment(Segment segment){
      return getLastSegment().isSegEqual(segment);
    }

    public boolean isFirstSegment(Segment segment){
      return getFirstSegment().isSegEqual(segment);
    }

    public void addListener(Listener l){
        listeners.add(l);
    }

    public void removeListener(Listener l){
        listeners.remove(l);
    }

    private void notifyListeners(){
        for (Listener l : listeners) {
            l.update();
        }
    }

    public void save (String csvOutputPath, String fileName) {
        String patrick = "patrick";
        String toBuild = "";

        toBuild += patrick;
        for (Segment segment : this.segmentBankContents) {
            toBuild += segment.segToSaveString();
        }

        toBuild += patrick;
        for (Segment segment : this.routeContents) {
            toBuild += segment.segToSaveString();
        }

        TXTHandler txtHandler = new TXTHandler();
        try {
            txtHandler.stringToExportFile(toBuild,csvOutputPath,fileName,".txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void load (File txtFile) {

     String txtContents = "";
     TXTHandler txtHandler = new TXTHandler();
     // try {
         // Scanner txtScanner = new Scanner(txtFile);
         // while (txtScanner.hasNext()){
         //     txtContents += txtScanner.next();
         // }


         txtContents = txtHandler.fileToString(txtFile);

         String[] routeSegBankSeparator = txtContents.split("patrick");
         String[] bankItems = routeSegBankSeparator[1].split("kevin");
         String[] routeItems = routeSegBankSeparator[2].split("kevin");

         for (int x=1; x<bankItems.length; x++) {
             Segment temp = new Segment();

             String[] segFields = bankItems[x].split("woody");
             System.out.println(segFields[1]);
             temp.setOriginalContent(segFields[1]);

             if (segFields[2].equals("true")) temp.setIsSelected(true);
             else temp.setIsSelected(false);

             if (segFields[3].equals("true")) temp.setIsAdded(true);
             else temp.setIsAdded(false);

             // temp.setNickName(segFields[4]);
             // temp.renameStart(segFields[5]);
             // temp.renameDestination(segFields[6]);
             // temp.setFileName(segFields[7]);
             // temp.setFilePath(segFields[8]);
             // temp.setStart(segFields[9]);
             // temp.setDestination(segFields[10]);
             // temp.setIntermediatePath(segFields[11]);
             // temp.setWeight(Double.valueOf(segFields[12]));
             // temp.setStartX(Integer.valueOf(segFields[13]));
             // temp.setStartY(Integer.valueOf(segFields[14]));
             // temp.setEndX(Integer.valueOf(segFields[15]));
             // temp.setEndY(Integer.valueOf(segFields[16]));

             temp.setNickName(segFields[4]);
             temp.renameStart(segFields[5]);
             temp.renameDestination(segFields[6]);
             temp.setFileName(segFields[7]);
             temp.setFilePath(segFields[8]);
             temp.setStart(segFields[9]);
             temp.setDestination(segFields[10]);
             temp.setIntermediatePath(segFields[11]);
             temp.setStartX(Integer.valueOf(segFields[12]));
             temp.setStartY(Integer.valueOf(segFields[13]));
             temp.setEndX(Integer.valueOf(segFields[14]));
             temp.setEndY(Integer.valueOf(segFields[15]));
             temp.setWeight(Double.valueOf(segFields[16]));

             this.segmentBankContents.add(temp);


         }

         for (int x=1; x<routeItems.length; x++) {
             Segment temp = new Segment();

             String[] segFields = routeItems[x].split("woody");
             temp.setOriginalContent(segFields[1]);

             if (segFields[2].equals("true")) temp.setIsSelected(true);
             else temp.setIsSelected(false);

             if (segFields[3].equals("true")) temp.setIsAdded(true);
             else temp.setIsAdded(false);

             // temp.setNickName(segFields[4]);
             // temp.renameStart(segFields[5]);
             // temp.renameDestination(segFields[6]);
             // temp.setFileName(segFields[7]);
             // temp.setFilePath(segFields[8]);
             // temp.setStart(segFields[9]);
             // temp.setDestination(segFields[10]);
             // temp.setIntermediatePath(segFields[11]);
             // temp.setWeight(Double.valueOf(segFields[12]));
             // temp.setStartX(Integer.valueOf(segFields[13]));
             // temp.setStartY(Integer.valueOf(segFields[14]));
             // temp.setEndX(Integer.valueOf(segFields[15]));
             // temp.setEndY(Integer.valueOf(segFields[16]));

             temp.setNickName(segFields[4]);
             temp.renameStart(segFields[5]);
             temp.renameDestination(segFields[6]);
             temp.setFileName(segFields[7]);
             temp.setFilePath(segFields[8]);
             temp.setStart(segFields[9]);
             temp.setDestination(segFields[10]);
             temp.setIntermediatePath(segFields[11]);
             temp.setStartX(Integer.valueOf(segFields[12]));
             temp.setStartY(Integer.valueOf(segFields[13]));
             temp.setEndX(Integer.valueOf(segFields[14]));
             temp.setEndY(Integer.valueOf(segFields[15]));
             temp.setWeight(Double.valueOf(segFields[16]));

             this.routeContents.add(temp);

         }
         // SegmentBank loadedSegmentBank = new SegmentBank();
         // loadedSegmentBank.setSegmentBankContents(this.segmentBankContents);
         notifyListeners();

     // } catch (FileNotFoundException e) {
         // System.out.println("FileNotFoundException is thrown");
     // }
 }

 // public void undo(){
 //   this.routeContents = routeState.getPreviousRoute();
 //   notifyListeners();
 // }
 //
 // public void redo(){
 //   this.routeContents = routeState.getNewerRoute();
 //   notifyListeners();
 // }



}
