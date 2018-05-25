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
				rb.setPosition(null);
				rb.setOrientation(null);
				return 0;
			} else { // si non on calcule la nouvelle victime à aller sauver
				chooseVictim(idRobot);
//				System.out.println("je suis "+rb.getName()+" je vais sauver "+rb.getGoalChoosen().getNameV());
//				System.out.println("premiere commande "+rb.getOrder().get(0).getNameV()+" orienté vers");
//				graph.printAdjacent(rb.getOrder().get(0).getVoisinsFace());
				if (rb.getGoalChoosen().getNameV().equals("L")) {
					System.out.println("j'affiche le sommet L");
					for (Vertex vertex : rb.getOrder()) {
						System.out.println("sommet : "+vertex.getNameV());
						graph.printAdjacent(vertex.getVoisinsFace());
					}
				}
				return begin(idRobot, rb.getOrder());
			}
		} else { // en cours de parcours
			int answer = rb.getReturneeValue();
			// 4 demi tour, 2-->left, 3-->right, 1-->lineF
			if (answer == 4 || answer == 2 || answer == 3 || answer == 214 || answer == 314) {
				return 1; // on fait un suivit de ligne
			} else { // je dois faire une gestion au cas ou il y a croisement
				rb.setIntersectionPoint(true); // je suis sur un point d'intersection
				return gestion(idRobot);
			}

		}
	}

	private int getDirection(Vertex direction, boolean bip, RobotConnection rbc) {
		System.out.println(rbc.getName()+" tu es a "+rbc.getPosition().getNameV()
				+" va au sommet "+direction.getNameV()+" par la "+direction.getDirection());
		rbc.setPosition(direction);
		rbc.setOrientation(null);
		rbc.setOrientation(direction.getVoisinsFace());
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
		boolean vide = false; //
		if (!rbc.getOrder().isEmpty()) { // il y a encore de cmd possible
			cmd = rbc.getNextCmd(true);
			if (rbc.getGoalChoosen().equals(cmd)) { // j'ai recuperer la victime
				rbc.setVictimTaking(true);
				bip = true;
			}
			vide = true;
		} else {
			if (rbc.isVictimTaking()) {
				System.out.println("je suis "+rbc.getName()+" j'amène "+rbc.getGoalChoosen().getNameV()+
						" à l'hopitale "+rbc.getHospitalChoosen().getNameV());
				// on decide de la directoin a prendre dans cette condition
				rbc.setVictimTaking(false);
				return begin(idRobot, rbc.getOrdreH());
			} else {
				cmd = rbc.getNextCmd(false);
				if (rbc.getHospitalChoosen().equals(cmd)) { // j'ai recuperer la victime
					System.out.println("je suis "+rbc.getName()+" je depose "+rbc.getGoalChoosen().getNameV()
							+ "à l'hopital "+rbc.getHospitalChoosen().getNameV());
					rbc.setVictimSaved(true);
					bip = true;
				}
				vide = false;
			}
		}

		// gestion de changement de direction
		return priority(cmd, bip, vide, idRobot);
	}
	
	// determine la priorite de passage en cas de session critique
	private synchronized int priority(Vertex cmd, boolean bip, boolean vide, int idRobot) {
		RobotConnection rbcfac = null;
		RobotConnection rbc = robotConnection.get(idRobot);
		// mise à jour mais attention
		int ret = 0;
		if ((rbcfac = isOccupated(idRobot)) != null) {
			if (!rbcfac.getPosition().equals(cmd)) { // le robot n'est pas sur le coté ou je veux y aller
				System.out.println(rbc.getName()+" je suis à la position : "+rbc.getPosition().getNameV()+
						" il n'y a personne à la position "+cmd.getNameV());
				rbcfac.setStop(true);
				return getDirection(cmd, bip, rbc);
			} else { // il est la ou je veux aller
				if (!rbcfac.isIntersectionPoint() || hospitals.contains(rbcfac.getPosition())) {
					cmdRst(cmd, vide, rbc);
					System.out.println("je suis "+rbc.getName()
					+" en position : "+rbc.getPosition().getNameV()
					+" et j'attends");
					System.out.println("car "+rbcfac.getName()+" est en position "+
					rbcfac.getPosition().getNameV());
					return 10; // demande d'attendre
				} else {
					// arrivé à l'intersection 
					if (getFirstCmd(rbcfac).equals(rbc.getPosition())) {
						System.out.println(rbc.getName() + " c'est moi qui gère la collision");
						System.out.println("je veux aller à "+cmd.getNameV());
						graph.printAdjacent(cmd.getVoisinsFace());
						ret = collision(cmd, idRobot, vide);
					}else {
//						System.out.println(rbc.getName() + " j'ai la priorité");
						ret = getDirection(cmd, bip, rbc);
					}
					rbcfac.setStop(true);
//					return ret;
				}
			}
		}else {
			ret = getDirection(cmd, bip, rbc);
		}
		rbc.setIntersectionPoint(false); // mettre à ajour qu'il quitte l'intersection
		return ret;
	}
	
	private Vertex getFirstCmd(RobotConnection rbc) {
		if (!rbc.getOrder().isEmpty()) {
			return rbc.getOrder().get(0);
		}else {
			if (!rbc.getOrdreH().isEmpty()) {
				return rbc.getOrdreH().get(0);
			}else {
				return null;
			}
		}
	}
	
	private int collision(Vertex cmd, int idRobot, boolean vide) {
		RobotConnection rbc = robotConnection.get(idRobot);
		int ret;
		if (cmd.getDirection() == Direction.LEFT) {
			// on remet la commande en changean la direction
			cmd.setDirection(Direction.RIGHT);
			ret = 314; // on prends la direction inverse on avance et on fait demi tour
			
		} else {
			cmd.setDirection(Direction.LEFT);
			ret = 214;
		}
		System.out.println(rbc.getName()+" j éais orienté vers ");
		graph.printAdjacent(rbc.getOrientation());
		System.out.println(" et ma position étais "+rbc.getPosition().getNameV());
		miseAjour(rbc, cmd, vide);
		System.out.println(rbc.getName()+" maintenant je suis orienté ");
		graph.printAdjacent(rbc.getOrientation());
		System.out.println(" et ma position "+rbc.getPosition().getNameV());
		return ret;

	}

	private void miseAjour(RobotConnection rbc, Vertex cmd, boolean vide) {
		List<Vertex> newOrientation = new ArrayList<>();
		newOrientation.add(rbc.getPosition());
		newOrientation.add(cmd);
		System.out.println("l'ancienne orientation");
		graph.printAdjacent(rbc.getOrientation());
		System.out.println("nouvelle orientation calculée ");
		graph.printAdjacent(newOrientation);
		cmdRst(cmd, vide, rbc);
		for (Vertex vertex : rbc.getOrientation()) {
			if (!vertex.equals(cmd)) {
				System.err.println("je rentre de dans ");
				rbc.setPosition(vertex);
				rbc.getPosition().setVoisinsFace(newOrientation);
				rbc.setOrientation(null);
				rbc.setOrientation(newOrientation);
				return;
			}
		}

	}
	
	private void cmdRst(Vertex cmd, boolean vide, RobotConnection rbc) {
		if (vide) {
			rbc.getOrder().add(0, cmd);
		} else {
			rbc.getOrdreH().add(0, cmd);
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
				&& (robotConnection2.getOrientation().contains(rbc.getPosition()) 
					|| hospitals.contains(robotConnection2.getPosition()));
	}

	// mis à jour de la nouvelle orientation du robot
	/**
	 * @param rbc
	 */
	// TODO
	private void updateOrientation(RobotConnection rbc) {
		List<Vertex> adj = this.graph.getAdjacent(rbc.getPosition());
		List<Vertex> update = new ArrayList<>();
		for (Vertex vertex : adj) {
			if (!rbc.getOrientation().contains(vertex)) {
				update.add(vertex);
			}
		}
		
		if (update.size() == 1) {
			Vertex temp = update.get(0);
			for (Vertex vertex : filter(adj)) {
				if (temp.getDirection() != vertex.getDirection()){
					update.add(vertex);
				}
			}
		}
		rbc.getPosition().setVoisinsFace(update);
		rbc.setOrientation(null);
		rbc.setOrientation(update);
	}
	
	private List<Vertex> filter(List<Vertex> adj) {
		List<Vertex> array = new ArrayList<>();
		
		for (int i = 0; i < adj.size(); i++) {
		    for (int j = 0; j < adj.size(); j++) {
		    	if (i != j && adj.get(i).equals(adj.get(j))) {
					array.add(adj.get(i));
					array.add(adj.get(j));
					return array;
				}
			}
		}
		return array;
	}
	
	// determine le type de demarrage demi tour tous droit ou demi tour simplement
	private int begin(int idRobot, List<Vertex> ordr) {
		RobotConnection rbc = robotConnection.get(idRobot);
//		System.out.println(rbc.getName());
//		System.out.println("depart "+rbc.getPosition().getNameV()+" victime "
//		+rbc.getGoalChoosen().getNameV()+" hopital "+rbc.getHospitalChoosen().getNameV());
		if (ordr.isEmpty() || !rbc.getOrientation().contains(ordr.get(0))) {
			updateOrientation(rbc);
			return 4;
		} else {
			return 1;
		}
	}

	private void searchVictim(Vertex starting, Vertex g, RobotConnection rbc) {
		Astar as = new Astar(graph, g, starting);
//		System.out.println("orientation de "+rbc.getName());
//		graph.printAdjacent(rbc.getOrientation());
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
		System.out.println(rbc.getName()+" je suis en position "+rbc.getPosition().getNameV()
				+" orienté vers ");
		graph.printAdjacent(rbc.getOrientation());
		rbc.setRateOfRouting(2000); // initialisation du parcours
		for (Vertex g : this.getGoals()) {
			searchVictim(rbc.getPosition(), g, rbc); // on cherche la victime ideal
		}
		// une fois la victime choisi on le supprime de la liste des victimes
		goals.remove(rbc.getGoalChoosen()); // verifier si ce n'est pas vide
		System.out.println("je vais sauver "+rbc.getGoalChoosen().getNameV()
				+" je l'amène "+rbc.getHospitalChoosen().getNameV());
	}

	public static void main(String[] args) throws NXTCommException {
		// information d'un roboot
		NXTInfo nxt = new NXTInfo(NXTCommFactory.BLUETOOTH, "R.O.B.B.Y.", "00:16:53:1C:15:FC");
		List<Vertex> orientation = new ArrayList<>();
		orientation.add(new Vertex("B", 0, 7, false, Direction.RIGHT));
		orientation.add(new Vertex("C", 0, 2, false, Direction.LEFT));
		Vertex start = new Vertex("A", 0, 28, false, Direction.STRAIGHT);

		NXTInfo hydra = new NXTInfo(NXTCommFactory.BLUETOOTH, "L.A.T.I.S", "00:16:53:1C:24E7");
//		NXTInfo hydra = new NXTInfo(NXTCommFactory.BLUETOOTH, "L.A.T.I.S", "00:16:53:16:22:92");
//		NXTInfo hydra = new NXTInfo(NXTCommFactory.BLUETOOTH, "L.A.T.I.S", "00:16:53:18:EB:71");
		
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
		goals.add(new Vertex("H", 0, 9, false, Direction.STRAIGHT));
		goals.add(new Vertex("J", 0, 8, false, Direction.STRAIGHT));
		goals.add(new Vertex("L", 0, 13, false, Direction.STRAIGHT));
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
