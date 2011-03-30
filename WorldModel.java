import jason.environment.grid.GridWorldModel;
import java.util.*;
import java.util.logging.*;

public class WorldModel extends GridWorldModel {

	private static final int NO_OF_STACKS = 5;
	private static final int NO_OF_BOXES = 10;
	
	public 	List<Stack<Box>>  piles;	
	private Random rnd = new Random();
	private HashMap<String,Crane> cranes = new HashMap<String,Crane>();
	private HashMap<Box,Vector<Crane>> lifters = new HashMap<Box,Vector<Crane>>(); 
	
	private Logger logger = Logger.getLogger("CargoWorld.mas2j."+ShippingYard.class.getName());		
	
	public WorldModel()
	{
		super(NO_OF_STACKS,NO_OF_BOXES,5);
		
		piles = new ArrayList<Stack<Box>>();
		for (int i=0;i<NO_OF_STACKS;i++)
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
	public Boolean lift(String agent, String box, String truck)
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
				
			}
			return true;
		}		
			return false;
	}
	
	/* Signin Action */
	public Boolean signin(String ag)
	{
		int c = (rnd.nextInt(3) + 1) * 5;		
		cranes.put(ag,new Crane(ag, c));
		return true;
	}	
	
	/* Signout Action */
	public Boolean signout(String ag)
	{
		cranes.remove(ag);
		return true;
	}	
	
}
