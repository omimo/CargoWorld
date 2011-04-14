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
		
		model = new WorldModel(Integer.parseInt(args[0]),Integer.parseInt(args[1]),Integer.parseInt(args[2]));
		view = new WorldView(model);
		model.setView(view);
		        
		updatePercepts();
		model.printStack();
    }

    @Override
    synchronized public boolean executeAction(String agName, Structure action) {
       		
		Boolean result = false;
		
		if (action.getFunctor().equals("lift")) {
			
			// logger.info(agName+ ": lift: "+action.getTerm(0).toString()+" > "+action.getTerm(1).toString()); 
			// result = model.lift(agName,action.getTerm(0).toString(),action.getTerm(1).toString());	//Call the move() method to perform the move action				
			//~ Modified by Pai
			// 2nd parameter removed
			logger.info(agName+ ": lift: "+action.getTerm(0).toString()); 
			result = model.lift(agName,action.getTerm(0).toString());	//Call the move() method to perform the move action	
		}
		
		else if (action.getFunctor().equals("signIn")) {
			
			logger.info("Agent: "+agName + " just signedin."); 
			if (agName.contains("truck"))
				result = model.signin(agName,action.getTerm(0).toString());
			else
				result = model.signin(agName);							
		}
		
		else if (action.getFunctor().equals("signOut")) {
			
			logger.info("Agent: "+agName + "  signed out."); 
			result = model.signout(agName);							
		}
		
		else if (action.getFunctor().equals("moveAndDrop")) {
			
			logger.info("Agent: "+agName + "  moveAndDrop( "+ action.getTerm(0).toString()+" , "+action.getTerm(1).toString()+" )"); 
			result = model.moveAndDrop(agName,action.getTerm(0).toString(),action.getTerm(1).toString());							
		}
		
		else if (action.getFunctor().equals("truckArrive")) {
			
			logger.info("Truck: "+agName + " arrived to the site"); 
			result = model.truckArrive(agName);							
		}
		
		else if (action.getFunctor().equals("truckLeave")) {
			
			logger.info("Truck: "+agName + " left to the site"); 
			result = model.truckLeave(agName);							
		}
		
		else
		{
			logger.info("executing: "+action+", but not implemented!");
		}
		
		updatePercepts();
		
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
		
		for (Box top: model.getBoxesOnTop()) //weight(BoxID,Weight) , onTop(BoxID)
		{			
			addPercept(Literal.parseLiteral("weight("+top.ID()+","+top.weight()+")")); // So far we just know the info of the top Boxes
			addPercept(Literal.parseLiteral("onTop("+top.ID()+")"));
		}
				
		for (Crane c : model.getCranes()) // capacity(CraneID,Capacity)
		{			
			addPercept(Literal.parseLiteral("capacity("+c.ID()+","+ c.capacity()+")"));
		}			
		
		for (Truck t : model.getTrucks()) // capacity(TruckID,Capacity)
		{			
			addPercept(Literal.parseLiteral("capacity("+t.ID()+","+ t.capacity()+")"));
		}
		
		for (String t : model.getTrucksOnSite()) // onSite(TruckID)
		{			
			addPercept(Literal.parseLiteral("onSite("+t+")"));
		}
		
		for (Box b : model.getLiftBoxes())  // Who is lifting what! 
		{			
				for (Crane c : model.getLiftersOf(b))
					addPercept(Literal.parseLiteral("lifting("+c.ID()+","+b.ID()+")"));
		}
		
		for (Box b : model.getLiftedBoxes())  // Boxes that are lifted 
		{							
			addPercept(Literal.parseLiteral("lifted("+b.ID()+")"));
		}
		
	}
	
	
	
	
	

}

