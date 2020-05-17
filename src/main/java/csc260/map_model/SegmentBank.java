package csc260.map_model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.*;
import csc260.map_model.Listener;
import csc260.map_model.Segment;



public class SegmentBank {

    FileHandler csvHandler = new CSVHandler();

    private ArrayList<Segment> segmentBankContents;
    private Vector<Listener> listeners;

    public SegmentBank(){
        this.segmentBankContents = new ArrayList<>();
        listeners = new Vector<Listener>();
    }

    public void setSegmentBankContents(ArrayList<Segment> segmentBankContents) {
      this.segmentBankContents = segmentBankContents;
    }

    public ArrayList<Segment> getSegmentBankContents () { return this.segmentBankContents; }

    public void addSegmentsToBank(File folderPath) throws FileNotFoundException {
        String[] csvFiles = folderPath.list();
        for (String singleFilName : csvFiles) {
            if (singleFilName.contains(".csv")) {
                String csvPath = folderPath + "/" + singleFilName;
                File newCSVFile = new File(csvPath);
                Segment newSegment = csvHandler.fileToSegment(newCSVFile);
                this.segmentBankContents.add(newSegment);
            }
        }
    }

    public void clearBank(){
      for (Segment segment: this.segmentBankContents) {
          segment.setIsAdded(false);
          segment.setIsSelected(false);
      }
      this.segmentBankContents.clear();
      notifyListeners();
    }

    public void addSegmentToBank(Segment segment) {
     if (!this.segmentBankContents.contains(segment)) {
         this.segmentBankContents.add(segment);
     }
   }

    public void removeSegmentFromBank(Segment segment) {
        if (this.segmentBankContents.contains(segment)) {
            this.segmentBankContents.remove(segment);
        }
    }

    public boolean containsSegment(Segment segment){
      return segmentBankContents.contains(segment);
    }

    public int numSegmentsInBank(){
      return segmentBankContents.size();
    }

    public void renameSegment(Segment segment, String nicknameStart, String nicknamePath, String nicknameEnd){
      SegmentVertex start = segment.getStartVer();
      SegmentVertex end = segment.getDestinationVer();
      Segment currentSegment = new Segment();
      // for(int i = 0; i < numSegmentsInBank(); i++){
      //   currentSegment = this.segmentBankContents.get(i);
      //   if(currentSegment.isSegEqual(segment)){
      //     currentSegment.setNickName(nicknamePath);
      //   }
      //   if(currentSegment.getStartVer().isSameName(start)){
      //     currentSegment.renameStart(nicknameStart);
      //   }
      //   if(currentSegment.getStartVer().isSameName(end)){
      //     currentSegment.renameStart(nicknameEnd);
      //   }
      //   if(currentSegment.getDestinationVer().isSameName(start)){
      //     currentSegment.renameDestination(nicknameStart);
      //   }
      //   if(currentSegment.getDestinationVer().isSameName(end)){
      //     currentSegment.renameDestination(nicknameEnd);
      //   }
      // }

      if (this.containsSegment(segment)){
        boolean foundSegment = false;
        int counter = 0;
        while (!foundSegment && counter < this.numSegmentsInBank()){
          currentSegment = this.segmentBankContents.get(counter);
          if (currentSegment.isSegEqual(segment)){
            foundSegment = true;
            currentSegment.setNickName(nicknamePath);
            if(currentSegment.getStartVer().isSameName(start)){
              currentSegment.renameStart(nicknameStart);
            }
            if(currentSegment.getStartVer().isSameName(end)){
              currentSegment.renameStart(nicknameEnd);
            }
            if(currentSegment.getDestinationVer().isSameName(start)){
              currentSegment.renameDestination(nicknameStart);
            }
            if(currentSegment.getDestinationVer().isSameName(end)){
              currentSegment.renameDestination(nicknameEnd);
            }
            this.addSegmentToBank(currentSegment);
          }
          counter++;
        }

      }
      notifyListeners();
    }


    public void addListener(Listener l)
    {
        listeners.add(l);
    }

    public void removeListener(Listener l)
    {
        listeners.remove(l);
    }

    private void notifyListeners()
    {
        for (Listener l : listeners) {
            l.update();
        }
    }
}
