package robot;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import behavior.HalfTurn;
import behavior.Junction;
import behavior.LineFollower;
import behavior.Turn;
import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.robotics.subsumption.Behavior;

/**
 * @author MMADI
 *
 */
public class Robot {
	private NXTRegulatedMotor lftMotor = Motor.C;
	private NXTRegulatedMotor rgtMotor = Motor.A;
	private SensorPort rgtLight = SensorPort.S1;
	private SensorPort lftLight = SensorPort.S4;
//	private List<Vertex> orientation = new ArrayList<>();
	private Turn turnL;
	private Turn turnR;
	private LineFollower linef;
	private Behavior htur;
	private DataOutputStream outputStream;
	private DataInputStream inputStream;
	
	public Robot() {
		this.linef = new LineFollower(rgtMotor, lftMotor, 
				rgtLight, lftLight);  
		this.htur = new HalfTurn(rgtMotor, lftMotor); 
		this.turnL = new Turn(rgtMotor, lftMotor, 
				rgtLight, lftLight, true);
		this.turnR = new Turn(rgtMotor, lftMotor, 
				rgtLight, lftLight, false);
		
		System.out.println("Waiting for Bluetooth connection...");
		BTConnection connection = Bluetooth.waitForConnection();
		System.out.println("OK!");
		
		System.out.println("R: ");
		
		this.inputStream = connection.openDataInputStream();
		this.outputStream = connection.openDataOutputStream();
	}
	
	
	public void Communication ( ) {
		boolean run = true;
		int rand = 1;
		while (run) {
			try {
				int input = inputStream.readInt();
				if (input != 0) {
					switch (input) {
					case 1:
						System.out.println("lineF");
						linef.action();
						rand = 1;
						break;
					case 2:
						System.out.println("Left");
						turnL.action();
						rand = 2;
						break;
					case 3:
						System.out.println("right");
						turnR.action();
						rand = 3;
						break;
					case 4:
						System.out.println("demi tour");
						htur.action();
						rand = 4;
						break;
					case 21:
						System.out.println("Left");
						turnL.action();
						System.out.println("lineF");
						linef.action();
						Sound.beep();
						rand = 21;
						break;
					case 31:
						System.out.println("right");
						turnR.action();
						System.out.println("lineF");
						linef.action();
						Sound.beep();
						rand = 31;
						break;
					case 214:
						System.out.println("left");
						turnL.action();
						System.out.println("lineF");
						linef.mouve();
						System.out.println("demi tour");
						htur.action();
						rand = 214;
						break;
					case 314:
						System.out.println("right");
						turnR.action();
						System.out.println("lineF");
						linef.mouve();
						System.out.println("demi tour");
						htur.action();
						rand = 314;
						break;
					}
					outputStream.writeInt(rand);
					outputStream.flush();
				} else {
					System.out.println("Ciao");
					run = false;
				}
			} catch (IOException e) {
				System.out.println("Exception: " + e.getClass());
				run = false;
			}
		}
	}
	
}
