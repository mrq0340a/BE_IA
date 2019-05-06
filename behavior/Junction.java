package behavior;

import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;

public class Junction implements Behavior {

	protected static final int DefaultMotorSpeed = 300;
	protected static final int lightThresholdNormalize = 487;
	protected static final int DefautRotateSpeed = 180;
	protected static final float slowDownPercent = 0.90f;
	protected static final int motorSpeedTurn = 250;
	protected static final float slowDownPercentTurn = 0.30f;

	protected static final int lowThreshold = 450;
	protected static final int highThreshold = 550;

	private boolean suppress = false;

	private NXTRegulatedMotor rgtMotor;
	private NXTRegulatedMotor lftMotor;
	private LightSensor rgtLight;
	private LightSensor lftLight;
	private DifferentialPilot pilote;

	public Junction(NXTRegulatedMotor rgtMotor, NXTRegulatedMotor lftMotor, SensorPort rgtLight, SensorPort lftLight) {
		this.lftLight = new LightSensor(lftLight);
		this.rgtLight = new LightSensor(rgtLight);
		this.lftMotor = lftMotor;
		this.rgtMotor = rgtMotor;
		this.pilote = new DifferentialPilot(56, 106, this.lftMotor, this.rgtMotor);
	}

	public void junction() {
		int i = 0;
		boolean variation = rgtLight.readValue() > lightThresholdNormalize;
		boolean oldVariation;
		boolean go = true;
		while (go) {
			if (i < 7) {
				if (lftLight.getNormalizedLightValue() >= lightThresholdNormalize) {
					lftMotor.setSpeed(DefaultMotorSpeed * 0.45f);
					rgtMotor.setSpeed(DefaultMotorSpeed);
				} /*
					 * else if (lftLight.getNormalizedLightValue() > lowThreshold &&
					 * lftLight.getNormalizedLightValue() < highThreshold) {
					 * lftMotor.setSpeed(DefaultMotorSpeed ((lftLight.getNormalizedLightValue() -
					 * lowThreshold) / (highThreshold - lowThreshold))); }
					 */else {
					lftMotor.setSpeed(DefaultMotorSpeed);
					rgtMotor.setSpeed(DefaultMotorSpeed * 0.45f);
				}
			} else {
				lftMotor.setSpeed(DefaultMotorSpeed * 0.7f);
				rgtMotor.setSpeed(DefaultMotorSpeed * 0.7f);
			}

			lftMotor.forward();
			rgtMotor.forward();

			// calculate variation
			oldVariation = variation;
			variation = rgtLight.readNormalizedValue() > lightThresholdNormalize;
			if (variation != oldVariation)
				i++;

			if (i >= 10) {
				go = false;
				pilote.stop();
				suppress = true;
				LCD.drawChar('0', 0, 0);
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
		junction();
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub

	}

}
