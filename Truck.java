import jason.asSemantics.Agent;

/**
 * The truck agent class
 *
 */
public class Truck
		extends Agent {
	private static int    nextID = 0;
	private final Integer ID;
	private final Integer weight;

	public Truck(Integer weight) {
		ID          = nextID++;
		this.weight = weight;
	}

	public Integer getID() {
		return ID;
	}

	public Integer getWeight() {
		return weight;
	}

	public String getName() {
		return "truck"+ID;
	}
}
