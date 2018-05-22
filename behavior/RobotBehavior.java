package behavior;

import lejos.nxt.LightSensor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;

/**
 * @author MMADI
 *
 */
public abstract class RobotBehavior {
	protected final static int DefaultMotorSpeed = 300;
	protected static final int lightThresholdNormalize = 487;
	protected static final int DefautRotateSpeed = 180;
	protected static final float slowDownPercent = 0.30f;
	protected static final int motorSpeedTurn = 250;
	protected static final float slowDownPercentTurn = 0.30f;
	
	private NXTRegulatedMotor lftMotor;
	private NXTRegulatedMotor rgtMotor;
	private LightSensor lftLight, rgtLight;
	
	public RobotBehavior (NXTRegulatedMotor rgtMotor, NXTRegulatedMotor lftMotor) {
		this.lftMotor = lftMotor;
		this.rgtMotor = rgtMotor;
	}
	public RobotBehavior (NXTRegulatedMotor rgtMotor, NXTRegulatedMotor lftMotor, SensorPort rgtLight,
			SensorPort lftLight) {
		this.lftMotor = lftMotor;
		this.rgtMotor = rgtMotor;
		this.lftLight = new LightSensor(lftLight);
		this.rgtLight = new LightSensor(rgtLight);
	}

	/**
	 * @return the lftMotor
	 */
	public NXTRegulatedMotor getLftMotor() {
		return lftMotor;
	}

	/**
	 * @return the rgtMotor
	 */
	public NXTRegulatedMotor getRgtMotor() {
		return rgtMotor;
	}

	/**
	 * @return the lftLight
	 */
	public LightSensor getLftLight() {
		return lftLight;
	}

	/**
	 * @return the rgtLight
	 */
	public LightSensor getRgtLight() {
		return rgtLight;
	}
	
	
	/**
	 * @param lftMotor the lftMotor to set
	 */
	public void setLftMotor(NXTRegulatedMotor lftMotor) {
		this.lftMotor = lftMotor;
	}
	/**
	 * @param rgtMotor the rgtMotor to set
	 */
	public void setRgtMotor(NXTRegulatedMotor rgtMotor) {
		this.rgtMotor = rgtMotor;
	}
	/**
	 * @param lftLight the lftLight to set
	 */
	public void setLftLight(LightSensor lftLight) {
		this.lftLight = lftLight;
	}
	/**
	 * @param rgtLight the rgtLight to set
	 */
	public void setRgtLight(LightSensor rgtLight) {
		this.rgtLight = rgtLight;
	}
	public boolean isStrip() {
		return lftLight.readNormalizedValue() > lightThresholdNormalize
		&& rgtLight.readNormalizedValue() > lightThresholdNormalize;
	}
	
	
	public void lineFollower() {}
	
	public void halfTurn () {}
	
	public void turn( ) {}
}
