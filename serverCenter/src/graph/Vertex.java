package graph;

import java.util.List;

import graph.Direction;

public class Vertex implements Comparable<Vertex> {
	private String nameV; 
	private Direction direction;
	private boolean goal = false;
	private boolean starting = false;
	private int junction;
	private boolean occupy = false;
	private int weight;
	
	//test 
	private List<Vertex> voisinsFace;

	private boolean obstacle;
	private Vertex father;
	private int rate; //le cout jusqu'a moi

	public Vertex(String nameV, int junction, int weight) {
		this.junction = junction;
		this.nameV = nameV;
		this.weight = weight;
		this.rate = weight;
	}
	
	public Vertex(String nameV, int junction, int weight, 
			boolean obstacle, Direction dir) {
		this.junction = junction;
		this.nameV = nameV;
		this.direction = dir;
		this.weight = weight;
		this.obstacle = obstacle;
		this.rate = weight;
	}
	
	/**
	 * @return the weight
	 */
	public int getWeight() {
		return weight;
	}

	/**
	 * @return the obstacle
	 */
	public boolean isObstacle() {
		return obstacle;
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
	public String getNameV() {
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
	public int isJunction() {
		return junction;
	}
	
	
	public boolean compareVertex(Vertex v) {
		return this.nameV.equals(v.getNameV());
	}
	
	
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			Vertex cmp = (Vertex) obj;
			return this.weight == cmp.getWeight() &&
			this.nameV.equals(cmp.getNameV());
		}
		return false;
	}
	
	/**
	 * @param goal the goal to set
	 */
	public void setGoal(boolean goal) {
		this.goal = goal;
	}

	/**
	 * @param starting the starting to set
	 */
	public void setStarting(boolean starting) {
		this.starting = starting;
	}

	@Override
	public int hashCode() {
		return 31*nameV.hashCode()+weight;
	}
	
	/**
	 * @return the occupy
	 */
	public boolean isOccupy() {
		return occupy;
	}

	/**
	 * @param occupy the occupy to set
	 */
	public void setOccupy(boolean occupy) {
		this.occupy = occupy;
	}
	
	
	public String printString() {
		return "{ "+nameV+": dir = "+direction+", wgt = "+weight+" rate = "+rate+" }";
	}
	
	@Override
	public String toString() {
		return "{ "+nameV+": dir = "+direction+", wgt = "+weight+" rate = "+rate+
				" pere : "+father.getNameV()+" rate father : "+father.getRate()+" }";
	}

	/**
	 * @return the junction
	 */
	public int getJunction() {
		return junction;
	}

	/**
	 * @return the father
	 */
	public Vertex getFather() {
		return father;
	}

	/**
	 * @param father the father to set
	 */
	public void setFather(Vertex father) {
		this.father = father;
	}

	/**
	 * @return the rate
	 */
	public int getRate() {
		return rate;
	}

	/**
	 * @param rate the rate to set
	 */
	public void setRate(int rate) {
		this.rate = rate;
	}

	@Override
	public int compareTo(Vertex o) {
		return weight-o.getWeight();
	}

	/**
	 * @return the voisinsFace
	 */
	public List<Vertex> getVoisinsFace() {
		return voisinsFace;
	}

	/**
	 * @param voisinsFace the voisinsFace to set
	 */
	public void setVoisinsFace(List<Vertex> voisinsFace) {
		this.voisinsFace = voisinsFace;
	}
}
