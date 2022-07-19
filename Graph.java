// --== CS400 File Header Information ==--
// Name: David Jiang
// Email: djiang38@wisc.edu

import java.util.List;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.NoSuchElementException;

public class Graph {

  protected HashTableMap airports; // holds graph verticies, key=data

  public Graph() {
    airports = new HashTableMap();
  }

  /**
   * Insert a new vertex into the graph.
   * 
   * @param data the data item stored in the new vertex
   * @return true if the data can be inserted as a new vertex, false if it is already in the graph
   * @throws NullPointerException if data is null
   */
  public boolean insertAirport(String data) {
    if (data == null || data.isBlank())
      throw new NullPointerException("Cannot add null vertex");
    if (airports.containsKey(data))
      return false; // duplicate values are not allowed
    airports.put(data, new Airport(data));
    return true;
  }

  /**
   * Remove a vertex from the graph. Also removes all edges adjacent to the vertex from the graph
   * (all edges that have the vertex as a source or a destination vertex).
   * 
   * @param data the data item stored in the vertex to remove
   * @return true if a vertex with *data* has been removed, false if it was not in the graph
   * @throws NullPointerException if data is null
   */
  public boolean removeAirport(String data) {
    if (data == null || data.isBlank())
      throw new NullPointerException("Cannot remove null vertex");
    Airport removeVertex = null;
    try {
      removeVertex = airports.get(data);
    } catch (NoSuchElementException e) {
      return false;
    }
    if (removeVertex == null)
      return false; // vertex not found within graph
    // search all vertices for edges targeting removeVertex
    for (Airport v : airports.values()) {
      Flight removeEdge = null;
      for (Flight e : v.edgesLeaving)
        if (e.target == removeVertex)
          removeEdge = e;
      // and remove any such edges that are found
      if (removeEdge != null)
        v.edgesLeaving.remove(removeEdge);
    }
    // finally remove the vertex and all edges contained within it
    return airports.remove(data) != null;
  }

  /**
   * Insert a new directed edge with a positive edge weight into the graph.
   * 
   * @param source the data item contained in the source vertex for the edge
   * @param target the data item contained in the target vertex for the edge
   * @param time   the weight for the edge (has to be a positive integer)
   * @return true if the edge could be inserted or its weight updated, false if the edge with the
   *         same weight was already in the graph
   * @throws IllegalArgumentException if either source or target or both are not in the graph, or if
   *                                  its weight is < 0
   * @throws NullPointerException     if either source or target or both are null
   */
  public boolean insertFlight(String source, String target, int time, int cost) {
    if (source == null || target == null)
      throw new NullPointerException("Cannot add edge with null source or target");
    Airport sourceVertex = null;
    Airport targetVertex = null;
    try {
      sourceVertex = airports.get(source);
      targetVertex = airports.get(target);
    } catch (NoSuchElementException e) {
      return false;
    }
    if (sourceVertex == null || targetVertex == null)
      throw new IllegalArgumentException("Cannot add edge with vertices that do not exist");
    if (time < 0 || cost < 0)
      throw new IllegalArgumentException("Cannot add edge with negative time/cost");
    // handle cases where edge already exists between these verticies
    for (Flight e : sourceVertex.edgesLeaving)
      if (e.target == targetVertex) {
        if (e.time == time || e.cost == cost)
          return false; // edge already exists
        else {
          e.time = time;
          e.cost = cost;
        }
        return true;
      }
    // otherwise add new edge to sourceVertex
    sourceVertex.edgesLeaving.add(new Flight(targetVertex, time, cost));
    return true;
  }

  /**
   * Remove an edge from the graph.
   * 
   * @param source the data item contained in the source vertex for the edge
   * @param target the data item contained in the target vertex for the edge
   * @return true if the edge could be removed, false if it was not in the graph
   * @throws IllegalArgumentException if either source or target or both are not in the graph
   * @throws NullPointerException     if either source or target or both are null
   */
  public boolean removeFlight(String source, String target) {
    if (source == null || target == null)
      throw new NullPointerException("Cannot remove edge with null source or target");
    Airport sourceVertex = null;
    Airport targetVertex = null;
    try {
      sourceVertex = airports.get(source);
      targetVertex = airports.get(target);
    } catch (NoSuchElementException e) {
      return false;
    }
    if (sourceVertex == null || targetVertex == null)
      throw new IllegalArgumentException("Cannot remove edge with vertices that do not exist");
    // find edge to remove
    Flight removeEdge = null;
    for (Flight e : sourceVertex.edgesLeaving)
      if (e.target == targetVertex)
        removeEdge = e;
    if (removeEdge != null) { // remove edge that is successfully found
      sourceVertex.edgesLeaving.remove(removeEdge);
      return true;
    }
    return false; // otherwise return false to indicate failure to find
  }

