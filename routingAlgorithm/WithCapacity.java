package routingAlgorithm;

import graph.Direction;
import graph.Graph;
import graph.Vertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import routingAlgorithm.Astar;

public class WithCapacity {
	private static Graph g;
	private int load = 0;
	private int capacity = 2;
	private Vertex start;
	private ArrayList<Vertex> aims = new ArrayList<>();
	private ArrayList<Vertex> hosp = new ArrayList<>();

	public WithCapacity(Graph g, int capacity, Vertex start, Vertex ... goals) {
		this.g = g;
		this.capacity = capacity;
		this.start = start;
		for(Vertex v : goals)
			this.aims.add(v);
	}
	
	public void addHosp(Vertex v) {
		this.hosp.add(v);
	}
	
	public void setStart(Vertex v) {
		this.start = v;
	}

	public static void main(String[] args) {
		HashMap<Vertex, List<Vertex>> graphe = new HashMap<>();
		List<Vertex> voisinsA = new ArrayList<>();
		Vertex A = new Vertex("A", 0, 10, false, Direction.STRAIGHT);
		voisinsA.add(new Vertex("B", 0, 15, false, Direction.LEFT));
		voisinsA.add(new Vertex("C", 0, 8, false, Direction.RIGH));
		voisinsA.add(new Vertex("D", 0, 6, false, Direction.LEFT));
		voisinsA.add(new Vertex("F", 0, 20, false, Direction.RIGH));
		graphe.put(A, voisinsA);

		List<Vertex> voisinsB = new ArrayList<>();
		Vertex B = new Vertex("B", 0, 15, false, Direction.STRAIGHT);
		voisinsB.add(new Vertex("A", 0, 10, false, Direction.RIGH));
		voisinsB.add(new Vertex("C", 0, 8, false, Direction.LEFT));
		voisinsB.add(new Vertex("E", 0, 6, false, Direction.RIGH));
		voisinsB.add(new Vertex("F", 0, 20, false, Direction.LEFT));
		graphe.put(B, voisinsB);

		List<Vertex> voisinsC = new ArrayList<>();
		Vertex C = new Vertex("C", 0, 8, false, Direction.STRAIGHT);
		voisinsC.add(new Vertex("A", 0, 10, false, Direction.LEFT));
		voisinsC.add(new Vertex("B", 0, 15, false, Direction.RIGH));
		voisinsC.add(new Vertex("D", 0, 6, false, Direction.RIGH));
		voisinsC.add(new Vertex("E", 0, 6, false, Direction.LEFT));
		graphe.put(C, voisinsC);

		List<Vertex> voisinsD = new ArrayList<>();
		Vertex D = new Vertex("D", 0, 6, false, Direction.STRAIGHT);
		voisinsD.add(new Vertex("A", 0, 10, false, Direction.RIGH));
		voisinsD.add(new Vertex("C", 0, 8, false, Direction.LEFT));
		voisinsD.add(new Vertex("E", 0, 6, false, Direction.RIGH));
		voisinsD.add(new Vertex("F", 0, 20, false, Direction.LEFT));
		graphe.put(D, voisinsD);

		List<Vertex> voisinsE = new ArrayList<>();
		Vertex E = new Vertex("E", 0, 6, false, Direction.STRAIGHT);
		voisinsE.add(new Vertex("B", 0, 15, false, Direction.LEFT));
		voisinsE.add(new Vertex("C", 0, 8, false, Direction.RIGH));
		voisinsE.add(new Vertex("D", 0, 6, false, Direction.LEFT));
		voisinsE.add(new Vertex("F", 0, 20, false, Direction.RIGH));
		graphe.put(E, voisinsE);

		List<Vertex> voisinsF = new ArrayList<>();
		Vertex F = new Vertex("F", 0, 20, false, Direction.STRAIGHT);
		voisinsF.add(new Vertex("A", 0, 10, false, Direction.LEFT));
		voisinsF.add(new Vertex("B", 0, 15, false, Direction.RIGH));
		voisinsF.add(new Vertex("D", 0, 6, false, Direction.RIGH));
		voisinsF.add(new Vertex("E", 0, 6, false, Direction.LEFT));
		graphe.put(F, voisinsF);

		Graph g = new Graph("testdij", graphe);
		WithCapacity Routing = new WithCapacity(g, 2, A, B, F, E);
		Routing.setStart(A);
		Routing.addHosp(B);

		Astar tmp1, tmp2, tmp3;
		int min = 250;
		int min_tmp;

		List<Vertex> dir = new ArrayList<>();
		List<Direction> result = new ArrayList<>();

		for(int i = 0; i < Routing.aims.size(); i += 1) {
			dir.clear();
			// start à A
			dir.add(new Vertex("B", 0, 15, false, Direction.RIGHT));
			dir.add(new Vertex("C", 0, 8, false, Direction.LEFT));
			tmp1 = new Astar(g, Routing.aims.get(i), A);
			tmp1.aStar(dir);
			System.out.println("A -> " + Routing.aims.get(i).getNameV() + " : " + tmp1.getRateOfRouting());
			for(int j = 0; j < Routing.aims.size(); j += 1) {
				if(i != j) {
					dir.clear();
					dir.addAll(tmp1.getAdjacentFilter(tmp1.getGoal())); // mettre à jour la direction pour le prochain astar
					tmp2 = new Astar(g, Routing.aims.get(j), tmp1.getGoal());
					tmp2.aStar(dir);
					System.out.println(tmp1.getGoal().getNameV() + " -> " + Routing.aims.get(j).getNameV() + " : " + tmp2.getRateOfRouting());
					// System.out.println(tmp2.getDirectionOrder() + " " + tmp1.getDirectionOrder());
					for(int k = 0; k < Routing.hosp.size(); k += 1) {
						dir.clear();
						dir.addAll(tmp2.getAdjacentFilter(tmp2.getGoal())); // mettre à jour la direction pour le prochain astar
						tmp3 = new Astar(g, Routing.hosp.get(k), tmp2.getGoal());
						tmp3.aStar(dir);
						System.out.println(tmp2.getGoal().getNameV() + " -> " + Routing.hosp.get(k).getNameV() + " : " + tmp3.getRateOfRouting());
						
						min_tmp = tmp1.getRateOfRouting() + tmp2.getRateOfRouting() + tmp3.getRateOfRouting();
						System.out.println(min_tmp);
						if(min > min_tmp) {
							min = min_tmp;
							result.clear();
							result.addAll(tmp1.getDirectionOrder());
							result.addAll(tmp2.getDirectionOrder());
							result.addAll(tmp3.getDirectionOrder());
						}
					}
				}
			}
			// System.out.println(tmp1.getAdjacentFilter(tmp1.getGoal()) + " " + tmp1.getRateOfRouting());
		}
		System.out.println(result);
		
		
		/* System.out.println(Routing.aims.size());
		for(Routing.load = 0; Routing.load < Routing.capacity; Routing.load += 1) {
			for(int i = 0; i < Routing.aims.size(); i += 1) {
				tmp1 = (new Astar(g, Routing.aims.get(i), Routing.start)).getRateOfRouting();
				tmp1.aStar(dir);
				System.out.println((new Astar(g, Routing.aims.get(2), Routing.start)).getRateOfRouting());
				for(int j = i + 1; j < Routing.aims.size(); j += 1) {
					// System.out.println("j ++");
					tmp2 = tmp1;
					tmp2 += (new Astar(g, Routing.aims.get(j), Routing.aims.get(i))).getRateOfRouting();
					for(int k = 0; k < Routing.hosp.size(); k += 1) {
						tmp3 = tmp2;
						tmp3 += (new Astar(g, Routing.hosp.get(k), Routing.aims.get(j))).getRateOfRouting();
						if(tmp3 < min) min = tmp3;
						// System.out.println(i + " " + j + " " + k);
						// System.out.println(tmp1 + " - " + tmp2 + " - " + tmp3);
					}
				}
			}
		}
		*/

	}
}
