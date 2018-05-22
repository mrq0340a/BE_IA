package behavior;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.robotics.subsumption.Behavior;

/**
 * @author MMADI
 *
 */
public class TestBehavior {

	public static void main(String[] args) throws InterruptedException {
//		 NXTRegulatedMotor mG = Motor.C; NXTRegulatedMotor mD = Motor.A;
//		 SensorPort LD = SensorPort.S1; SensorPort LG = SensorPort.S4;
//		Robot rb = new Robot();
		Behavior linef = new LineFollower(Motor.A, Motor.C, SensorPort.S1, SensorPort.S4);  
		Behavior htur = new HalfTurn(Motor.A, Motor.C); 
		Behavior turnL = new Turn(Motor.A, Motor.C, SensorPort.S1, SensorPort.S4, true);
		Behavior turnR = new Turn(Motor.A, Motor.C, SensorPort.S1, SensorPort.S4, false);
		Behavior strL = new Straight(Motor.A, Motor.C, SensorPort.S1, SensorPort.S4, true);
		Behavior strR = new Straight(Motor.A, Motor.C, SensorPort.S1, SensorPort.S4, false);
		Behavior jun = new Junction(Motor.A, Motor.C, SensorPort.S1, SensorPort.S4);
		TestMarking ts = new TestMarking(Motor.C, Motor.A, SensorPort.S1, SensorPort.S4);
		Behavior[] bv = {linef, htur, turnL, turnR, strL, strR, jun};
		linef.action();
		turnL.action();
//		ts.testMarking(); 
//		turnR.action();
//		linef.action();
//		ts.testMarking(); 
//		turnL.action();
//		linef.action();
//		ts.testMarking(); 
//		turnR.action();
//		linef.action();
//		ts.testMarking(); 
//		turnL.action();
//		linef.action();
		
//		while (! Button.ESCAPE.isDown()) {
//			LCD.drawInt(new LightSensor(SensorPort.S1).getNormalizedLightValue(), 0, 0);
//			LCD.drawInt(new LightSensor(SensorPort.S4).getNormalizedLightValue(), 0, 1);
//		}
		
	}
}