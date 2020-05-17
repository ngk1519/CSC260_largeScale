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
public class RouteTest {

  private Route r;
  private SegmentBank bank;
  private CSVHandler csvHandler;

  public Segment segmentFactory(String filePath){
    csvHandler = new CSVHandler();
    File csv = new File(filePath);
    Segment toReturn = csvHandler.fileToSegment(csv);
    return toReturn;
  }

  private void setUpSegBank(SegmentBank bank){
    Segment toAdd1 = segmentFactory("/home/wud2/Documents/csc260-w2019-project2/sample-inputs/Currybush+Skyline-MariavilleStore-DuanesburgChurches.csv");
    Segment toAdd2 = segmentFactory("/home/wud2/Documents/csc260-w2019-project2/sample-inputs/Currybush+Skyline-MariavilleStore-Herrick.csv");
    Segment toAdd3 = segmentFactory("/home/wud2/Documents/csc260-w2019-project2/sample-inputs/Currybush+Skyline-MariavilleStore-Weast.csv");
    Segment toAdd4 = segmentFactory("/home/wud2/Documents/csc260-w2019-project2/sample-inputs/SCCC-Schermerhorn+Putnam.csv");
    Segment toAdd5 = segmentFactory("/home/wud2/Documents/csc260-w2019-project2/sample-inputs/Schermerhorn+Putnam-Currybush+Skyline-steep-route.csv");
    bank.addSegmentToBank(toAdd1);
    bank.addSegmentToBank(toAdd2);
    bank.addSegmentToBank(toAdd3);
    bank.addSegmentToBank(toAdd4);
    bank.addSegmentToBank(toAdd5);
  }

  @Before
  public void setUp(){
    bank = new SegmentBank();
    setUpSegBank(bank);
    r = new Route(bank);
  }

  @Test
  public void testAddSegmentToRoute(){
    ArrayList<Segment> bankContents = bank.getSegmentBankContents();
    Segment toAdd = bankContents.get(0);
    toAdd.setIsSelected(true);
    r.addSegmentToRoute(toAdd);
    assertEquals("Add a segment increase the route's size to 1", 1, r.numSegmentsInRoute());
    assertEquals("Route should contain the added segment", toAdd, r.getFirstSegment());
  }

  @Test
  public void testAddSegment_complexCase_addInOrder(){
    ArrayList<Segment> contentsArray = bank.getSegmentBankContents();
    Segment CurrybushToMariavilleStoreThroughDuanesburgChurches = contentsArray.get(0);
    Segment CurrybushToMariavilleStoreThroughHerrick = contentsArray.get(1);
    Segment CurrybushToMariavilleStoreThroughWeast = contentsArray.get(2);
    Segment SCCCToSchermerhorn = contentsArray.get(3);
    Segment SchermerhornToCurrybushThroughSkyline = contentsArray.get(4);
    Segment notImported = segmentFactory("/home/wud2/Documents/csc260-w2019-project2/sample-inputs/Schermerhorn+Putnam-Currybush+Skyline-MariavilleRd.csv");

    CurrybushToMariavilleStoreThroughDuanesburgChurches.setIsSelected(true);
    CurrybushToMariavilleStoreThroughHerrick.setIsSelected(true);
    CurrybushToMariavilleStoreThroughWeast.setIsSelected(true);
    SCCCToSchermerhorn.setIsSelected(true);
    SchermerhornToCurrybushThroughSkyline.setIsSelected(true);
    notImported.setIsSelected(true);

    r.addSegmentToRoute(notImported);
    assertEquals("Adding a segment that doesn't exist in the bank is invalid", 0, r.numSegmentsInRoute());

    r.addSegmentToRoute(SCCCToSchermerhorn);
    r.addSegmentToRoute(CurrybushToMariavilleStoreThroughDuanesburgChurches);
    assertEquals("Number of segments in the route now should still be 1", 1, r.numSegmentsInRoute());

    r.addSegmentToRoute(SchermerhornToCurrybushThroughSkyline);
    assertEquals("Number of segments in the route should be 2", 2, r.numSegmentsInRoute());
    assertEquals("The segment should be added at the end of the route", SchermerhornToCurrybushThroughSkyline, r.getLastSegment());
    assertEquals("The start of the route should be SCCC", "SCCC", r.getFirstSegment().getStart());
    assertEquals("The end of the route should be Currybush+SkyLine", "Currybush+Skyline", r.getLastSegment().getDestination());

    r.addSegmentToRoute(CurrybushToMariavilleStoreThroughWeast);
    assertTrue("The added segment should marked 'added'", CurrybushToMariavilleStoreThroughWeast.getIsAdded());
    assertEquals("Number of segments in the route should be 3", 3, r.numSegmentsInRoute());
    assertEquals("The segment should be added at the end of the route", CurrybushToMariavilleStoreThroughWeast, r.getLastSegment());
    assertEquals("The end of the route now should be MariavilleStore", "MariavilleStore", r.getLastSegment().getDestination());
    assertEquals("The intermediate path of the last segment should be Weast", "Weast", r.getLastSegment().getIntermediatePath());

    r.addSegmentToRoute(CurrybushToMariavilleStoreThroughHerrick);
    assertFalse("The replaced segment shouldn't marked 'added' anymore", CurrybushToMariavilleStoreThroughWeast.getIsAdded());
    assertTrue("The current segment should mared 'added'", CurrybushToMariavilleStoreThroughHerrick.getIsAdded());
    assertEquals("Number of segments in the route should still be 3 because current add replaces a segment in the route", 3, r.numSegmentsInRoute());
    assertEquals("The intermediate path of the last segment should be Herrick", "Herrick", r.getLastSegment().getIntermediatePath());
  }

