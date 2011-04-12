public class Truck {
	private int capacity;
	private String ID;
	private static int nextID = 0;

	public Truck(String tr, int w)
	{
		ID = tr;
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
