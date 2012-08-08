package se.kth.pymdht;

import java.io.ByteArrayInputStream;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Vector;


public class IncomingMsg {
	/*
    Create an object by decoding the given Datagram object. Raise 'MsgError'
    whenever the decoder fails to decode the datagram's data (e.g., invalid
    bencode).

    ?TODO: List attributes.
	 */
	
	static  class MsgError extends Exception {
		private static final long serialVersionUID = 1L;
	}

	
	private DatagramPacket _datagram;
	private Map<ByteBuffer, Object> top_dict;
	private Vector<Node> nodes;
	public Vector<Node> all_nodes;
	public Vector<InetSocketAddress> peers;
	private Map<ByteBuffer, Object> r_dict;
	public IncomingMsg(DatagramPacket datagram) throws MsgError{
		_datagram = datagram;
//		this.src_node = null;
//		this.info_hash = null;
//		this.nodes = null;
//		this.nodes2 = null;
//		this.all_nodes = null;
//		this.peers = null;

		try{//  bencode.decode may raise bencode.DecodeError
			Bencode b = new Bencode(new ByteArrayInputStream(_datagram.getData()));
			this.top_dict = (Map<ByteBuffer, Object>) b.getRootElement();
			//  type == response 
			if (!(MsgConst.RESPONSE.equals(
					this.top_dict.get(MsgConst.TYPE)))){
				throw new MsgError();
			}
			this.r_dict = (Map<ByteBuffer, Object>) this.top_dict.get(MsgConst.RESPONSE);
			System.out.println("IncomingMsg");
			// get nodes
			ByteBuffer cnodes = (ByteBuffer) this.r_dict.get(MsgConst.NODES);
			if (cnodes != null){
				this.nodes = uncompact_nodes(cnodes);
			}
			else{
				this.nodes = new Vector<Node>();
			}
//			this.nodes2 = uncompact_nodes((ByteBuffer) this._msg_dict.get(MsgConst.NODES2));
			this.all_nodes = this.nodes;
			System.out.println("IncomingMsg nodes OK");
			
			// get peers
			List<ByteBuffer> cpeers = (List<ByteBuffer>) this.r_dict.get(MsgConst.VALUES);
			if (cpeers != null){	
				this.peers = uncompact_peers(cpeers);
			}
			else{
				System.out.println("IncomingMsg NO values");
				this.peers = new Vector<InetSocketAddress>();
			}
			System.out.println("IncomingMsg peers OK");
		}
		catch (Exception e){
			throw new MsgError();
		}
	}
	
	private Vector<Node> uncompact_nodes(ByteBuffer c_nodes) throws MsgError{
		Vector<Node> nodes = new Vector<Node>();
		byte[] cn = c_nodes.array();
		if (cn.length % 26 != 0){
			throw new MsgError();
		}
		Id id;
		Inet4Address addr;
		int port;
		int pos = 0;
		while (pos < cn.length - 1){
			try{
				id = new Id(Arrays.copyOfRange(cn, pos, pos + 20));
				pos += 20;
				addr = (Inet4Address) InetAddress.getByAddress(Arrays.copyOfRange(cn, pos, pos + 4));
				pos += 4;
				port = (char)cn[pos] * 256 + (char)cn[pos + 1]; 
				pos += 2;
				nodes.add(new Node(new InetSocketAddress(addr, port), id));
			}
			catch (Exception e){
				nodes = new Vector<Node>();
			}
		}
		return nodes;
	}
	
