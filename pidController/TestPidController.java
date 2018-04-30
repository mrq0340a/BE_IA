package pidController;

import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.util.PIDController;

public class TestPidController {

	public static void main(String[] args) {
		int SPEED = 200;
		PIDController pid = new PIDController(487, 10);
		pid.setPIDParam(PIDController.PID_KP, 2.0f);
		// pid.setPIDParam(PIDController.PID_KI, 0.05f);
		// pid.setPIDParam(PIDController.PID_KD, 10f);
		Motor.A.setSpeed(SPEED);
		Motor.C.setSpeed(SPEED);
		final LightSensor detector = new LightSensor(SensorPort.S1);
		while (!Button.ESCAPE.isDown()) {
			// range is from 200 to 500
			int sensor = detector.readNormalizedValue();
			// System.out.println(sensor);
			int speedDelta = pid.doPID(sensor);
			// System.out.println(speedDelta);
			Motor.B.setSpeed(SPEED - speedDelta);
			Motor.C.setSpeed(SPEED + speedDelta);
			Motor.A.forward();
			Motor.C.forward();

		}
	}

}
