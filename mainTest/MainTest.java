package mainTest;

import java.util.ArrayList;
import java.util.List;

import behavior.Cst;
import behavior.HalfTurn;
import behavior.Junction;
import behavior.LineFollower;
import behavior.Turn;
import graph.CreatGraph;
import graph.Direction;
import graph.Graph;
import graph.Vertex;
import lejos.robotics.subsumption.Behavior;
import robot.Robot;
import routing.RoutingWithOneRobot;

public class MainTest {

	public static void main(String[] args) {
		List<Vertex> orientation = new ArrayList<>();
		orientation.add(new Vertex("B", 0, 15, false,  Direction.LEFT));
		orientation.add(new Vertex("C", 0, 8, true,  Direction.RIGH));
		List<Vertex> goals = new ArrayList<>();
		goals.add(new Vertex("E", 0, 6, false,  Direction.STRAIGHT));
		goals.add(new Vertex("B", 0, 15, false,  Direction.STRAIGHT));
		goals.add(new Vertex("F", 0, 20, false,  Direction.STRAIGHT)); 
		Vertex start = new Vertex("A", 0, 10, false, Direction.STRAIGHT);
		Robot rb = new Robot(orientation);
		Graph g = new Graph("circuit1", CreatGraph.mapTest());
		
//		List<Vertex> orientation = new ArrayList<>();
//		orientation.add(new Vertex("D", 0, 9, false,  Direction.LEFT));
//		orientation.add(new Vertex("E", 0, 7, true,  Direction.RIGH));
//		List<Vertex> goals = new ArrayList<>();
//		goals.add(new Vertex("F", 0, 8, false,  Direction.STRAIGHT));
////		goals.add(new Vertex("B", 0, 15, false,  Direction.STRAIGHT));
////		goals.add(new Vertex("F", 0, 20, false,  Direction.STRAIGHT)); 
//		Vertex start = new Vertex("B", 0, 6, false,  Direction.STRAIGHT);
//		Robot rb = new Robot(orientation);
//		Graph g = new Graph("circuit1", CreatGraph.housMap());
		///
		Behavior linef = new LineFollower(rb.getRgtMotor(), rb.getLftMotor(), 
				rb.getRgtLight(), rb.getLftLight());  
		Behavior htur = new HalfTurn(rb.getRgtMotor(), rb.getLftMotor()); 
		Behavior turnL = new Turn(rb.getRgtMotor(), rb.getLftMotor(), 
				rb.getRgtLight(), rb.getLftLight(), true);
		Behavior turnR = new Turn(rb.getRgtMotor(), rb.getLftMotor(), 
				rb.getRgtLight(), rb.getLftLight(), false);
		Behavior jun = new Junction(rb.getRgtMotor(), rb.getLftMotor(), 
				rb.getRgtLight(), rb.getLftLight());

		Behavior [] behaviors = {linef, turnL, turnR, htur, jun};
		RoutingWithOneRobot rt = new RoutingWithOneRobot(g, rb, start, goals, behaviors);
		rt.routing();
	}

}
