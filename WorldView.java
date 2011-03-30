import jason.environment.grid.GridWorldView;
import java.awt.Color;
import java.awt.Graphics;

public class WorldView extends GridWorldView {
	
	public WorldView(WorldModel model)
	{
		super(model,"The Shipping Yard",500);
		//setVisible(true);
		repaint();
	}
	
	@Override
    public void drawAgent(Graphics g, int x, int y, Color c, int id) {
       
    }
	
	public void drawBlock(Graphics g, int x, int y) {
       
    }
}
