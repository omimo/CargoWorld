import jason.environment.grid.GridWorldView;
import java.awt.*;
import java.awt.geom.*;

public class WorldView extends GridWorldView {
	
	public WorldView(WorldModel model)
	{
		super(model,"The Shipping Yard",650);
		setVisible(true);
		repaint();
	}
	
	@Override
    public void draw(Graphics g, int x, int y, int object) {

        switch (object) {
            case WorldModel.BOX:   drawBox(g, x, y);  break;
			case WorldModel.PLATE:   drawPlate(g, x, y);  break;
			case WorldModel.TRUCK:   drawTruck(g, x, y);  break;
        }
    }
	
	@Override
    public void drawAgent(Graphics g, int x, int y, Color c, int id) {
		
		if (!(((WorldModel)getModel()).getLabel(x,y) instanceof Crane)) return;
		
		int nCranes = ((WorldModel)getModel()).NO_OF_CRANES;
		int nBoxes = ((WorldModel)getModel()).NO_OF_BOXES;
		int craneID = id + 1;
		
		Graphics2D g2=(Graphics2D)g;		
		g.setColor(Color.darkGray);					

		g.fillRect(x * cellSizeW, 
					(craneID  * 2 - 1)* cellSizeH, 
					cellSizeW, 
					cellSizeH/2);
					
		g.fillRect(x * cellSizeW + 5, 
					(craneID  * 2 - 1)* cellSizeH, 
					cellSizeW / 2, 
					(nBoxes + (nCranes - id) * 2 ) * cellSizeH);
						
		// Draw the Capacity of the Crane								
		int cap = ((Crane)((WorldModel)getModel()).getLabel(x,y)).capacity();					
		
		g.setColor(Color.black);
		drawString(g, x,0 , defaultFont, "C"+(id+1) +"("+ cap + ")");		
    }
	
    public void drawPlate(Graphics g, int x, int y) {        		
		g.setColor(Color.black);
		g.fillRect(x * cellSizeW, y * cellSizeH, cellSizeW, cellSizeH);	      
    }
	
	public void drawTruck(Graphics g, int x, int y) {   
		
		if (!( ((WorldModel)getModel()).getLabel(x,y) instanceof Truck )) return;
		
		Truck tr = ((Truck)((WorldModel)getModel()).getLabel(x,y)); 
		
		g.setColor(new Color(0,102,51));
		g.fillRect(x * cellSizeW, y * cellSizeH, 2*cellSizeW, cellSizeH);
		
		g.setColor(new Color(163,163,0));
		g.fillRect((x+2) * cellSizeW, y * cellSizeH +cellSizeH/2, cellSizeW/2, cellSizeH/2);
		
		g.setColor(new Color(102,102,0));
		g.fillOval(x * cellSizeW + 4, (y+1) * cellSizeH,8,8);
		g.fillOval(x * cellSizeW + 14, (y+1) * cellSizeH,8,8);
		g.fillOval((x+2) * cellSizeW + 4, (y+1) * cellSizeH,8,8);
		
		g.setColor(Color.white);
		drawString(g, x+1, y, defaultFont, "T" + tr.ID().substring(5) + " ("+ tr.capacity()+")");
    }

	public void drawBox(Graphics g, int x, int y) {        		
		int nCranes = ((WorldModel)getModel()).NO_OF_CRANES;		
		Graphics2D g2 = (Graphics2D)g;
				
		g2.setStroke(new BasicStroke(1.0f));
		if (!(((WorldModel)getModel()).getLabel(x,y) instanceof Box)) 
			return;		
		Box b = ((Box)((WorldModel)getModel()).getLabel(x,y));
		
		switch (b.weight()) {
			case 10:g.setColor(Color.yellow);break;
			case 20:g.setColor(Color.orange);break;
			case 30:g.setColor(Color.red);break;
			default : g.setColor(Color.orange);break;
		}
			
        g.fillRect(x * cellSizeW, y * cellSizeH, cellSizeW, cellSizeH);
        g.setColor(Color.black);
        g.drawRect(x * cellSizeW + 2, y * cellSizeH + 2, cellSizeW - 4, cellSizeH - 4);
        drawString(g, x, y, defaultFont, b.weight() + " lbs");
				
		//------------------------
		int p = 0;
		for (Box box : ((WorldModel)getModel()).getBoxesOnTop())
		{
			drawString(g2,nCranes + p,0 , defaultFont, "B("+box.ID().substring(3)+ ")");
			p++;
		}
		//------------------------
		for (Crane c : ((WorldModel)getModel()).getLiftersOf(b.ID()))
		{			
			int craneID = 0;
			try { craneID = Integer.parseInt(c.ID().substring(5));}
			catch (Exception e) {}					
					    		    
		    g2.setStroke(new BasicStroke(8.0f));
			g.setColor(Color.darkGray);
		    
			// The horizontal line from crane to top of box
			g.drawLine((craneID -1) * cellSizeW + 8, 
					   ((craneID) * 2 -1)* cellSizeH + 5,
				 	   x * cellSizeW + (cellSizeW * (nCranes+1 - craneID) -20)/ nCranes,
					   ((craneID) * 2 -1)* cellSizeH + 5
					   );
			
			// The joint
			g2.setStroke(new BasicStroke(1.0f));
			g.fillOval(x * cellSizeW + (cellSizeW * (nCranes+1 - craneID) -20)/ nCranes - 5,
					   ((craneID) * 2 -1)* cellSizeH - 1,
					   10,
					   10
					   );
					   
			// The vertical line from top of box to box
			g2.setStroke(new BasicStroke(4.0f));
		    g.drawLine(x * cellSizeW + ((cellSizeW * (nCranes+1 - craneID) -20)/ nCranes),
					   ((craneID) * 2 -1)* cellSizeH + 5,
				 	   x * cellSizeW + ((cellSizeW * (nCranes+1 - craneID) -20)/ nCranes),
					   y * cellSizeH
					   );
		}
		//------------------------
		
    }
}
