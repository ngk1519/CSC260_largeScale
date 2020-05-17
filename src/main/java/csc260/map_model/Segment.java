package csc260.map_model;

import java.util.*;

public class Segment{

    private String originalContent;
    private boolean isSelected;
    private boolean isAdded;
    private String nickName;
    private String startNickname;
    private String destinationNickname;
    private String fileName;
    private String filePath;
    private SegmentVertex start;
    private SegmentVertex destination;
    private String intermediatePath;
    private double weight;

    public Segment(){
      this.originalContent = "";
      this.isSelected = false;
      this.isAdded = false;
      this.nickName = "";
      this.startNickname = "";
      this.destinationNickname = "";
      this.filePath = "";
      this.fileName  = "";
      this.start = new SegmentVertex();
      this.destination = new SegmentVertex();
      this.intermediatePath = "";
      this.weight = 0;
    }

    public Segment (String originalContent, boolean isSelected, boolean isAdded,
    String nickName, String filePath, String fileName, String start,
    String destination, String intermediatePath, double weight,
    int startX, int startY, int endX, int endY, String startNickname, String endNickname){
        this.originalContent = originalContent;
        this.isSelected = isSelected;
        this.isAdded = isAdded;
        this.nickName = nickName;
        this.startNickname = startNickname;
        this.destinationNickname = destinationNickname;
        this.filePath = filePath;
        this.fileName  = fileName;
        this.start = new SegmentVertex(startX, startY, start);
        this.destination = new SegmentVertex(endX, endY, destination);
        this.intermediatePath = intermediatePath;
        this.weight = weight;
    }

    // All getter methods
    public String getOriginalContent() { return this.originalContent; }
    public boolean getIsSelected() { return  this.isSelected; }
    public boolean getIsAdded() {return this.isAdded;}
    public String getNickName() { return this.nickName; }
    public String getFileName(){ return this.fileName; }
    public String getFilePath(){ return this.filePath; }
    public String getStart(){ return this.start.getName(); }
    public String getDestination(){ return this.destination.getName(); }
    public String getStartNickname() {return this.startNickname;}
    public String getDestinationNickname() {return this.destinationNickname;}
    public double getWeight(){ return this.weight; }
    public String getIntermediatePath(){ return this.intermediatePath; }
    public int getStartX() {return this.start.getX(); }
    public int getStartY() {return this.start.getY(); }
    public int getEndX() { return this.destination.getX(); }
    public int getEndY() { return this.destination.getY(); }
    public SegmentVertex getStartVer() { return this.start;}
    public SegmentVertex getDestinationVer() { return this.destination;}

    // All setter methods
    public void setOriginalContent(String originalContent) { this.originalContent = originalContent; }
    public void setIsSelected(boolean state) { this.isSelected = state; }
    public void setIsAdded(boolean state) {this.isAdded = state; }
    public void setNickName(String nickName) {this.nickName = nickName; }
    public void renameStart(String nicknameStart) {this.startNickname = nicknameStart;}
    public void renameDestination(String nicknameEnd) {this.destinationNickname = nicknameEnd;}
    public void setFileName(String fileName) { this.fileName = fileName; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public void setStart(String start) { this.start.renameVertex(start); }
    public void setDestination(String destination) { this.destination.renameVertex(destination); }
    public void setIntermediatePath(String intermediatePath) { this.intermediatePath = intermediatePath; }
    public void setWeight (double weight) { this.weight = weight; }
    public void setStartX(int x) {this.start.setXPosition(x); }
    public void setStartY(int y) {this.start.setYPosition(y); }
    public void setEndX(int x) { this.destination.setXPosition(x); }
    public void setEndY(int y) { this.destination.setYPosition(y); }

    public void setStartVer(SegmentVertex start) { this.start = start; }
    public void setDestinationVer(SegmentVertex destination){ this.destination = destination; }

    public boolean isSegEqual(Segment segment) {
      return (this.originalContent.equals(segment.getOriginalContent()));
    }

    public boolean sameButDifferentPath(Segment segment){
      if(!getIntermediatePath().equals(segment.getIntermediatePath())){
        System.out.println(true);
        return getStart().equals(segment.getStart()) && getDestination().equals(segment.getDestination());
      }
      else{
        System.out.println(false);
        return false;
      }
    }

    /**
    * @param toCopy the segment to copy
    * @return a copy segment of the segment
    */
    public Segment clone(){

      String originalContent = this.getOriginalContent();
      boolean isSelected = this.getIsSelected();
      boolean isAdded = this.getIsAdded();
      String nickName = this.getNickName();
      String fileName = this.getFileName();
      String filePath = this.getFilePath();
      String start = this.getStart();
      String destination = this.getDestination();
      String intermediatePath = this.getIntermediatePath();
      double weight = this.getWeight();
      int startX = this.getStartX();
      int startY = this.getStartY();
      int endX = this.getStartX();
      int endY = this.getStartY();
      String startNickname = this.getStartNickname();
      String endNickname = this.getDestinationNickname();

      return new Segment(originalContent, isSelected, isAdded, nickName,
      filePath, fileName, start, destination, intermediatePath, weight,
      startX, startY, endX, endY, startNickname,endNickname);
    }

    public String segToSaveString() {
      String kevin, woody, toBuild;
      kevin = "kevin";
      woody = "woody";
      toBuild = "";
      toBuild += kevin + woody + this.getOriginalContent();
      System.out.println(toBuild);
      toBuild += woody + this.getIsSelected();
      toBuild += woody + this.getIsAdded() ;
      toBuild += woody + this.getNickName();
      toBuild += woody + this.getStartNickname();
      toBuild += woody + this.getDestinationNickname();
      toBuild += woody + this.getFileName();
      toBuild += woody + this.getFilePath();
      toBuild += woody + this.getStart();
      toBuild += woody + this.getDestination();
      toBuild += woody + this.getIntermediatePath();
      toBuild += woody + this.getStartX();
      toBuild += woody + this.getStartY();
      toBuild += woody + this.getEndX();
      toBuild += woody + this.getEndY();
      toBuild += woody + this.getWeight();
      return toBuild;
    }
}
