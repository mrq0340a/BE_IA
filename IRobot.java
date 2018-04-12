package robot;

public interface IRobot {
	// move robot moves along the line
	public boolean lineFollower() throws Exception;

	// robot picks a path
	void makeDecision(boolean b) throws Exception;

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
	public void straight();
	
	// test the presence of an obstacle 
	public void isObstacle();
	
	public void goBack(int motorSpeed, int distance);
	
	public void forward(int motorSpeed, int distance);
}
