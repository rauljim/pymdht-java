package se.kth.pymdht;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.List;

public class Reactor {

	int timeout_delay = 50;
	private boolean _running;
	private DatagramSocket _s;
	private Controller _controller;
	
	public Reactor(int port, Controller controller) throws SocketException{
		this._running = false;
		this._controller = controller;
		this._s = new DatagramSocket(port);
		this._s.setReuseAddress(true);
		this._s.setSoTimeout(timeout_delay);
	}
	public void start(){
		assert !this._running;
		this._running = true;
		try{
			while (this._running){
				this.run_one_step();
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	public void run_one_step(){
		List<DatagramPacket> datagrams_to_send = null;
		byte[] buf = new byte[2000];
		DatagramPacket datagram = new DatagramPacket(buf, buf.length);
		try{
			this._s.receive(datagram);
			datagrams_to_send =  _controller.on_datagram_received(datagram);
		}
		catch (SocketTimeoutException e){
			datagrams_to_send = _controller.on_heartbeat();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		for(DatagramPacket datagram_to_send : datagrams_to_send){
			try{
				this._s.send(datagram_to_send);
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}
	}
}
