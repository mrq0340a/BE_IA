package robot;

import java.util.List;

public interface IRobot {
	// move robot moves along the line
	public void lineFollower() throws Exception;

	//
	public void stopEngines();

	// robot test the present of strip on the way
	public boolean isStrip();

	// if isStrip, the robot test the present a second one
	public boolean isDoubleStrip() throws Exception;
	
	// turn back 
	public void halfTurn();
	
	// turn left
	public void leftDrift() throws Exception;
	
	// turn right
	public void rightDrift() throws Exception;
	
	// go straight
	public void rStraight();
	
	// 
	public void lStraight();
	
	// test the presence of an obstacle 
	public Mark testMarking();

	//
	public void travel(int motorSpeed, int distance, int mode);

	//
	public void turnJunction();
	
	//
	public void straightJunction();
}
