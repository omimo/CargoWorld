// Environment code for project CargoWorld.mas2j

import jason.asSyntax.*;
import jason.environment.*;
import java.util.logging.*;
import java.util.*;

public class ShippingYard extends Environment {

    private Logger logger = Logger.getLogger("CargoWorld.mas2j."+ShippingYard.class.getName());
	
	// The Model Info
	private Stack<Box> stack1 = new Stack<Box>();
	private Random rnd = new Random();
	private HashMap<String,Crane> cranes = new HashMap<String,Crane>();
	private HashMap<Box,Vector<Crane>> lifters = new HashMap<Box,Vector<Crane>>(); 
	//
	
    /** Called before the MAS execution with the args informed in .mas2j */
    @Override
    public void init(String[] args) {
        super.init(args);
        loadTheShip();
		updatePercepts();
		printStack(stack1);
    }

    @Override
    synchronized public boolean executeAction(String agName, Structure action) {
       		
		Boolean result = false;
		
		if (action.getFunctor().equals("lift")) {
			
			logger.info(agName+ ": lift: "+action.getTerm(0).toString()+" > "+action.getTerm(1).toString()); 
			result = lift(agName,action.getTerm(0).toString(),action.getTerm(1).toString());	//Call the move() method to perform the move action								
		}
		
		else if (action.getFunctor().equals("signIn")) {
			
			logger.info("Agent: "+agName + " just signedin."); 
			result = signin(agName);							
		}
		
		else
		{
			logger.info("executing: "+action+", but not implemented!");
		}
		
		updatePercepts();
		printStack(stack1);
		
        return result;
    }

    /** Called before the end of MAS execution */
    @Override
    public void stop() {
        super.stop();
    }
	
	/* Updates the percepts based on the current model of the world */
	public void updatePercepts()
	{		
		clearPercepts();  // Clear old & invalid percepts 
		
		if (!stack1.empty())
		{
			Box top = stack1.peek(); //Get the box on the top
			addPercept(Literal.parseLiteral("weight("+top.ID()+","+top.weight()+")")); // So far we just know the info of the top Boxes
			addPercept(Literal.parseLiteral("onTop("+top.ID()+")"));
		}
		
		Set<String> cra = cranes.keySet();
		for (String cr : cra)
		{
			Crane c = cranes.get(cr);
			addPercept(Literal.parseLiteral("capacity("+c.ID()+","+ c.capacity()+")"));
		}			
	
		
		Set<Box> boxes = lifters.keySet();				
		
		for (Box b : boxes)
		{
			Vector<Crane> crs = lifters.get(b);
				for (Crane c : crs)
					addPercept(Literal.parseLiteral("lifting("+c.ID()+","+b.ID()+")"));
		}
		
	}
	
	/* The Move Action */
	public Boolean lift(String agent, String box, String truck)
	{				
		Box top = stack1.peek();
		
		if (top.ID().equals(box))
		{							
			int sumCap = 0;
			
			if (!lifters.containsKey(top))
			{
				Vector<Crane> crs = new Vector<Crane>();
				crs.add(cranes.get(agent));
				lifters.put(top, crs);					
			}
			else 
			{
				lifters.get(top).add(cranes.get(agent));
			}
			
				for (Crane c : lifters.get(top))
				{
					if(c==null)
					{
						StringBuilder builder = new StringBuilder();
						Set<Box> boxes = lifters.keySet();
						for(Box check : boxes)
						{
							builder.append(Integer.toString(check.hashCode()));
							builder.append(' ');
						}
						System.out.printf("Could not find %d in %s\n", top.hashCode(), builder);	
						try{System.in.read();}
						catch(Throwable exception){}
						System.exit(1);
					}
					sumCap += c.capacity();							
				}
			logger.info("LIFTACT: "+ sumCap +"  vs "+top.weight());
			if (sumCap >= top.weight())
			{
				stack1.pop();
				lifters.remove(top);
				logger.info("LIFTACT: "+ top.ID() +"  lifted by "+agent);				
				return true;
			}
		}		
			return false;
	}
	
	/* Signin Action */
	public Boolean signin(String ag)
	{
		int c = (rnd.nextInt(2) + 1) * 5;		
		cranes.put(ag,new Crane(ag, c));
		return true;
	}
	
	/* Initializes the stack */
	public void loadTheShip()
	{
		int i;
		for(i=0;i<10;i++)
		{
			int w = rnd.nextInt(3) + 1;  // Boxes with 10 & 20 & 30 lbs
			stack1.push(new Box(w*10));
		}
	}
	
	/* A Silly Way to Print The Stack Elements!!! */
	public void printStack(Stack<Box> s)
	{
		Stack<Box> temp = (Stack<Box>)s.clone();
		Box b;
		String st = "Stack contents:  ";
		
		while ( !temp.empty() )
        	{
				b = (Box)temp.pop();
				st+=b.weight()+" ," ;                
            }
		logger.info(st);
	}
}

