import java.util.*;
import java.io.*;

public class CityConnect
{
	HashMap<City, HashSet<Edge>> cityMap;
	HashSet<City> cities;
	HashSet<Edge> edges;
	City start, end;

	public CityConnect()
	{
		cities = new HashSet<City>();
		edges = new HashSet<Edge>();
		ArrayList<String> cityList = new ArrayList<String>();
		cityMap = new HashMap<City, HashSet<Edge>>();
		File file = new File("City Distances.txt");
		try
		{
			BufferedReader input = new BufferedReader(new FileReader(file));
			String text = "";
			while((text = input.readLine()) != null)
			{
				String[] info = text.split(",");
				City c1 = new City(info[0]);
				City c2 = new City(info[1]);
				int distance = Integer.parseInt(info[2]);
				if(!cityList.contains(c1.getName()))
					cityList.add(c1.getName());
				if(!cityList.contains(c2.getName()))
					cityList.add(c2.getName());
				cities.add(c1);
				cities.add(c2);
				edges.add(new Edge(c1,c2,distance));
				edges.add(new Edge(c2,c1,distance));
				if(!cityMap.containsKey(c1))
				{
					cityMap.put(c1, new HashSet<Edge>());
				}
				if(!cityMap.containsKey(c2))
				{
					cityMap.put(c2, new HashSet<Edge>());
				}
				cityMap.get(c1).add(new Edge(c1,c2,distance));
				cityMap.get(c2).add(new Edge(c2,c1,distance));
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		System.out.println("Vertices - Cities");
		Iterator it = cities.iterator();
		while(it.hasNext())
		{
			System.out.println("\t"+it.next());
		}

		System.out.println("\nEdges - Connecting cities and distances between");
		it = edges.iterator();
		while(it.hasNext())
		{
			System.out.println("\t"+it.next());
		}

		for(int x=0; x<cityList.size(); x++)
		{
			for(int y=x+1; y<cityList.size(); y++)
			{
				for(City c:cities)
				{
					if(c.getName().equals(cityList.get(x)))
						start = c;
					if(c.getName().equals(cityList.get(y)))
						end = c;
				}
				Graph graph = new Graph(cities, edges);
				DijkstrasAlgorithm da = new DijkstrasAlgorithm(graph);
				da.createTravelPaths(start);
				ArrayList<City> shortestPath = da.getShortestPath(end);
				int distance = 0;
				System.out.println("Shortest path between "+start.getName()+" to "+end.getName()+".");

				for(int i=0; i<shortestPath.size()-1; i++)
				{
					City c1 = shortestPath.get(i);
					City c2 = shortestPath.get(i+1);
					System.out.println("\t"+c1+" to "+c2);
					for(Edge e:cityMap.get(c1))
					{
						if(e.getStart().equals(c1) && e.getDestination().equals(c2))
							distance += e.getDistance();
						else if(e.getStart().equals(c2) && e.getDestination().equals(c1))
								distance += e.getDistance();
					}
				}
				System.out.println("Distance between: "+distance+" miles\n\n");
			}
		}
	}

	public static void main(String[] args)
	{
		CityConnect app = new CityConnect();
	}

	public class DijkstrasAlgorithm
	{
		ArrayList<City> cities;
		HashSet<City> visitedCities, unvisitedCities;
		ArrayList<Edge> edges;
		HashMap<City, City> predecessors;
		HashMap<City, Integer> distance;

		public DijkstrasAlgorithm(Graph graph)
		{
			cities = new ArrayList<City>(graph.getCities());
			edges = new ArrayList<Edge>(graph.getEdges());
		}

		public void createTravelPaths(City source)
		{
			visitedCities = new HashSet<City>();
			unvisitedCities = new HashSet<City>();
			predecessors = new HashMap<City, City>();
			distance = new HashMap<City, Integer>();
			distance.put(source, 0);
			unvisitedCities.add(source);
			while(unvisitedCities.size() > 0)
			{
				City city = getMinimum(unvisitedCities);
				visitedCities.add(city);
				unvisitedCities.remove(city);
				findMinimalDistances(city);
			}
		}

		public HashMap<City, City> getPred()
		{
			return predecessors;
		}

		public void findMinimalDistances(City tempCity)
		{
			ArrayList<City> adjNodes = getNeighbors(tempCity);
			for(City targetCity:adjNodes)
			{
				if(getShortestDistance(targetCity)>getShortestDistance(tempCity) + getDistance(tempCity, targetCity))
				{
					distance.put(targetCity, getShortestDistance(tempCity) + getDistance(tempCity, targetCity));
					predecessors.put(targetCity, tempCity);
					unvisitedCities.add(targetCity);
				}
			}
		}
		public int getDistance(City tempCity, City target)
		{
			for(Edge edge:edges)
			{
				if((edge.getStart().equals(tempCity) && edge.getDestination().equals(target))
					|| (edge.getStart().equals(target) && edge.getDestination().equals(tempCity)))
				{
					return edge.getDistance();
				}
			}
			throw new RuntimeException();
		}

		public ArrayList<City> getNeighbors(City tempCity)
		{
			ArrayList<City> neighbors = new ArrayList<City>();
			for(Edge edge:edges)
			{
				if(edge.getStart().equals(tempCity) && !wasVisited(edge.getDestination()))
				{
					neighbors.add(edge.getDestination());
				}
				if(edge.getDestination().equals(tempCity) && !wasVisited(edge.getStart()))
				{
					neighbors.add(edge.getStart());
				}
			}
			return neighbors;
		}

		public City getMinimum(HashSet<City> cities)
		{
			City min = null;
			for(City city:cities)
			{
				if(min == null)
				{
					min = city;
				}
				else
				{
					if(getShortestDistance(city) < getShortestDistance(min))
						min = city;
				}
			}
			return min;
		}

		public boolean wasVisited(City city)
		{
			return visitedCities.contains(city);
		}

		public int getShortestDistance(City dest)
		{
			Integer dist = distance.get(dest);
			if(dist == null)
				return Integer.MAX_VALUE;
			return dist;
		}

		public ArrayList<City> getShortestPath(City target)
		{
			ArrayList<City> connectingCities = new ArrayList<City>();
			City step = target;
			if(predecessors.get(step) == null)
				return null;
			connectingCities.add(step);
			while(predecessors.get(step)!=null)
			{
				step = predecessors.get(step);
				connectingCities.add(step);
			}
			Collections.reverse(connectingCities);
			return connectingCities;
		}
	}

	public class Graph
	{
		HashSet<City> cities;
		HashSet<Edge> edges;
		public Graph(HashSet<City> cities, HashSet<Edge> edges)
		{
			this.cities = cities;
			this.edges = edges;
		}

		public HashSet<City> getCities()
		{
			return cities;
		}
		public HashSet<Edge> getEdges()
		{
			return edges;
		}
	}

	public class Edge
	{
		City start, destination;
		int distance, id;

		public Edge(City start, City destination, int distance)
		{
			this.start = start;
			this.destination = destination;
			this.distance = distance;
			id = start.hashCode() + destination.hashCode();
		}

		public City getDestination()
		{
			return destination;
		}

		public City getStart()
		{
			return start;
		}

		public int getDistance()
		{
			return distance;
		}

		public int hashCode()
		{
			return id;
		}

		public boolean equals(Object other)
		{
			if(other == this)
				return true;

			Edge obj = (Edge) other;

			return obj.hashCode() == hashCode();
		}

		public String toString()
		{
			return start+" TO "+destination+" : "+distance;
		}
	}

	public class City
	{
		String name;
		int id;

		public City(String name)
		{
			this.name = name;
			id = name.hashCode();
		}

		public String getName()
		{
			return name;
		}

		public int hashCode()
		{
			return id;
		}

		public boolean equals(Object other)
		{
			if(other == this)
				return true;

			City obj = (City) other;

			return this.hashCode() == obj.hashCode();
		}

		public String toString()
		{
			return name;
		}
	}
}