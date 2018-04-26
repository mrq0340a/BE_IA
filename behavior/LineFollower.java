package behavior;

import lejos.nxt.LightSensor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;
import lejos.util.PIDController;
import lejos.util.PilotProps;

public class LineFollower implements Behavior {
	protected final static int DefaultMotorSpeed = 300;
	protected static final int lightThresholdNormalize = 487;
	protected static final int DefautRotateSpeed = 180;
	protected static final float slowDownPercent = 0.30f;
	protected static final int motorSpeedTurn = 250;
	protected static final float slowDownPercentTurn = 0.30f;

	protected static final int lowThreshold = 460;
	protected static final int highThreshold = 530;

	private NXTRegulatedMotor lftMotor;
	private NXTRegulatedMotor rgtMotor;
	private LightSensor lftLight, rgtLight;
//	private DifferentialPilot pilote;
	
	private boolean suppress = false;

	public LineFollower(NXTRegulatedMotor rgtMotor, NXTRegulatedMotor lftMotor, SensorPort rgtLight,
			SensorPort lftLight) {
		this.lftMotor = lftMotor;
		this.rgtMotor = rgtMotor;
		this.lftLight = new LightSensor(lftLight);
		this.rgtLight = new LightSensor(rgtLight);
//		pilote = new DifferentialPilot(56, 106, rgtMotor, lftMotor);
	}

	private boolean isStrip() {
		return lftLight.readNormalizedValue() > Cst.lightThresholdNormalize
				&& rgtLight.readNormalizedValue() > Cst.lightThresholdNormalize;
	}

	private void lineFollower2() {
		PIDController pid1 = new PIDController(430, 10);
		PIDController pid2 = new PIDController(430, 10);
		pid1.setPIDParam(PIDController.PID_KD, 3.0f);
//		pid1.setPIDParam(PIDController.PID_KI, 0.005f);
//		pid1.setPIDParam(PIDController.PID_KP, 5f);

		pid2.setPIDParam(PIDController.PID_KD, 3.0f);
//		pid2.setPIDParam(PIDController.PID_KI, 0.005f);
//		pid2.setPIDParam(PIDController.PID_KP, 5f);

		boolean go = true;
		while (go) {
			int speedDelta1 = pid1.doPID(lftLight.readNormalizedValue());
			int speedDelta2 = pid2.doPID(rgtLight.readNormalizedValue());

			if (lftLight.readNormalizedValue() <= lightThresholdNormalize
					|| rgtLight.readNormalizedValue() <= lightThresholdNormalize) {
				if (lftLight.readNormalizedValue() > lightThresholdNormalize) {
					lftMotor.setSpeed(DefaultMotorSpeed + speedDelta1);
					rgtMotor.setSpeed(DefaultMotorSpeed - speedDelta1);
				} else if (rgtLight.readNormalizedValue() > lightThresholdNormalize) {
					lftMotor.setSpeed(DefaultMotorSpeed - speedDelta2);
					rgtMotor.setSpeed(DefaultMotorSpeed + speedDelta2);
				} else {
					if (speedDelta1 > speedDelta2) {
						lftMotor.setSpeed(DefaultMotorSpeed + speedDelta1);
						rgtMotor.setSpeed(DefaultMotorSpeed - speedDelta1);
					} else {
						lftMotor.setSpeed(DefaultMotorSpeed - speedDelta2);
						rgtMotor.setSpeed(DefaultMotorSpeed + speedDelta2);
					}
				}
				lftMotor.forward();
				rgtMotor.forward();
			}else {
				go = false;
//				pilote.stop();
//				lftMotor.setSpeed(DefaultMotorSpeed);
//				rgtMotor.setSpeed(DefaultMotorSpeed);
			}
			
			
			if (isStrip()) {
				go = false;
//				lftMotor.stop();
//				rgtMotor.stop();
				
				suppress = true;
			}
		}

	}

	private void lineFollower1() {
		boolean go = true;
		// ArrayList<Integer> lastValues = new ArrayList<>();
		while (go) {
			if (lftLight.readNormalizedValue() > lightThresholdNormalize) {
				lftMotor.setSpeed(DefaultMotorSpeed * slowDownPercent*0);
			} else if (rgtLight.readNormalizedValue() > lightThresholdNormalize) {
				rgtMotor.setSpeed(DefaultMotorSpeed * slowDownPercent*0);
			} else {
				lftMotor.setSpeed(DefaultMotorSpeed);
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

	public void lineFollower() {
		boolean go = true;
		while (go) {
			if (lftLight.getNormalizedLightValue() >= highThreshold) {
				lftMotor.setSpeed((int) (DefaultMotorSpeed * 0.1));
			} else if (lftLight.getNormalizedLightValue() > lowThreshold
					&& lftLight.getNormalizedLightValue() < highThreshold) {
				lftMotor.setSpeed(DefaultMotorSpeed
						* ((lftLight.getNormalizedLightValue() - lowThreshold) / (highThreshold - lowThreshold)));
			} else {
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
