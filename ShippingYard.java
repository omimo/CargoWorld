// Environment code for project CargoWorld.mas2j

import jason.asSyntax.*;
import jason.environment.*;
import java.util.logging.*;

public class ShippingYard extends Environment {

    private Logger logger = Logger.getLogger("CargoWorld.mas2j."+ShippingYard.class.getName());

    /** Called before the MAS execution with the args informed in .mas2j */
    @Override
    public void init(String[] args) {
        super.init(args);
        
    }

    @Override
    public boolean executeAction(String agName, Structure action) {
       		
		if (action.getFunctor().equals("move")) {
			//to-do
			System.out.println(action.getTerm(0).toString(),action.getTerm(1).toString());
			return true;
		}
		else
		{
			logger.info("executing: "+action+", but not implemented!");
		}
		
        return true;
    }

    /** Called before the end of MAS execution */
    @Override
    public void stop() {
        super.stop();
    }
}

