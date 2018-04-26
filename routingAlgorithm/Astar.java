package routingAlgorithm;

import java.util.ArrayList;
import java.util.List;
//import java.util.TreeSet;
import java.util.Scanner;

import graph.CreatGraph;
import graph.Direction;
import graph.Graph;
import graph.Vertex;

/**
 * @author MMADI
 *
 */
public class Astar {
	private static final int INFINI = 1000; // the value of the vertex that has an obstacle  
	private Graph graph;
	private List<Vertex> order = new ArrayList<>(); // list of step 
//	private TreeSet<Pair> listCreate = new TreeSet<>(); // list of created vertex during the routing
	private List<Pair> listCreate = new ArrayList<>();
	private List<Pair> listSeen = new ArrayList<>();  // List of developed vertex during the routing 
	private Vertex goal; // inform the point of arrive 
	private Vertex start; // inform the point of departure  
	private int rateOfRouting = 0;
	private List<Vertex> orientation;
	
	/**
	 * @param graph : the map 
	 * @param goal : inform the point of arrive
	 * @param start : inform the point of departure
	 */
	public Astar(Graph graph, Vertex goal, Vertex start) {
		this.graph = graph;
		this.goal = goal;
		this.start = start;
		this.start.setFather(start); // on initialise son pere 
		this.start.setStarting(true);
		this.goal.setGoal(true);
	}
	
	
	/**
	 * @param v : the vertex to compare with an objective 
	 * @return : true if its same
	 */
	private boolean isGoal (Vertex v) {
		return this.goal.compareVertex(v);
	}
	
	/**
	 * @return : list of direction, from the beginig at the destination
	 */
	public List<Direction> getDirectionOrder () {
		List<Direction> ret = new ArrayList<>();
		for (Vertex v : order) {
			ret.add(v.getDirection());
		}
		return ret;
	}
	
	/**
	 * @param v : the vertex who want to calculate the <heuristique> 
	 * @return : the worth of this vertex 
	 */
	private int heuristique (Vertex v) {
		List<Vertex> adjFilter = graph.getAdjacentFilter(v);
		int rate = adjFilter.get(0).getWeight();
		if (isGoal(v)) {
			return 0;
		}else if (v.isObstacle()) {
			return -INFINI;
		}else {
			for (Vertex vertex : adjFilter) {
				if (vertex.getWeight() < rate) {
					rate = vertex.getWeight();
				}
			}
			return rate+v.getWeight();
		}
	}
	
	/**
	 * @param v : the vertex who want to calculate the rate
	 * @return : the rate of this vertex
	 */
	private int eval (Vertex v) {
		return v.getRate()+heuristique(v);
	}
	
	private void addfirt(Pair p) {
		if (listCreate.isEmpty()) {
			listCreate.add(p);
			return;
		}
		for(int i = 0; i < listCreate.size(); i++) {
			if (p.getRate() < listCreate.get(i).getRate()) {
				listCreate.add(i,p);
				return;
			}
		}
	}
	
	private void putAdjStarting(List<Vertex> adj, List<Vertex> orientation) {
		Pair p;
//		graph.printAdjacent(orientation);
		for (Vertex vertex : adj) {
			if (orientation.contains(vertex)) {
				vertex.setRate(vertex.getWeight());
//				System.out.println("s "+vertex.getNameV()+" rate "+vertex.getRate());
			}
			p = new Pair(vertex, eval(vertex));
			addfirt(p);
		}
	}
	
	/**
	 * @param adj : list of vertex adjacent
	 */
	private void putAdj(List<Vertex> adj) {
		Pair p;
		for (Vertex vertex : adj) {
			p = new Pair(vertex, eval(vertex));
			boolean find = false;
			for (Pair pair : listSeen) {
				if (pair.getV().getNameV() == vertex.getNameV() &&
						pair.getV().getRate()+pair.getV().getWeight() <
						vertex.getRate()+vertex.getWeight()) {
						find = true;
						break; // on arrete la boucle 
				}
			}
			if (!find) {
				addfirt(p);
			}
		}
	}
	
