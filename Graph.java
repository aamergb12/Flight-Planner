import java.util.*;

// Flight class to represent a flight
class Flight {
    String from;
    String to;
    int cost;
    int time;

    // Constructor for the Flight class
    public Flight(String from, String to, int cost, int time) {
        this.from = from;
        this.to = to;
        this.cost = cost;
        this.time = time;
    }
}

//class for city and its flights
class CityNode {
    String city;
    List<Flight> flights;

    public CityNode(String city) {
        this.city = city;
        this.flights = new ArrayList<>();
    }

    public void addFlight(String to, int cost, int time) {
        flights.add(new Flight(this.city, to, cost, time));
    }
}

//represents a flight path
public class Graph {
    private Map<String, CityNode> nodes;

    // Constructor for the Graph class
    public Graph() {
        this.nodes = new HashMap<>();
    }

    // Add a flight to the graph
    public void addFlight(String from, String to, int cost, int time) {
        nodes.computeIfAbsent(from, CityNode::new).addFlight(to, cost, time);
        nodes.computeIfAbsent(to, CityNode::new).addFlight(from, cost, time); // Make it UNDIRECTED
    }

    // Get all flights from a city
    public List<Flight> getFlightsFromCity(String city) {
        return nodes.getOrDefault(city, new CityNode(city)).flights;
    }


    // Get all cities in the graph
    public boolean hasCity(String city) {
        return nodes.containsKey(city);
    }
}