  /**
   * Check if the graph contains a vertex with data item *data*.
   * 
   * @param data the data item to check for
   * @return true if data item is stored in a vertex of the graph, false otherwise
   * @throws NullPointerException if *data* is null
   */
  public boolean containsAirport(String data) {
    if (data == null)
      throw new NullPointerException("Cannot contain null data vertex");
    return airports.containsKey(data);
  }

  /**
   * @return String of all the airports comma seperated
   */
  public String toString() {
    String ret = "";
    for (Airport i : airports.values()) {
      ret += i.toString();
      ret += ", ";
    }
    ret = ret.substring(0, ret.length() - 2);
    return ret;
  }

  /**
   * Check if edge is in the graph.
   * 
   * @param source the data item contained in the source vertex for the edge
   * @param target the data item contained in the target vertex for the edge
   * @return true if the edge is in the graph, false if it is not in the graph
   * @throws NullPointerException if either source or target or both are null
   */
  public boolean containsFlight(String source, String target) {
    if (source == null || target == null)
      throw new NullPointerException("Cannot contain edge adjacent to null data");
    Airport sourceVertex = null;
    Airport targetVertex = null;
    try {
      sourceVertex = airports.get(source);
      targetVertex = airports.get(target);
    } catch (NoSuchElementException e) {
      return false;
    }
    if (sourceVertex == null)
      return false;
    for (Flight e : sourceVertex.edgesLeaving)
      if (e.target == targetVertex)
        return true;
    return false;
  }

  /**
   * Return the time of an edge.
   * 
   * @param source the data item contained in the source vertex for the edge
   * @param target the data item contained in the target vertex for the edge
   * @return the weight of the edge (0 or positive integer)
   * @throws IllegalArgumentException if either sourceVertex or targetVertex or both are not in the
   *                                  graph
   * @throws NullPointerException     if either sourceVertex or targetVertex or both are null
   * @throws NoSuchElementException   if edge is not in the graph
   */
  public int getTime(String source, String target) {
    if (source == null || target == null)
      throw new NullPointerException("Cannot contain weighted edge adjacent to null data");
    Airport sourceVertex = airports.get(source);
    Airport targetVertex = airports.get(target);
    if (sourceVertex == null || targetVertex == null)
      throw new IllegalArgumentException(
          "Cannot retrieve weight of edge between vertices that do not exist");
    for (Flight e : sourceVertex.edgesLeaving)
      if (e.target == targetVertex)
        return e.time;
    throw new NoSuchElementException("No directed edge found between these vertices");
  }

  /**
   * Return the cost of an edge.
   * 
   * @param source the data item contained in the source vertex for the edge
   * @param target the data item contained in the target vertex for the edge
   * @return the weight of the edge (0 or positive integer)
   * @throws IllegalArgumentException if either sourceVertex or targetVertex or both are not in the
   *                                  graph
   * @throws NullPointerException     if either sourceVertex or targetVertex or both are null
   * @throws NoSuchElementException   if edge is not in the graph
   */
  public int getCost(String source, String target) {
    if (source == null || target == null)
      throw new NullPointerException("Cannot contain weighted edge adjacent to null data");
    Airport sourceVertex = airports.get(source);
    Airport targetVertex = airports.get(target);
    if (sourceVertex == null || targetVertex == null)
      throw new IllegalArgumentException(
          "Cannot retrieve weight of edge between vertices that do not exist");
    for (Flight e : sourceVertex.edgesLeaving)
      if (e.target == targetVertex)
        return e.cost;
    throw new NoSuchElementException("No directed edge found between these vertices");
  }

  /**
   * Return the number of edges in the graph.
   * 
   * @return the number of edges in the graph
   */
  public int getEdgeCount() {
    int edgeCount = 0;
    for (Airport v : airports.values())
      edgeCount += v.edgesLeaving.size();
    return edgeCount;
  }

  /**
   * Return the number of vertices in the graph
   * 
   * @return the number of vertices in the graph
   */
  public int getVertexCount() {
    return airports.size();
  }

