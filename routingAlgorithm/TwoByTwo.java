package routingAlgorithm;

import graph.Direction;
import graph.Graph;
import graph.Vertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TwoByTwo {
	private Graph g;
	private Vertex start;
	private List<Vertex> path = new ArrayList<>();
	private List<Vertex> aims = new ArrayList<>();
	private List<Vertex> hosp = new ArrayList<>();
	public TwoByTwo(Graph g, Vertex start, Vertex ... aims) {
		this.g = g;
		this.start = start;
		for(Vertex v : aims)
			this.aims.add(v);
	}
	
	public void addHosp(Vertex v) {
		this.hosp.add(v);
	}

	public static void main(String[] args) {
		HashMap<Vertex, List<Vertex>> graph = new HashMap<>();
		List<Vertex> voisinsA = new ArrayList<>();
		Vertex A = new Vertex("A", 0, 13, false, Direction.STRAIGHT);
		voisinsA.add(new Vertex("B", 0, 5, false, Direction.LEFT));
		voisinsA.add(new Vertex("C", 0, 2, false, Direction.RIGHT));
		voisinsA.add(new Vertex("H", 0, 6, false, Direction.LEFT));
		voisinsA.add(new Vertex("L", 0, 13, false, Direction.RIGHT));
		graph.put(A, voisinsA);
		
		List<Vertex> voisinsB = new ArrayList<>();
		Vertex B = new Vertex("B", 0, 5, false,  Direction.STRAIGHT);
		voisinsB.add(new Vertex("A", 0, 13, false, Direction.RIGHT));
		voisinsB.add(new Vertex("C", 0, 2, false, Direction.LEFT));
		voisinsB.add(new Vertex("F", 0, 2, false, Direction.LEFT));
		voisinsB.add(new Vertex("D", 0, 2, false, Direction.RIGHT));
		graph.put(B, voisinsB);
		
		List<Vertex> voisinsC = new ArrayList<>();
		Vertex C = new Vertex("C", 0, 2, false,  Direction.STRAIGHT);
		voisinsC.add(new Vertex("B", 0, 5, false,  Direction.RIGHT));
		voisinsC.add(new Vertex("A", 0, 13, false,  Direction.LEFT));
		voisinsC.add(new Vertex("E", 0, 2, false,  Direction.RIGHT));
		voisinsC.add(new Vertex("D", 0, 2, false,  Direction.LEFT));
		graph.put(C, voisinsC);
		
		List<Vertex> voisinsD = new ArrayList<>();
		Vertex D = new Vertex("D", 0, 2, false,  Direction.STRAIGHT);
		voisinsD.add(new Vertex("B", 0, 5, false,  Direction.LEFT));
		voisinsD.add(new Vertex("E", 0, 2, false,  Direction.LEFT));
		voisinsD.add(new Vertex("F", 0, 2, false,  Direction.RIGHT));
		voisinsD.add(new Vertex("C", 0, 2, false,  Direction.RIGHT));
		graph.put(D, voisinsD);
		
		List<Vertex> voisinsE = new ArrayList<>();
		Vertex E = new Vertex("E", 0, 2, false,  Direction.STRAIGHT);
		voisinsE.add(new Vertex("G", 0, 2, false,  Direction.LEFT));
		voisinsE.add( new Vertex("D", 0, 2, false,  Direction.RIGHT));
		voisinsE.add(new Vertex("H", 0, 6, false,  Direction.RIGHT));
		voisinsE.add(new Vertex("C", 0, 2, false,  Direction.LEFT));
		graph.put(E, voisinsE);
		
		List<Vertex> voisinsF = new ArrayList<>();
		Vertex F = new Vertex("F", 0, 2, false,  Direction.STRAIGHT);
		voisinsF.add(new Vertex("G", 0, 2, false,  Direction.RIGHT));
		voisinsF.add( new Vertex("D", 0, 2, false,  Direction.LEFT));
		voisinsF.add(new Vertex("B", 0, 5, false,  Direction.RIGHT));
		voisinsF.add(new Vertex("I", 0, 8, false,  Direction.LEFT));
		graph.put(F, voisinsF);
		
		List<Vertex> voisinsG = new ArrayList<>();
		Vertex G = new Vertex("G", 0, 2, false,  Direction.STRAIGHT);
		voisinsG.add( new Vertex("E", 0, 2, false,  Direction.RIGHT));
		voisinsG.add(new Vertex("F", 0, 2, false,  Direction.LEFT));
		voisinsG.add(new Vertex("I", 0, 8, false, Direction.LEFT));
		voisinsG.add(new Vertex("H", 0, 6, false, Direction.RIGHT));
		graph.put(G, voisinsG);
		
		List<Vertex> voisinsH = new ArrayList<>();
		Vertex H = new Vertex("H", 0, 6, false, Direction.STRAIGHT);
		voisinsH.add( new Vertex("E", 0, 2, false,  Direction.LEFT));
		voisinsH.add(new Vertex("G", 0, 2, false,  Direction.RIGHT));
		voisinsH.add(new Vertex("A", 0, 13, false,  Direction.RIGHT));
		voisinsH.add(new Vertex("L", 0, 13, true,  Direction.LEFT));
		graph.put(H, voisinsH);
		
		List<Vertex> voisinsI = new ArrayList<>();
		Vertex I = new Vertex("I", 0, 8, false, Direction.STRAIGHT);
		voisinsI.add(new Vertex("F", 0, 2, false, Direction.RIGHT));
		voisinsI.add(new Vertex("G", 0, 2, false, Direction.LEFT));
		voisinsI.add(new Vertex("K", 0, 5, false, Direction.LEFT));
		voisinsI.add(new Vertex("J", 0, 5, false, Direction.RIGHT));
		graph.put(I, voisinsI);
		
		List<Vertex> voisinsJ = new ArrayList<>();
		Vertex J = new Vertex("J", 0, 5, false, Direction.STRAIGHT);
		voisinsJ.add(new Vertex("I", 0, 8, false, Direction.LEFT));
		voisinsJ.add(new Vertex("K", 0, 5, false, Direction.RIGHT));
		voisinsJ.add(new Vertex("K", 0, 5, false, Direction.LEFT));
		voisinsJ.add(new Vertex("L", 0, 13, false, Direction.RIGHT));
		graph.put(J, voisinsJ);
		
		
		List<Vertex> voisinsK = new ArrayList<>();
		Vertex K = new Vertex("K", 0, 5, false, Direction.STRAIGHT);
		voisinsK.add(new Vertex("I", 0, 8, false, Direction.RIGHT));
		voisinsK.add(new Vertex("J", 0, 5, false, Direction.RIGHT));
		voisinsK.add(new Vertex("J", 0, 5, false, Direction.LEFT));
		voisinsK.add(new Vertex("L", 0, 13, false, Direction.LEFT));
		graph.put(K, voisinsK);
		
		List<Vertex> voisinsL = new ArrayList<>();
		Vertex L = new Vertex("L", 0, 13, false, Direction.STRAIGHT);
		voisinsL.add(new Vertex("A", 0, 13, false, Direction.LEFT));
		voisinsL.add(new Vertex("K", 0, 5, false, Direction.RIGHT));
		voisinsL.add(new Vertex("J", 0, 5, false, Direction.LEFT));
		voisinsL.add(new Vertex("H", 0, 6, false, Direction.RIGHT));
		graph.put(L, voisinsL);

		Graph g = new Graph("testdij", graph);
		TwoByTwo Routing = new TwoByTwo(g, L, H, I, K);
		Routing.addHosp(B);
		
		Astar as;
		Vertex currentGoal = null;
		Vertex currentGoal2 = null;
		List<Vertex> currentDir = new ArrayList<>();
		List<Vertex> tmpOrder = new ArrayList<>();
		
		
		List<Vertex> dir = new ArrayList<>();
		dir.add(new Vertex("J", 0, 5, false, Direction.LEFT));
		dir.add(new Vertex("K", 0, 5, false, Direction.RIGHT));

		int min = 250;
		for(int i = 0; i < Routing.aims.size(); i += 1){
			as = new Astar(g, Routing.aims.get(i), Routing.start);
			as.aStar(dir);
			if(min > as.getRateOfRouting()){
				min = as.getRateOfRouting();
				currentGoal = as.getGoal();
				currentDir = currentGoal.getVoisinsFace();
				tmpOrder = as.getOrder();
			}
		}
		Routing.aims.remove(currentGoal);
		Routing.path.addAll(tmpOrder);
		
		min = 250;
		for(int i = 0; i < Routing.aims.size(); i += 1){
			as = new Astar(g, Routing.aims.get(i), currentGoal);
			as.aStar(currentDir);
			if(min > as.getRateOfRouting()){
				min = as.getRateOfRouting();
				currentGoal2 = as.getGoal();
				dir = currentGoal2.getVoisinsFace();
				tmpOrder = as.getOrder();
			}
		}
		Routing.aims.remove(currentGoal2);
		Routing.path.addAll(tmpOrder);

		min = 250;
		for(int i = 0; i < Routing.hosp.size(); i += 1){
			as = new Astar(g, Routing.hosp.get(i), currentGoal);
			as.aStar(dir);
			if(min > as.getRateOfRouting()){
				min = as.getRateOfRouting();
				currentGoal = as.getGoal();
				currentDir = currentGoal.getVoisinsFace();
				tmpOrder = as.getOrder();
			}
		}
		Routing.path.addAll(tmpOrder);
		
		Vertex lastV = Routing.path.get(1);
		for(int i = 0; i < Routing.path.size(); i += 1){
			if(lastV.getNameV() != Routing.path.get(i).getNameV())
				System.out.print(Routing.path.get(i).getNameV() + " -> ");
			lastV = Routing.path.get(i);
		}
		System.out.println("BUT");
	}
}
