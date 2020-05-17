package csc260.map_model;
import java.util.*;
import java.io.*;

public interface FileHandler{
  
  public boolean isHandleable(String fileType);

  public String fileToString(File file);

  public double getWeight(String fileContents);

  public Segment fileToSegment (File file);

  public void stringToExportFile (String routeInString, String fileOutputPath, String fileName, String fileType) throws Exception;

}
