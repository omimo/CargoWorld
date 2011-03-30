// Environment code for project CargoWorld.mas2j

import jason.asSyntax.*;
import jason.environment.*;
import java.util.logging.*;
import java.util.*;

public class ShippingYard extends Environment {

    private Logger logger = Logger.getLogger("CargoWorld.mas2j."+ShippingYard.class.getName());
	
	WorldModel model;
	WorldView view;
	
    /** Called before the MAS execution with the args informed in .mas2j */
    @Override
    public void init(String[] args) {
        super.init(args);
		
		model = new WorldModel();
		view = new WorldView(model);
		model.setView(view);
		        
		updatePercepts();
		model.printStack();
    }

    @Override
    synchronized public boolean executeAction(String agName, Structure action) {
       		
		Boolean result = false;
		
		if (action.getFunctor().equals("lift")) {
			
			logger.info(agName+ ": lift: "+action.getTerm(0).toString()+" > "+action.getTerm(1).toString()); 
			result = model.lift(agName,action.getTerm(0).toString(),action.getTerm(1).toString());	//Call the move() method to perform the move action								
		}
		
		else if (action.getFunctor().equals("signIn")) {
			
			logger.info("Agent: "+agName + " just signedin."); 
			result = model.signin(agName);							
		}
		
		else
		{
			logger.info("executing: "+action+", but not implemented!");
		}
		
		updatePercepts();
		model.printStack();
		
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
		
		for (Box top: model.getBoxesOnTop())
		{			
			addPercept(Literal.parseLiteral("weight("+top.ID()+","+top.weight()+")")); // So far we just know the info of the top Boxes
			addPercept(Literal.parseLiteral("onTop("+top.ID()+")"));
		}
				
		for (Crane c : model.getCranes())
		{			
			addPercept(Literal.parseLiteral("capacity("+c.ID()+","+ c.capacity()+")"));
		}			
		
		
		for (Box b : model.getLiftBoxes())
		{			
				for (Crane c : model.getLiftersOf(b))
					addPercept(Literal.parseLiteral("lifting("+c.ID()+","+b.ID()+")"));
		}
		
	}
	
	
	
	
	

}

