import java.util.*;

public class Graph
{
	HashSet<Artist> artists;
	HashSet<Edge> edges;

	public Graph()
	{
		artists = new HashSet<Artist>();
		edges = new HashSet<Edge>();
	}

	public HashSet<Artist> getArtists()
	{
		return artists;
	}

	public HashSet<Edge> getEdges()
	{
		return edges;
	}

	public void addArtist(Artist artist)
	{
		artists.add(artist);
	}

	public void addEdge(Edge edge)
	{
		edges.add(edge);
	}

	public void addEdge(Artist artist, Artist similar)
	{
		edges.add(new Edge(artist, similar));
	}
}