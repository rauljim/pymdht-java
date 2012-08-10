package se.kth.pymdht;

import java.net.SocketException;

public class Pymdht{
	private Controller controller;
	private Reactor reactor;

	public Pymdht(int port){
		this.controller = new Controller();
		try {
			this.reactor = new Reactor(port, controller);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		Pymdht pymdht = new Pymdht(9991);
		pymdht.reactor.start();
	}
}