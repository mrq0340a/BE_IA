package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import graph.Vertex;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTInfo;

public class RobotConnection implements Runnable {

	private int idRobot;
	private List<Vertex> orientation;
	private DataInputStream m_dis;
	private DataOutputStream m_dos;
	private final NXTInfo m_nxt;
	private ServerCenter server;
	@SuppressWarnings("unused") // attention ici
	private boolean connected = false;
	private int returnedValue; // attention variable partager à manipuler avec precaution
	private Vertex position;
	private int nextCommend = 0;
	private int nextCommendH = 0;

	private List<Vertex> ordreH = new ArrayList<>();
	private List<Vertex> order = new ArrayList<>(); // on sauvegarde l'emseble d'orde
	private int rateOfRouting = 2000;
	private Vertex goalChoosen = null;
	private Vertex hospitalChoosen = null;
	private boolean victimSaved = false;
	private boolean VictimTaking = false;
	private boolean intersectionPoint = false;
	private Thread thr;
	private boolean stop = false;
	
	public RobotConnection(RobotInfos rbInfos, int idRobot, ServerCenter server) {
		this.orientation = rbInfos.getOrientation();
		this.m_nxt = rbInfos.getNxt();
		this.server = server;
		this.position = rbInfos.getStart();
		this.idRobot = idRobot;
	}

	public boolean connect(NXTComm comm) throws NXTCommException {
		if (comm.open(m_nxt)) {
			m_dis = new DataInputStream(comm.getInputStream());
			m_dos = new DataOutputStream(comm.getOutputStream());
		}
		connected = true;
		return m_dos != null;
	}

	/**
	 * @return the status of connection
	 */
	public boolean isConnected() {
		return isConnected();
	}

	@Override
	public void run() {
		boolean continuer = true;
		int toSend = 0;
		try {
			while (continuer) {
				toSend = server.sendMsg(idRobot); // get new oder
				if (toSend != 10) { // si on me demande pas d'attendre alors j'envoie la commande
//					System.out.println(" different de 10 "+toSend);
					if (this.isStop()) {
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						this.setStop(false);
					}
					this.setIntersectionPoint(false); // on sort de l'intersection
					if (toSend == 0) {
						continuer = false;
					} else {
						m_dos.writeInt(toSend);
						m_dos.flush();
						returnedValue = m_dis.readInt(); // retour fin de tache
						System.out.println(m_nxt.name + " returned " + returnedValue);
					}
				}else {
					Thread.sleep(3000); // pas sur de moi sur ce coup
				}
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}

	public String getName() {
		return this.m_nxt.name;
	}

	/**
	 * @return the returnedValue
	 */
	public int getReturneeValue() {
		return returnedValue;
	}

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
	public void setOrientation(List<Vertex> orientation) {
		this.orientation = orientation;
	}

	/**
	 * @return the position
	 */
	public Vertex getPosition() {
		return position;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(Vertex position) {
		this.position = position;
	}

	/**
	 * @return the nextCommend
	 */
	public int getNextCommend() {
		return nextCommend;
	}

	/**
	 * @param nextCommend
	 *            the nextCommend to set
	 */
	public void setNextCommend(int nextCommend) {
		this.nextCommend = nextCommend;
	}

	/**
	 * @return the order
	 */
	public synchronized List<Vertex> getOrder() {
		return order;
	}

	/**
	 * @param order
	 *            the order to set
	 */
	public void setOrder(List<Vertex> order) {
		this.order = order;
	}

	/**
	 * @return the ordreH
	 */
	public synchronized List<Vertex> getOrdreH() {
		return ordreH;
	}

	/**
	 * @param ordreH
	 *            the ordreH to set
	 */
	public void setOrdreH(List<Vertex> ordreH) {
		this.ordreH = ordreH;
	}

	/**
	 * @return the rateOfRouting
	 */
	public int getRateOfRouting() {
		return rateOfRouting;
	}

	/**
	 * @param rateOfRouting
	 *            the rateOfRouting to set
	 */
	public void setRateOfRouting(int rateOfRouting) {
		this.rateOfRouting = rateOfRouting;
	}

	/**
	 * @return the goalChoosen
	 */
	public Vertex getGoalChoosen() {
		return goalChoosen;
	}

	/**
	 * @param goalChoosen
	 *            the goalChoosen to set
	 */
	public void setGoalChoosen(Vertex goalChoosen) {
		this.goalChoosen = goalChoosen;
	}

	/**
	 * @return the victimSaved
	 */
	public boolean isVictimSaved() {
		return victimSaved;
	}

	/**
	 * @param victimSaved
	 *            the victimSaved to set
	 */
	public void setVictimSaved(boolean victimSaved) {
		this.victimSaved = victimSaved;
	}

	/**
	 * @return the victimTaking
	 */
	public boolean isVictimTaking() {
		return VictimTaking;
	}

	/**
	 * @param victimTaking
	 *            the victimTaking to set
	 */
	public void setVictimTaking(boolean victimTaking) {
		VictimTaking = victimTaking;
	}

	/**
	 * @return the hospitalChoosen
	 */
	public Vertex getHospitalChoosen() {
		return hospitalChoosen;
	}

	/**
	 * @param hospitalChoosen
	 *            the hospitalChoosen to set
	 */
	public void setHospitalChoosen(Vertex hospitalChoosen) {
		this.hospitalChoosen = hospitalChoosen;
	}

	/**
	 * @return the intersectionPoint
	 */
	public synchronized boolean isIntersectionPoint() {
		return intersectionPoint;
	}

	/**
	 * @param intersectionPoint
	 *            the intersectionPoint to set
	 */
	public synchronized void setIntersectionPoint(boolean intersectionPoint) {
		this.intersectionPoint = intersectionPoint;
	}
	
	/**
	 * @return the thr
	 */
	public Thread getThr() {
		return thr;
	}

	/**
	 * @param thr the thr to set
	 */
	public void setThr(Thread thr) {
		this.thr = thr;
	}

	/**
	 * @return the stop
	 */
	public boolean isStop() {
		return stop;
	}

	/**
	 * @param stop the stop to set
	 */
	public void setStop(boolean stop) {
		this.stop = stop;
	}

	
	// true on recupere la commande sur les ordre
	// false on recupere sur les commande de l'hopital
	public Vertex getNextCmd(boolean typeOfCmd) {
		if (typeOfCmd) {
			return order.remove(0);
		} else {
			return ordreH.remove(0);
		}
	}
}
