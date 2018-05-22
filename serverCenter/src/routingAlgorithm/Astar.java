package routingAlgorithm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
	private List<Pair> listCreate = new ArrayList<>();
	private List<Pair> listSeen = new ArrayList<>(); // List of developed vertex during the routing
	private Vertex goal; // inform the point of arrive
	private Vertex start; // inform the point of departure
	private int rateOfRouting = 0;
	private List<Vertex> orientation;

	/**
	 * @param graph
	 *            : the map
	 * @param goal
	 *            : inform the point of arrive
	 * @param start
	 *            : inform the point of departure
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
	 * @param v
	 *            : the vertex to compare with an objective
	 * @return : true if its same
	 */
	private boolean isGoal(Vertex v) {
		return this.goal.compareVertex(v);
	}

	/**
	 * @return : list of direction, from the beginig at the destination
	 */
	public List<Direction> getDirectionOrder() {
		List<Direction> ret = new ArrayList<>();
		for (Vertex v : order) {
			ret.add(v.getDirection());
		}
		return ret;
	}

	/**
	 * @param v
	 *            : the vertex who want to calculate the <heuristique>
	 * @return : the worth of this vertex
	 */
	private int heuristique(Vertex v) {
		List<Vertex> adjFilter = getAdjacentFilter(v);
		if (isGoal(v)) {
			return 0;
		} else {
			if (v.isObstacle()) {
				return INFINI;
			} else {
				int w = 0;//adjFilter.get(0).getWeight();
				for (Vertex vertex : adjFilter) {
					if (vertex.getWeight() < w) {
						w += vertex.getWeight();
					}
				}
				return w + v.getWeight();
			}
		}
	}

	/**
	 * @param v
	 *            : the vertex who want to calculate the rate
	 * @return : the rate of this vertex
	 */
	private int eval(Vertex v) {
		return v.getRate() + heuristique(v);
	}

	private void addfirt(Pair p) {
		if (listCreate.isEmpty()) {
			listCreate.add(p);
			return;
		}
		if (!p.getV().equals(start)) { // car on ferrait un detour inutile
			for (int i = 0; i <= listCreate.size(); i++) {
				if (i == listCreate.size()) {// cas ou on tous parcourus à optimiser au cas ou
					listCreate.add(i, p);
					return;
				} else {
					if (p.getRate() < listCreate.get(i).getRate()) {
						listCreate.add(i, p);
						return;
					}
				}
			}
		}
	}

	private List<Vertex> getVoisinInvFace(List<Vertex> listFace, List<Vertex> adj) {
		List<Vertex> ret = new ArrayList<>();
		for (Vertex vertex : adj) {
			if (!listFace.contains(vertex)) {
				ret.add(vertex);
			}
		}
		return ret;
	}
	
	
	private List<Vertex> filter(List<Vertex> adj) {
		List<Vertex> array = new ArrayList<>();
		
		for (int i = 0; i < adj.size(); i++) {
		    for (int j = 0; j < adj.size(); j++) {
		    	if (i != j && adj.get(i).equals(adj.get(j))) {
					array.add(adj.get(i));
					array.add(adj.get(j));
					return array;
				}
			}
		}
		return array;
	}
	
	/**
	 * @param v
	 *            : vertex who we want get the filter adjacent
	 * @param voisinFace
	 * @return : the vertexes adjacent to v and who haven't a same father as v
	 */
	private List<Vertex> getAdjacentFilter(Vertex v) {
		Vertex father = v.getFather();
		List<Vertex> filter = new ArrayList<>();
		List<Vertex> adj = graph.getAdjacent(v);
		List<Vertex> voisinFace = father.getVoisinsFace();
		if (v.isObstacle()) {
			v.setVoisinsFace(adj);
			return adj;
		}
		if (!voisinFace.contains(v)) { // on est dans le cas ou le fis est ne fait pas face au pere
			voisinFace = getVoisinInvFace(voisinFace, graph.getAdjacent(father));
		}
		for (Vertex vertex : adj) {
			if (!voisinFace.contains(vertex) && !vertex.equals(father)) {
				vertex.setRate(v.getRate() + vertex.getWeight());
				filter.add(vertex);
			}
		}
		
		// le cas d'une boucle 
		if (filter.size() == 1) {
			Vertex temp = filter.get(0);
//			Scanner sc = new Scanner(System.in);
//			System.out.println("sommet en question "+temp.getNameV()+"\nest je suis "+v);
//			graph.printAdjacent(adj);
//			sc.nextInt();
			for (Vertex vertex : filter(adj)) {
				if (temp.getDirection() != vertex.getDirection()){
					filter.add(vertex);
				}
			}
		}
		v.setVoisinsFace(filter);
		return filter;
	}

	private void putAdjStarting(List<Vertex> adj, List<Vertex> orientation) {
		Pair p;
		start.setVoisinsFace(orientation);
		for (Vertex vertex : adj) {
			if (orientation.contains(vertex)) {
				vertex.setRate(vertex.getWeight());
			} else {
				vertex.setRate(vertex.getWeight() + vertex.getFather().getWeight());
			}
			p = new Pair(vertex, eval(vertex));
			addfirt(p);
		}
	}

	/**
	 * @param adj
	 *            : list of vertex adjacent
	 */
	private void putAdj(List<Vertex> adj) {
		Pair p;
		for (Vertex vertex : adj) {
			p = new Pair(vertex, eval(vertex));
			boolean find = false;
			for (Pair pair : listSeen) {
				if (pair.getV().equals(vertex)) {
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
			return listCreate.remove(0);
		}
		return null;
	}

	/**
	 * modify the list of order, insert the different step to attain the destination
	 * 
	 * @param v
	 *            : the goal
	 */
	private void constOrder(Vertex v) {
		boolean go = true;
		Vertex vertex;
		Vertex oldVertex;
		order.add(0, v);
//		rateOfRouting = v.getRate();
		vertex = v;
		while (go) {
			oldVertex = vertex;
			vertex = vertex.getFather();
			if (!vertex.getFather().compareVertex(vertex)) {
				order.add(0, vertex);
//				rateOfRouting += vertex.getWeight();
			} else {
				if (!orientation.contains(oldVertex)) {
//					rateOfRouting += vertex.getRate();
				}
				go = false; // stop while
			}
		}
	}

	/**
	 * the Astar algorithm finds a path to the goal with using an evaluation
	 * function
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
			listSeen.add(p);
			if (isGoal(p.getV())) {
				constOrder(p.getV());
				this.goal = p.getV();
				rateOfRouting = this.goal.getRate();
				go = false;
			} else {
				adjcent = getAdjacentFilter(p.getV());
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
		Graph g = new Graph("graphe partiel", CreatGraph.compet1());
		// Vertex I = new Vertex("I", 2, 104, true, Direction.STRAIGHT);
		Vertex goal = new Vertex("J", 0, 5, false, Direction.STRAIGHT);
		Vertex star = new Vertex("L", 0, 13, false, Direction.STRAIGHT);
		List<Vertex> dir = new ArrayList<>();
		dir.add(new Vertex("J", 0, 5, false, Direction.LEFT));
		dir.add(new Vertex("K", 0, 5, false, Direction.RIGHT));
		Astar as = new Astar(g, goal, star);
		as.aStar(dir);
//		g.printAdjacent(as.getOrder());
		System.out.println(as.getRateOfRouting());
		System.out.println(as.getGoal().getFather().getNameV());
		System.out.println(as.getGoal().getVoisinsFace());
//		System.out.println(as.getGoal().getFather().getFather().getVoisinsFace());
//		for (Vertex vertex : g.getVertex()) {
//			Astar as = new Astar(g, vertex, star);
//			as.aStar(dir);
//			System.out.println("de "+star.getNameV()+" a "+vertex.getNameV());
//			g.printAdjacent(as.getOrder());
//			System.out.println("avec un cout de "+as.getRateOfRouting());
//		}

	}

	/**
	 * @author MMadi
	 *
	 */
	private class Pair implements Comparable<Pair> {
		private Vertex v;
		private int rate;

		public Pair(Vertex v, int rate) {
			this.v = v;
			this.rate = rate;
		}

		public boolean equals(Object obj) {
			if (obj != null && obj.getClass() == this.getClass()) {
				Pair p = (Pair) obj;
				return (this.v.getNameV().equals(p.v.getNameV()));
				// return (this.v.getNameV().equals(p.v.getNameV()) && this.rate == p.rate);
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
			return v.getNameV() + "|" + rate;
		}

	}

	/**
	 * @return the order
	 */
	public List<Vertex> getOrder() {
		return order;
	}

}
