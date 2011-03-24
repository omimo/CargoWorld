public class Truck {
	
	private String ID;
	private static int nextID = 0;

	public Truck()
	{
		ID = "truck"+nextID++;		
	}	
	
	public String ID()
	{
		return ID;
	}
}
