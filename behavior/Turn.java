package behavior;

import lejos.nxt.LightSensor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.robotics.subsumption.Behavior;

public class Turn implements Behavior {
	protected final static int DefaultMotorSpeed = 300;
	protected static final int lightThresholdNormalize = 487;
	protected static final int DefautRotateSpeed = 180;
	protected static final float slowDownPercent = 0.30f;
	protected static final int motorSpeedTurn = 250;
	protected static final float slowDownPercentTurn = 0.25f;

	protected static final int lowThreshold = 450;
	protected static final int highThreshold = 550;

	private boolean suppress = false;

	private NXTRegulatedMotor rgtMotor;
	private NXTRegulatedMotor lftMotor;
	private LightSensor rgtLight;
	private LightSensor lftLight;

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
	}

	public void turn() {
		int i = 0;
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
				lftMotor.setSpeed(DefaultMotorSpeed * 0);
			} else if (lftLight.getNormalizedLightValue() > lowThreshold
					&& lftLight.getNormalizedLightValue() < highThreshold && i < 5) {
				lftMotor.setSpeed(DefaultMotorSpeed
						* ((lftLight.getNormalizedLightValue() - lowThreshold) / (highThreshold - lowThreshold)));
			} else {
				lftMotor.setSpeed(DefaultMotorSpeed);
				rgtMotor.setSpeed(DefaultMotorSpeed * 0.8f);
			}
			
			
			lftMotor.forward();
			rgtMotor.forward();

			oldVariation = variation;
			variation = rgtLight.readNormalizedValue() > lightThresholdNormalize;
			if (variation != oldVariation)
				i += 1;

			if (i > 5) {
				go = false;
				lftMotor.stop();
				rgtMotor.stop();
				suppress = true;
			}
		}

	}

	// public void turn() {
	// int i = 0;
	// boolean variation = Constante.rgtLight.readNormalizedValue() >
	// Constante.lightThresholdNormalize;
	// boolean oldVariation;
	// boolean go = true;
	// Constante.rgtMotor.setSpeed(Constante.motorSpeedTurn);
	// while (go) {
	// if (Constante.lftLight.readNormalizedValue() >
	// Constante.lightThresholdNormalize) {
	// Constante.lftMotor.setSpeed(Constante.motorSpeedTurn *
	// Constante.slowDownPercentTurn);
	// } else {
	// Constante.lftMotor.setSpeed(Constante.motorSpeedTurn);
	// }
	// Constante.lftMotor.forward();
	// Constante.rgtMotor.forward();
	//
	// oldVariation = variation;
	// if (Constante.rgtLight.readNormalizedValue() >
	// Constante.lightThresholdNormalize) {
	// variation = true;
	// } else {
	// variation = false;
	// }
	// if (variation != oldVariation) {
	// i++;
	// }
	//
	// if (i > 5) {
	// go = false;
	// Constante.lftMotor.stop();
	// Constante.rgtMotor.stop();
	// }
	// }
	//
	// }

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

}
