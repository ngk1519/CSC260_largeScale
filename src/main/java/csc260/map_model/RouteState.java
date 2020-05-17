package csc260.map_model;

import java.util.*;
import java.io.*;
import java.awt.*;

/**
* @author Woody Wu
* @version 03/07/2019
*/

public class RouteState {

  private ArrayList<Route> routeState;
  private int pointer;
  private int size;

  public RouteState(int undoLimit){
    this.routeState = new ArrayList<Route>();
    this.pointer = 0;
    this.size = undoLimit;
  }

  // public Route getCurrentRoute(){
  //   return this.routeState.get(pointer);
  // }

  /**
  * when the route object is modified, store the newest route
  * to RouteState, and set it as the current route
  */
  public void addRoute(Route modifiedRoute){
    int numStates = this.routeState.size();
    if (numStates == this.size){
      this.routeState.remove(0);
      System.out.println("Removed from routeState");
    }
    this.routeState.add(modifiedRoute);
    System.out.println("The route just got added to routeState is: " + modifiedRoute.printStartEnd());
    pointer = this.routeState.size() - 1;
    System.out.println("The pointer value is " + pointer);

    for (int i = 0; i < this.routeState.size(); i++){
      String eachSeg = this.routeState.get(i).printStartEnd();
      System.out.println("The " + i + "th Route is "+ eachSeg);
    }
  }
  public Route getPreviousRoute(){
    if(pointer <= 0){
      System.out.println("cannot undo because this is the oldest version.");
    }
    else{
      pointer--;
    }
    // return getCurrentRoute();
    // this.currentRoute = this.routeState.get(pointer);
    // notifyListeners();
    return this.routeState.get(pointer);
  }

  public Route getNewerRoute(){
    if(pointer >= this.routeState.size() - 1){
      System.out.println("cannot redo because this is the latest version.");
    }
    else{
      pointer++;
    }
    // return getCurrentRoute();
    // this.currentRoute = this.routeState.get(pointer);
    // notifyListeners();
    return this.routeState.get(pointer);
  }
}
