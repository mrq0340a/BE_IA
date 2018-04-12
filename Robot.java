package robot;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.LDCMotor;
import lejos.robotics.navigation.DifferentialPilot;

public class Robot implements IRobot {

	// const declaration
	private static final int lightThreshold = 48;
	private static final int motorSpeed = 55;
	private static final int motorSpeedRotate1 = 35;
	private static final int motorSpeedRotate2 = 10;
	private static final int sampleInterval = 10;
	private static final int forward = 1;
	private static final int backward = 2;
	private static final int stop = 3;
	private static final int flt = 4;

	//
	private int idRobot;
	// private MotorPort lftMotor;
	// private MotorPort rgtMotor;
	private LightSensor lftLight;
	private LightSensor rgtLight;
	private DifferentialPilot pilote;
	private static final MotorPort lftMotor = MotorPort.B;
	private static final MotorPort rgtMotor = MotorPort.C;

	/**
	 * @param idRobot
	 * @param rM
	 * @param lM
	 */
	public Robot(int idRobot, LightSensor lftLgt, LightSensor rgtLgt) {
		this.idRobot = idRobot;
		// this.lftMotor = lM;
		// this.rgtMotor = rM;
		this.lftLight = lftLgt;
		this.rgtLight = rgtLgt;
		pilote = new DifferentialPilot(56, 105, Motor.B, Motor.C);
		pilote.setTravelSpeed(40);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see robot.IRobot#lineFollower()
	 */
	@Override
	public boolean lineFollower() throws Exception {
		// LCD.drawString("lineFolower", 0, 0);
		while (!isStrip()) {
			if (lftLight.readValue() > lightThreshold) {
				synchronized (lftMotor) {
					lftMotor.controlMotor(stop, forward);
				}
			} else {
				synchronized (lftMotor) {
					lftMotor.controlMotor(motorSpeed, forward);
				}
			}

			if (rgtLight.readValue() > lightThreshold) {
				synchronized (rgtMotor) {
					rgtMotor.controlMotor(stop, forward);
				}
			} else {
				synchronized (rgtMotor) {
					rgtMotor.controlMotor(motorSpeed, forward);
				}
			}
			LCD.drawInt(lftLight.readValue() * 100 + rgtLight.readValue(), 3, 9, 0);
			Thread.sleep(sampleInterval);

		}
		synchronized (lftMotor) {
			lftMotor.controlMotor(0, stop);
		}

		synchronized (rgtMotor) {
			rgtMotor.controlMotor(0, stop);
		}

		return isDoubleStrip();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see robot.IRobot#makeDecision()
	 */
	@Override
	public void makeDecision(boolean b) throws Exception {
		lftMotor.controlMotor(0, flt);
		rgtMotor.controlMotor(0, flt);

		LCD.drawInt(lftLight.readValue() * 100 + rgtLight.readValue(), 3, 9, 0);
		Thread.sleep(3000);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see robot.IRobot#isStrip()
	 */
	@Override
	synchronized public boolean isStrip() {
		return lftLight.readValue() > lightThreshold && rgtLight.readValue() > lightThreshold;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see robot.IRobot#isDoubleStrip()
	 */
	@Override
	synchronized public boolean isDoubleStrip() throws Exception {
		lftMotor.controlMotor(0, flt);
		rgtMotor.controlMotor(0, flt);
		forward(30, 3);
		return lftLight.readValue() > lightThreshold && rgtLight.readValue() > lightThreshold;
	}

	@Override
	synchronized public void halfTurn() {
		// goBack(30, 3);
		int i = 0;
		boolean go = true;
		boolean variation = false;
		boolean oldVariation = false;
		while (go) {
			synchronized (lftMotor) {
				lftMotor.controlMotor(45, backward);
			}

			synchronized (rgtMotor) {
				rgtMotor.controlMotor(45, forward);
			}

			// calculate variation
			oldVariation = variation;
			if (rgtLight.readValue() > lightThreshold)
				variation = true;
			else
				variation = false;

			if (variation != oldVariation) {
				i++;
			}

			if (i > 2) {
				go = false;
			}
		}
	}

	@Override
	public void leftDrift() throws Exception {
		int i = 0;
		boolean variation = false;
		boolean oldVariation = false;
		boolean go = true;
		while (go) {
			if (rgtLight.readValue() > lightThreshold) {
				synchronized (lftMotor) {
					lftMotor.controlMotor(motorSpeedRotate1, forward);
				}
				synchronized (rgtMotor) {
					rgtMotor.controlMotor(motorSpeedRotate2, forward);
				}

			} else {
				synchronized (lftMotor) {
					lftMotor.controlMotor(motorSpeedRotate1, forward);
				}
				synchronized (rgtMotor) {
					rgtMotor.controlMotor(motorSpeedRotate1, forward);
				}
			}

			// calculate variation
			oldVariation = variation;
			if (lftLight.readValue() > lightThreshold) {
				variation = true;
			} else {
				variation = false;
			}
			if (variation != oldVariation) {
				i++;
			}

			if (i > 4) {
				LCD.drawInt(i, 0, 0);
				go = isStrip();
			}
			Thread.sleep(sampleInterval);
		}
		Thread.sleep(200);
	}

	@Override
	public void rightDrift() throws Exception {
		int i = 0;
		boolean variation = false;
		boolean oldVariation = false;
		boolean go = true;
		LCD.drawString("rightdrift", 0, 1);
		while (go) {
			if (lftLight.readValue() > lightThreshold) {
				synchronized (lftMotor) {
					lftMotor.controlMotor(motorSpeedRotate2, forward);
				}
				synchronized (rgtMotor) {
					rgtMotor.controlMotor(motorSpeedRotate1, forward);
				}

			} else {
				synchronized (lftMotor) {
					lftMotor.controlMotor(motorSpeedRotate1, forward);
				}
				synchronized (rgtMotor) {
					rgtMotor.controlMotor(motorSpeedRotate1, forward);
				}
			}

			// calculate variation
			oldVariation = variation;
			if (rgtLight.readValue() > lightThreshold)
				variation = true;
			else
				variation = false;

			if (variation != oldVariation) {
				i++;
			}

			if (i > 4) {
				go = !isStrip();
			}
			Thread.sleep(sampleInterval);
			LCD.drawInt(rgtLight.readValue(), 1, 1);
		}
		Thread.sleep(200);
	}

	@Override
	synchronized public void straight() {
		// TODO Auto-generated method stub

	}

	@Override
	synchronized public void isObstacle() {
		// TODO Auto-generated method stub

	}

	@Override
	public void goBack(int motorSpeed, int distance) {
		int i = distance * 1000;
		while (i > 0) {
			synchronized (lftMotor) {
				lftMotor.controlMotor(motorSpeed, backward);
			}
			synchronized (rgtLight) {
				rgtMotor.controlMotor(motorSpeed, backward);
			}
			i--;
		}

	}

	@Override
	public void forward(int motorSpeed, int distance) {
		int i = distance * 1000;
		while (i > 0) {
			synchronized (lftMotor) {
				lftMotor.controlMotor(motorSpeed, forward);
			}
			synchronized (rgtLight) {
				rgtMotor.controlMotor(motorSpeed, forward);
			}
			i--;
		}

	}

	public static void main(String[] args) throws Exception {
		IRobot rb = new Robot(1, new LightSensor(SensorPort.S1), new LightSensor(SensorPort.S4));
		rb.lineFollower();
		rb.leftDrift();
		rb.lineFollower();
		rb.halfTurn();
		rb.lineFollower();
		rb.rightDrift();

	}
}
