public class Box {
	private int weight;
	private String ID;
	private static int nextID = 0;

	public Box(int w)
	{
		ID = "box"+nextID++;
		weight = w;
	}
	
	public int weight()
	{
		return weight;
	}
	
	public String ID()
	{
		return ID;
	}
}
