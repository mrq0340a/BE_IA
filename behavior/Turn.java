package behavior;

import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;
import lejos.util.PIDController;
import lejos.util.PilotProps;

/**
 * @author MMADI
 *
 */
public class Turn implements Behavior {
	protected final static int DefaultMotorSpeed = 200;
	protected static final int lightThresholdNormalize = 480;
	protected static final int DefautRotateSpeed = 180;
	protected static final float slowDownPercent = 0.30f;
	protected static final int motorSpeedTurn = 250;
	protected static final float slowDownPercentTurn = 0.30f;

	protected static final int lowThreshold = 460;
	protected static final int highThreshold = 530;

	private boolean suppress = false;
	private int variationMax = 5;
	private NXTRegulatedMotor rgtMotor;
	private NXTRegulatedMotor lftMotor;
	private LightSensor rgtLight;
	private LightSensor lftLight;
	private DifferentialPilot pilote;

	public Turn(NXTRegulatedMotor rgtMotor, NXTRegulatedMotor lftMotor, SensorPort rgtLight, SensorPort lftLight,
			boolean direction) {

		if (!direction) {
			this.lftLight = new LightSensor(rgtLight);
			this.rgtLight = new LightSensor(lftLight);
			this.lftMotor = rgtMotor;
			this.rgtMotor = lftMotor;
		} else {
			this.lftLight = new LightSensor(lftLight);
			this.rgtLight = new LightSensor(rgtLight);
			this.lftMotor = lftMotor;
			this.rgtMotor = rgtMotor;
		}
		pilote = new DifferentialPilot(56, 106, rgtMotor, lftMotor);
		pilote.setTravelSpeed(50);
	}

	public void turn() {
		int i = 0;
		pilote.travel(15);
		boolean variation = rgtLight.readNormalizedValue() > lightThresholdNormalize;
		boolean oldVariation;
		boolean go = true;
		// rgtMotor.setSpeed(motorSpeedTurn);
		PIDController pid = new PIDController(400, 10);
		pid.setPIDParam(PIDController.PID_KD, 2.0f);
		pid.setPIDParam(PIDController.PID_KI, 0.001f);
		 pid.setPIDParam(PIDController.PID_KP, 0.6f);
		while (go) {
			int speedDelta = pid.doPID(lftLight.getNormalizedLightValue());
			if (i < variationMax) {
//				if (lftLight.getNormalizedLightValue() > lightThresholdNormalize) {
//					lftMotor.setSpeed(motorSpeedTurn + speedDelta);
//					rgtMotor.setSpeed(motorSpeedTurn - speedDelta);
//				} else {
					lftMotor.setSpeed(motorSpeedTurn + speedDelta);
					rgtMotor.setSpeed(motorSpeedTurn - speedDelta);
//				}
			} else {
				lftMotor.setSpeed(motorSpeedTurn*0.70f);
				rgtMotor.setSpeed(motorSpeedTurn);
			}

			lftMotor.forward();
			rgtMotor.forward();

			oldVariation = variation;
			variation = rgtLight.readNormalizedValue() > lightThresholdNormalize;
			if (variation != oldVariation)
				i++;

			if (i > variationMax) {
				go = false;
				// lftMotor.stop();
				// rgtMotor.stop();
				pilote.stop();
				suppress = true;
			}
		}
	}

	public void turn1() {
		int i = 0;
		pilote.travel(30);
		boolean variation = rgtLight.readNormalizedValue() > lightThresholdNormalize;
		boolean oldVariation;
		boolean go = true;
		rgtMotor.setSpeed(DefaultMotorSpeed);
		while (go) {
			// if (lftLight.readNormalizedValue() > lightThresholdNormalize) {
			// lftMotor.setSpeed(motorSpeedTurn * slowDownPercentTurn);
			// rgtMotor.setSpeed(motorSpeedTurn);
			// } else {
			// rgtMotor.setSpeed(motorSpeedTurn*slowDownPercent);
			// lftMotor.setSpeed(motorSpeedTurn);
			// }
			if (lftLight.getNormalizedLightValue() >= highThreshold && i < 5) {
				lftMotor.setSpeed(motorSpeedTurn * 0);
			} else if (lftLight.getNormalizedLightValue() > lowThreshold
					&& lftLight.getNormalizedLightValue() < highThreshold && i < 5) {
				lftMotor.setSpeed(motorSpeedTurn
						* ((lftLight.getNormalizedLightValue() - lowThreshold) / (highThreshold - lowThreshold)));
			} else {
				lftMotor.setSpeed(motorSpeedTurn);
				rgtMotor.setSpeed(motorSpeedTurn * 0.88f);
			}

			lftMotor.forward();
			rgtMotor.forward();

			oldVariation = variation;
			variation = rgtLight.readNormalizedValue() > lightThresholdNormalize;
			if (variation != oldVariation) i += 1;

			if (i > 5) {
				go = false;
				// lftMotor.stop();
				// rgtMotor.stop();
				pilote.stop();
				suppress = true;
			}
		}

	}

	@Override
	public boolean takeControl() {
		return lftLight.readNormalizedValue() > lightThresholdNormalize
				&& rgtLight.readNormalizedValue() > lightThresholdNormalize;
	}

	@Override
	public void action() {
		suppress = false;
		turn();
	}

	@Override
	public void suppress() {
		suppress = true;
	}

	public static void main(String[] args) {
		Turn t = new Turn(Motor.A, Motor.C, SensorPort.S1, SensorPort.S4, true);
		t.action();
	}

	/**
	 * @return the suppress
	 */
	synchronized public boolean isSuppress() {
		return suppress;
	}

	/**
	 * @param suppress
	 *            the suppress to set
	 */
	synchronized public void setSuppress(boolean suppress) {
		this.suppress = suppress;
	}
}
