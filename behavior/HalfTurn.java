package behavior;

import lejos.nxt.NXTRegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;

public class HalfTurn implements Behavior {
	private boolean suppress = true; 
	private DifferentialPilot pilote;
	private NXTRegulatedMotor rgtMotor;
	private NXTRegulatedMotor lftMotor;
	public HalfTurn(NXTRegulatedMotor rgtMotor, NXTRegulatedMotor lftMotor) {
		this.lftMotor = lftMotor; this.rgtMotor = rgtMotor;
		pilote = new DifferentialPilot(56, 106, rgtMotor, lftMotor);
	}
	
	public void halfTurn () {
		pilote.rotate(200);
		suppress = true;
	}

	@Override
	public boolean takeControl() {
		return suppress = false;
	}

	@Override
	public void action() {
		suppress = false;
		halfTurn();
	}

	@Override
	public void suppress() {
		suppress = true;
	}
}
