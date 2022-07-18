// --== CS400 File Header Information ==--
// Name: David Jiang
// Email: djiang38@wisc.edu
// Notes to Grader: <optional extra notes>

import java.util.NoSuchElementException;
import java.util.LinkedList;

public class HashTableMap {
  public int capacity;
  private int size = 0;
  protected LinkedList<keyAndValue>[] arr;

  @SuppressWarnings("unchecked")
  public HashTableMap() {
    this.capacity = 10;
    arr = new LinkedList[capacity];
  }

  /**
   * Method used to test if the hashtable is oversized and resizes it appropriatly.
   */
  private void grow() {
    if (size + 1.0 >= capacity * 0.85) {
      capacity *= 2;
      LinkedList<keyAndValue>[] arrC = arr;
      int tSize = capacity / 2;
      clear(); // Clears the instance array to be replaced with the values rehashed
      for (int i = 0; i < tSize; i++) {
        try {
          for (int j = 0; j < arrC[i].size(); j++) {
            put(arrC[i].get(j).getKey(), arrC[i].get(j).getValue()); // Uses put to rehash values
                                                                     // and replace them into the
                                                                     // array
          }
        } catch (NullPointerException e) { // In case a linkedlist is empty, this will skip it
                                           // rather than throw and error
        }
      }
    }
  }

  /**
   * A method to put data into the hashmap
   * 
   * @param key   String of the key to put into the hashmap
   * @param value Airport of the airport that we are storing
   * @return Boolean of whether or not we added the airport correctly
   */
  public boolean put(String key, Airport value) {
    if (key == null || containsKey(key))
      return false;
    grow();
    int index = Math.abs(key.hashCode()) % capacity;
    if (arr[index] == null) {
      arr[index] = new LinkedList<keyAndValue>();
    }
    keyAndValue temp = new keyAndValue(key, value);
    arr[index].add(temp);
    size++;
    return true;
  }

  /**
   * Search for the key we are looking for in our hash table
   * 
   * @param key String of the key that we are looking for.
   * @return
   * @throws NoSuchElementException
   */
  public Airport get(String key) throws NoSuchElementException {
    int index = Math.abs(key.hashCode()) % capacity;
    if (arr[index] == null) {
      throw new NoSuchElementException("No such element was found");
    }
    for (keyAndValue i : arr[index]) {
      if (i.getKey().equals(key)) {
        return i.getValue();
      }
    }
    throw new NoSuchElementException("No such element was found");
  }

  /**
   * @return returns size
   */
  public int size() {
    return size;
  }

  /**
   * Checks whether or not the key that we are looking for has already been stored in our hashmap
   * 
   * @param key String of the key that we are looking for
   * @return Boolean of whether or not an element is included.
   */
  public boolean containsKey(String key) {
    int index = Math.abs(key.hashCode()) % capacity;
    if (arr[index] == null)
      return false;
    for (int i = 0; i < arr[index].size(); i++) {
      if (arr[index].get(i).getKey().equals(key)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Removes an element from our hashmap
   * 
   * @param key String of the key that we are looking to remove
   * @return keyAndValue of the element that we removed
   */
  public keyAndValue remove(String key) {
    int index = Math.abs(key.hashCode()) % capacity;
    for (int i = 0; i < arr[index].size(); i++) {
      if (arr[index].get(i).getKey().equals(key)) {
        return arr[index].remove(i);
      }
    }
    return null;
  }

  /**
   * Returns a list of all airports currently stored in our hashmap
   * 
   * @return Linked list of all airports in our hashmap
   */
  public LinkedList<Airport> values() {
    LinkedList<Airport> ret = new LinkedList<Airport>();
    for (int i = 0; i < arr.length; i++) {
      if (arr[i] != null) {
        for (keyAndValue j : arr[i]) {
          ret.add(j.getValue());
        }
      }
    }
    return ret;
  }

  @SuppressWarnings("unchecked")
  public void clear() {
    arr = new LinkedList[capacity];
    size = 0;
  }

}
