// --== CS400 File Header Information ==--
// Name: David Jiang
// Email: djiang38@wisc.edu
// Notes to Grader: <optional extra notes>
/**
 * Special object used to store the airports and the keys in the hashmap
 * 
 * @author davidjiang
 *
 */
public class keyAndValue {
  private String key;
  private Airport value;

  public keyAndValue(String key, Airport value) {
    this.key = key;
    this.value = value;
  }

  public String getKey() {
    return key;
  }

  public Airport getValue() {
    return value;
  }
}
