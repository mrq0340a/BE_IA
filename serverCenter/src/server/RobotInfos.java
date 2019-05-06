package server;

import java.util.List;

import graph.Vertex;
import lejos.pc.comm.NXTInfo;

public class RobotInfos {
	private NXTInfo nxt;
	private Vertex start;
	private List<Vertex> orientation;
	public RobotInfos(NXTInfo nxt, Vertex start, List<Vertex> orientation) {
		super();
		this.nxt = nxt;
		this.start = start;
		this.orientation = orientation;
	}
	/**
	 * @return the nxt
	 */
	public NXTInfo getNxt() {
		return nxt;
	}
	/**
	 * @return the start
	 */
	public Vertex getStart() {
		return start;
	}
	/**
	 * @return the orientation
	 */
	public List<Vertex> getOrientation() {
		return orientation;
	}
	
	
}
