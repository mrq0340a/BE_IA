package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import graph.CreatGraph;
import graph.Direction;
import graph.Graph;
import graph.Vertex;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;
import routingAlgorithm.Astar;


public class RandomConnection implements Runnable {

	private DataInputStream m_dis;
	private DataOutputStream m_dos;
	private final NXTInfo m_nxt;

	public RandomConnection(NXTInfo _nxt) {
		m_nxt = _nxt;
	}

	public boolean connect(NXTComm _comm) throws NXTCommException {
		if (_comm.open(m_nxt)) {

			m_dis = new DataInputStream(_comm.getInputStream());
			m_dos = new DataOutputStream(_comm.getOutputStream());
		}
		return isConnected();
	}

	public boolean isConnected() {
		return m_dos != null;
	}

	@Override
	public void run() {
//		Scanner sc = new Scanner(System.in);
		boolean continuer = true;
		List<Vertex> orientation = new ArrayList<>();
		orientation.add(new Vertex("B", 0, 7, false, Direction.RIGHT));
		orientation.add(new Vertex("C", 0, 2, false, Direction.LEFT));
		
		Vertex goal = new Vertex("I", 0, 2, false, Direction.STRAIGHT);
		Vertex start = new Vertex("A", 0, 28, false, Direction.STRAIGHT);
		Astar astar = new Astar(new Graph("essaie", CreatGraph.compet2()), start, start);
		astar.aStar(orientation);
		List<Vertex> order = astar.getOrder();
		Direction dir;
		int toSend = 0;
		try {

//			System.out.println("je rentre la");
			//demi tour 
			if (order.isEmpty() || !orientation.contains(order.get(0))) {
				m_dos.writeInt(4);
				m_dos.flush();
			}else {
				m_dos.writeInt(1);
				m_dos.flush();
			}
			
			while (continuer) {
				int answer = m_dis.readInt();
				if (answer == 4 || answer == 2 || answer == 3) {
					toSend = 1;
				}else if (answer == 1) {
					
					if (!order.isEmpty()) {
						dir = order.remove(0).getDirection();
						switch (dir) {
						case LEFT:
							toSend = 2;
							break;
						case RIGHT:
							toSend = 3;
							break;
						default:
							break;
						}
					}else {
						continuer = false;
						toSend = 0;
					}
				}
				m_dos.writeInt(toSend);
				m_dos.flush();
				System.out.println(m_nxt.name + " returned " + answer);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param args
	 * @throws NXTCommException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws NXTCommException, IOException {
		
//		NXTComm nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
//		NXTInfo nxtInfo = new NXTInfo(NXTCommFactory.BLUETOOTH, "NXT", "00:16:53:1C:15:FC");
//		System.out.println(nxtComm.open(nxtInfo));
//		System.out.println("ok");
//		nxtComm.close();
		try {

			NXTInfo[] nxts = {

					new NXTInfo(NXTCommFactory.BLUETOOTH, "R.O.B.B.Y.",
							"00:16:53:1C:15:FC")};

			ArrayList<RandomConnection> connections = new ArrayList<>(
					nxts.length);

			for (NXTInfo nxt : nxts) {
				connections.add(new RandomConnection(nxt));
			}

			for (RandomConnection connection : connections) {
				NXTComm nxtComm = NXTCommFactory
						.createNXTComm(NXTCommFactory.BLUETOOTH);
				connection.connect(nxtComm);
			}

			ArrayList<Thread> threads = new ArrayList<>(nxts.length);

			for (RandomConnection connection : connections) {
				threads.add(new Thread(connection));
			}

			for (Thread thread : threads) {
				thread.start();
			}

			for (Thread thread : threads) {
				try {
					thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		} catch (NXTCommException e) {
			e.printStackTrace();
		}

	}
}