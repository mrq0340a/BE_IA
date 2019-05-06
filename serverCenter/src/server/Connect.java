package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

public class Connect {

	private DataInputStream oRcve;
	private DataOutputStream iSend;
	private NXTInfo nxt;
	private NXTComm nxtComm;
	private boolean connected;
	// thread de communication
	private Thread threadSend;
	private Thread threadRecev;

	public Connect(NXTInfo nxt) {
		this.nxt = nxt;
		// initialisation de la connection est ouverture des flux
		try {
			nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
			if (nxtComm.open(nxt)) {
				oRcve = new DataInputStream(nxtComm.getInputStream());
				iSend = new DataOutputStream(nxtComm.getOutputStream());
				this.connected = true;
			}
		} catch (NXTCommException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean closeConn() {
		try {
			nxtComm.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	//
	
	synchronized public void stopRecv() {
		this.connected = false;
	}
}
