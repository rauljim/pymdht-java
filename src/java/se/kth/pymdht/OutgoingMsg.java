package se.kth.pymdht;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.TreeMap;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

public class OutgoingMsg {
	private Node dst_node;
	private Map<ByteBuffer, Object> _dict;
	private ByteBuffer version;
	private boolean _already_encoded;
	private long sending_ts;
	private Bencode _bencode;
	private Object lookup_obj;
	private boolean got_response;
	private byte[] _src_id;
	private Map<ByteBuffer, Object> args;
	/*
	 */
	public OutgoingMsg(Node dst_node){
		this.version = ByteBuffer.wrap("An00".getBytes());
		this.dst_node = dst_node;
		this._already_encoded = false;
		this._dict = new TreeMap<ByteBuffer, Object>();
		this._bencode = new Bencode();
		this._bencode.setRootElement(_dict);
	}
	
	public void stamp(ByteOutputStream os){
		/*
        Return a Datagram object ready to be sent over the network. The
        message's state is changed internally to reflect that this message has
        been stamped. This call will raise MsgError if the message has already
        been stamped.

		 */

		this._dict.put(MsgConst.TID, "11".getBytes());
		this.sending_ts = System.currentTimeMillis() / 1000L;
		try {
			this._bencode.print(os);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Object query(){
		return this._dict.get(MsgConst.QUERY);
	}
	
	public Object tid(){
		return this._dict.get(MsgConst.TID);
	}
//	public void match_response(response_msg){
//		/*
//      Return a boolean indicating whether 'response\_msg' matches this
//      outgoing query. If so, as a side effect, the round trip time is
//      calculated and stored in 'self.rtt'. 'self.got\_response' is set to
//      True.
//
//		 */
//		matched = this._dict.get(TID).get(0).equals(response_msg.tid.get(0));
//		if (matched) {
//			this.rtt = (time.time()-this.sending_ts);
//			this.got_response = true;
//			if (response_msg.type.equals(RESPONSE)&&!this.dst_node.id) {
//				this.dst_node.id = response_msg.src_node.id;
//			}
//		}
//		return matched;
//	}
	
	public void make_query(Id src_id, Object lookup_obj){
		this._dict.put(MsgConst.TYPE, MsgConst.QUERY);
		args = new TreeMap<ByteBuffer, Object>();
		args.put(MsgConst.ID, src_id._bin);
		this._dict.put(MsgConst.ARGS, args);
		this._src_id = src_id._bin;
		this.lookup_obj = lookup_obj;
		this.got_response = false;
	}
	
	public void get_peers_query(Id info_hash){
		this._dict.put(MsgConst.QUERY, MsgConst.GET_PEERS);
		args.put(MsgConst.INFO_HASH,info_hash);
	}
//	public void announce_peer_query(info_hash,port,token){
//		this._dict.put(QUERY,ANNOUNCE_PEER);
//		this._dict.get(ARGS).put(INFO_HASH,str(info_hash));
//		this._dict.get(ARGS).put(PORT,port);
//		this._dict.get(ARGS).put(TOKEN,token);
//	}
}
