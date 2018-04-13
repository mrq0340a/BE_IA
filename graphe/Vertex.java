package graphe;

public class Vertex {
	private char nameV;
	private Direction direction;
	private boolean goal;
	private boolean starting;
	private boolean junction;
	
	public Vertex(char nameV, boolean goal, boolean starting, boolean junction) {
		this.goal = goal; 
		this.junction = junction;
		this.starting = starting;
		this.nameV = nameV;
	}

	/**
	 * @return the direction
	 */
	public Direction getDirection() {
		return direction;
	}

	/**
	 * @param direction the direction to set
	 */
	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	/**
	 * @return the nameV
	 */
	public char getNameV() {
		return nameV;
	}

	/**
	 * @return the goal
	 */
	public boolean isGoal() {
		return goal;
	}

	/**
	 * @return the starting
	 */
	public boolean isStarting() {
		return starting;
	}

	/**
	 * @return the junction
	 */
	public boolean isJunction() {
		return junction;
	}
	
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			Vertex cmp = (Vertex) obj;
			return this.direction == cmp.direction &&
			this.nameV == cmp.nameV;
		}
		return false;
	}
	
}
