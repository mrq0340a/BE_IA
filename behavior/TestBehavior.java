package behavior;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.robotics.subsumption.Behavior;

public class TestBehavior {

	public static void main(String[] args) throws InterruptedException {
		// NXTRegulatedMotor mG = Motor.C; NXTRegulatedMotor mD = Motor.A;
		// SensorPort LD = SensorPort.S1; SensorPort LG = SensorPort.S4;
		Behavior linef = new LineFollower(Motor.A, Motor.C, SensorPort.S1, SensorPort.S4);
		Behavior htur = new HalfTurn(Motor.A, Motor.C);
		Behavior turnL = new Turn(Motor.A, Motor.C, SensorPort.S1, SensorPort.S4, true);
		Behavior turnR = new Turn(Motor.A, Motor.C, SensorPort.S1, SensorPort.S4, false);
		Behavior strL = new Straight(Motor.A, Motor.C, SensorPort.S1, SensorPort.S4, true);
		Behavior strR = new Straight(Motor.A, Motor.C, SensorPort.S1, SensorPort.S4, false);
		Behavior jun = new Junction(Motor.A, Motor.C, SensorPort.S1, SensorPort.S4);
		TestMarking ts = new TestMarking(Motor.C, Motor.A, SensorPort.S1, SensorPort.S4);
		Behavior[] bv = {linef, htur, turnL, turnR, strL, strR, jun};
//		str.action();
//		linef.action();
//		htur.action();
//		linef.action();
//		LCD.drawString("Press LEFT", 0, 2);
//		LCD.drawString("to start", 0, 3);
//		while (!Button.LEFT.isDown()) {
//			LCD.drawInt(new LightSensor(SensorPort.S1).readNormalizedValue() * 1000 + 
//					new LightSensor(SensorPort.S4).readNormalizedValue(), 3, 9, 0);
//		}
//		linef.action();
//		turn.action();
//		while (true) {
//			bv[0].action();
//			switch (ts.testMarking()) {
//			case SIMPLE_STRIP:
//				bv[3].action();
//				break;
//			case DOUBLE_STRIP:
//				bv[3].action();
//				break;
//			case OBSTACLE:
//				bv[3].action();
//				break;
//			default:
//				break;
//			}
//		}

		bv[0].action();
		ts.testMarking();
		bv[2].action();
		bv[0].action();
		ts.testMarking();
		bv[2].action();
		bv[0].action();
		ts.testMarking();
		bv[2].action();
		bv[0].action();
		ts.testMarking();
		bv[2].action();
		bv[0].action();
		ts.testMarking();
		bv[6].action();
		bv[0].action();
		ts.testMarking();
		bv[1].action();
		
		
		bv[0].action();
		ts.testMarking();
		bv[6].action();
		bv[0].action();
		ts.testMarking();
		bv[3].action();
		bv[0].action();
		ts.testMarking();
		bv[3].action();
		bv[0].action();
		ts.testMarking();
		bv[3].action();
		bv[0].action();
		ts.testMarking();
		bv[3].action();
		bv[0].action();
		ts.testMarking();
		
//		Arbitrator arb = new Arbitrator(bv);
//		arb.start();

	}
}
