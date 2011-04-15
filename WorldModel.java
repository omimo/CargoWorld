import jason.environment.grid.GridWorldModel;
import java.util.*;
import java.util.logging.*;

public class WorldModel extends GridWorldModel {

    public static final int   BOX = 32;
    public static final int   PLATE = 64;
	public static final int   TRUCK = 128;
	
	public static int sleep = 200;
	
	public static int NO_OF_PILES = 5;
	public static int NO_OF_BOXES = 10;
	public static int NO_OF_CRANES = 5;
	public static int NO_OF_TRUCKS = 3;
	
	private Random rnd = new Random();
			
	private ArrayList<Stack<Box>>  piles;	
	private HashMap<String,Crane> cranes = new HashMap<String,Crane>();
	
	private ArrayList<Box> liftedBoxes = new ArrayList<Box>();
	private HashMap<Box,Vector<Crane>> lifters = new HashMap<Box,Vector<Crane>>(); 
	
	private HashMap<String,Truck> trucks = new HashMap<String,Truck>();
	private HashMap<String,String> trucksOnSite = new HashMap<String,String>();
	
	private Logger logger = Logger.getLogger("CargoWorld.mas2j."+ShippingYard.class.getName());		
	
	private Object[][] labels;
	
	public WorldModel(int nAg, int nTrucks, int nPiles, int nBoxes,int sl)
	{
		super(nPiles+nAg*2,nBoxes+nAg*2+2,nAg);
		NO_OF_PILES = nPiles;
		NO_OF_BOXES = nBoxes;
		NO_OF_CRANES = nAg;		
		NO_OF_TRUCKS = nTrucks;
		sleep = sl;
		
		labels = new Object[NO_OF_PILES+nAg*2][NO_OF_BOXES+nAg*2+2];
		
		piles = new ArrayList<Stack<Box>>();
		for (int i=0;i<NO_OF_PILES;i++)
			piles.add(new Stack<Box>());
		
		loadTheShip();
					
		for (int i=0; i<NO_OF_CRANES*2+NO_OF_PILES; i++) 
			add(PLATE,i,NO_OF_BOXES+NO_OF_CRANES*2+1);				
		
	}
	
	public Object getLabel(int x,int y)
	{
		return labels[x][y];	
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
	
	public Vector<Crane> getLiftersOf(String bid)
	{
		for (Box b : lifters.keySet()) 
			if (b.ID().equals(bid))
				return lifters.get(b);
		return new Vector<Crane>();
	}
	
	
	public Set<Box> getLiftBoxes()
	{
		return lifters.keySet();
	}
	
	public ArrayList<Box> getLiftedBoxes()
	{
		return liftedBoxes;
	}
	
	public ArrayList<Box> getBoxesOnTop()
	{				
		ArrayList<Box> result = new ArrayList<Box>();
						
		for (int p=0;p<NO_OF_PILES;p++)
		{						
			Stack<Box> st = piles.get(p);
			
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
		for (int p=0;p<NO_OF_PILES;p++)
		{			
			Stack s = piles.get(p);
			for(int i=0;i<NO_OF_BOXES;i++)
			{
				int w = rnd.nextInt(3) + 1;  // Boxes with 10 & 20 & 30 lbs			
				Box b = new Box(w*10);
				s.push(b);
				add(BOX,
					NO_OF_CRANES + p,
					NO_OF_CRANES * 2 + (10 - i));
				
				labels[NO_OF_CRANES + p][NO_OF_CRANES * 2 + (10 - i)] = b;								
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
				// update the GUI before finishing the action
				// we need this to show single lifts 				
				view.update();
				try {	
					Thread.sleep(sleep);
				} catch (Exception e) {}
				//							
				
				for (int p=0;p<NO_OF_PILES;p++)
				{			
					Stack<Box> st = piles.get(p);								
					{
						if (!st.empty() && st.peek().ID().equals(box))
						{							
							remove(BOX,
									NO_OF_CRANES + p,
									NO_OF_CRANES * 2 + (10 - st.indexOf(st.peek())));
							liftedBoxes.add(st.pop());													
						}
					}
				}			
				view.update(); // Another update to clear the lines
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
		//int c = (rnd.nextInt(3) + 1) * 50; // made it large for testing - Pai	
		Crane cr = new Crane(ag, c);
		cranes.put(ag,cr);	
		
		
		int k = Integer.parseInt(cr.ID().substring(5)) -1 ;
		setAgPos(k,k,NO_OF_BOXES+NO_OF_CRANES*2);
		labels[k][NO_OF_BOXES+NO_OF_CRANES*2] = cr;
		view.update();
		
		return true;
	}	
	
	public Boolean signin(String truck, String capacity)  // Truck
	{			
		Truck tr = new Truck(truck, Integer.parseInt(capacity));
		trucks.put(truck,tr);
		/*
		int trn = Integer.parseInt(tr.ID().substring(5));
		add(TRUCK,
					NO_OF_CRANES + NO_OF_PILES+2,
					NO_OF_CRANES * 2 + NO_OF_BOXES - trn*2 );
				
		labels[NO_OF_CRANES + NO_OF_PILES+2][NO_OF_CRANES * 2 + NO_OF_BOXES - trn*2] = tr;		
		*/
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
		
		Truck tr = trucks.get(truck);
		int trn = Integer.parseInt(tr.ID().substring(5));
		add(TRUCK,
					NO_OF_CRANES + NO_OF_PILES+2,
					NO_OF_CRANES * 2 + NO_OF_BOXES - trn*2 );
				
		labels[NO_OF_CRANES + NO_OF_PILES+2][NO_OF_CRANES * 2 + NO_OF_BOXES - trn*2] = tr;		
				
		return true;
	}

	/* truckLeave Action */
	public Boolean truckLeave(String truck)
	{
		trucksOnSite.remove(truck);
		
		Truck tr = trucks.get(truck);
		int trn = Integer.parseInt(tr.ID().substring(5));
		remove(TRUCK,
					NO_OF_CRANES + NO_OF_PILES+2,
					NO_OF_CRANES * 2 + NO_OF_BOXES - trn*2 );
					
		return true;
	}
			
}
