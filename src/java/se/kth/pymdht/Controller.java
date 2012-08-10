package se.kth.pymdht;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import se.kth.pymdht.IncomingMsg.MsgError;

public class Controller {
	
	private Id _my_id;
	private SwiftTracker swift_tracker = new SwiftTracker();
	private GetPeersLookup lookup;
	private OverlayBootstrapper bootstrapper;
	
	public Controller(){
		this._my_id = new RandomId();
		this.bootstrapper = new OverlayBootstrapper();
		this.lookup = null;
	}
	
	public void start(){
		
	}
	
	public List<DatagramPacket> on_heartbeat(){
		if (this.lookup == null){
			return new ArrayList<DatagramPacket>(0);
		}
		return lookup.get_datagrams();
	}
	
	public List<DatagramPacket> on_datagram_received(DatagramPacket datagram){
		List<DatagramPacket> datagrams_to_send = new ArrayList<DatagramPacket>();
		IncomingMsg msg = null;
		try{
			msg = new IncomingMsg(datagram);
		}catch (MsgError e){
			// this is not a DHT message.
		}
		if (msg == null){
			// this is not a DHT message. Swift?
			//if swift: create lookup
			if (swift_tracker.on_handshake(datagram)){
				datagrams_to_send.add(swift_tracker.handshake_reply_datagram);
				lookup = new GetPeersLookup(swift_tracker.hash, bootstrapper);
				datagrams_to_send = lookup.get_datagrams();
				datagrams_to_send.add(swift_tracker.handshake_reply_datagram);
			}
			else{
				//ignore
			}
		}	
		else{
			//DHT get_peers response
			lookup.on_response(msg);
			//more lookup queries
			datagrams_to_send = lookup.get_datagrams();
			//peers
			List<ByteBuffer> cpeers = lookup.get_cpeers();
			if (cpeers.size() > 0){
				DatagramPacket pex_datagram = swift_tracker.get_swift_pex_datagram(cpeers);
				datagrams_to_send.add(pex_datagram);
			}
			
		}
		return datagrams_to_send;
	}
}