  @Test
  public void testAddSegment_complexCase_addInReverseOrder(){
    ArrayList<Segment> contentsArray = bank.getSegmentBankContents();
    Segment CurrybushToMariavilleStoreThroughDuanesburgChurches = contentsArray.get(0);
    Segment CurrybushToMariavilleStoreThroughHerrick = contentsArray.get(1);
    Segment CurrybushToMariavilleStoreThroughWeast = contentsArray.get(2);
    Segment SCCCToSchermerhorn = contentsArray.get(3);
    Segment SchermerhornToCurrybushThroughSkyline = contentsArray.get(4);

    CurrybushToMariavilleStoreThroughDuanesburgChurches.setIsSelected(true);
    CurrybushToMariavilleStoreThroughHerrick.setIsSelected(true);
    CurrybushToMariavilleStoreThroughWeast.setIsSelected(true);
    SCCCToSchermerhorn.setIsSelected(true);
    SchermerhornToCurrybushThroughSkyline.setIsSelected(true);

    r.addSegmentToRoute(SchermerhornToCurrybushThroughSkyline);
    assertTrue("The added segment should marked 'added'", SchermerhornToCurrybushThroughSkyline.getIsAdded());
    assertEquals("Number of segments in the route should be 1", 1, r.numSegmentsInRoute());

    r.addSegmentToRoute(SchermerhornToCurrybushThroughSkyline);
    assertEquals("Adding an added segment changes nothing", 1, r.numSegmentsInRoute());

    r.addSegmentToRoute(SCCCToSchermerhorn);
    assertEquals("Number of segments in the route should be 2", 2, r.numSegmentsInRoute());
    assertEquals("The segment should be added at the start of the route", SCCCToSchermerhorn, r.getFirstSegment());
    assertEquals("The start of the route should be SCCC", "SCCC", r.getFirstSegment().getStart());
    assertEquals("The end of the route should be Currybush+SkyLine", "Currybush+Skyline", r.getLastSegment().getDestination());
  }

  @Test
  public void testRemoveSegment(){
    try{
      ArrayList<Segment> contentsArray = bank.getSegmentBankContents();
      Segment CurrybushToMariavilleStoreThroughDuanesburgChurches = contentsArray.get(0);
      Segment CurrybushToMariavilleStoreThroughHerrick = contentsArray.get(1);
      Segment CurrybushToMariavilleStoreThroughWeast = contentsArray.get(2);
      Segment SCCCToSchermerhorn = contentsArray.get(3);
      Segment SchermerhornToCurrybushThroughSkyline = contentsArray.get(4);

      CurrybushToMariavilleStoreThroughDuanesburgChurches.setIsSelected(true);
      CurrybushToMariavilleStoreThroughHerrick.setIsSelected(true);
      CurrybushToMariavilleStoreThroughWeast.setIsSelected(true);
      SCCCToSchermerhorn.setIsSelected(true);
      SchermerhornToCurrybushThroughSkyline.setIsSelected(true);

      r.removeSegment(CurrybushToMariavilleStoreThroughWeast);
      assertEquals("Remove a segment from an empty bank does nothing.", 0, r.numSegmentsInRoute());

      r.addSegmentToRoute(CurrybushToMariavilleStoreThroughDuanesburgChurches);
      CurrybushToMariavilleStoreThroughDuanesburgChurches.setIsSelected(false);
      r.removeSegment(CurrybushToMariavilleStoreThroughDuanesburgChurches);
      assertEquals("Removing a segment that is not selected changes nothing.", 1, r.numSegmentsInRoute());

      r.addSegmentToRoute(SchermerhornToCurrybushThroughSkyline);
      r.addSegmentToRoute(SCCCToSchermerhorn);
      r.removeSegment(SchermerhornToCurrybushThroughSkyline);
      assertEquals("Removing a intermediate segment of the route changes nothing.", 3, r.numSegmentsInRoute());

      CurrybushToMariavilleStoreThroughDuanesburgChurches.setIsSelected(true);
      r.removeSegment(CurrybushToMariavilleStoreThroughDuanesburgChurches);
      assertEquals("Test removing the last segment.", 2, r.numSegmentsInRoute());
      assertEquals("Now the last segment should be Schermerhorn to Currybush.", SchermerhornToCurrybushThroughSkyline, r.getLastSegment());
    }
    catch(IllegalStateException e){

    }
  }

