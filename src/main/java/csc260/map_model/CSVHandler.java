package csc260.map_model;
import java.util.*;
import java.io.*;

public class CSVHandler implements FileHandler {

  private FileHandler successor;

  public CSVHandler(){
    successor = new TXTHandler();
  }

  public boolean isHandleable(String fileType){
    return fileType.equals(".csv");
  }

  public String fileToString(File file) {
    String fileName = file.getName();
    int index = fileName.lastIndexOf(".");
    String fileType = fileName.substring(index);

    if (isHandleable(fileType)){
      String contents = "";
      try {
          Scanner scanner = new Scanner(file);
          while (scanner.hasNextLine())contents+= scanner.nextLine() + "\n";
      } catch (FileNotFoundException exception) {
          System.out.println("Invalid file");
      }
      return contents;
    } else {
      return successor.fileToString(file);
    }
  }

  public double getWeight(String fileContents) {
      double weight = 0;
      Scanner scanner = new Scanner(fileContents);
      //scanner.nextLine();
      while (scanner.hasNextLine()) {
          String temp = scanner.nextLine();
          if (!scanner.hasNext()){
              String[] contents = temp.split(",");
              weight += Double.valueOf(contents[2]);
          }
      }
      return weight;
  }


  public Segment fileToSegment (File file){
    String fileName = file.getName();
    int indexOfLastDot = fileName.lastIndexOf(".");
    String fileType = fileName.substring(indexOfLastDot);

    if(isHandleable(fileType)){
      String start = "";
      String destination = "";
      String intermediatePath = "";
      String originalContent = fileToString(file);
      double segmentWeight = getWeight(originalContent);
      // String originalContent = fileToString(file);

      String temp = fileName.replace(".csv","");
      Scanner scan = new Scanner(temp);
      scan.useDelimiter("-");
      int index = 0;
      while (scan.hasNext()){
          if (index == 0){
              start = scan.next();
          } else if (index == 1){
              destination = scan.next();
          } else {
              intermediatePath += scan.next();
              if (scan.hasNext()) intermediatePath += ",";
          }
          index++;
      }

      // Set segment attributes
      Segment segment = new Segment();
      segment.setIsSelected(false);
      segment.setFilePath(file.getPath());
      segment.setFileName(temp);
      segment.setStart(start);
      segment.setDestination(destination);
      segment.setIntermediatePath(intermediatePath);
      segment.setWeight(segmentWeight);
      segment.setOriginalContent(originalContent);
      return segment;
    }
    else{
      return successor.fileToSegment(file);
    }
  }

  public void stringToExportFile (String routeInString, String fileOutputPath, String fileName, String fileType) throws Exception{
    if(isHandleable(fileType)){
      String outputPath = fileOutputPath + "/" + fileName + fileType;
      try {
          FileWriter writer = new FileWriter(outputPath);
          writer.write(routeInString);
          writer.flush();
          writer.close();
      } catch (Exception e) {
          System.out.println("Invalid output");
      }
    }
    else{
      successor.stringToExportFile(routeInString,fileOutputPath,fileName,fileType);
    }
  }

}