	/**
	 * @return : the first element in the list create 
	 */
	private Pair getFirst() {
		if (!listCreate.isEmpty()) {
			Pair p = listCreate.get(0);
			listCreate.remove(p);
			return p;
		}
		return null;
	}
	
	
	/**
	 * modify the list of order, insert the different step to attain the destination
	 * @param v : the goal 
	 */
	private void constOrder(Vertex  v) {
		boolean go = true;
		Vertex vertex;
		Vertex oldVertex;
		order.add(0, v);
		rateOfRouting = v.getWeight();
		vertex = v;
		while(go) {
			oldVertex = vertex;
			vertex = vertex.getFather();
			if (!vertex.getFather().compareVertex(vertex)) {
				order.add(0, vertex);
				rateOfRouting += vertex.getWeight();
//				System.out.println("r "+vertex.getRate()+" s "+vertex.getNameV());
			}else {
				if (!orientation.contains(oldVertex)) {
					rateOfRouting += vertex.getRate();
				}
				go = false; // stop while
			}
		}
	}

	
	/**
	 * the Astar algorithm finds a path to the goal with using an evaluation function
	 */
	public void aStar(List<Vertex> orientation) {
		if (start.equals(goal)) { // when the goal and the starting are same
			rateOfRouting = start.getRate();
			return;
		}
		this.orientation = orientation;
		List<Vertex> adjcent = graph.getAdjacent(start);
		putAdjStarting(adjcent, orientation);
		Pair p;
		boolean go = true;
		do {
			p = getFirst();
			listSeen.add(p); // on ajoute dans la liste des vues 
//			System.out.println("on choisis : "+p);;
			if (isGoal(p.getV())) {
//				rateOfRouting = p.getRate();
				constOrder(p.getV());
				this.goal = p.getV();
				go = false;
			}else {
				//je cree les fils de ce nouveau sommet choisi
				adjcent = graph.getAdjacentFilter(p.getV());
//				adjcent = graph.getAdjacent(p.getV()); // je garde ma premiere idee
				putAdj(adjcent); // j'ajoute ça dans la liste des creer
			}
		} while (go);
	}

	/**
	 * @return the goal
	 */
	public Vertex getGoal() {
		return goal;
	}


	/**
	 * @return the rateOfRouting
	 */
	public int getRateOfRouting() {
		return rateOfRouting;
	}

	public static void main(String[] args) {
		Graph g = new Graph("graphe partiel", CreatGraph.mapTest());
//		Vertex I = new Vertex("I", 2, 104, true, Direction.STRAIGHT);
		Vertex A = new Vertex("A", 0, 10, false, Direction.STRAIGHT);
//		Vertex A = new Vertex("A", 0, 2, true, Direction.STRAIGHT);
		for (Vertex v : g.getVertex()) {
			Astar as = new Astar(g, v, A);
//			as.aStar();
//			System.out.println("de A "+v.getNameV()+" cout de : "+as.getRateOfRouting()+
//					"  ensemble de commande :"+as.getDirectionOrder());
		}
	}
	



	/**
	 * @author MMadi
	 *
	 */
	private class Pair implements Comparable<Pair>{
		private Vertex v;
		private int rate;
		
		public Pair(Vertex v, int rate) {
			this.v = v; this.rate = rate;
		}
		
		public boolean equals(Object obj) {
			if (obj != null && obj.getClass() == this.getClass()) {
				Pair p = (Pair)obj;
				return (this.v.getNameV().equals(p.v.getNameV()) && this.rate == p.rate);
			}
			return false;
		}

		@Override
		public int compareTo(Pair o) {
			return this.rate - o.rate;
		}
		
		/**
		 * @return the v
		 */
		public Vertex getV() {
			return v;
		}

		/**
		 * @return the rate
		 */
		public int getRate() {
			return rate;
		}
		
		@Override
		public String toString() {
			return v.getNameV()+"|"+ rate;
		}

	}




	/**
	 * @return the order
	 */
	public List<Vertex> getOrder() {
		return order;
	}
	
}
