package routing;

import java.util.ArrayList;
import java.util.List;

import behavior.Cst;
import behavior.TestMarking;
import graph.CreatGraph;
import graph.Direction;
import graph.Graph;
import graph.Vertex;
import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.robotics.subsumption.Behavior;
import mainTest.DataLogger;
import robot.Robot;
import routingAlgorithm.Astar;

/**
 * @author MMADI
 *
 */
public class RoutingWithOneRobot {
	private Graph graph;
	private Robot robot;
	private Behavior[] behavior;
	private List<Vertex> goals = new ArrayList<>();
	private Vertex goal = null;
	private Vertex starting;
	private TestMarking testMarking;

	public RoutingWithOneRobot(Graph graph, Robot robot, Vertex starting, List<Vertex> goals, Behavior[] behavior) {
		this.goals = goals;
		this.robot = robot;
		this.behavior = behavior;
		this.graph = graph;
		this.starting = starting;
		this.testMarking = new TestMarking(robot.getRgtMotor(), robot.getLftMotor(), robot.getRgtLight(),
				robot.getLftLight());
	}

	public void go() {
		behavior[Cst.LINEF].action();
	}

	private List<Vertex> nearVictim(Vertex start, List<Vertex> goals) {
		List<Vertex> ret = null;
		Astar astar;
		int rateRouting = 10000;
		for (Vertex g : goals) {
			astar = new Astar(graph, g, start);
			astar.aStar(robot.getOrientation());
			if (astar.getRateOfRouting() < rateRouting) {
				rateRouting = astar.getRateOfRouting();
				this.goal = astar.getGoal(); // on informe le le bu choisis
				ret = astar.getOrder();
				
			}
//			System.out.println("de "+starting.getNameV()+" a "+g.getNameV());
//			System.out.println("cout = "+astar.getRateOfRouting());
		}
		this.goals.remove(this.goal);
		return ret;
	}

//	private void delGoal(Vertex goal, List<Vertex> goals) {
//		goals.remove(goal);
//	}

	private void cmdExecute(Direction direction) {
		switch (direction) {
		case LEFT:
			behavior[Cst.TURN_LEFT].action();
			break;
		case RIGH:
			behavior[Cst.TURN_RIGHT].action();
			break;
		default:
			break;
		}
	}
	
	public void filterAdj() {
		List<Vertex> adj = graph.getAdjacentFilter(this.goal);
		if (this.goal.isObstacle()) { //contien un obstacl
			List<Vertex> nill = new ArrayList<>();
			nill.add(goal);
			robot.setDirection(nill);
			return;
		}
		if (adj.size() == 1) {
			List<Vertex> adj1 = graph.getAdjacent(adj.get(0));
			List<Vertex> adjP = graph.getAdjacentFilter(this.goal.getFather());
			for (Vertex vertex : adj1) {
				if (!adjP.contains(vertex) && vertex.getNameV() != this.goal.getNameV()) {
					adj.add(vertex);
//					graph.printAdjacent(adj); // on affiche la liste des sommet adjascent au goal
					return;
				}
			}
			robot.setDirection(adj);
		}else {
//			graph.printAdjacent(adj); // on affiche la liste des sommet adjascent au goal
			robot.setDirection(adj);
		}
		
	}
	
	//test de modification
	public void filterAdj1() {
		if (this.goal.isObstacle()) { //contien un obstacl
			List<Vertex> nill = new ArrayList<>();
			nill.add(goal);
			robot.setDirection(nill);
			return;
		}
		List<Vertex> adj = graph.getAdjacentFilter(this.goal);
		robot.setDirection(adj);
		if (adj.size() == 1) {
			List<Vertex> adj1 = graph.getAdjacent(adj.get(0));
			System.out.println("pere "+this.goal.getFather().getNameV());
			List<Vertex> adjP = graph.getAdjacentFilter(this.goal.getFather());
			graph.printAdjacent(adjP);
			for (Vertex vertex : adj1) {
				if (!adjP.contains(vertex)) {
					adj.add(vertex);
					robot.setDirection(adj);
//					graph.printAdjacent(adj); // on affiche la liste des sommet adjascent au goal
					return;
				}
			}
		}else {
//			graph.printAdjacent(adj); // on affiche la liste des sommet adjascent au goal
			robot.setDirection(adj);
		}
		
	}
	
	private List<Vertex> miseAjour(List<Vertex> m, Vertex v) {
		List<Vertex> adj = graph.getAdjacent(v);
		List<Vertex> newM = new ArrayList<>();
		for (Vertex vertex : adj) {
			if (!m.contains(vertex)) {
				newM.add(vertex);
			}
		}
		return newM;
	}
	
	public void routing() {
		do {
//			for (Vertex vertex : robot.getOrientation()) {
//				System.out.println("rb face "+vertex.getNameV());
//			}
			List<Vertex> newDir = robot.getOrientation();
//			System.out.println("de "+starting.getNameV()+" a "+g.getNameV());
			List<Vertex> listOrder = nearVictim(starting, goals);
			if (!robot.getOrientation().contains(listOrder.get(0))) {
				behavior[Cst.HALFTURN].action();
				newDir = miseAjour(robot.getOrientation(), starting);
			}
			
			System.out.println("de "+starting.getNameV()+" a "+goal.getNameV());
			filterAdj();
			this.starting = this.goal;
			
			for (Vertex vertex : listOrder) {
				 System.out.println(vertex.getNameV()+"->"+vertex.getDirection());
			}
			do {
				behavior[Cst.LINEF].action();
				switch (testMarking.testMarking()) {
				case DOUBLE_STRIP:
					behavior[Cst.JUNCTION].action();
					break;
				case SIMPLE_STRIP:
					if (!listOrder.isEmpty()) {
						cmdExecute(listOrder.remove(0).getDirection());
						newDir = miseAjour(newDir, listOrder.remove(0));
					}
//					System.out.println(listOrder.isEmpty());
					break;
				default:
					break;
				}
				if (listOrder.isEmpty()) {
					behavior[Cst.LINEF].action();
				}
			} while (!listOrder.isEmpty());
			graph = new Graph(graph.getNameG(), CreatGraph.mapTest()); // on remet le nouveau graphe
			Sound.beep();
		} while (!goals.isEmpty());
//		switch (testMarking.testMarking()) {
//		case DOUBLE_STRIP:
//			behavior[Cst.JUNCTION].action();
//			break;
//		case OBSTACLE:
//			behavior[Cst.HALFTURN].action();
//			break;
//		default:
//			break;
//		}
		Sound.twoBeeps();
	}

}
