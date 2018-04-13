package graphe;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("deprecation")
public class Graph {
	private String nameG;
	private Map<Vertex, List<Vertex>> graph;

	public Graph(String nameG, Map<Vertex, List<Vertex>> graphe) {
		this.nameG = nameG;
		this.graph = graphe;
	}
	
	public Map<Vertex, List<Vertex>> getGraph() {
		return graph;
	}

	public void setGraph(HashMap<Vertex, List<Vertex>> graph) {
		this.graph = graph;
	}
	
	public static void main(String[] args) {
		List<Vertex> voisin = new ArrayList<>();
		voisin.add(new Vertex('B', false, false, false));
		Map<Vertex, List<Vertex>> ens = new HashMap<>();
		ens.put(new Vertex('A', false, true, false), voisin);
		Graph g = new Graph("g1", ens);
		System.out.println(voisin);
	}
}
