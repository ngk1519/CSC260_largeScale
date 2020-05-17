package csc260.map_model;


import java.util.*;
import java.awt.*;

public class SegmentVertex{

  private int x;
  private int y;
  private String name;
  private String nickName;
  private Color color;

  public SegmentVertex(){
    this.x = 0;
    this.y = 0;
    this.name = "";
    this.nickName = "";
    color = Color.black;
  }

  public SegmentVertex(int xPos, int yPos, String name){
    this.x = xPos;
    this.y = yPos;
    this.name = name;
    this.nickName = "";
    color = Color.black;
  }

  //getters
  public int getX(){
    return this.x;
  }
  public int getY(){
    return this.y;
  }
  public String getName() { return this.name; }

  public String getNickName(){ return this.nickName; }

  public Color getColor(){ return this.color;}

  //setters
  public void setXPosition(int x) {
    this.x = x;
  }
  public void setYPosition(int y) {
    this.y = y;
  }

  public void setNickName(String nickName){
    this.nickName = nickName;
  }

  public void renameVertex(String newName) {
    this.name = newName;
  }

  public void setColor(Color newColor){
    color = newColor;
  }

  public boolean vertEquals(SegmentVertex vert){
    boolean result = false;
    if (vert.getX() == this.x && vert.getY() == this.y && vert.getName().equals(this.name)){
      result = true;
    }
    return result;
  }

  public boolean isSameName(SegmentVertex vert){
    return this.name.equals(vert.getName());
  }
}
