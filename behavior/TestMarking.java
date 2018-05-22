package behavior;


import lejos.nxt.LightSensor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.robotics.navigation.DifferentialPilot;
/**
 * @author MMADI
 *
 */
public class TestMarking {
	protected final static int DefaultMotorSpeed = 300;
	protected static final int lightThresholdNormalize = 487;
	protected static final int DefautRotateSpeed = 180;
	protected static final float slowDownPercent = 0.30f;
	protected static final int motorSpeedTurn = 250;
	protected static final float slowDownPercentTurn = 0.30f;
	
	private NXTRegulatedMotor lftMotor;
	private NXTRegulatedMotor rgtMotor;
	private LightSensor lftLight, rgtLight;
	private boolean suppress = false; 
	private DifferentialPilot pilote;
	private Mark marking;
	
	public TestMarking(NXTRegulatedMotor rgtMotor, NXTRegulatedMotor lftMotor, 
			SensorPort rgtLight, SensorPort lftLight) {
		this.lftMotor = lftMotor;
		this.rgtMotor = rgtMotor;
		this.lftLight = new LightSensor(lftLight);
		this.rgtLight = new LightSensor(rgtLight);
		pilote = new DifferentialPilot(56, 106, rgtMotor, lftMotor);
		pilote.setTravelSpeed(60);
	}
	
	public Mark testMarking(){
		pilote.travel(40);
		if (lftLight.readNormalizedValue() > lightThresholdNormalize
				&& rgtLight.readNormalizedValue() > lightThresholdNormalize) { // double strip
			pilote.travel(20);
			if (lftLight.readNormalizedValue() > lightThresholdNormalize
					&& rgtLight.readNormalizedValue() > lightThresholdNormalize) {
				pilote.stop();
				return Mark.OBSTACLE;
			}
			pilote.stop();
			return Mark.DOUBLE_STRIP;
		}
		pilote.stop();
		return  Mark.SIMPLE_STRIP;  
	}
	
//	@Override
//	public boolean takeControl() {
//		 return lftLight.readNormalizedValue() > lightThresholdNormalize &&
//				 rgtLight.readNormalizedValue() > lightThresholdNormalize;
//	}
//
//	@Override
//	public void action() {
//		setSuppress(false);
//	}
//
//	@Override
//	public void suppress() {
//		setSuppress(true);
//	}

	public Mark getMarking() {
		return marking;
	}

	public void setMarking(Mark marking) {
		this.marking = marking;
	}

	public boolean isSuppress() {
		return suppress;
	}

	public void setSuppress(boolean suppress) {
		this.suppress = suppress;
	}
	
}
