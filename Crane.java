public class Crane {
	private int capacity;
	private String ID;
	private static int nextID = 0;

	public Crane(String ag, int w)
	{
		ID = ag;
		capacity = w;
	}
	
	public int capacity()
	{
		return capacity;
	}
	
	public String ID()
	{
		return ID;
	}
}
