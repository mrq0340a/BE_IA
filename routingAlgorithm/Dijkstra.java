package routingAlgorithm;

import graph.Direction;
import graph.Graph;
import graph.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Dijkstra {
	private static final int INFINITE = 1000; // the cost value of an obstacle
	private Graph graph;
	private Map<Vertex, List<Integer>> tabValues = new LinkedHashMap<>();
	private int currentCost = 0;
	private int finalCost = 0;
	private Vertex goal;
	private Vertex start;
	private static List<Vertex> vertexList = new ArrayList<>();
	private List<Vertex> availableVertex = new ArrayList<>(); // uncovered vertexes
	private List<Integer> availableVertexIndex = new ArrayList<>(); // and their indexes
	private List<Vertex> coveredVertex = new ArrayList<>();
	private List<Vertex> finalPath = new ArrayList<>();

	public Dijkstra(Graph graph, Vertex start, Vertex goal) {
		this.graph = graph;
		this.goal = goal;
		this.start = start;
		this.start.setStarting(true); // TODO : refactoring de l'anglais dans Vertex
		this.goal.setGoal(true);
	}
	
	private int getValue() {
		return finalCost;
	}

	private void init() {
		List<Integer> tmp = new ArrayList<>();
		for (Vertex v : vertexList) {
			if (v.isStarting())
				tmp.add(0);
			else
				tmp.add(INFINITE);
		}
		tabValues.put(start, tmp);
		finalPath.add(start);
	}

	private Vertex chooseBest(Vertex currentChosen) {
		int min = INFINITE;
		int i = 0;
		int current_i;
		Vertex result = null;
		availableVertex.clear();
		availableVertexIndex.clear();

		for (Vertex v1 : vertexList) {
			if (!coveredVertex.contains(v1)) {
				availableVertex.add(v1); // Vertex list that has not been covered
				availableVertexIndex.add(i);
			}
			i += 1;
		}

		i = 0;
		for (Vertex v2 : availableVertex) {
			current_i = availableVertexIndex.get(i);
			if (min >= tabValues.get(currentChosen).get(current_i)) {
				min = tabValues.get(currentChosen).get(current_i);
				result = v2;
			}
			i += 1;
		}
		currentCost = min; // current cost update
		coveredVertex.add(result); // adding result in the covered vertexes list
		return result;
	}

	private void nextStep(Vertex chosen, Vertex lastChosen) {
		int i = 0;
		int calculation;
		List<Vertex> chosenAdj = graph.getAdjacent(chosen);
		List<Integer> tmp = new ArrayList<>();
		List<Vertex> workWith = new ArrayList<>();
		List<Integer> workWithIndex = new ArrayList<>();
		for (Vertex v : availableVertex) { // saving available vertex indexes
			if (chosenAdj.contains(v)) {
				workWith.add(v);
				workWithIndex.add(availableVertexIndex.get(i));
			}
			i += 1;
		}
		
		for (int j = 0; j < vertexList.size(); j += 1) { // new dijkstra line calculation
			if (workWithIndex.contains(j)) {
				calculation = currentCost + vertexList.get(j).getWeight();
				if (calculation <= tabValues.get(lastChosen).get(j))
					tmp.add(calculation);
				else
					tmp.add(tabValues.get(lastChosen).get(j));
			} else
				tmp.add(tabValues.get(lastChosen).get(j));
		}
		tabValues.put(chosen, tmp);
	}

	private void recoverFinalPath() {
		Vertex currentKey;
		int currentVertex = vertexList.indexOf(goal);
		for(int i = vertexList.size() - 1; i >= 0; i -= 1) {
			currentKey = coveredVertex.get(i);
			if(i > 0 && tabValues.get(coveredVertex.get(i)).get(currentVertex) != tabValues.get(coveredVertex.get(i - 1)).get(currentVertex)){
				finalPath.add(currentKey);
				currentVertex =  vertexList.indexOf(currentKey);
			}
		}
		finalPath.add(goal);
	}

	private void run() {
		Vertex chosen = start;
		Vertex lastChosen = start;
		init();
		while (coveredVertex.size() < vertexList.size()) {
			chosen = chooseBest(chosen);
			nextStep(chosen, lastChosen);
			lastChosen = chosen;
		}
		recoverFinalPath();

		for(int i = 0; i < finalPath.size(); i += 1) { // display path and cost
			System.out.print(finalPath.get(i).getNameV());
			finalCost += finalPath.get(i).getWeight();
		}
		System.out.println(" : " + (finalCost - start.getWeight()));
	}

	public static void main(String args[]) {
		HashMap<Vertex, List<Vertex>> graphe = new HashMap<>();
		List<Vertex> voisinsA = new ArrayList<>();
		Vertex A = new Vertex("A", 0, 10, false, Direction.STRAIGHT);
		voisinsA.add(new Vertex("B", 0, 15, false, Direction.LEFT));
		voisinsA.add(new Vertex("C", 0, 8, false, Direction.RIGHT));
		voisinsA.add(new Vertex("D", 0, 6, false, Direction.LEFT));
		voisinsA.add(new Vertex("F", 0, 20, false, Direction.RIGHT));
		graphe.put(A, voisinsA);

		List<Vertex> voisinsB = new ArrayList<>();
		Vertex B = new Vertex("B", 0, 15, false, Direction.STRAIGHT);
		voisinsB.add(new Vertex("A", 0, 10, false, Direction.RIGHT));
		voisinsB.add(new Vertex("C", 0, 8, false, Direction.LEFT));
		voisinsB.add(new Vertex("E", 0, 6, false, Direction.RIGHT));
		voisinsB.add(new Vertex("F", 0, 20, false, Direction.LEFT));
		graphe.put(B, voisinsB);

		List<Vertex> voisinsC = new ArrayList<>();
		Vertex C = new Vertex("C", 0, 8, false, Direction.STRAIGHT);
		voisinsC.add(new Vertex("A", 0, 10, false, Direction.LEFT));
		voisinsC.add(new Vertex("B", 0, 15, false, Direction.RIGHT));
		voisinsC.add(new Vertex("D", 0, 6, false, Direction.RIGHT));
		voisinsC.add(new Vertex("E", 0, 6, false, Direction.LEFT));
		graphe.put(C, voisinsC);

		List<Vertex> voisinsD = new ArrayList<>();
		Vertex D = new Vertex("D", 0, 6, false, Direction.STRAIGHT);
		voisinsD.add(new Vertex("A", 0, 10, false, Direction.RIGHT));
		voisinsD.add(new Vertex("C", 0, 8, false, Direction.LEFT));
		voisinsD.add(new Vertex("E", 0, 6, false, Direction.RIGHT));
		voisinsD.add(new Vertex("F", 0, 20, false, Direction.LEFT));
		graphe.put(D, voisinsD);

		List<Vertex> voisinsE = new ArrayList<>();
		Vertex E = new Vertex("E", 0, 6, false, Direction.STRAIGHT);
		voisinsE.add(new Vertex("B", 0, 15, false, Direction.LEFT));
		voisinsE.add(new Vertex("C", 0, 8, false, Direction.RIGHT));
		voisinsE.add(new Vertex("D", 0, 6, false, Direction.LEFT));
		voisinsE.add(new Vertex("F", 0, 20, false, Direction.RIGHT));
		graphe.put(E, voisinsE);

		List<Vertex> voisinsF = new ArrayList<>();
		Vertex F = new Vertex("F", 0, 20, false, Direction.STRAIGHT);
		voisinsF.add(new Vertex("A", 0, 10, false, Direction.LEFT));
		voisinsF.add(new Vertex("B", 0, 15, false, Direction.RIGHT));
		voisinsF.add(new Vertex("D", 0, 6, false, Direction.RIGHT));
		voisinsF.add(new Vertex("E", 0, 6, false, Direction.LEFT));
		graphe.put(F, voisinsF);

		vertexList.add(A);
		vertexList.add(B);
		vertexList.add(C);
		vertexList.add(D);
		vertexList.add(E);
		vertexList.add(F);
		Graph g = new Graph("testdij", graphe);
		Dijkstra dij = new Dijkstra(g, A, E);
		dij.run();
	}
}
