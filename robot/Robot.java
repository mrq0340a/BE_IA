package robot;

import java.util.ArrayList;
import java.util.List;

import graph.Graph;
import graph.Vertex;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;

/**
 * @author MMADI
 *
 */
public class Robot {
	private NXTRegulatedMotor lftMotor = Motor.C;
	private NXTRegulatedMotor rgtMotor = Motor.A;
	private SensorPort rgtLight = SensorPort.S1;
	private SensorPort lftLight = SensorPort.S4;
	private List<Vertex> orientation = new ArrayList<>();

	public Robot(List<Vertex> orientation) {
		this.orientation = orientation;
	}

	/**
	 * @return the lftMotor
	 */
	public NXTRegulatedMotor getLftMotor() {
		return lftMotor;
	}

	/**
	 * @param lftMotor
	 *            the lftMotor to set
	 */
	public void setLftMotor(NXTRegulatedMotor lftMotor) {
		this.lftMotor = lftMotor;
	}

	/**
	 * @return the rgtMotor
	 */
	public NXTRegulatedMotor getRgtMotor() {
		return rgtMotor;
	}

	/**
	 * @param rgtMotor
	 *            the rgtMotor to set
	 */
	public void setRgtMotor(NXTRegulatedMotor rgtMotor) {
		this.rgtMotor = rgtMotor;
	}

	/**
	 * @return the rgtLight
	 */
	public SensorPort getRgtLight() {
		return rgtLight;
	}

	/**
	 * @param rgtLight
	 *            the rgtLight to set
	 */
	public void setRgtLight(SensorPort rgtLight) {
		this.rgtLight = rgtLight;
	}

	/**
	 * @return the lftLight
	 */
	public SensorPort getLftLight() {
		return lftLight;
	}

	/**
	 * @param lftLight
	 *            the lftLight to set
	 */
	public void setLftLight(SensorPort lftLight) {
		this.lftLight = lftLight;
	}
	
	
//	public boolean isEmptyOrientation( ) {
//		return orientation.isEmpty();
//	}
	
	/**
	 * @return the orientation
	 */
	public List<Vertex> getOrientation() {
		return orientation;
	}

	/**
	 * @param orientation
	 *            the orientation to set
	 */
	public void setDirection(List<Vertex> direction) {
		this.orientation = direction;
	}

}
