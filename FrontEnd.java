// --== CS400 File Header Information ==--
// Name: David Jiang
// Email: djiang38@wisc.edu
// Notes to Grader: <optional extra notes>

import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * The class that the user actually interacts with
 * 
 * @author davidjiang
 *
 */
public class FrontEnd {
  public static ArrayList<String> airport;
  public static ArrayList<String[]> flight;
  public static Graph init = new Graph();

  /**
   * sets up the lists that we need and creates the graph.
   */
  public static void setup() {
    FileInputStream airportBye = null;
    try {
      airportBye = new FileInputStream("Airports");
    } catch (FileNotFoundException e) {
    }
    Scanner file = new Scanner(airportBye);
    FileInputStream flightBye = null;
    try {
      flightBye = new FileInputStream("Flights");
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
    for (String i : airport) {
      init.insertAirport(i.toLowerCase().trim());
    }
    for (String[] i : flight) {
      init.insertFlight(i[0].toLowerCase().trim(), i[1].toLowerCase().trim(),
          Integer.valueOf(i[2].toLowerCase().trim()), Integer.valueOf(i[3].toLowerCase().trim()));
    }
    file.close();
    file2.close();
  }

  /**
   * Method to ask the user if what airports they want to travel from and what they prefer to use
   * between cost and time.
   * 
   * @return boolean of whether or not they want to continue/
   */
  public static boolean ask() {
    Scanner scnr = new Scanner(System.in);
    System.out.println(
        "Welcome to Flight Calculator, this is an app that will let you figure out the best flight path based on either time or cost!");
    System.out.println("Here is a list of airports you can travel to and from!\n");
    for (String i : airport) {
      System.out.print(i + ", ");
    }
    String airportIn1 = "";
    while (true) {
      System.out.println("\n\nChoose an airport to start your trip: ");
      airportIn1 = scnr.next().trim().toLowerCase();
      if (airportIn1 == null || !init.containsAirport(airportIn1)) {
        System.out.println("That is not a valid airport, please try another airport");
      } else {
        break;
      }
    }
    String airportIn2 = "";
    while (true) {
      System.out.println("Choose an airport to end your trip: ");
      airportIn2 = scnr.next().trim().toLowerCase();
      if (airportIn2 == null || !init.containsAirport(airportIn2)
          || airportIn1.equals(airportIn2)) {
        System.out.println("That is not a valid airport, please try another airport");
      } else {
        break;
      }
    }
    String option = "";
    while (true) {
      System.out.println(
          "Choose whether you care more about time or cost (type time for time, cost for cost): ");
      option = scnr.next().trim().toLowerCase();
      if (option == null || (!option.equals("time") && !option.equals("cost"))) {
        System.out.println("That is not a valid option, please try another option");
      } else {
        break;
      }
    }
    System.out.println("\nThe best flight options for you is: "
        + init.shortestPath(airportIn1, airportIn2, option));
    if (option.equals("time")) {
      System.out.println("This flight route takes: "
          + init.getPathWeight(airportIn1, airportIn2, option) + " minutes");
    } else if (option.equals("cost")) {
      System.out.println("This flight route costs: "
          + init.getPathWeight(airportIn1, airportIn2, option) + " USD");
    }
    String bool = "";
    while (true) {
      System.out.println("Would you like to try a different trip? (y/n)");
      bool = scnr.next().trim().toLowerCase();
      if (bool != null
          && (bool.equals("y") || bool.equals("n") || bool.equals("yes") || bool.equals("no"))) {
        scnr.close();
        return bool.equals("y") || bool.equals("yes");
      } else {
        System.out.println("Sorry, this is not a correct response, please try again!");
      }
    }
  }

  public static void main(String[] args) {
    setup();
    // Weird while loop to keep asking if they want to continue using the app.
    while (ask()) {
    }
    System.out.println("Thanks for trying my app out!");
  }
}
