package behavior;

import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
/**
 * @author MMADI
 *
 */
public class Cst {
	protected final static int DefaultMotorSpeed = 400;
	protected static final int lightThresholdNormalize = 487;
	protected static final int DefautRotateSpeed = 180;
	protected static final float slowDownPercent = 0.30f;
	protected static final int motorSpeedTurn = 250;
	protected static final float slowDownPercentTurn = 0.30f;

	protected static final int lowThreshold = 450;
	protected static final int highThreshold = 550;
	
	public final static int LINEF = 0, TURN_LEFT = 1, TURN_RIGHT = 2, HALFTURN = 3, JUNCTION = 4;
 
}
