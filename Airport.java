// --== CS400 File Header Information ==--
// Name: David Jiang
// Email: djiang38@wisc.edu
// Notes to Grader: <optional extra notes>

import java.util.LinkedList;

/**
 * Special class to store the airport data.
 * 
 * @author davidjiang
 *
 */
public class Airport {
  public String data; // vertex label or application specific data
  public LinkedList<Flight> edgesLeaving;

  public Airport(String data) {
    this.data = data;
    this.edgesLeaving = new LinkedList<>();
  }

  public String toString() {
    return data;
  }
}
