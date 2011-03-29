// Environment code for project CargoWorld.mas2j

import jason.asSyntax.*;
import jason.environment.*;
import java.util.logging.*;
import java.util.Stack;
import java.util.Random;

public class ShippingYard extends Environment {

    private Logger logger = Logger.getLogger("CargoWorld.mas2j."+ShippingYard.class.getName());
	
	// The Model Info
	private Stack<Box> stack1 = new Stack<Box>();
	private Random rnd = new Random();
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
    public boolean executeAction(String agName, Structure action) {
       		
		Boolean result = false;
		
		if (action.getFunctor().equals("lift")) {
			
			logger.info("lift: "+action.getTerm(0).toString()+" > "+action.getTerm(1).toString()); 
			result = lift(action.getTerm(0).toString(),action.getTerm(1).toString());	//Call the move() method to perform the move action								
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
	}
	
	/* The Move Action */
	public Boolean lift(String box, String truck)
	{
		if (((Box)stack1.peek()).ID().equals(box))
		{			
			stack1.pop();
			return true;
		}
		else	
			return false;
	}
	
	
	/* Initializes the stack */
	public void loadTheShip()
	{
		int i;
		for(i=0;i<10;i++)
		{
			int w = rnd.nextInt(2) + 1;  // Boxes with 10 & 20 lbs
			stack1.push(new Box(w*10));
		}
	}
	
	/* A Silly Way to Print The Stack Elements!!! */
	public void printStack(Stack<Box> s)
	{
		Stack<Box> temp = (Stack<Box>)s.clone();
		Box b;
		
		System.out.print("Stack contents:  ");
		while ( !temp.empty() )
        	{
				b = (Box)temp.pop();
				System.out.print (b.weight()+" ," );                
            }
		System.out.println("");
	}
}

