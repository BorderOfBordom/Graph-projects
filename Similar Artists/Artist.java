public class Artist
{
	String name;
	int id;

	public Artist(String name)
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

	public String toString()
	{
		return name;
	}

	public boolean equals(Object obj)
	{
		if(obj.getClass() != getClass())
		{
			return false;
		}
		Artist otherArtist = (Artist) obj;
		if(otherArtist.hashCode() != hashCode())
			return false;
		return true;
	}
}