  /**
   * Check if the graph is empty (does not contain any vertices or edges).
   * 
   * @return true if the graph does not contain any vertices or edges, false otherwise
   */
  public boolean isEmpty() {
    return airports.size() == 0;
  }

  /**
   * Path objects store a discovered path of vertices and the overal distance of cost of the
   * weighted directed edges along this path. Path objects can be copied and extended to include new
   * edges and verticies using the extend constructor. In comparison to a predecessor table which is
   * sometimes used to implement Dijkstra's algorithm, this eliminates the need for tracing paths
   * backwards from the destination vertex to the starting vertex at the end of the algorithm.
   */
  protected class PathCost extends PathTime {
    public int totCost;

    public PathCost(Airport start) {
      super(start);
      this.totCost = 0;
    }

    /**
     * Constructor of extending a path with a new flight to connect it with.
     * 
     * @param copyPath
     * @param extendBy
     */
    public PathCost(PathCost copyPath, Flight extendBy) {
      super(copyPath, extendBy);
      this.totCost = copyPath.totCost + extendBy.cost;
    }

    public int compareTo(PathCost other) {
      int cmp = this.totCost - other.totCost;
      if (cmp != 0)
        return cmp; // use path distance as the natural ordering
      // when path distances are equal, break ties by comparing the string
      // representation of data in the end vertex of each path
      return this.end.data.toString().compareTo(other.end.data.toString());
    }
  }

  protected class PathTime implements Comparable<PathTime> {
    public Airport start; // first vertex within path
    public int totTime; // summed weight of all edges in path
    public List<String> dataSequence; // ordered sequence of data from vertices in path
    public Airport end; // last vertex within path

    /**
     * Creates a new path containing a single vertex. Since this vertex is both the start and end of
     * the path, it's initial distance is zero.
     * 
     * @param start is the first vertex on this path
     */
    public PathTime(Airport start) {
      this.start = start;
      this.totTime = 0;
      this.dataSequence = new LinkedList<>();
      this.dataSequence.add(start.data);
      this.end = start;
    }

    /**
     * This extension constructor makes a copy of the path passed into it as an argument without
     * affecting the original path object (copyPath). The path is then extended by the Edge object
     * extendBy.
     * 
     * @param copyPath is the path that is being copied
     * @param extendBy is the edge the copied path is extended by
     */
    public PathTime(PathTime copyPath, Flight extendBy) {
      this.start = copyPath.start;
      this.totTime = copyPath.totTime + extendBy.time;
      this.dataSequence = new LinkedList<>();
      for (String i : copyPath.dataSequence) {
        dataSequence.add(i);
      }
      this.dataSequence.add(extendBy.target.data);
      this.end = extendBy.target;
    }

    /**
     * Allows the natural ordering of paths to be increasing with path distance. When path distance
     * is equal, the string comparison of end vertex data is used to break ties.
     * 
     * @param other is the other path that is being compared to this one
     * @return -1 when this path has a smaller distance than the other, +1 when this path has a
     *         larger distance that the other, and the comparison of end vertex data in string form
     *         when these distances are tied
     */
    public int compareTo(PathTime other) {
      int cmp = this.totTime - other.totTime;
      if (cmp != 0)
        return cmp; // use path distance as the natural ordering
      // when path distances are equal, break ties by comparing the string
      // representation of data in the end vertex of each path
      return this.end.data.toString().compareTo(other.end.data.toString());
    }
  }

  /**
   * Uses Dijkstra's shortest path algorithm to find and return the shortest path between two
   * vertices in this graph: start and end. This path contains an ordered list of the data within
   * each node on this path, and also the distance or cost of all edges that are a part of this
   * path.
   * 
   * @param start data item within first node in path
   * @param end   data item within last node in path
   * @return the shortest path from start to end, as computed by Dijkstra's algorithm
   * @throws NoSuchElementException when no path from start to end can be found, including when no
   *                                vertex containing start or end can be found
   */
  protected PathTime dijkstrasShortestPathTime(String start, String end) {
    if (airports.get(start) == null || airports.get(end) == null) {
      throw new NoSuchElementException("awu");
    }
    HashTableMap explore = new HashTableMap();
    PriorityQueue<PathTime> heap = new PriorityQueue<PathTime>();
    Airport a = airports.get(start);
    heap.add(new PathTime(a));
    explore.put(start, a);
    PathTime min = null; // The "shortest" path at the time
    while (!heap.isEmpty()) { // Keeps iterating while we still have paths to explore
      PathTime path = heap.poll();
      Airport startVert = path.end; // Vertex we start to explore from (not starting vertex)
      for (Flight e : startVert.edgesLeaving) {
        if (explore.containsKey(e.target.data) && !e.target.data.equals(end)) {
          continue;
        }
        explore.put(e.target.data, e.target);
        if (e.target.data.equals(end)) {
          PathTime testPath = new PathTime(path, e);
          if (min == null) {
            min = testPath;
            continue;
          } else {
            min = min.compareTo(testPath) < 0 ? min : testPath;
            continue;
          }
        }
        heap.add(new PathTime(path, e));
      }
    }
    if (min != null) {
      return min;
    } else {
      throw new NoSuchElementException("wau");
    }
  }