  @Test
  public void testGetPositionToAdd(){
    ArrayList<Segment> contentsArray = bank.getSegmentBankContents();
    Segment CurrybushToMariavilleStoreThroughDuanesburgChurches = contentsArray.get(0);
    Segment CurrybushToMariavilleStoreThroughHerrick = contentsArray.get(1);
    Segment CurrybushToMariavilleStoreThroughWeast = contentsArray.get(2);
    Segment SCCCToSchermerhorn = contentsArray.get(3);
    Segment SchermerhornToCurrybushThroughSkyline = contentsArray.get(4);

    CurrybushToMariavilleStoreThroughHerrick.setIsSelected(true);
    CurrybushToMariavilleStoreThroughWeast.setIsSelected(true);
    SCCCToSchermerhorn.setIsSelected(true);
    SchermerhornToCurrybushThroughSkyline.setIsSelected(true);

    assertEquals("The returning index should be 0 if route is empty.", 0, r.getPositionToAdd(SchermerhornToCurrybushThroughSkyline));
    assertEquals("The returning index should be 0 if route is empty.", 0, r.getPositionToAdd(SCCCToSchermerhorn));
    assertEquals("The returning index should be 0 if route is empty.", 0, r.getPositionToAdd(CurrybushToMariavilleStoreThroughWeast));

    r.addSegmentToRoute(SCCCToSchermerhorn);
    assertEquals("Index to add SchermerhornToCurrybushThroughSkyline should be 1.", 1, r.getPositionToAdd(SchermerhornToCurrybushThroughSkyline));
    assertEquals("Invalid add should return -2", -2, r.getPositionToAdd(CurrybushToMariavilleStoreThroughHerrick));

    r.removeSegment(SCCCToSchermerhorn);
    r.addSegmentToRoute(SchermerhornToCurrybushThroughSkyline);
    assertEquals("Index to add SCCCToSchermerhorn should be -1.", -1, r.getPositionToAdd(SCCCToSchermerhorn));

    r.addSegmentToRoute(CurrybushToMariavilleStoreThroughHerrick);
    assertEquals("Adding another segment from Currybush to MariavilleStore should replace the orignal one, therefore return 1", 1, r.getPositionToAdd(CurrybushToMariavilleStoreThroughWeast));
  }

  @Test
  public void testClearRoute(){
    ArrayList<Segment> contentsArray = bank.getSegmentBankContents();
    Segment CurrybushToMariavilleStoreThroughDuanesburgChurches = contentsArray.get(0);
    Segment CurrybushToMariavilleStoreThroughHerrick = contentsArray.get(1);
    Segment CurrybushToMariavilleStoreThroughWeast = contentsArray.get(2);
    Segment SCCCToSchermerhorn = contentsArray.get(3);
    Segment SchermerhornToCurrybushThroughSkyline = contentsArray.get(4);

    CurrybushToMariavilleStoreThroughDuanesburgChurches.setIsSelected(true);
    CurrybushToMariavilleStoreThroughHerrick.setIsSelected(true);
    CurrybushToMariavilleStoreThroughWeast.setIsSelected(true);
    SCCCToSchermerhorn.setIsSelected(true);
    SchermerhornToCurrybushThroughSkyline.setIsSelected(true);

    r.addSegmentToRoute(CurrybushToMariavilleStoreThroughDuanesburgChurches);
    r.addSegmentToRoute(SchermerhornToCurrybushThroughSkyline);
    r.addSegmentToRoute(SCCCToSchermerhorn);

    r.clearRoute();

    assertEquals("route should be empty after cleaned.", 0, r.numSegmentsInRoute());
    assertFalse("all segments in the segmentbank should be not selected or added", CurrybushToMariavilleStoreThroughDuanesburgChurches.getIsSelected());
    assertFalse("all segments in the segmentbank should be not selected or added", CurrybushToMariavilleStoreThroughDuanesburgChurches.getIsAdded());
    assertFalse("all segments in the segmentbank should be not selected or added", SCCCToSchermerhorn.getIsSelected());
    assertFalse("all segments in the segmentbank should be not selected or added", SCCCToSchermerhorn.getIsAdded());
  }

