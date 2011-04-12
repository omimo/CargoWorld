import jason.environment.grid.GridWorldModel;
import java.util.*;
import java.util.logging.*;

public class WorldModel extends GridWorldModel {

	private static int NO_OF_PILES = 5;
	private static int NO_OF_BOXES = 10;
	private static int NO_OF_CRANES = 5;
	
	public 	List<Stack<Box>>  piles;	
	private Random rnd = new Random();
	private HashMap<String,Crane> cranes = new HashMap<String,Crane>();
	private HashMap<Box,Vector<Crane>> lifters = new HashMap<Box,Vector<Crane>>(); 
	
	private HashMap<String,Truck> trucks = new HashMap<String,Truck>();
	private HashMap<String,String> trucksOnSite = new HashMap<String,String>();
	
	private Logger logger = Logger.getLogger("CargoWorld.mas2j."+ShippingYard.class.getName());		
	
	public WorldModel(int nAg, int nPiles, int nBoxes)
	{
		super(nPiles,nBoxes,5);
		NO_OF_PILES = nPiles;
		NO_OF_BOXES = nBoxes;
		NO_OF_CRANES = nAg;
		
		piles = new ArrayList<Stack<Box>>();
		for (int i=0;i<NO_OF_PILES;i++)
			piles.add(new Stack<Box>());
		loadTheShip();
	}
	
	public Set<String> getCranesNames()
	{
		return cranes.keySet();	
	}
	
	public Collection<Crane> getCranes()
	{
		return cranes.values();	
	}
	
	public Collection<Truck> getTrucks()
	{
		return trucks.values();	
	}
	
	public Collection<String> getTrucksOnSite()
	{
		return trucksOnSite.values();	
	}
	
	public Vector<Crane> getLiftersOf(Box b)
	{
		return lifters.get(b);
	}
	
	public Set<Box> getLiftBoxes()
	{
		return lifters.keySet();
	}
	
	public Set<Box> getBoxesOnTop()
	{				
		Set<Box> result = new HashSet<Box>();
		Iterator<Stack<Box>> iter=piles.iterator();
		
		while(iter.hasNext())
		{
			Stack<Box> st =iter.next(); 
			if (!st.empty())
			{
				Box top = st.peek(); //Get the box on the top			
				result.add(top);
			}
		}
		return result;
	}
	
		/* A Silly Way to Print The Stack Elements!!! */
	public void printStack()
	{
		
		String st = "";
		
		Iterator<Stack<Box>> iter=piles.iterator();
		while(iter.hasNext())
		{
			st="\n"+st;
			Stack<Box> temp = (Stack<Box>)(iter.next()).clone();
			Box b;
			while ( !temp.empty() )
        	{
				b = (Box)temp.pop();
				st=b.weight()+" ," + st ;                
            }
		}
		
		logger.info("----Piles-----\n"+st);		
	}
	
/*                       */

/* Initializes the stack */
	public void loadTheShip()
	{
		int i;			
		Iterator iter=piles.iterator();
		while(iter.hasNext())
		{			
			Stack s = (Stack)iter.next();
			for(i=0;i<NO_OF_BOXES;i++)
			{
				int w = rnd.nextInt(3) + 1;  // Boxes with 10 & 20 & 30 lbs			
				s.push(new Box(w*10));												
			}
		}
	}
	
	
/* Actions */

/* The Move Action */
	public Boolean lift(String agent, String box/*, String truck*/)
	{				
		Boolean exists = false;
		
		for(Box b : getBoxesOnTop())
			if (b.ID().equals(box)) 
				exists = true;
		
		Box top = new Box(0);
				
		
		if(exists)		
		{
			Iterator<Stack<Box>> iter=piles.iterator();
			while(iter.hasNext())
			{
				Stack<Box> st = iter.next();
				if (!st.empty() && st.peek().ID().equals(box))
					top=st.peek();
			}		
		
			int sumCap = 0;
			
			if (!lifters.containsKey(top))											
				lifters.put(top, new Vector<Crane>());					
			
			lifters.get(top).add(cranes.get(agent));
			
			
			for (Crane c : lifters.get(top))							
				sumCap += c.capacity();							
			
			logger.info("LIFTACT: "+ sumCap +"  vs "+top.weight());
			
			if (sumCap >= top.weight())
			{								
				iter=piles.iterator();
				while(iter.hasNext())
				{
					Stack<Box> st = iter.next();
					if (!st.empty() && st.peek().ID().equals(box))
						st.pop();
				}			
				
				lifters.remove(top);
				logger.info("LIFTACT: "+ top.ID() +"  lifted by "+agent);
				printStack();
				
			}
			return true;
		}		
			return true;
	}
	
	/* Signin Actions */
	public Boolean signin(String ag)  // Crane
	{
		int c = (rnd.nextInt(3) + 1) * 5;		
		cranes.put(ag,new Crane(ag, c));
		return true;
	}	
	
	public Boolean signin(String truck, String capacity)  // Truck
	{			
		trucks.put(truck,new Truck(truck, Integer.parseInt(capacity)));
		return true;
	}
	
	/* Signout Action */
	public Boolean signout(String ag)
	{
		if (ag.contains("crane")) 
			cranes.remove(ag);
		else if (ag.contains("truck"))
			trucks.remove(ag);
		else 
			return false;
		return true;
	}	
	
	/* moveAndDrop Action */
	public Boolean moveAndDrop(String ag,String box, String truck)
	{
		//cranes.remove(ag);
		return true;
	}

	/* truckArrive Action */
	public Boolean truckArrive(String truck)
	{
		trucksOnSite.put(truck,truck);
		return true;
	}

	/* truckLeave Action */
	public Boolean truckLeave(String truck)
	{
		trucksOnSite.remove(truck);
		return true;
	}	
}
