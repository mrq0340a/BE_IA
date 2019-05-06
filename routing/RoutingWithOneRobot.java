package routing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import behavior.Cst;
import behavior.TestMarking;
import graph.CreatGraph;
import graph.Direction;
import graph.Graph;
import graph.Vertex;
import lejos.nxt.Sound;
import lejos.robotics.subsumption.Behavior;
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
	private HashMap<Vertex, List<Vertex>> map;

	public RoutingWithOneRobot(HashMap<Vertex, List<Vertex>> map, Robot robot, Vertex starting, List<Vertex> goals,
			Behavior[] behavior) {
		this.goals = goals;
		this.robot = robot;
		this.behavior = behavior;
		this.starting = starting;
		this.graph = new Graph("test", map);
		this.map = map;
		this.testMarking = new TestMarking(robot.getRgtMotor(), robot.getLftMotor(), robot.getRgtLight(),
				robot.getLftLight());
	}

	public void go() {
		behavior[Cst.LINEF].action();
	}

	private List<Vertex> nearVictim(Vertex start, List<Vertex> goals, Graph graph) {
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
		}
		this.goals.remove(this.goal);
		return ret;
	}

	private void cmdExecute(Direction direction) {
		switch (direction) {
		case LEFT:
			behavior[Cst.TURN_LEFT].action();
			break;
		case RIGHT:
			behavior[Cst.TURN_RIGHT].action();
			break;
		default:
			break;
		}
	}

	private List<Vertex> miseAjour(List<Vertex> m, Vertex v) {
		List<Vertex> adj = graph.getAdjacent(v);
		List<Vertex> newM = new ArrayList<>();
		for (Vertex vertex : adj) {
			if (!m.contains(vertex) && !vertex.equals(v.getFather())) {
				newM.add(vertex);
			}
		}
		return newM;
	}

	public void routing() {
		do {
			List<Vertex> newDir = robot.getOrientation();
			List<Vertex> listOrder = nearVictim(starting, goals, graph);
			// if (listOrder.isEmpty()) {

			// } else {
			if (listOrder.isEmpty() || !robot.getOrientation().contains(listOrder.get(0))) {
				behavior[Cst.HALFTURN].action();
				newDir = miseAjour(robot.getOrientation(), starting);
			}
			// }

			System.out.println("de " + starting.getNameV() + " a " + goal.getNameV());
			this.starting = this.goal;

			for (Vertex vertex : listOrder) {
				System.out.println(vertex.getNameV() + "->" + vertex.getDirection());
			}
			do {
				behavior[Cst.LINEF].action();
				switch (testMarking.testMarking()) {
				case DOUBLE_STRIP:
					behavior[Cst.JUNCTION].action();
					break;
				case SIMPLE_STRIP:
					if (!listOrder.isEmpty()) {
						newDir = miseAjour(newDir, listOrder.get(0));
						cmdExecute(listOrder.remove(0).getDirection());
					}
					// System.out.println(listOrder.isEmpty());
					break;
				default:
					break;
				}
				if (listOrder.isEmpty()) {
					behavior[Cst.LINEF].action();
					robot.setDirection(newDir);
				}
			} while (!listOrder.isEmpty());
			// graph = new Graph("circuit", CreatGraph.mapTest()); // on remet le nouveau
			// graphe
			Sound.beep();
		} while (!goals.isEmpty());
		// switch (testMarking.testMarking()) {
		// case DOUBLE_STRIP:
		// behavior[Cst.JUNCTION].action();
		// break;
		// case OBSTACLE:
		// behavior[Cst.HALFTURN].action();
		// break;
		// default:
		// break;
		// }
		Sound.twoBeeps();
	}

}
