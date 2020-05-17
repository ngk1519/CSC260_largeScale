package csc260.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.Before;
import org.junit.After;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.*;
import java.util.*;
import csc260.map_model.CSVHandler;
import csc260.map_model.Route;
import csc260.map_model.Segment;
import csc260.map_model.SegmentBank;
import csc260.map_model.SegmentVertex;

@RunWith(JUnit4.class)
public class SegmentTest {

  private CSVHandler csvHandler;

  public Segment segmentFactory(String filePath){
    csvHandler = new CSVHandler();
    File csv = new File(filePath);
    Segment toReturn = csvHandler.fileToSegment(csv);
    return toReturn;
  }

  @Test
  public void testSegmentGetters(){
    Segment segment1 = segmentFactory("/home/wud2/Documents/csc260-w2019-project2/sample-inputs/Currybush+Skyline-MariavilleStore-DuanesburgChurches.csv");
    assertFalse("new segment is not selected", segment1.getIsSelected());
    assertFalse("new segment is not added", segment1.getIsAdded());
    assertEquals("new segment doesn't have a nick name","" , segment1.getNickName());
    assertEquals("segment should have a file name", "Currybush+Skyline-MariavilleStore-DuanesburgChurches", segment1.getFileName());
    assertEquals("segment should have a file path", "/home/wud2/Documents/csc260-w2019-project2/sample-inputs/Currybush+Skyline-MariavilleStore-DuanesburgChurches.csv", segment1.getFilePath());
    assertEquals("segment should have start Currybush+Skyline", "Currybush+Skyline", segment1.getStart());
    assertEquals("segment should have destination MariavilleStore", "MariavilleStore", segment1.getDestination());
    assertEquals("segmet should have weight 10.07", 10.07, segment1.getWeight(), 0.0);
    assertEquals("segment should have intermediate path DuanesburgChurches", "DuanesburgChurches", segment1.getIntermediatePath());
    assertEquals("segment should have initial start x position 0", 0, segment1.getStartX());
    assertEquals("segment should have the initial start y position 0", 0, segment1.getStartY());
    assertEquals("segment should have the initial destination x position 0", 0, segment1.getEndX());
    assertEquals("segment should have the initial destination y position 0", 0 , segment1.getEndY());
  }

  @Test
  public void testSegmentSetters(){
    Segment toAdd1 = segmentFactory("/Users/wujie/Documents/csc260-w2019-project2/sample-inputs/Currybush+Skyline-MariavilleStore-DuanesburgChurches.csv");
    Segment toAdd2 = segmentFactory("/Users/wujie/Documents/csc260-w2019-project2/sample-inputs/Currybush+Skyline-MariavilleStore-Herrick.csv");
  }

  @Test
  public void testMethodSameButDifferentPath(){
    Segment toAdd1 = segmentFactory("/Users/wujie/Documents/csc260-w2019-project2/sample-inputs/Currybush+Skyline-MariavilleStore-DuanesburgChurches.csv");
    Segment toAdd2 = segmentFactory("/Users/wujie/Documents/csc260-w2019-project2/sample-inputs/Currybush+Skyline-MariavilleStore-Herrick.csv");
    toAdd1.setIsSelected(true);
    toAdd2.setIsSelected(true);
    assertTrue("compare two segments with same start and destination but different path", toAdd1.sameButDifferentPath(toAdd2));
  }

  @Test
  public void testCloneMethod(){
    Segment segment = segmentFactory("/Users/wujie/Documents/csc260-w2019-project2/sample-inputs/Schermerhorn+Putnam-Currybush+Skyline-steep-route.csv");
    Segment copy = segment.clone();
    assertTrue("the copy should equal to the original segment", segment.isSegEqual(copy));
    copy.setNickName("copy");
    assertEquals("the rename of copy doesn't affect the original name", "", segment.getNickName());
  }
}
