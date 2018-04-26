package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
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
		Vertex subVertex = new Vertex(v.getNameV(), v.getJunction(), 
				v.getWeight(), v.isObstacle(), Direction.STRAIGHT);
		subVertex.setFather(v.getFather());
		List<Vertex> ret = new ArrayList<>();
		for(Vertex vertex : graph.get(subVertex)) {
			vertex.setRate(v.getRate()+vertex.getWeight());
			if (vertex.getFather() == null) {
				vertex.setFather(v);
			}
			ret.add(vertex);
		}
		return ret;
	}

	/**
	 * @param v : vertex who we want get the filter adjacent 
	 * @return : the vertexes adjacent to v and who haven't a same father as v
	 */
	public List<Vertex> getAdjacentFilter(Vertex v) {
		Vertex father = v.getFather();
		List<Vertex> filter = new ArrayList<>();
		List<Vertex> adj = getAdjacent(v);
		List<Vertex> adjFather = getAdjacent(father);
		for (Vertex vertex : adj) {
			if (adj.size() > 2) {
				if (!adjFather.contains(vertex) && (!father.getNameV().equals(vertex.getNameV()))) {
					vertex.setRate(v.getRate() + vertex.getWeight());
					filter.add(vertex);
				}
			} else {
				if (!father.getNameV().equals(vertex.getNameV())) {
					vertex.setRate(v.getRate() + vertex.getWeight()); // le cout g chemin parcouru jusqu'a la
					filter.add(vertex);
				}
			}

		}
		return filter;
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

//	public static void main(String[] args) {
//		Graph g = new Graph("graphe partiel", CreatGraph.halfMap());
//		Set<Vertex> sommet = g.getVertex();
//		for (Vertex vertex : sommet) {
//			System.out.println("--------" + vertex.getNameV() + "---------");
//			g.printAdjacent(g.getAdjacent(vertex));
//		}
//	}

}