  /**
   * Uses Dijkstra's shortest path algorithm to find and return the shortest path between two
   * vertices in this graph: start and end. This path contains an ordered list of the data within
   * each node on this path, and also the distance or cost of all edges that are a part of this
   * path.
   * 
   * @param start data item within first node in path
   * @param end   data item within last node in path
   * @return the shortest path from start to end, as computed by Dijkstra's algorithm
   * @throws NoSuchElementException when no path from start to end can be found, including when no
   *                                vertex containing start or end can be found
   */
  protected PathCost dijkstrasShortestPathCost(String start, String end) {
    if (airports.get(start) == null || airports.get(end) == null) {
      throw new NoSuchElementException("awu");
    }
    HashTableMap explore = new HashTableMap();
    PriorityQueue<PathCost> heap = new PriorityQueue<PathCost>();
    Airport a = airports.get(start);
    heap.add(new PathCost(a));
    explore.put(start, a);
    PathCost min = null; // The "shortest" path at the time
    while (!heap.isEmpty()) { // Keeps iterating while we still have paths to explore
      PathCost path = heap.poll();
      Airport startVert = path.end; // Vertex we start to explore from (not starting vertex)
      for (Flight e : startVert.edgesLeaving) {
        if (explore.containsKey(e.target.data) && !e.target.data.equals(end)) {
          continue;
        }
        explore.put(e.target.data, e.target);
        if (e.target.data.equals(end)) {
          PathCost testPath = new PathCost(path, e);
          if (min == null) {
            min = testPath;
            continue;
          } else {
            min = min.compareTo(testPath) < 0 ? min : testPath;
            continue;
          }
        }
        heap.add(new PathCost(path, e));
      }
    }
    if (min != null) {
      return min;
    } else {
      throw new NoSuchElementException("wau");
    }
  }

  /**
   * Returns the shortest path between start and end. Uses Dijkstra's shortest path algorithm to
   * find the shortest path.
   * 
   * @param start the data item in the starting vertex for the path
   * @param end   the data item in the destination vertex for the path
   * @return list of data item in vertices in order on the shortest path between vertex with data
   *         item start and vertex with data item end, including both start and end
   * @throws NoSuchElementException when no path from start to end can be found including when no
   *                                vertex containing start or end can be found
   */
  public List<String> shortestPath(String start, String end, String option) {
    if (option.trim().toLowerCase().equals("time")) {
      return dijkstrasShortestPathTime(start, end).dataSequence;
    } else if (option.trim().toLowerCase().equals("cost")) {
      return dijkstrasShortestPathCost(start, end).dataSequence;
    }
    throw new IllegalArgumentException("wau okehios");
  }

  /**
   * Returns the cost of the path (sum over edge weights) between start and end. Uses Dijkstra's
   * shortest path algorithm to find the shortest path.
   * 
   * @param start the data item in the starting vertex for the path
   * @param end   the data item in the end vertex for the path
   * @return the cost of the shortest path between vertex with data item start and vertex with data
   *         item end, including all edges between start and end
   * @throws NoSuchElementException when no path from start to end can be found including when no
   *                                vertex containing start or end can be found
   */
  public int getPathWeight(String start, String end, String option) {
    if (option.trim().toLowerCase().equals("time")) {
      return dijkstrasShortestPathTime(start, end).totTime;
    } else if (option.trim().toLowerCase().equals("cost")) {
      return dijkstrasShortestPathCost(start, end).totCost;
    }
    throw new IllegalArgumentException("wau okehios");
  }

}
