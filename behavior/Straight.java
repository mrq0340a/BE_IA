package behavior;

import lejos.nxt.LightSensor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;

/**
 * @author MMADI
 *
 */
public class Straight implements Behavior {

	protected static final int DefaultMotorSpeed = 500;
	protected static final int lightThresholdNormalize = 487;
	protected static final int DefautRotateSpeed = 180;
	protected static final float slowDownPercent = 0.90f;
	protected static final int motorSpeedTurn = 250;
	protected static final float slowDownPercentTurn = 0.30f;

	private boolean suppress = false;

	private NXTRegulatedMotor rgtMotor;
	private NXTRegulatedMotor lftMotor;
	private LightSensor rgtLight;
	private LightSensor lftLight;
	private DifferentialPilot pilote;

	public Straight(NXTRegulatedMotor rgtMotor, NXTRegulatedMotor lftMotor, SensorPort rgtLight, SensorPort lftLight,
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
		this.pilote = new DifferentialPilot(56, 106, this.lftMotor, this.rgtMotor);
	}

	public void straight() {
		int i = 0;
		boolean variation = rgtLight.readNormalizedValue() > lightThresholdNormalize;
		boolean oldVariation;
		boolean go = true;
		while (go) {
			if (lftLight.readNormalizedValue() > lightThresholdNormalize) {
				lftMotor.setSpeed(DefaultMotorSpeed * slowDownPercent);
				rgtMotor.setSpeed(DefaultMotorSpeed);

			} else {
				lftMotor.setSpeed(DefaultMotorSpeed);
				rgtMotor.setSpeed(DefaultMotorSpeed * slowDownPercent);
			}
			// if (i == 4) {
			// lftMotor.setSpeed(DefaultMotorSpeed);
			// rgtMotor.setSpeed(DefaultMotorSpeed);
			// }
			lftMotor.forward();
			rgtMotor.forward();

			// calculate variation
			oldVariation = variation;
			variation = rgtLight.readNormalizedValue() > lightThresholdNormalize;
			if (variation != oldVariation)
				i += 1;

			if (i == 5) {
				go = false;
				pilote.stop();
				suppress = true;
			}
		}
	}

	@Override
	public boolean takeControl() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void action() {
		suppress = false;
		straight();
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub

	}

}
