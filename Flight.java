// --== CS400 File Header Information ==--
// Name: David Jiang
// Email: djiang38@wisc.edu
// Notes to Grader: <optional extra notes>

/**
 * Flight class to represent nodes in the graph
 * 
 * @author davidjiang
 *
 */
public class Flight {
  public Airport target;
  public int time;
  public int cost;

  public Flight(Airport target, int time, int cost) {
    this.target = target;
    this.time = time;
    this.cost = cost;
  }
}
