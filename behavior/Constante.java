package behavior;

import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;

public class Constante {
	protected final static int DefaultMotorSpeed = 300;
	protected static final int lightThresholdNormalize = 487;
	protected static final int DefautRotateSpeed = 180;
	protected static final float slowDownPercent = 0.30f;
	protected static final int motorSpeedTurn = 250;
	protected static final float slowDownPercentTurn = 0.30f;
	
	public static NXTRegulatedMotor lftMotor = Motor.C;
	public static NXTRegulatedMotor rgtMotor = Motor.A;
	public static LightSensor lftLight = new LightSensor(SensorPort.S4),
			rgtLight = new LightSensor(SensorPort.S1);
}
