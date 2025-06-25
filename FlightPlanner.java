/*
Aamer Goual Belhamidi
CS3345- Kamran Khan
4/27/2025
Assignment 4-Flight Planner
*/


import java.io.*;
import java.util.*;

// Flight class to represent a flight
class FlightPath {
    List<String> path;
    // List of cities in the path
    int totalCost;
    int totalTime;

    // Total cost of the path
    public FlightPath(List<String> path, int totalCost, int totalTime) {
        this.path = new ArrayList<>(path);
        this.totalCost = totalCost;
        this.totalTime = totalTime;
    }
}

public class FlightPlanner {
    private Graph graph;

    // Graph to represent the flight network
    public FlightPlanner() {
        graph = new Graph();
    }

    // Read flight data from a file
    public void readFlightData(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            // Read the number of cities
            int numFlights = Integer.parseInt(reader.readLine().trim());
            for (int i = 0; i < numFlights; i++) {
                String[] parts = reader.readLine().split("\\|");
                graph.addFlight(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
            }
        }
    }
    // Find flight plans based on requests
    // Read requests from a file and find flight plans
    public void findFlightPlans(String requestFile) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(requestFile))) {
            int numRequests = Integer.parseInt(reader.readLine().trim());
            for (int i = 0; i < numRequests; i++) {
                // Read each request
                String[] parts = reader.readLine().split("\\|");
                String startCity = parts[0];
                String endCity = parts[1];
                String sortBy = parts[2];

                // Check if the start and end cities are valid
                System.out.println("Flight " + (i + 1) + ": " + startCity + ", " + endCity + " (" + (sortBy.equals("T") ? "Time" : "Cost") + ")");

                //
                List<FlightPath> allPaths = new ArrayList<>();
                Stack<FlightPath> stack = new Stack<>();
                stack.push(new FlightPath(Arrays.asList(startCity), 0, 0));

                // do DFS to find all paths
                while (!stack.isEmpty()) {
                    FlightPath currentPath = stack.pop();
                    String currentCity = currentPath.path.get(currentPath.path.size() - 1);

                    if (currentCity.equals(endCity)) {
                        allPaths.add(currentPath);
                        continue;
                    }

                    // Check if the current city has flights

                    for (Flight flight : graph.getFlightsFromCity(currentCity)) {
                        if (!currentPath.path.contains(flight.to)) {
                            List<String> newPath = new ArrayList<>(currentPath.path);
                            newPath.add(flight.to);
                            stack.push(new FlightPath(newPath, currentPath.totalCost + flight.cost, currentPath.totalTime + flight.time));
                        }
                    }
                }
                // Sort the paths based on the specified criteria
                if (allPaths.isEmpty()) {
                    System.out.println("No flight plans found.\n");
                    // No paths found
                } else {
                    if (sortBy.equals("T")) {
                        allPaths.sort(Comparator.comparingInt(fp -> fp.totalTime));
                    } else {
                        allPaths.sort(Comparator.comparingInt(fp -> fp.totalCost));
                    }

                    // Print the top 3 paths
                    for (int j = 0; j < Math.min(3, allPaths.size()); j++) {
                        FlightPath fp = allPaths.get(j);
                        System.out.print("Path " + (j + 1) + ": ");
                        for (int k = 0; k < fp.path.size(); k++) {
                            System.out.print(fp.path.get(k));
                            if (k < fp.path.size() - 1) {
                                System.out.print(" -> ");
                            }
                        }
                        System.out.printf(". Time: %d Cost: %.2f%n", fp.totalTime, (double) fp.totalCost);
                    }
                    System.out.println();
                }
            }
        }
    }
    // Main method to run the flight planner
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java FlightPlanner <flight_data_file> <flight_requests_file>");
            return;
        }

        // read flight data and requests from files
        FlightPlanner planner = new FlightPlanner();
        try {
            planner.readFlightData(args[0]);
            planner.findFlightPlans(args[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

