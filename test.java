// --== CS400 File Header Information ==--
// Name: David Jiang
// Email: djiang38@wisc.edu
// Notes to Grader: <optional extra notes>

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Testing method
 * 
 * @author davidjiang
 *
 */
class test {
  public static ArrayList<String> airport; // List of test data for use
  public static ArrayList<String[]> flight;

  /**
   * Setups our lists to be used for our testing
   */
  public static void Setup() {
    FileInputStream airportBye = null;
    try {
      airportBye = new FileInputStream("airportTestData");
    } catch (FileNotFoundException e) {
    }
    Scanner file = new Scanner(airportBye);
    FileInputStream flightBye = null;
    try {
      flightBye = new FileInputStream("flightTestData");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    Scanner file2 = new Scanner(flightBye);
    airport = new ArrayList<String>();
    while (file.hasNext()) {
      airport.add(file.nextLine());
    }
    flight = new ArrayList<String[]>();
    String[] info = new String[4];
    while (file2.hasNext()) {
      info = new String[4];
      for (int i = 0; i < 4; i++) {
        info[i] = file2.next();
      }
      flight.add(info);
    }
    file.close();
    file2.close();
  }

  @Test
  /**
   * Tests the addition and removal of a singular airport into the graph.
   */
  public void testSingleAirportInsertRemove() {
    Graph testGraph = new Graph();
    Assertions.assertTrue(testGraph.insertAirport("ORD"));
    String expect = "ORD";
    Assertions.assertEquals(expect, testGraph.toString());
    Assertions.assertTrue(testGraph.containsAirport(expect));
    // Tests if containsAirport isn't just giving a false positive.
    Assertions.assertFalse(testGraph.containsAirport("CAT"));
    // Tests airport correct removal
    Assertions.assertTrue(testGraph.removeAirport("ORD"));
    // Tests null/blank removal
    try {
      testGraph.removeAirport("");
      Assertions.assertFalse(true);
    } catch (NullPointerException e) {
      Assertions.assertTrue(true);
    } catch (Exception e) {
      Assertions.assertFalse(true);
    }
    // Tests invalid removal
    Assertions.assertFalse(testGraph.removeAirport("CAT"));
    // Tests null insertion
    Assertions.assertThrows(NullPointerException.class, () -> testGraph.insertAirport(""));
  }

  /**
   * Tests the addition and removal of multiple airports.
   */
  @Test
  public void testMultipleAirportInsertRemove() {
    Setup();
    Graph testGraph = new Graph();
    // Expected order is different from the order we add because of the hashmap resizing algorithm.
    String expected =
        "POG, KON, MRA, RNA, CAT, PRA, MSN, ORD, MSP, MAN, PRE, AUG, SSR, LAX, TRI, MET, WME, RAX, PNW, TRO, MCI, WAU, DOG, MKE, DNA, LAC, DTW";
    for (String i : airport) {
      Assertions.assertTrue(testGraph.insertAirport(i));
    }
    Assertions.assertEquals(expected, testGraph.toString());
    for (String i : airport) {
      Assertions.assertTrue(testGraph.containsAirport(i));
    }
    // Tests invalid airport lookup
    Assertions.assertFalse(testGraph.containsAirport("fake"));
    // Tests removal of multiple airports
    for (String i : airport) {
      Assertions.assertTrue(testGraph.removeAirport(i));
    }
  }

  /**
   * Tests the addition and removal of a singular edge between 2 airports.
   */
  @Test
  public void testSingleEdgeInsertRemove() {
    Graph testGraph = new Graph();
    testGraph.insertAirport("ORD");
    testGraph.insertAirport("MSN");
    Assertions.assertTrue(testGraph.insertFlight("ORD", "MSN", 45, 185));
    Assertions.assertTrue(testGraph.containsFlight("ORD", "MSN"));
    // Tests for directed graph
    Assertions.assertFalse(testGraph.containsFlight("MSN", "ORD"));
    Assertions.assertFalse(testGraph.containsFlight("MSP", "POW"));
    // Tests wrong input flight insertion
    Assertions.assertFalse(testGraph.insertFlight("MSP", "MSN", 109, 382));
    // Tests single edge removal
    Assertions.assertTrue(testGraph.removeFlight("ORD", "MSN"));
    // Tests invalid single edge removal
    Assertions.assertFalse(testGraph.removeFlight("MSN", "ORD"));
  }

  /**
   * Tests the addition and removal of multiple edges between multiple airports.
   */
  @Test
  public void testMultipleEdgeInsertRemove() {
    Setup();
    Graph testGraph = new Graph();
    for (String i : airport) {
      testGraph.insertAirport(i);
    }
    // Tests the insertion of multiple edges
    for (String[] i : flight) {
      Assertions.assertTrue(testGraph.insertFlight(i[0].trim(), i[1].trim(),
          Integer.valueOf(i[2].trim()), Integer.valueOf(i[3].trim())));
    }
    // Tests for multiple edge lookup
    for (String[] i : flight) {
      Assertions.assertTrue(testGraph.containsFlight(i[0], i[1]));
    }
    // Tests for removal of multiple edges
    for (String[] i : flight) {
      Assertions.assertTrue(testGraph.removeFlight(i[0], i[1]));
    }
  }

  @Test
  public void testSmallGraphTraversal() {
    Graph testGraph = new Graph();
    testGraph.insertAirport("ORD");
    testGraph.insertAirport("MSN");
    testGraph.insertAirport("DTW");
    testGraph.insertFlight("ORD", "MSN", 45, 180);
    testGraph.insertFlight("ORD", "DTW", 60, 200);
    testGraph.insertFlight("MSN", "DTW", 14, 21);
    ArrayList<String> expected = new ArrayList<String>();
    expected.add("ORD");
    expected.add("MSN");
    Assertions.assertEquals(testGraph.shortestPath("ORD", "MSN", "time"), expected);
    Assertions.assertEquals(testGraph.shortestPath("ORD", "MSN", "cost"), expected);
    Assertions.assertEquals(testGraph.getPathWeight("ORD", "MSN", "cost"), 180);
    Assertions.assertEquals(testGraph.getPathWeight("ORD", "MSN", "time"), 45);

    expected.add("DTW");
    Assertions.assertEquals(testGraph.shortestPath("ORD", "DTW", "time"), expected);
    expected.remove("MSN");
    Assertions.assertEquals(testGraph.shortestPath("ORD", "DTW", "cost"), expected);
  }

  @Test
  public void testLargeGraphTraversal() {
    Setup();
    Graph testGraph = new Graph();
    for (String i : airport) {
      testGraph.insertAirport(i);
    }
    for (String[] i : flight) {
      testGraph.insertFlight(i[0].trim(), i[1].trim(), Integer.valueOf(i[2].trim()),
          Integer.valueOf(i[3].trim()));
    }
    ArrayList<String> expected = new ArrayList<String>();
    expected.add("TRO");
    expected.add("ORD");
    expected.add("LAC");
    expected.add("MKE");
    Assertions.assertEquals(testGraph.shortestPath("TRO", "MKE", "cost"), expected);
    Assertions.assertEquals(testGraph.shortestPath("TRO", "MKE", "time"), expected);

    Assertions.assertEquals(testGraph.getPathWeight("TRO", "MKE", "cost"), 636);
    Assertions.assertEquals(testGraph.getPathWeight("TRO", "MKE", "time"), 327);
  }
}
