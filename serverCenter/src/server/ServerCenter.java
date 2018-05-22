package server;

import java.util.ArrayList;
import java.util.List;

import javax.swing.text.Position;

import graph.CreatGraph;
import graph.Direction;
import graph.Graph;
import graph.Vertex;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;
import routingAlgorithm.Astar;

public class ServerCenter {

	private List<RobotConnection> robotConnection = new ArrayList<>();
	private List<Thread> threadControl = new ArrayList<>();
	private List<Vertex> goals;
	private List<Vertex> hospitals;
	private Graph graph;

	public ServerCenter(List<RobotInfos> robot, Graph graph, List<Vertex> hospitals, List<Vertex> goals)
			throws NXTCommException {
		this.graph = graph;
		this.hospitals = hospitals;
		this.goals = goals;
		int i = 0;
		for (RobotInfos rb : robot) {
			RobotConnection rbC = new RobotConnection(rb, i++, this);
			robotConnection.add(rbC);
			// demarrage de la connection
			rbC.connect(NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH));
		}
	}

	public void start() {
		for (RobotConnection robotConnection2 : robotConnection) {
			Thread tr = new Thread(robotConnection2);
			threadControl.add(tr);
			robotConnection2.setThr(tr);
			tr.start();
		}
	}

	public void destoy() {
		for (Thread tr : threadControl) {
			try {
				tr.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public int sendMsg(int idRobot) { // un robot s'identifie avant toute demande
		RobotConnection rb = robotConnection.get(idRobot);
		if (rb.getOrder().isEmpty() && rb.getOrdreH().isEmpty()) { // soit on est en fin de parcours ou debut de
																	// recherche
			if (this.getGoals().isEmpty()) { // tous les victime ont été sauvé on sort du circuit
				// TODO pour l'instant on arrete juste le programme
				return 0;
			} else { // si non on calcule la nouvelle victime à aller sauver
				chooseVictim(idRobot);
				return begin(idRobot, rb.getOrder());
			}
		} else { // en cours de parcours
			int answer = rb.getReturneeValue();
			// 4 demi tour, 2-->left, 3-->right, 1-->lineF
			if (answer == 4 || answer == 2 || answer == 3) {
				return 1; // on fait un suivit de ligne
			} else { // je dois faire une gestion au cas ou il y a croisement
				rb.setIntersectionPoint(true); // je suis sur un point d'intersection
				return gestion(idRobot);
			}

		}
	}

	private int getDirection(Vertex direction, boolean bip) {
		if (bip) {
			if (direction.getDirection() == Direction.LEFT) {
				return 21;
			} else {
				return 31;
			}
		} else {
			if (direction.getDirection() == Direction.LEFT) {
				return 2;
			} else {
				return 3;
			}
		}

	}

	private int gestion(int idRobot) {
		RobotConnection rbc = robotConnection.get(idRobot);
		Vertex cmd;
		boolean bip = false;
		if (!rbc.getOrder().isEmpty()) { // il y a encore de cmd possible
			cmd = rbc.getNextCmd(true);
			if (rbc.getGoalChoosen().equals(cmd)) { // j'ai recuperer la victime
				rbc.setVictimTaking(true);
				bip = true;
			}
		} else {
			if (rbc.isVictimTaking()) {
				System.out.println("victime recuperer ");
				// on decide de la directoin a prendre dans cette condition
				rbc.setVictimTaking(false);
				return begin(idRobot, rbc.getOrdreH());
			} else {
				cmd = rbc.getNextCmd(false);
				if (rbc.getHospitalChoosen().equals(cmd)) { // j'ai recuperer la victime
					System.out.println("victime sauver");
					rbc.setVictimSaved(true);
					bip = true;
				}
			}
		}

		// gestion de changement de direction
		return priority(cmd, bip, idRobot);
		// System.out.println(cmd.getNameV());
		// mis à jour de l'orientation du ribot
		// rbc.setPosition(cmd);
		// rbc.setOrientation(cmd.getVoisinsFace());
		// return getDirection(cmd, bip);
	}

	// determine la priorite de passage en cas de session critique
	private synchronized int priority(Vertex cmd, boolean bip, int idRobot) {
		RobotConnection rbcfac = null;
		RobotConnection rbc = robotConnection.get(idRobot);
//		if (rbc.isStop()) {
//			try {
//				Thread.sleep(5000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			rbc.setStop(false);
//		}
		// mise à jour mais attention
		if ((rbcfac = isOccupated(idRobot)) != null) {
			if (!rbcfac.getPosition().equals(cmd)) { // le robot n'est pas sur le coté ou je veux y aller
//				System.out.println("il n' y a pas de robot ou je veux aller");
				rbc.setPosition(cmd);
				rbc.setOrientation(cmd.getVoisinsFace());	// traverser
				System.out.println(rbc.getName() + "OK 1");
				rbcfac.setStop(true);
				return getDirection(cmd, bip);
			} else { // il est la ou je veux aller

				System.out.println(rbc.getName() + "OK 2");
				if (!rbcfac.isIntersectionPoint()) {
					if (bip) { // on remet la commande
						rbc.getOrder().add(0, cmd);
					} else {
						rbc.getOrdreH().add(0, cmd);
					}
					return 10; // demande d'attendre
				} else {

					System.out.println(rbc.getName() + "OK 3");
					int ret = 0;
				
					if (rbcfac.getOrder().get(0).equals(rbc.getPosition())) {
						ret = collision(cmd, idRobot, bip);
					}else {
						rbc.setPosition(cmd);
						rbc.setOrientation(cmd.getVoisinsFace());
							// traverser
						ret = getDirection(cmd, bip);
					}
					rbcfac.setStop(true);
					System.out.println(rbc.getName() + "OK 4");
					return ret;
				}
			}
		}else {
			rbc.setPosition(cmd);
			rbc.setOrientation(cmd.getVoisinsFace());
			return getDirection(cmd, bip);
		}
	}

	private int collision(Vertex cmd, int idRobot, boolean bip) {
		RobotConnection rbc = robotConnection.get(idRobot);
		int ret;
		if (cmd.getDirection() == Direction.LEFT) {
			// on remet la commande en changean la direction
			ret = 214; // on prends la direction inverse on avance et on fait demi tour
		} else {
			ret = 314;
		}

		if (cmd.getDirection() == Direction.LEFT) {
			cmd.setDirection(Direction.RIGHT);
		} else {
			cmd.setDirection(Direction.LEFT);
		}
		if (bip) {
			rbc.getOrder().add(0, cmd);
		} else {
			rbc.getOrdreH().add(0, cmd);
		}
		
		miseAjour(rbc, cmd);
		return ret;

	}

	private void miseAjour(RobotConnection rbc, Vertex cmd) {
		List<Vertex> newOrientation = new ArrayList<>();
		newOrientation.add(rbc.getPosition());
		newOrientation.add(cmd);
		rbc.getOrder().add(cmd);
		for (Vertex vertex : rbc.getOrientation()) {
			if (!vertex.equals(cmd)) {
				rbc.setPosition(vertex);
				rbc.getPosition().setVoisinsFace(newOrientation);
				rbc.setOrientation(newOrientation);
				return;
			}
		}

	}

	// cherche s'il y a un robot qui peut provoquer un conflic
	private RobotConnection isOccupated(int idRobot) {
		RobotConnection rbc = robotConnection.get(idRobot);
		for (int i = 0; i < robotConnection.size(); i++) {
			if (i != idRobot) {
				if (isFace(rbc, robotConnection.get(i))) {
					return robotConnection.get(i);
				}
			}
		}
		return null;
	}

	// si le sommet me fait face
	private boolean isFace(RobotConnection rbc, RobotConnection robotConnection2) {
		return rbc.getOrientation().contains(robotConnection2.getPosition())
				&& robotConnection2.getOrientation().contains(rbc.getPosition());

	}

	// mis à jour de la nouvelle orientation du robot
	/**
	 * @param rbc
	 */
	private void updateOrientation(RobotConnection rbc) {
		List<Vertex> adj = this.graph.getAdjacent(rbc.getPosition());
		List<Vertex> update = new ArrayList<>();
		for (Vertex vertex : adj) {
			if (!rbc.getOrientation().contains(vertex)) {
				update.add(vertex);
			}
		}
		rbc.setOrientation(update);
	}

	// determine le type de demarrage demi tour tous droit ou demi tour simplement
	private int begin(int idRobot, List<Vertex> ordr) {
		RobotConnection rbc = robotConnection.get(idRobot);
		System.out.println("je suis "+rbc.getName()+" et ");
		System.out.println("je pars de "+rbc.getPosition().getNameV()+" à "+rbc.getGoalChoosen().getNameV());
		if (ordr.isEmpty() || !rbc.getOrientation().contains(ordr.get(0))) {
//			System.out.println(rbc.getPosition().getNameV());
//			graph.printAdjacent(rbc.getOrientation());
			updateOrientation(rbc);
			return 4;
		} else {
			return 1;
		}
	}

	private void searchVictim(Vertex starting, Vertex g, RobotConnection rbc) {
		Astar as = new Astar(graph, g, starting);
		as.aStar(rbc.getOrientation());
		List<Vertex> newOrientation = as.getGoal().getVoisinsFace();
		Vertex newStart = as.getGoal();
		for (Vertex vertex : this.hospitals) {
			Astar as1 = new Astar(graph, vertex, newStart);
			as1.aStar(newOrientation);
			if (as1.getRateOfRouting() + as.getRateOfRouting() < rbc.getRateOfRouting()) { // on calcule le circuit en
																							// entier
				rbc.setRateOfRouting(as1.getRateOfRouting() + as.getRateOfRouting());
				rbc.setOrder(as.getOrder()); // mis a jour des ordres
				rbc.setOrdreH(as1.getOrder());
				rbc.setGoalChoosen(as.getGoal());
				rbc.setHospitalChoosen(as1.getGoal());
			}
		}
	}

	// on synchronise cette fonction
	public synchronized void chooseVictim(int idRobot) {
		RobotConnection rbc = robotConnection.get(idRobot);
		rbc.setRateOfRouting(2000); // initialisation du parcours
		for (Vertex g : this.getGoals()) {
			searchVictim(rbc.getPosition(), g, rbc); // on cherche la victime ideal
		}
		// une fois la victime choisi on le supprime de la liste des victimes
		goals.remove(rbc.getGoalChoosen()); // verifier si ce n'est pas vide
	}

	public static void main(String[] args) throws NXTCommException {
		// information d'un roboot
		NXTInfo nxt = new NXTInfo(NXTCommFactory.BLUETOOTH, "R.O.B.B.Y.", "00:16:53:1C:15:FC");
		List<Vertex> orientation = new ArrayList<>();
		orientation.add(new Vertex("B", 0, 7, false, Direction.RIGHT));
		orientation.add(new Vertex("C", 0, 2, false, Direction.LEFT));
		Vertex start = new Vertex("A", 0, 28, false, Direction.STRAIGHT);

//		NXTInfo hydra = new NXTInfo(NXTCommFactory.BLUETOOTH, "L.A.T.I.S", "00:16:53:1C:24E7");
		NXTInfo hydra = new NXTInfo(NXTCommFactory.BLUETOOTH, "L.A.T.I.S", "00:16:53:16:22:92");
		
		List<Vertex> orientation1 = new ArrayList<>();
		orientation1.add(new Vertex("K", 0, 4, false, Direction.LEFT));
		orientation1.add(new Vertex("L", 0, 13, false, Direction.RIGHT));
		Vertex start1 = new Vertex("A", 0, 28, false, Direction.STRAIGHT);

		// ensemble de robot a connecter
		List<RobotInfos> robot = new ArrayList<>();
		robot.add(new RobotInfos(nxt, start, orientation));
		robot.add(new RobotInfos(hydra, start1, orientation1));
		// ensemble des buts
		List<Vertex> goals = new ArrayList<>();
		goals.add(new Vertex("I", 0, 2, false, Direction.STRAIGHT));
		goals.add(new Vertex("K", 0, 4, false, Direction.STRAIGHT));
		// ensemble d'hopitaux
		List<Vertex> hospitals = new ArrayList<>();
		hospitals.add(new Vertex("B", 0, 7, false, Direction.STRAIGHT));
		hospitals.add(new Vertex("G", 0, 9, false, Direction.STRAIGHT));

		Graph graph = new Graph("cours", CreatGraph.compet2());

		ServerCenter sc = new ServerCenter(robot, graph, hospitals, goals);
		sc.start();
		sc.destoy();
	}

	/**
	 * @return the goals
	 */
	public synchronized List<Vertex> getGoals() {
		return goals;
	}

}
