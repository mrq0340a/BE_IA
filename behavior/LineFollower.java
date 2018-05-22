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
	// private DifferentialPilot pilote;

	private boolean suppress = false;

	public LineFollower(NXTRegulatedMotor rgtMotor, NXTRegulatedMotor lftMotor, SensorPort rgtLight,
			SensorPort lftLight) {
		this.lftMotor = lftMotor;
		this.rgtMotor = rgtMotor;
		this.lftLight = new LightSensor(lftLight);
		this.rgtLight = new LightSensor(rgtLight);
		// pilote = new DifferentialPilot(56, 106, rgtMotor, lftMotor);
	}

	private boolean isStrip() {
		return lftLight.readNormalizedValue() > Cst.lightThresholdNormalize
				&& rgtLight.readNormalizedValue() > Cst.lightThresholdNormalize;
	}

	private void lineFollower2() {
		PIDController pid1 = new PIDController(390, 1);
		PIDController pid2 = new PIDController(390, 1);
		pid1.setPIDParam(PIDController.PID_KD, 10.0f);
		pid1.setPIDParam(PIDController.PID_KI, 0.0009f);
		pid1.setPIDParam(PIDController.PID_KP, 2f);

		pid2.setPIDParam(PIDController.PID_KD, 10.0f);
		pid2.setPIDParam(PIDController.PID_KI, 0.0009f);
		pid2.setPIDParam(PIDController.PID_KP, 2f);

		boolean go = true;
		if (!isStrip()) {
			while (go) {
				int lv1 = lftLight.readNormalizedValue();
				int lv2 = rgtLight.readNormalizedValue();
				int speedDelta1 = pid1.doPID(lv1);
				int speedDelta2 = pid2.doPID(lv2);
				if (lv1 < lightThresholdNormalize && lv2 > lightThresholdNormalize) {
					rgtMotor.setSpeed(DefaultMotorSpeed + speedDelta1);
					lftMotor.setSpeed(DefaultMotorSpeed - speedDelta2);
				} else if (lv1 > lightThresholdNormalize && lv2 < lightThresholdNormalize) {
					lftMotor.setSpeed(DefaultMotorSpeed + speedDelta2);
					rgtMotor.setSpeed(DefaultMotorSpeed - speedDelta1);
				} else if (lv1 < lightThresholdNormalize && lv2 < lightThresholdNormalize) {
					rgtMotor.setSpeed(DefaultMotorSpeed + speedDelta1);
					lftMotor.setSpeed(DefaultMotorSpeed + speedDelta2);
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

	}

	public void mouve () {
		PIDController pid = new PIDController(400, 10);
		pid.setPIDParam(PIDController.PID_KD, 2.0f);
		pid.setPIDParam(PIDController.PID_KI, 0.001f);
		pid.setPIDParam(PIDController.PID_KP, 0.6f);
		for (int i = 0; i < 70; i++) {
			int speedDelta = pid.doPID(lftLight.getNormalizedLightValue());
			int speedDelta1 = pid.doPID(rgtLight.getNormalizedLightValue());
			if (i < 70) {
				if (lftLight.getNormalizedLightValue() > lightThresholdNormalize) {
					lftMotor.setSpeed(motorSpeedTurn + speedDelta);
					rgtMotor.setSpeed(motorSpeedTurn - speedDelta);
				}else {
					lftMotor.setSpeed(motorSpeedTurn - speedDelta1);
					rgtMotor.setSpeed(motorSpeedTurn + speedDelta1);
				}
				
			} else {
				lftMotor.setSpeed(motorSpeedTurn);
				rgtMotor.setSpeed(motorSpeedTurn);
			}
			lftMotor.forward();
			rgtMotor.forward();
		}
		
		lftMotor.flt();
		rgtMotor.stop();
	}
	
	public void lineFollower() {
		boolean go = true;
		if (!isStrip()) {
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
	
	/**
	 * @return the suppress
	 */
	synchronized public boolean isSuppress() {
		return suppress;
	}

	/**
	 * @param suppress the suppress to set
	 */
	synchronized public void setSuppress(boolean suppress) {
		this.suppress = suppress;
	}
	
	public static void main(String[] args) {
		LineFollower l = new LineFollower(Motor.A, Motor.C, SensorPort.S1, SensorPort.S4);
		l.mouve();
	}
}
