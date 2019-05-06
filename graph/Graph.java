package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author MMADI
 *
 */
public class Graph {
	private String nameG;
	private Map<Vertex, List<Vertex>> graph;

	/**
	 * @param nameG
	 * @param graphe
	 */
	public Graph(String nameG, Map<Vertex, List<Vertex>> graphe) {
		this.nameG = nameG;
		this.graph = graphe;
	}

	/**
	 * @return
	 */
	public Map<Vertex, List<Vertex>> getGraph() {
		return graph;
	}

	public void setGraph(HashMap<Vertex, List<Vertex>> graph) {
		this.graph = graph;
	}

	/**
	 * @param v
	 * @return
	 */
	public List<Vertex> getAdjacent(Vertex v) {
		List<Vertex> ret = new ArrayList<>();
		for(Vertex vertex : graph.get(v)) {
			Vertex newV = new Vertex(vertex.getNameV(), vertex.getJunction(), 
					vertex.getWeight(), vertex.isObstacle(), vertex.getDirection());
			newV.setFather(v);
			ret.add(newV);
			
		}
		return ret;
	}

	
	
	
	/**
	 * @return
	 */
	public Set<Vertex> getVertex() {
		return graph.keySet();
	}

	/**
	 * @return the nameG
	 */
	public String getNameG() {
		return nameG;
	}

	/**
	 * @param v1
	 * @param v2
	 * @return
	 */
	public int getWeightEdge(Vertex v1, Vertex v2) {
		return v1.getWeight() + v2.getWeight();
	}

	public void printAdjacent(List<Vertex> adj) {
		System.out.println("affice s :");
		for (Vertex vertex : adj) {
			System.out.println(vertex.getNameV());
		}
	}

	public static void main(String[] args) {
		Graph g = new Graph("graphe partiel", CreatGraph.compet1());
		List<Vertex> voisinB = g.getAdjacent(new Vertex("B", 0, 5, false,  Direction.STRAIGHT));
		List<Vertex> voisinC = g.getAdjacent(new Vertex("C", 0, 2, false,  Direction.STRAIGHT));
		Vertex AC = null;
		Vertex AB = null;
		for (Vertex vertex : voisinB) {
			if (vertex.getNameV().equals("A")) {
				AB = vertex;
			}
		}
		
		for (Vertex vertex : voisinC) {
			if (vertex.getNameV().equals("A")) {
				AC = vertex;
			}
		}
		AB.setFather(AC);
//		voisinB = g.getAdjacent(new Vertex("B", 0, 5, false,  Direction.STRAIGHT));
		
		System.out.println(AC);
		System.out.println(AB);
		
		for (Vertex vertex : voisinB) {
			System.err.println(vertex);
		}
//		
//		for (Vertex vertex : voisinC) {
//			System.out.println(vertex);
//		}
	}

}
