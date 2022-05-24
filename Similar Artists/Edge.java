public class Edge
{
	Artist artist, similar;
	int id;

	public Edge(Artist artist, Artist similarArtist)
	{
		this.artist = artist;
		similar = similarArtist;
		id = artist.getID() + similarArtist.getID();
	}

	public Artist getArtist()
	{
		return artist;
	}

	public Artist getSimilar()
	{
		return similar;
	}

	public int hashCode()
	{
		return id;
	}

	public boolean equals(Object obj)
	{
		if(obj.getClass() != getClass())
			return false;

		Edge otherEdge = (Edge)obj;
		if(otherEdge.hashCode() != hashCode())
			return false;
		return true;
	}

	public String toString()
	{
		return artist+" is similar to "+similar;
	}
}