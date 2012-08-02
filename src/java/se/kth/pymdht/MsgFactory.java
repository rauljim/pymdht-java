package se.kth.pymdht;

import java.net.DatagramPacket;

public class MsgFactory {
	private byte[] version_label;
	private Id src_id;
	
	public MsgFactory(byte[] version_label, Id src_id){
		this.version_label = version_label;
		this.src_id = src_id;
	}
	
	public OutgoingMsg outgoing_get_peers_query(dst_node,info_hash,lookup_obj,experimental_obj){
		msg = OutgoingMsg(this.version_label,dst_node,this.private_dht_name);
		msg.make_query(this.src_id,experimental_obj,lookup_obj);
		msg.get_peers_query(info_hash);
		return msg;
	}
//	public void outgoing_announce_peer_query(dst_node,info_hash,port,token,experimental_obj){
//		msg = OutgoingMsg(this.version_label,dst_node,this.private_dht_name);
//		msg.make_query(this.src_id,experimental_obj);
//		msg.announce_peer_query(info_hash,port,token);
//		return msg;
//	}

	public IncomingMsg incoming_msg(DatagramPacket datagram){
		msg = IncomingMsg(datagram);
		return msg;
	}
}
