package mainTest;

//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//import behavior.HalfTurn;
//import behavior.Junction;
//import behavior.LineFollower;
//import behavior.Turn;
//import lejos.nxt.Button;
//import lejos.nxt.Sound;
//import lejos.nxt.comm.BTConnection;
//import lejos.nxt.comm.Bluetooth;
//import lejos.robotics.subsumption.Behavior;
import robot.Robot;
import routing.RoutingWithOneRobot;

public class MainTest {
	
	void modif(int n ) {
		n = 2;
	}
	public static void main(String[] args) {		
		Robot rb = new Robot();	
		rb.Communication();
		
//		Behavior linef = new LineFollower(rb.getRgtMotor(), rb.getLftMotor(), 
//				rb.getRgtLight(), rb.getLftLight());  
//		final Behavior htur = new HalfTurn(rb.getRgtMotor(), rb.getLftMotor()); 
//		Behavior turnL = new Turn(rb.getRgtMotor(), rb.getLftMotor(), 
//				rb.getRgtLight(), rb.getLftLight(), true);
//		Behavior turnR = new Turn(rb.getRgtMotor(), rb.getLftMotor(), 
//				rb.getRgtLight(), rb.getLftLight(), false);
//		Behavior jun = new Junction(rb.getRgtMotor(), rb.getLftMotor(), 
//				rb.getRgtLight(), rb.getLftLight());
//		
//		System.out.println("Waiting for Bluetooth connection...");
//		BTConnection connection = Bluetooth.waitForConnection();
//		System.out.println("OK!");
//
//		int rand = new Random().nextInt();
//		
//		System.out.println("R: " + rand);
//		
//		DataInputStream inputStream = connection.openDataInputStream();
//		DataOutputStream outputStream = connection.openDataOutputStream();
//
//		boolean run = true;
//		while (run) {
//
//			try {
//				int input = inputStream.readInt();
//				if (input != 0) {
//					if (input == 1) {
////						new Thread(new Runnable() {
////
////							@Override
////							public void run() {
////								htur.action();
////							}
////						}).start();
////						System.out.println("fini");
//						htur.action();	
//					}
////					Button.waitForAnyPress();
//					if (Button.ESCAPE.isDown()) {
//						rand = 2;
//					}else {
//						rand = 1;
//					}
//					
//					outputStream.writeInt(rand);
//					outputStream.flush();
//				} else {
//					run = false;
//				}
//			} catch (IOException e) {
//				System.out.println("Exception: " + e.getClass());
//				run = false;
//			}
//		}
//		
	}
	
	
}