	private Vector<InetSocketAddress> uncompact_peers(List<ByteBuffer> c_peers) throws MsgError{
		System.out.println("uncompact peers");
		
		Vector<InetSocketAddress> peers = new Vector<InetSocketAddress>();
		Inet4Address addr;
		int port;
		int pos;
		for (ByteBuffer c_peer : c_peers){
			byte[] cp = c_peer.array();
			if (cp.length % 6 != 0){
				throw new MsgError();
			}
			pos = 0;
			try{
				addr = (Inet4Address) InetAddress.getByAddress(Arrays.copyOfRange(cp, pos, pos + 4));
				pos += 4;
				port = (char)cp[pos] * 256 + (char)cp[pos + 1]; 
				pos += 2;
				System.out.println(new InetSocketAddress(addr, port));
				peers.add(new InetSocketAddress(addr, port));
			}
			catch (Exception e){
				e.printStackTrace();
				peers = new Vector<InetSocketAddress>();
			}
		}
		return peers;
	}
	
//	
//	public void __repr__(){
//		return repr(this._msg_dict);
//	}
//	//  
//	//  Sanitize functions 
//	//  
//	public void _get_value(ByteBuffer k, ByteBuffer kk){
//		Object v = this._msg_dict.get(k);
//		if (kk) {
//			v = v.get(kk);
//		}
//		return v;
//		}catch (KeyError){if (optional) {
//			return null;
//		} else {
//			MsgError;
//			String.format("Non-optional key (%s:%s) not found",k,kk);
//		}
//		};
//		catch (TypeError){MsgError;
//		String.format("Probably k (%r) is not a dictionary",k);
//		};
//		;
//	}
//	
//	public void _get_str(k,kk,optional){
//		if (vv = this._get_value(k,kk,optional);
//		null) {
//			return null;
//		}
//		if (!isinstance(v,str)) {
//			MsgError;
//			String.format("Value (%s:%s,%s) must be a string",k,kk,v);
//		}
//		return v;
//	}
//	public void _get_id(k,kk){
//		v = this._get_str(k,kk);
//		try{return Id(v);
//		}catch (IdError){MsgError;
//		String.format("Value (%s:%s,%s) must be a valid Id",k,kk,v);
//		};
//		;
//	}
//	public void _get_int(k,kk){
//		v = this._get_value(k,kk);
//		try{return int(v);
//		}catch (TypeError,ValueError){MsgError;
//		String.format("Value (%s:%s,%s) must be an int",k,kk,v);
//		};
//		;
//	}
//	public void _sanitize_common(){
//		//  Make sure the decoded data is a dict and has a TID key 
//		try{this.tid = this._msg_dict.get(TID);
//		}catch (TypeError){MsgError;
//		//decoded data is not a dictionary
//		};
//		catch (KeyError){MsgError;
//		//key TID not found
//		};
//		;
//		//  Sanitize TID 
//		if (!isinstance(this.tid,str)&&this.tid) {
//			MsgError;
//			//TID must be a non-empty binary string
//		}
//		//  Sanitize TYPE 
//		try{this.type = this._msg_dict.get(TYPE);
//		}catch (KeyError){MsgError;
//		//key TYPE not found
//		};
//		;
//		//  private dht name 
//		if (this.private_dht_name) {
//			try{if (!this._msg_dict.get("d").equals(this.private_dht_name)) {
//				MsgError;
//				String.format("invalid private DHT name %r!=%r",this._msg_dict.get("d"),this.private_dht_name);
//			}
//			}catch (KeyError,TypeError){MsgError;
//			//invalid private DHT name
//			};
//			;
//		}
//		//  version (optional) 
//		this.version = this._get_str(VERSION);
//		this.ns_node = this.version&&this.version.startswith("NS");
//	}
//	public void _sanitize_query(){
//		//  src_id 
//		this.src_id = this._get_id(ARGS,ID);
//		this.src_node = Node(this.src_addr,this.src_id,this.version);
//		//  query 
//		this.query = this._get_str(QUERY);
//		if (Arrays.asList({GET_PEERS,ANNOUNCE_PEER}).contains(this.query)) {
//			//  info_hash 
//			this.info_hash = this._get_id(ARGS,INFO_HASH);
//			if (this.query.equals(ANNOUNCE_PEER)) {
//				this.bt_port = this._get_int(ARGS,PORT);
//				<=;
//				this.bt_port;
//				if (!MIN_BT_PORT<=MAX_BT_PORT) {
//					MsgError;
//					String.format("announcing to %d. Out of range",this.bt_port);
//				}
//				this.token = this._get_str(ARGS,TOKEN);
//			}
//		} else {
//			if (this.query.equals(FIND_NODE)) {
//				//  target 
//				this.target = this._get_id(ARGS,TARGET);
//			}
//		}
//		return;
//	}
//	public void _sanitize_response(){
//		//  src_id 
//		this.src_id = this._get_id(RESPONSE,ID);
//		this.src_node = Node(this.src_addr,this.src_id,this.version);
//		//  all nodes 
//		this.all_nodes = new ArrayList();
//		//  nodes 
//		c_nodes = this._get_str(RESPONSE,NODES);
//		if (c_nodes) {
//			this.nodes = mt.uncompact_nodes(c_nodes);
//			this.all_nodes = this.nodes;
//		}
//		//  nodes2 
//		try{c_nodes2 = this._msg_dict.get(RESPONSE).get(NODES2);
//		}catch (KeyError){this.nodes2 = null;
//		};
//		;
//		//  token 
//		this.token = this._get_str(RESPONSE,TOKEN);
//		//  peers 
//		this.peers = null;
//		c_peers = this._get_value(RESPONSE,PEERS);
//		if (c_peers) {
//			this.peers = mt.uncompact_peers(c_peers);
//		}
//	}
//	public void _sanitize_error(){
//		this.src_id = null;
//		catch (this.src_node = Node(this.src_addr);
//				){MsgError;
//				//Invalid error message
//		};
//		try{this.error = Arrays.asList({int(this._msg_dict.get(ERROR).get(0)),str(this._msg_dict.get(ERROR).get(1))});
//		};
//	}
}
