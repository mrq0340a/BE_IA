package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author MMADI
 *
 */
public class CreatGraph {
	
	public static HashMap<Vertex, List<Vertex>> housMap() {
		HashMap<Vertex, List<Vertex>> graphe = new HashMap<>();
		
		List<Vertex> voisinsA = new ArrayList<>();
		Vertex A = new Vertex("A", 0, 2, true, Direction.STRAIGHT);
		voisinsA.add(new Vertex("B", 0, 6, false,  Direction.LEFT));
		voisinsA.add(new Vertex("C", 0, 6, false,  Direction.RIGH));
		graphe.put(A, voisinsA);
		
		List<Vertex> voisinsB = new ArrayList<>();
		Vertex B = new Vertex("B", 0, 6, false,  Direction.STRAIGHT);
		voisinsB.add(new Vertex("A", 0, 2, true, Direction.RIGH));
		voisinsB.add(new Vertex("C", 0, 6, false, Direction.LEFT));
		voisinsB.add(new Vertex("E", 0, 7, false, Direction.RIGH));
		voisinsB.add(new Vertex("D", 0, 9, false, Direction.LEFT));
		graphe.put(B, voisinsB);
		
		List<Vertex> voisinsC = new ArrayList<>();
		Vertex C = new Vertex("C", 0, 6, false,  Direction.STRAIGHT);
		voisinsC.add(new Vertex("B", 0, 6, false,  Direction.RIGH));
		voisinsC.add(new Vertex("A", 0, 2, true,  Direction.LEFT));
		voisinsC.add(new Vertex("E", 0, 7, false,  Direction.LEFT));
		voisinsC.add(new Vertex("F", 0, 8, false,  Direction.RIGH));
		graphe.put(C, voisinsC);
		
		List<Vertex> voisinsD = new ArrayList<>();
		Vertex D = new Vertex("D", 0, 9, false,  Direction.STRAIGHT);
		voisinsD.add(new Vertex("B", 0, 6, false,  Direction.RIGH));
		voisinsD.add(new Vertex("E", 0, 7, false,  Direction.LEFT));
		voisinsD.add(new Vertex("F", 0, 8, false,  Direction.RIGH));
		voisinsD.add(new Vertex("G", 0, 2, true,  Direction.LEFT));
		graphe.put(D, voisinsD);
		
		List<Vertex> voisinsE = new ArrayList<>();
		Vertex E = new Vertex("E", 0, 7, false,  Direction.STRAIGHT);
		voisinsE.add(new Vertex("B", 0, 6, false,  Direction.LEFT));
		voisinsE.add( new Vertex("D", 0, 9, false,  Direction.RIGH));
		voisinsE.add(new Vertex("F", 0, 8, false,  Direction.LEFT));
		voisinsE.add(new Vertex("C", 0, 6, false,  Direction.RIGH));
		graphe.put(E, voisinsE);
		
		List<Vertex> voisinsF = new ArrayList<>();
		Vertex F = new Vertex("F", 0, 8, false,  Direction.STRAIGHT);
		voisinsF.add(new Vertex("G", 0, 2, true,  Direction.RIGH));
		voisinsF.add( new Vertex("D", 0, 9, false,  Direction.LEFT));
		voisinsF.add(new Vertex("E", 0, 7, false,  Direction.RIGH));
		voisinsF.add(new Vertex("C", 0, 6, false,  Direction.LEFT));
		graphe.put(F, voisinsF);
		
		List<Vertex> voisinsG = new ArrayList<>();
		Vertex G = new Vertex("G", 0, 2, true,  Direction.STRAIGHT);
		voisinsG.add( new Vertex("D", 0, 9, false,  Direction.RIGH));
		voisinsG.add(new Vertex("F", 0, 8, false,  Direction.LEFT));
		graphe.put(G, voisinsG);
		
		return graphe;
	}
	
	
	public static HashMap<Vertex, List<Vertex>> mapTest() {
		HashMap<Vertex, List<Vertex>> graphe = new HashMap<>();
		List<Vertex> voisinsA = new ArrayList<>();
		Vertex A = new Vertex("A", 0, 10, false, Direction.STRAIGHT);
		voisinsA.add(new Vertex("B", 0, 15, false,  Direction.LEFT));
		voisinsA.add(new Vertex("C", 0, 8, false,  Direction.RIGH));
		voisinsA.add(new Vertex("F", 0, 20, false,  Direction.RIGH));
		voisinsA.add(new Vertex("D", 0, 6, false,  Direction.LEFT));
		graphe.put(A, voisinsA);
		
		List<Vertex> voisinsB = new ArrayList<>();
		Vertex B = new Vertex("B", 0, 15, false,  Direction.STRAIGHT);
		voisinsB.add(new Vertex("A", 0, 10, false, Direction.RIGH));
		voisinsB.add(new Vertex("C", 0, 8, false,  Direction.LEFT));
		voisinsB.add(new Vertex("E", 0, 6, false,  Direction.RIGH));
		voisinsB.add(new Vertex("F", 0, 20, false,  Direction.LEFT));
		graphe.put(B, voisinsB);
		
		List<Vertex> voisinsC = new ArrayList<>();
		Vertex C = new Vertex("C", 0, 8, false,  Direction.STRAIGHT);
		voisinsC.add(new Vertex("A", 0, 10, false, Direction.LEFT));
		voisinsC.add(new Vertex("B", 0, 15, false,  Direction.RIGH));
		voisinsC.add(new Vertex("E", 0, 6, false,  Direction.LEFT));
		voisinsC.add(new Vertex("D", 0, 6, false,  Direction.RIGH));
		graphe.put(C, voisinsC);
		
		
		List<Vertex> voisinsD = new ArrayList<>();
		Vertex D = new Vertex("D", 0, 6, false,  Direction.STRAIGHT);
		voisinsD.add(new Vertex("C", 0, 8, false,  Direction.LEFT));
		voisinsD.add(new Vertex("E", 0, 6, false,  Direction.RIGH));
		voisinsD.add(new Vertex("A", 0, 10, false, Direction.RIGH));
		voisinsD.add(new Vertex("F", 0, 20, false,  Direction.LEFT));
		graphe.put(D, voisinsD);
		
		List<Vertex> voisinsE = new ArrayList<>();
		Vertex E = new Vertex("E", 0, 6, false,  Direction.STRAIGHT);
		voisinsE.add(new Vertex("C", 0, 8, false,  Direction.RIGH));
		voisinsE.add(new Vertex("D", 0, 6, false,  Direction.LEFT));
		voisinsE.add(new Vertex("B", 0, 15, false,  Direction.LEFT));
		voisinsE.add(new Vertex("F", 0, 20, false,  Direction.RIGH));
		graphe.put(E, voisinsE);
		
		List<Vertex> voisinsF = new ArrayList<>();
		Vertex F = new Vertex("F", 0, 20, false, Direction.STRAIGHT);
		voisinsF.add(new Vertex("B", 0, 15, false, Direction.RIGH));
		voisinsF.add(new Vertex("E", 0, 6, false,  Direction.LEFT));
		voisinsF.add(new Vertex("D", 0, 6, false,  Direction.RIGH));
		voisinsF.add(new Vertex("A", 0, 10, false, Direction.LEFT));
		graphe.put(F, voisinsF);	
		return graphe;
	}
	
	
	public static HashMap<Vertex, List<Vertex>> halfMap () {
		HashMap<Vertex, List<Vertex>> graphe = new HashMap<>();
		List<Vertex> voisinsA = new ArrayList<>();
		Vertex A = new Vertex("A", 0, 6, true, Direction.STRAIGHT);
		voisinsA.add(new Vertex("B", 0, 4, false,  Direction.LEFT));
		voisinsA.add(new Vertex("C", 0, 22, false,  Direction.RIGH));
		graphe.put(A, voisinsA);
		
		List<Vertex> voisinsB = new ArrayList<>();
		Vertex B = new Vertex("B", 0, 4, false, Direction.STRAIGHT);
		voisinsB.add(new Vertex("A", 0, 6, false,  Direction.RIGH));
		voisinsB.add(new Vertex("C", 0, 22, false,  Direction.LEFT));
		voisinsB.add(new Vertex("D", 1, 29, true,  Direction.RIGH));
		voisinsB.add(new Vertex("E", 0, 15, false,  Direction.LEFT));
		graphe.put(B, voisinsB);
		
		List<Vertex> voisinsC = new ArrayList<>();
		Vertex C = new Vertex("C", 0, 22,false, Direction.STRAIGHT);
		voisinsC.add(new Vertex("A", 0, 6, false,  Direction.LEFT));
		voisinsC.add(new Vertex("B", 0, 4, false,  Direction.RIGH));
		voisinsC.add(new Vertex("N", 2, 289, false,  Direction.RIGH));
		voisinsC.add(new Vertex("M", 1, 26, false,  Direction.LEFT));
		graphe.put(C, voisinsC);
		
		List<Vertex> voisinsD = new ArrayList<>();
		Vertex D = new Vertex("D", 1, 29, true, Direction.STRAIGHT);
		voisinsD.add(new Vertex("B", 0, 4, false,  Direction.LEFT));
		voisinsD.add(new Vertex("E", 0, 15, false,  Direction.RIGH));
		graphe.put(D, voisinsD);
		
		List<Vertex> voisinsE = new ArrayList<>();
		Vertex E = new Vertex("E", 0, 15, false, Direction.STRAIGHT);
		voisinsE.add(new Vertex("B", 0, 4, false, Direction.RIGH));
		voisinsE.add(new Vertex("D", 0, 29, true, Direction.LEFT));
		voisinsE.add(new Vertex("F", 2, 103, false, Direction.LEFT));
		voisinsE.add(new Vertex("G", 1, 21, false, Direction.RIGH));
		graphe.put(E, voisinsE);
		
		List<Vertex> voisinsF = new ArrayList<>();
		Vertex F = new Vertex("F", 0, 103, false, Direction.STRAIGHT);
		voisinsF.add(new Vertex("E", 0, 15, false, Direction.LEFT));
		voisinsF.add(new Vertex("G", 1, 21, false,  Direction.RIGH));
		voisinsF.add(new Vertex("I", 1, 104, true,  Direction.LEFT));
		voisinsF.add(new Vertex("H", 0, 31, false,  Direction.RIGH));
		graphe.put(F, voisinsF);
		
		List<Vertex> voisinsG = new ArrayList<>();
		Vertex G = new Vertex("G", 0, 21, false, Direction.STRAIGHT);
		voisinsG.add(new Vertex("E", 0, 15, false,   Direction.LEFT));
		voisinsG.add(new Vertex("F", 2, 103, false,  Direction.RIGH));
		voisinsG.add(new Vertex("H", 0, 31, false,  Direction.LEFT));
		voisinsG.add(new Vertex("J", 0, 4, false,  Direction.RIGH));
		graphe.put(G, voisinsG);
		
		List<Vertex> voisinsH = new ArrayList<>();
		Vertex H = new Vertex("H", 0, 31, false, Direction.STRAIGHT);
		voisinsH.add(new Vertex("F", 2, 103, false,  Direction.LEFT));
		voisinsH.add(new Vertex("J", 0, 4, false,  Direction.LEFT));
		voisinsH.add(new Vertex("G", 1, 21, false,  Direction.RIGH));
		voisinsH.add(new Vertex("I", 1, 104, true,  Direction.RIGH));
		graphe.put(H, voisinsH);
		
		List<Vertex> voisinsI = new ArrayList<>();
		Vertex I = new Vertex("I", 0, 104, true, Direction.STRAIGHT);
		voisinsI.add(new Vertex("F", 2, 103, false,  Direction.RIGH));
		voisinsI.add(new Vertex("H", 0, 31, false,  Direction.LEFT));
		graphe.put(I, voisinsI);
		
		List<Vertex> voisinsJ = new ArrayList<>();
		Vertex J = new Vertex("J", 0, 4, false, Direction.STRAIGHT);
		voisinsJ.add(new Vertex("G", 1, 21, false,  Direction.LEFT));
		voisinsJ.add(new Vertex("H", 0, 31, false,  Direction.RIGH));
		voisinsJ.add(new Vertex("K", 1, 166, false,  Direction.LEFT));
		voisinsJ.add(new Vertex("L", 1, 60, false,  Direction.RIGH));
		graphe.put(J, voisinsJ);
		
		List<Vertex> voisinsK = new ArrayList<>();
		Vertex K = new Vertex("K", 1, 166, false, Direction.STRAIGHT);
		voisinsK.add(new Vertex("J", 0, 4, false,  Direction.RIGH));
		voisinsK.add(new Vertex("L", 1, 60, false,  Direction.LEFT));
		graphe.put(K, voisinsK);
		
		List<Vertex> voisinsL = new ArrayList<>();
		Vertex L = new Vertex("L", 1, 60, false, Direction.STRAIGHT);
		voisinsL.add(new Vertex("K", 1, 166, false, Direction.RIGH));
		voisinsL.add(new Vertex("J", 0, 4, false, Direction.LEFT));
		voisinsL.add(new Vertex("M", 1, 26, false, Direction.RIGH));
		voisinsL.add(new Vertex("N", 2, 289, false, Direction.LEFT));
		graphe.put(L, voisinsL);
		
		List<Vertex> voisinsM = new ArrayList<>();
		Vertex M =  new Vertex("M", 1, 26);
		voisinsM.add(new Vertex("C", 0, 22, false,  Direction.RIGH));
		voisinsM.add(new Vertex("N", 2, 289, false,  Direction.LEFT));
		voisinsM.add(new Vertex("N", 2, 289, false,  Direction.RIGH));
		voisinsM.add(new Vertex("L", 1, 60, false,  Direction.LEFT));
		graphe.put(M, voisinsM);
		
		List<Vertex> voisinsN = new ArrayList<>();
		Vertex N = new Vertex("N", 1, 289, false, Direction.STRAIGHT);
		voisinsN.add(new Vertex("C", 0, 22, false,  Direction.LEFT));
		voisinsN.add(new Vertex("M", 1, 26, false,  Direction.LEFT));
		voisinsN.add(new Vertex("M", 1, 26, false,  Direction.RIGH));
		voisinsN.add(new Vertex("L", 1, 60, false,  Direction.RIGH));
		graphe.put(N, voisinsN);
		
		return graphe;
	}
	
	
}