  @Test
  public void testBuildRouteString(){
    ArrayList<Segment> contentsArray = bank.getSegmentBankContents();
    Segment CurrybushToMariavilleStoreThroughDuanesburgChurches = contentsArray.get(0);
    Segment CurrybushToMariavilleStoreThroughHerrick = contentsArray.get(1);
    Segment CurrybushToMariavilleStoreThroughWeast = contentsArray.get(2);
    Segment SCCCToSchermerhorn = contentsArray.get(3);
    Segment SchermerhornToCurrybushThroughSkyline = contentsArray.get(4);

    CurrybushToMariavilleStoreThroughDuanesburgChurches.setIsSelected(true);
    CurrybushToMariavilleStoreThroughHerrick.setIsSelected(true);
    CurrybushToMariavilleStoreThroughWeast.setIsSelected(true);
    SCCCToSchermerhorn.setIsSelected(true);
    SchermerhornToCurrybushThroughSkyline.setIsSelected(true);

    r.addSegmentToRoute(CurrybushToMariavilleStoreThroughDuanesburgChurches);
    String expectedCurrentString = CurrybushToMariavilleStoreThroughDuanesburgChurches.getOriginalContent();
    assertEquals("Testing string output of one segment route.", expectedCurrentString, r.buildRouteString());

    r.addSegmentToRoute(SchermerhornToCurrybushThroughSkyline);
    expectedCurrentString =
    "Type,Notes,Distance (miles) From Start\n"
    + "Start,Start of route,0\n"
    + "Right,Turn right onto Putnam Rd,0\n"
    + "Left,Turn left onto Currybush Connection,3.76\n"
    + "Right,Turn right onto Currybush Rd,4.47\n"
    + "Right,Turn right onto N Kelley Rd,5.05\n"
    + "Left,Turn left onto Skyline Dr,5.06\n"
    + "Right,Turn right onto Duanesburg Churches Rd,9.50\n"
    + "Right,Turn right onto Batter St,12.43\n"
    + "End,End of route,15.13\n";
    assertEquals("Testing string output of two segments route.", expectedCurrentString, r.buildRouteString());

    r.addSegmentToRoute(SCCCToSchermerhorn);
    expectedCurrentString =
    "Type,Notes,Distance (miles) From Start\n"
    + "Start,Start of route,0\n"
    + "Right,Turn right to stay on Mohawk-Hudson Bike-Hike Trail/Mohawk Hudson Bikeway,1.44\n"
    + "Left,Turn left toward Rice Rd,2.60\n"
    + "Left,Turn left onto Rice Rd,2.62\n"
    + "Right,Turn right onto Schermerhorn Rd,3.15\n"
    + "Right,Turn right onto Putnam Rd,4.13\n"
    + "Left,Turn left onto Currybush Connection,7.89\n"
    + "Right,Turn right onto Currybush Rd,8.60\n"
    + "Right,Turn right onto N Kelley Rd,9.18\n"
    + "Left,Turn left onto Skyline Dr,9.19\n"
    + "Right,Turn right onto Duanesburg Churches Rd,13.63\n"
    + "Right,Turn right onto Batter St,16.56\n"
    + "End,End of route,19.26\n";
    assertEquals("Testing string output of three segments route.", expectedCurrentString, r.buildRouteString());
  }

  @Test
  public void testAbbreviatedString(){
    ArrayList<Segment> contentsArray = bank.getSegmentBankContents();
    Segment CurrybushToMariavilleStoreThroughDuanesburgChurches = contentsArray.get(0);
    Segment CurrybushToMariavilleStoreThroughHerrick = contentsArray.get(1);
    Segment CurrybushToMariavilleStoreThroughWeast = contentsArray.get(2);
    Segment SCCCToSchermerhorn = contentsArray.get(3);
    Segment SchermerhornToCurrybushThroughSkyline = contentsArray.get(4);

    CurrybushToMariavilleStoreThroughDuanesburgChurches.setIsSelected(true);
    CurrybushToMariavilleStoreThroughHerrick.setIsSelected(true);
    CurrybushToMariavilleStoreThroughWeast.setIsSelected(true);
    SCCCToSchermerhorn.setIsSelected(true);
    SchermerhornToCurrybushThroughSkyline.setIsSelected(true);

    r.addSegmentToRoute(CurrybushToMariavilleStoreThroughDuanesburgChurches);

    String expectedCurrentString =
    "Type,Notes,Distance (miles) From Start\n"
    + "Start,Start of route,0\n"
    + "Left,L:Skyline Dr,0\n"
    + "Right,R:Duanesburg Churches Rd,4.44\n"
    + "Right,R:Batter St,7.37\n"
    + "End,End of route,10.07\n";
    assertEquals("Testing abbreviate string on single segment route.", expectedCurrentString, r.buildAbbreviatedRouteString());
  }
}
