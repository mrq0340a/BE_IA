package behavior;

import java.util.ArrayList;

import lejos.nxt.LightSensor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.robotics.subsumption.Behavior;

public class LineFollower implements Behavior {
	protected final static int DefaultMotorSpeed = 400;
	protected static final int lightThresholdNormalize = 487;
	protected static final int DefautRotateSpeed = 180;
	protected static final float slowDownPercent = 0.30f;
	protected static final int motorSpeedTurn = 250;
	protected static final float slowDownPercentTurn = 0.30f;

	protected static final int lowThreshold = 450;
	protected static final int highThreshold = 550;

	private NXTRegulatedMotor lftMotor;
	private NXTRegulatedMotor rgtMotor;
	private LightSensor lftLight, rgtLight;

	private boolean suppress = false;

	public LineFollower(NXTRegulatedMotor rgtMotor, NXTRegulatedMotor lftMotor, SensorPort rgtLight,
			SensorPort lftLight) {
		this.lftMotor = lftMotor;
		this.rgtMotor = rgtMotor;
		this.lftLight = new LightSensor(lftLight);
		this.rgtLight = new LightSensor(rgtLight);
	}

	private boolean isStrip() {
		return lftLight.readNormalizedValue() > lightThresholdNormalize
				&& rgtLight.readNormalizedValue() > lightThresholdNormalize;
	}

	public void lineFollower() {
		boolean go = true;
		// ArrayList<Integer> lastValues = new ArrayList<>();
		// while (go) {
		// if (lftLight.readNormalizedValue() > lightThresholdNormalize) {
		// lftMotor.setSpeed(DefaultMotorSpeed * slowDownPercent);
		// } else if (rgtLight.readNormalizedValue() > lightThresholdNormalize) {
		// rgtMotor.setSpeed(DefaultMotorSpeed * slowDownPercent);
		// } else {
		// lftMotor.setSpeed(DefaultMotorSpeed);
		// rgtMotor.setSpeed(DefaultMotorSpeed);
		// }
		// lftMotor.forward();
		// rgtMotor.forward();
		// if (isStrip()) {
		// go = false;
		// lftMotor.stop();
		// rgtMotor.stop();
		// suppress = true;
		// }
		// }

		while (go) {
			if (lftLight.getNormalizedLightValue() >= highThreshold) {
				lftMotor.setSpeed(DefaultMotorSpeed * 0);
			} else if (lftLight.getNormalizedLightValue() > lowThreshold
					&& lftLight.getNormalizedLightValue() < highThreshold) {
				lftMotor.setSpeed(DefaultMotorSpeed
						* ((lftLight.getNormalizedLightValue() - lowThreshold) / (highThreshold - lowThreshold)));
			}else {
				lftMotor.setSpeed(DefaultMotorSpeed);
			}
			
			if (rgtLight.getNormalizedLightValue() >= highThreshold) {
				rgtMotor.setSpeed(0);
			} else if (rgtLight.getNormalizedLightValue() > lowThreshold
					&& rgtLight.getNormalizedLightValue() < highThreshold) {
				rgtMotor.setSpeed(DefaultMotorSpeed
						* ((rgtLight.getNormalizedLightValue() - lowThreshold) / (highThreshold - lowThreshold)));
			} else {
				rgtMotor.setSpeed(DefaultMotorSpeed);
			}

			lftMotor.forward();
			rgtMotor.forward();
			if (isStrip()) {
				go = false;
				lftMotor.stop();
				rgtMotor.stop();
				suppress = true;
			}
		}
	}

	@Override
	public boolean takeControl() {
		return true;
	}

	@Override
	public void action() {
		suppress = false;
		lineFollower();
	}

	@Override
	public void suppress() {
		suppress = true;
	}
}
