package robot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
	private static final int motorSpeed = 60;
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
	private static final MotorPort lftMotor = MotorPort.C;
	private static final MotorPort rgtMotor = MotorPort.B;

	/**
	 * @param idRobot
	 * @param rM
	 * @param lM
	 */
	public Robot(int idRobot, LightSensor rgtLgt, LightSensor lftLgt) {
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
	public void lineFollower() throws Exception {
		// LCD.drawString("lineFolower", 0, 0);
		while (!isStrip()) {
			if (lftLight.readValue() > lightThreshold) {
				synchronized (lftMotor) {
					lftMotor.controlMotor(motorSpeed / 4, forward);
				}
			} else {
				synchronized (lftMotor) {
					lftMotor.controlMotor(motorSpeed, forward);
				}
			}

			if (rgtLight.readValue() > lightThreshold) {
				synchronized (rgtMotor) {
					rgtMotor.controlMotor(motorSpeed / 4, forward);
				}
			} else {
				synchronized (rgtMotor) {
					rgtMotor.controlMotor(motorSpeed, forward);
				}
			}
		}
		lftMotor.controlMotor(0, stop);
		rgtMotor.controlMotor(0, stop);
//		stopEngines();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see robot.IRobot#makeDecision()
	 */
	@Override
	public void stopEngines() {
		lftMotor.controlMotor(0, stop);
		rgtMotor.controlMotor(0, stop);
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
		travel(30, 3, forward);
		return lftLight.readValue() > lightThreshold && rgtLight.readValue() > lightThreshold;
	}
	
	@Override
	public void halfTurn() {
		int i = 0;
		boolean go = true;
		boolean variation = lftLight.readValue() > lightThreshold;
		boolean oldVariation;
		while (go) {
			synchronized (lftMotor) {
				lftMotor.controlMotor(45, backward);
			}

			synchronized (rgtMotor) {
				rgtMotor.controlMotor(45, forward);
			}

			// calculate variation
			oldVariation = variation;
			if (lftLight.readValue() > lightThreshold)
				variation = true;
			else
				variation = false;

			if (variation != oldVariation) {
				i++;
			}

			if (i >= 3) {
				go = false;
			}
		}
	}

	@Override
	public void leftDrift() throws Exception {
		int i = 0;
		boolean variation = rgtLight.readValue() > lightThreshold;
		boolean oldVariation;
		boolean go = true;
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
			if (rgtLight.readValue() > lightThreshold) {
				variation = true;
			} else {
				variation = false;
			}
			if (variation != oldVariation) {
				i++;
			}

			if (i >= 5) {
				go = isStrip();
			}
			Thread.sleep(sampleInterval);
		}
		Thread.sleep(200);
	}

	@Override
	public void rightDrift() throws Exception {
		int i = 0;
		boolean variation = lftLight.readValue() > lightThreshold;
		boolean oldVariation;
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
			if (lftLight.readValue() > lightThreshold)
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
	public void rStraight() {
		int i = 0;
		boolean variation = rgtLight.readValue() > lightThreshold;
		boolean oldVariation;
		boolean go = true;
		while (go) {
			if (lftLight.readValue() < lightThreshold) {
				synchronized (lftMotor) {
					lftMotor.controlMotor(motorSpeedRotate1, forward);
				}
				synchronized (rgtMotor) {
					rgtMotor.controlMotor(motorSpeedRotate2, forward);
				}

			} else {
				synchronized (lftMotor) {
					lftMotor.controlMotor(motorSpeedRotate2, forward);
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

			if (i >= 4) {
				go = !isStrip();
			}
			// Thread.sleep(sampleInterval);
			// LCD.drawInt(rgtLight.readValue(), 1, 1);
		}
	}

	@Override
	public void lStraight() {
		int i = 0;
		boolean variation = lftLight.readValue() > lightThreshold;
		boolean oldVariation;
		boolean go = true;
		while (go) {
			if (rgtLight.readValue() < lightThreshold) {
				synchronized (lftMotor) {
					lftMotor.controlMotor((int) (motorSpeed * 0.7), forward);
				}
				synchronized (rgtMotor) {
					rgtMotor.controlMotor(motorSpeed, forward);
				}

			} else {
				synchronized (lftMotor) {
					lftMotor.controlMotor(motorSpeed, forward);
				}
				synchronized (rgtMotor) {
					rgtMotor.controlMotor((int) (motorSpeed * 0.7), forward);
				}
			}

			// calculate variation
			oldVariation = variation;
			if (lftLight.readValue() > lightThreshold)
				variation = true;
			else
				variation = false;

			if (variation != oldVariation)
				i++;

			if (i >= 4)
				go = !isStrip();

			// Thread.sleep(sampleInterval);
			// LCD.drawInt(rgtLight.readValue(), 1, 1);
		}
	}

	@Override
	public Mark testMarking() {
		travel(50, 2, forward);
		if (lftLight.readValue() > lightThreshold && rgtLight.readValue() > lightThreshold) { // double strip
			travel(50, 1, forward);
			if (lftLight.readValue() > lightThreshold && rgtLight.readValue() > lightThreshold) {
				return Mark.OBSTACLE;
			}
			return Mark.DOUBLE_STRIP;
		}
		return Mark.SIMPLE_STRIP;
	}

	@Override
	public void travel(int motorSpeed, int distance, int mode) {
		int i = distance * 1000;
		while (i > 0) {
			synchronized (lftMotor) {
				lftMotor.controlMotor(motorSpeed, mode);
			}
			synchronized (rgtLight) {
				rgtMotor.controlMotor(motorSpeed, mode);
			}
			i--;
		}
	}

	public void turnJunction() {
		int i = 0;
		boolean variation = lftLight.readValue() > lightThreshold;
		boolean oldVariation;
		boolean go = true;
		while (go) {
			if (lftLight.readValue() > lightThreshold) {
				synchronized (lftMotor) {
					lftMotor.controlMotor((int) (motorSpeed * 0.3), forward);
				}
				synchronized (rgtMotor) {
					rgtMotor.controlMotor(motorSpeed, forward);
				}

			} else {
				synchronized (lftMotor) {
					lftMotor.controlMotor(motorSpeed, forward);
				}
				synchronized (rgtMotor) {
					rgtMotor.controlMotor((int) (motorSpeed * 0.3), forward);
				}
			}

			// calculate variation
			oldVariation = variation;
			if (rgtLight.readValue() > lightThreshold)
				variation = true;
			else
				variation = false;

			if (variation != oldVariation)
				i++;

			if (i >= 6)
				go = !isStrip();
		}
		travel(30, 3, forward);
	}

	public void straightJunction() {
		int i = 0, j = 0;
		boolean variation = rgtLight.readValue() > lightThreshold;;
		boolean oldVariation;
		while (i < 2) {
			variation = rgtLight.readValue() > lightThreshold;
			if(j % 2 == 0) {
				if (rgtLight.readValue() > lightThreshold) {
					synchronized (lftMotor) {
						lftMotor.controlMotor(motorSpeed, forward);
					}
					synchronized (rgtMotor) {
						rgtMotor.controlMotor((int) (motorSpeed * 0.3), forward);
					}
	
				} else {
					synchronized (lftMotor) {
						lftMotor.controlMotor((int) (motorSpeed * 0.3), forward);
					}
					synchronized (rgtMotor) {
						rgtMotor.controlMotor(motorSpeed, forward);
					}
				}
	
				// calculate variation
				oldVariation = variation;

				if (lftLight.readValue() > lightThreshold)
					variation = true;
				else
					variation = false;
	
				if (variation != oldVariation)
					i++;
			}
			else {
				variation = rgtLight.readValue() > lightThreshold;
				if (lftLight.readValue() > lightThreshold) {
					synchronized (rgtMotor) {
						rgtMotor.controlMotor(motorSpeed, forward);
					}
					synchronized (lftMotor) {
						lftMotor.controlMotor((int) (motorSpeed * 0.3), forward);
					}
	
				} else {
					synchronized (rgtMotor) {
						rgtMotor.controlMotor((int) (motorSpeed * 0.3), forward);
					}
					synchronized (lftMotor) {
						lftMotor.controlMotor(motorSpeed, forward);
					}
				}
	
				// calculate variation
				oldVariation = variation;
				if (rgtLight.readValue() > lightThreshold)
					variation = true;
				else
					variation = false;
	
				if (variation != oldVariation)
					i++;
			}

			// test de Sortie de boucle
			if (i >= 2) {
				i = 0;
				j += 1;
			}
			if(j >= 4)
				i = 3;
		}
	}
	
	public static void main(String[] args) throws Exception {
		IRobot rb = new Robot(1, new LightSensor(SensorPort.S1), new LightSensor(SensorPort.S4));
//		rb.leftDrift();
		rb.lineFollower();
//		rb.travel(30, 2, 1);
//		rb.rStraight();
//		rb.travel(30, 2, 1);
//		rb.lineFollower();
//		rb.halfTurn();
//		rb.lineFollower();
//		rb.travel(30, 2, 1);
//		rb.leftDrift();
//		rb.lineFollower();
//		rb.travel(30, 2, 1);
//		rb.rStraight();
//		rb.travel(30, 2, 1);
//		rb.lineFollower();
//		rb.travel(30, 2, 1);
//		rb.rStraight();
//		rb.travel(30, 2, 1);
//		rb.lineFollower();

		
	}
}
