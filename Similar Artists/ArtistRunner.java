import java.util.*;
import java.io.*;

public class ArtistRunner
{
	HashMap<Artist, HashSet<Edge>> artistMap;
	Artist start, end;
	Graph graph;
	Stack<Artist> currentPath;
	HashSet<Artist> visited;

	public ArtistRunner()
	{
		artistMap = new HashMap<Artist, HashSet<Edge>>();
		graph = new Graph();
		File file = new File("SimilarArtists.txt");
		try
		{
			BufferedReader input = new BufferedReader(new FileReader(file));
			String text = "";
			while((text = input.readLine()) != null)
			{
				String[] splitText = text.split(", ");
				Artist a1 = new Artist(splitText[0]);
				Artist a2 = new Artist(splitText[1]);
				graph.addArtist(a1);
				graph.addArtist(a2);
				graph.addEdge(a1, a2);
				graph.addEdge(a2, a1);
				if(!(artistMap.containsKey(a1)))
				{
					artistMap.put(a1, new HashSet<Edge>());
				}
				if(!(artistMap.containsKey(a2)))
				{
					artistMap.put(a2, new HashSet<Edge>());
				}
				artistMap.get(a1).add(new Edge(a1, a2));
				artistMap.get(a2).add(new Edge(a2, a1));
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		System.out.println("Edges - Connecting artists with similar");
		for(Edge edge: graph.getEdges())
		{
			System.out.println("\t"+edge);
		}

		for(Artist startingA: graph.getArtists())
		{
			for(Artist endingA: graph.getArtists())
			{
				if(!startingA.equals(endingA))
				{
					currentPath = new Stack<Artist>();
					visited = new HashSet<Artist>();
					dft(startingA, endingA);
				}
			}
		}
	}

	public void dft(Artist currentA, Artist endA)
	{
		currentPath.push(currentA);
		visited.add(currentA);
		if(!currentA.equals(endA))
		{
			for(Edge edge:graph.getEdges())
			{
				Artist artist = edge.getArtist();
				Artist similar = edge.getSimilar();
				if(visited.contains(artist) && !visited.contains(similar))
				{
					dft(similar, endA);
				}
				else if(!visited.contains(artist) && visited.contains(similar))
				{
					dft(artist, endA);
				}
			}
		}
		else
		{
			printCurrentPath();
		}
	}

	public void printCurrentPath()
	{
		System.out.print("Current path: ");
		for(Artist edge: currentPath)
		{
			System.out.print(edge+", ");
		}
		System.out.println();
	}

	public static void main(String[] args)
	{
		ArtistRunner app = new ArtistRunner();
	}
}